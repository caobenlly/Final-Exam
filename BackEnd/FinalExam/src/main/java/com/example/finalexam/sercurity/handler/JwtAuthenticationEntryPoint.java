package com.example.finalexam.sercurity.handler;

import com.example.finalexam.config.exceptions.ResponseAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
        ResponseAuthentication.responseJson(httpServletResponse, new ResponseAuthentication(401, "Truy cap tu choi", httpServletRequest.getRequestURI()));
    }
}
