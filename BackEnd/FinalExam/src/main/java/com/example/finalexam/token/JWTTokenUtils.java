package com.example.finalexam.token;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.finalexam.config.exceptions.ResponseAuthentication;
import com.example.finalexam.sercurity.model.SysUserDetails;
import com.example.finalexam.sercurity.model.Token;
import com.example.finalexam.service.JWTTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Component
public class JWTTokenUtils {
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static JWTTokenService jwtTokenService;

    private final JWTTokenService jwtTokenServiceAutowired;

    public JWTTokenUtils(JWTTokenService jwtTokenServiceAutowired) {
        this.jwtTokenServiceAutowired = jwtTokenServiceAutowired;
    }

    @PostConstruct
    public void init() {
        jwtTokenService = this.jwtTokenServiceAutowired;
    }

    public static String createAccessToken(SysUserDetails sysUserDetails) {

        Key key = Keys.hmacShaKeyFor(JWTConfig.secret.getBytes());

        String token = Jwts.builder()
                .claim("authorities", sysUserDetails.getAuthorities())
                .claim("ip", sysUserDetails.getIp())
                .setId(sysUserDetails.getId().toString())
                .setSubject(sysUserDetails.getUsername())
                .setIssuedAt(new Date())
                .setIssuer("NMS")
                .setExpiration(new Date(System.currentTimeMillis() + JWTConfig.expiration))
                .signWith(SignatureAlgorithm.HS256, key).compact();
        return JWTConfig.tokenPrefix + token;
    }

//    public static String refreshAccessToken(String oldToken, StringRedisTemplate redisTemplate) {
//        String username = JWTTokenUtils.getUserNameByToken(oldToken, redisTemplate);
//        SysUserDetails sysUserDetails = (SysUserDetails) jwtTokenUtils.jwtUserDetailService
//                .loadUserByUsername(username);
//        sysUserDetails.setIp(JWTTokenUtils.getIpByToken(oldToken, redisTemplate));
//        return createAccessToken(sysUserDetails);
//    }

    public static SysUserDetails parseAccessToken(String token) {
        SysUserDetails sysUserDetails = new SysUserDetails();
        if (!token.isEmpty()) {
            try {
                token = token.substring(JWTConfig.tokenPrefix.length()).trim();
                Claims claims = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(JWTConfig.secret.getBytes())).parseClaimsJws(token).getBody();
                sysUserDetails.setId(Integer.parseInt(claims.getId()));
                sysUserDetails.setUsername(claims.getSubject());

                Set<GrantedAuthority> authorities = new HashSet<>();
                String authority = claims.get("authorities").toString();
                if (!authority.isEmpty()) {
                    List<Map<String, String>> authorityList = JSON.parseObject(authority,
                            new TypeReference<List<Map<String, String>>>() {
                            });
                    for (Map<String, String> role : authorityList) {
                        if (!role.isEmpty()) {
                            authorities.add(new SimpleGrantedAuthority(role.get("authority")));
                        }
                    }
                }
                sysUserDetails.setAuthorities(authorities);

                String ip = claims.get("ip").toString();
                sysUserDetails.setIp(ip);
            } catch (Exception e) {
                log.error(e.getMessage());
//                throw new BadCredentialsException("Error");
            }
        }
        return sysUserDetails;
    }

    public static void setTokenInfo(String token, String username, String userAgent) {
        if (!token.isEmpty()) {
            token = token.replace(JWTConfig.tokenPrefix, "").trim();
            LocalDateTime localDateTime = LocalDateTime.now();
            LocalDateTime refreshTime = localDateTime.plus(JWTConfig.refreshTime, ChronoUnit.MILLIS);
            LocalDateTime expiration = localDateTime.plus(JWTConfig.refreshTime, ChronoUnit.MILLIS);
            String ip = userAgent;
            boolean isBlackList = false;
            Token entity = new Token(token, ip, isBlackList, refreshTime, expiration);
            jwtTokenService.save(entity);
        }
    }

    public static void updateExpiration(String token) {
        if (!token.isEmpty()) {
            Token entity = jwtTokenService.findByToken(token);
            token = token.replace(JWTConfig.tokenPrefix, "").trim();

            Integer refreshTime = JWTConfig.refreshTime;
            LocalDateTime localDateTime = LocalDateTime.now();

            entity.setExpiration(localDateTime.plus(JWTConfig.expiration, ChronoUnit.MILLIS));
            entity.setRefreshTime(localDateTime.plus(JWTConfig.refreshTime, ChronoUnit.MILLIS));
            entity.setRefreshTime(localDateTime.plus(JWTConfig.refreshTime, ChronoUnit.MILLIS));

            jwtTokenService.save(entity);
        }
    }

    public static boolean checkToken(String token, HttpServletResponse response, HttpServletRequest httpServletRequest) {
        try {
            if (StringUtils.isEmpty(token)) {
                ResponseAuthentication.responseJson(response,
                        new ResponseAuthentication(401, "Có lỗi xảy ra, mời đăng nhập lại", httpServletRequest.getRequestURI()));
                return false;
            }
            Token entity = jwtTokenService.findByToken(token);
            if (token == null || !token.startsWith(JWTConfig.tokenPrefix)) {
                ResponseAuthentication.responseJson(response, new ResponseAuthentication(401, "Token ko hợp lệ", httpServletRequest.getRequestURI()));
                return false;
            }
            token = token.replace(JWTConfig.tokenPrefix, "").trim();
            if (entity == null) {
                ResponseAuthentication.responseJson(response, new ResponseAuthentication(401, "Token ko tồn tại", httpServletRequest.getRequestURI()));
                return false;
            }

            if (isBlackList(token, entity)) {
                ResponseAuthentication.responseJson(response, new ResponseAuthentication(401, "Token trong danh sách đen", httpServletRequest.getRequestURI()));
                return false;
            }

            String ip = httpServletRequest.getHeader("User-Agent");
            if (!Objects.equals(ip, entity.getIp())) {
                ResponseAuthentication.responseJson(response, new ResponseAuthentication(401, "Token đc dùng ở chỗ khác", httpServletRequest.getRequestURI()));
                return false;
            }
//            if (JWTTokenUtils.isExpiration(entity.getExpiration().toString())) {
//                ResponseAuthentication.responseJson(response, new ResponseAuthentication(401, "Token hết hiệu lực", httpServletRequest.getRequestURI()));
//                return false;
//            }
        } catch (Exception e) {
            ResponseAuthentication.responseJson(response, new ResponseAuthentication(401, e.getMessage(), httpServletRequest.getRequestURI()));
            return false;
        }
        return true;
    }
