package com.example.event.Filter;

import com.example.event.Service.CustomOrganizerServiceImpl;
import com.example.event.Service.CustomUserServiceImpl;
import com.example.event.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserServiceImpl customUserService;

    @Autowired
    private CustomOrganizerServiceImpl customOrganizerService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Get the JWT token from the request header
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt); // Extract the username from the JWT
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Extract role from JWT token
            String role = jwtUtil.extractRole(jwt);

            UserDetails userDetails = null;
//            System.out.println(role);
            // Switch between UserDetailsService implementations based on role
            if ("ROLE_USER".equals(role)) {
                userDetails = customUserService.loadUserByUsername(username);  // Use user service for users
            } else if ("ROLE_ORGANIZER".equals(role)) {
                userDetails = customOrganizerService.loadUserByUsername(username);  // Use organizer service for organizers
            }

            // Validate the token and set authentication if valid
            if (userDetails != null && jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
