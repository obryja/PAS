package org.example.rest.security;

import org.example.rest.exceptions.UnauthorizedException;
import org.example.rest.models.User;
import org.example.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username);

        if (!user.isActive()) {
            throw new UnauthorizedException("User is not active");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(),            // enabled
                true,                       // accountNonExpired
                true,                       // credentialsNonExpired
                true,                       // accountNonLocked
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}