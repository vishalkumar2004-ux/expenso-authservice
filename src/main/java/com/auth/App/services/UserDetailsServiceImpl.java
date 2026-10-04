package com.auth.App.services;
import com.auth.App.entities.UserInfo;
import com.auth.App.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository1) {
        this.userRepository = userRepository1;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserInfo user = userRepository.findByUsername(username);
        if( user == null)
            throw new UsernameNotFoundException("user not found");

        return new CustomUserDetails(user);
    }
}
