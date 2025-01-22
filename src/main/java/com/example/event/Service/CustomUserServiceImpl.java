package com.example.event.Service;


import com.example.event.Entity.UserEntity;
import com.example.event.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


@Service
public class CustomUserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("username "+username);
        UserEntity user = userRepository.findByUsername(username);
//        System.out.println("User : " +user);
//        System.out.println("username from db :"+user.getPassword());
        if (user==null || user.getUsername().isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        if (user != null) {
            System.out.println("user role "+user.getRole());
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())  // Ensure roles are handled correctly
                    .build();

        }

        System.out.println("User not found: " + username); // Debug logging
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}