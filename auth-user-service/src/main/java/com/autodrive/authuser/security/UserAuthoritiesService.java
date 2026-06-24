package com.autodrive.authuser.security;

import com.autodrive.authuser.entity.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserAuthoritiesService {

    public Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().authority()));

        if (user.getEmployee() != null && user.getEmployee().getPosition() != null) {
            authorities.add(new SimpleGrantedAuthority(user.getEmployee().getPosition().authority()));
        }

        return authorities;
    }
}
