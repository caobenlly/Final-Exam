//package com.example.finalexam.config;
//
//
//import com.example.finalexam.service.AccountService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@EnableWebSecurity
//public class WebSercurityConfiguration  extends WebSecurityConfigurerAdapter {
//
//    private final AccountService accountService;
//
//    public WebSercurityConfiguration(AccountService accountService) {
//        this.accountService = accountService;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(accountService).passwordEncoder(new BCryptPasswordEncoder());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//
//        http.
//                authorizeRequests().
//                antMatchers("api/v1/accounts").hasAnyAuthority("ADMIN").
//                antMatchers("api/v1/department").hasAnyAuthority("MANAGER","ADMIN").
//                antMatchers("/api/v1/login").anonymous().
//                anyRequest().
//                authenticated().
//                and().
//                httpBasic().
//                and().
//                csrf().
//                disable();
//
//
//    }
//}
