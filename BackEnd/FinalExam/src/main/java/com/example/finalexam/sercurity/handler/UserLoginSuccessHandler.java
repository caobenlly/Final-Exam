package com.example.finalexam.sercurity.handler;

import com.example.finalexam.config.exceptions.ResponseAuthentication;
import com.example.finalexam.entity.dto.LoginResponse;


import com.example.finalexam.sercurity.model.SysUserDetails;
import com.example.finalexam.token.JWTConfig;
import com.example.finalexam.token.JWTTokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // Lây đối tượng user đã đăng nhập
        SysUserDetails sysUserDetails = (SysUserDetails) authentication.getPrincipal();
        sysUserDetails.setIp(request.getHeader("User-Agent"));

        // Tạo ra token

        String token = JWTTokenUtils.createAccessToken(sysUserDetails);

        // Cơ chế lưu token
        JWTTokenUtils.setTokenInfo(token, sysUserDetails.getUsername(), request.getHeader("User-Agent"));
//        Map<String, String> tokenMap = new HashMap<>();
//        tokenMap.put("token", token.replace(JWTConfig.tokenPrefix, "").trim());

        LoginResponse loginResponse = new LoginResponse(sysUserDetails.getRole(), token.replace(JWTConfig.tokenPrefix, "").trim());
        ResponseAuthentication.responseSuccess(response, loginResponse);
    }
}