//
//    public static void addBlackList(String token, StringRedisTemplate redisTemplate) {
//        if (!token.isEmpty()) {
//            RedisUtils.hset("blackList", token, df.format(LocalDateTime.now()), redisTemplate);
//        }
//    }
//
//    public static void deleteRedisToken(String token, StringRedisTemplate redisTemplate) {
//        if (!token.isEmpty()) {
//            RedisUtils.deleteKey(token, redisTemplate);
//        }
//    }

    public static boolean isBlackList(String token,Token entity) {
        if (!token.isEmpty()) {
            return entity.isBlackList();
        }
        return false;
    }

    public static boolean isExpiration(String expiration) {
        LocalDateTime expirationTime = LocalDateTime.parse(expiration, df);
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.compareTo(expirationTime) > 0;
    }
//
//    public static boolean isValid(String refreshTime) {
//        LocalDateTime validTime = LocalDateTime.parse(refreshTime, df);
//        LocalDateTime localDateTime = LocalDateTime.now();
//        return localDateTime.compareTo(validTime) > 0;
//    }
//
//    public static boolean hasToken(String token, StringRedisTemplate redisTemplate) {
//        if (!token.isEmpty()) {
//            return RedisUtils.hasKey(token, redisTemplate);
//        }
//        return false;
//    }
//
//    public static String getExpirationByToken(String token, StringRedisTemplate redisTemplate) {
//        if (!token.isEmpty()) {
//            return RedisUtils.hget(token, "expiration", redisTemplate);
//        }
//        return null;
//    }
//
//    public static String getRefreshTimeByToken(String token, StringRedisTemplate redisTemplate) {
//        if (!token.isEmpty()) {
//            return RedisUtils.hget(token, "refreshTime", redisTemplate).toString();
//        }
//        return null;
//    }
//
//    public static String getUserNameByToken(String token, StringRedisTemplate redisTemplate) {
//        if (!token.isEmpty()) {
//            return RedisUtils.hget(token, "username", redisTemplate).toString();
//        }
//        return null;
//    }
//
//    public static String getIpByToken(String token, StringRedisTemplate redisTemplate) {
//        if (!token.isEmpty()) {
//            return RedisUtils.hget(token, "ip", redisTemplate).toString();
//        }
//        return null;
//    }

}
