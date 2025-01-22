package com.example.event.Service;

import com.example.event.Entity.OrgEntity;
import com.example.event.Entity.UserEntity;
import com.example.event.Repository.OrgRepository;
import com.example.event.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomOrganizerServiceImpl implements UserDetailsService {

    @Autowired
    private OrgRepository orgRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        OrgEntity user = orgRepository.findByUsername(username);
        if (user != null) {
            System.out.println("User found: " + user.getUsername());
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())  // Ensure roles are handled correctly
                    .build();
        }

        System.out.println("User not found: " + username);
        throw new UsernameNotFoundException("User not found with username: " + username);
    }


}

