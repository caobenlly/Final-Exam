package com.example.finalexam.sercurity.filter;

import com.example.finalexam.sercurity.model.SysUserDetails;
import com.example.finalexam.token.JWTConfig;
import com.example.finalexam.token.JWTTokenUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
//    @Autowired
//    private StringRedisTemplate redisTemplate;

    //    public JwtRequestFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
//    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // Lấy token từ api
        String header = httpServletRequest.getHeader(JWTConfig.tokenHeader);
        if (StringUtils.endsWithIgnoreCase(httpServletRequest.getRequestURI(), "/login")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            if (JWTTokenUtils.checkToken(header, httpServletResponse, httpServletRequest)) {
                // Giải mã token -> lấy thông tin user -> authen
                SysUserDetails sysUserDetails = JWTTokenUtils.parseAccessToken(header);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        sysUserDetails, sysUserDetails.getId(), sysUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                JWTTokenUtils.updateExpiration(header);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        }
    }
}
