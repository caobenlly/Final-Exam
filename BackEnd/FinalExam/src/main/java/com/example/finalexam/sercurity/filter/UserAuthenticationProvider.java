package com.example.finalexam.sercurity.filter;


import com.example.finalexam.sercurity.model.SysUserDetails;
import com.example.finalexam.service.AccountService;
import com.example.finalexam.service.AccountServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
// xác thực người dùng
    private final AccountServiceImpl userEntityService;

    public UserAuthenticationProvider(AccountServiceImpl userEntityService) {
        this.userEntityService = userEntityService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        SysUserDetails sysUserDetails = (SysUserDetails) userEntityService.loadUserByUsername(username);
        if (sysUserDetails == null) {
            throw new BadCredentialsException("Username is not valid");
        }else if (!new BCryptPasswordEncoder().matches(password, sysUserDetails.getPassword())) {
            throw new BadCredentialsException("Password wrong");
        }
        System.out.println("UserAuthenticationProvider 79: " + sysUserDetails.getAuthorities());
        return new UsernamePasswordAuthenticationToken(sysUserDetails, password, sysUserDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}
