package com.etaargus.taskflow.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        Long userId = null;
        String jwt = null;

        // 1. Extract token from header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            
            // 2. We use our utility to check if it's valid, and if so, grab the ID.
            if (jwtUtil.isValid(jwt)) {
                try {
                    userId = jwtUtil.extractUserId(jwt);
                } catch (Exception e) {
                    System.out.println("Could not parse JWT token: " + e.getMessage());
                }
            }
        }

        // 3. If we found a valid userId and the context isn't already authenticated...
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // We create an Authentication object. 
            // In a full app, we might load the UserDetails from DB here. 
            // For performance, since the ID is in the token, we just use the ID directly!
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userId, null, new ArrayList<>());
                    
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
            // Pass it to Spring Security! This tells Spring "this user is authenticated!"
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            
            // Also, setting it as an attribute allows controllers to easily grab it using @RequestAttribute
            request.setAttribute("userId", userId);
        }
        
        // Let the request continue to the controller
        chain.doFilter(request, response);
    }
}
