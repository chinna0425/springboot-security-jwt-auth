package com.example.spring_sec_demo.config;

import com.example.spring_sec_demo.service.JwtService;
import com.example.spring_sec_demo.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        String token=null;
        String userName=null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            token=authHeader.substring(7).trim();
            System.out.println("JwtFilter token:-"+token);
            userName=jwtService.extractUserName(token);
        }
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("THis is 1st if");
            UserDetails userDetails = null;
            try {
                userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);
            } catch (Exception e) {
                System.out.println("loadUserByUsername threw: " + e.getMessage());
            }

            if (userDetails != null) {
                System.out.println("userDetails returned username: '" + userDetails.getUsername() + "'");
                boolean valid = jwtService.validateToken(token, userDetails);
                System.out.println("Token valid? " + valid);
                if (valid) {
                    System.out.println("This is inside 1st if (setting SecurityContext)");
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("Token validation failed - not setting SecurityContext");
                }
            } else {
                System.out.println("userDetails is null for username: '" + userName + "'");
            }
        }
        filterChain.doFilter(request,response);
    }
}


