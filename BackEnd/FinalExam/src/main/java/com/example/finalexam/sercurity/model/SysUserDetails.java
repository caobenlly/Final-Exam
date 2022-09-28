package com.example.finalexam.sercurity.model;


import com.example.finalexam.entity.Account;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Data
public class SysUserDetails extends Account implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;
    private Collection<GrantedAuthority> authorities;
    private boolean isAccountNonExpired = false;
    private boolean isAccountNonLocked = false;
    private boolean isCredentialsNonExpired = false;
    private boolean isEnabled = true;
    private String ip;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
