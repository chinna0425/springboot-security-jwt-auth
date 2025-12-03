package com.example.spring_sec_demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private  UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public AuthenticationProvider authProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider(userDetailsService);
        // DaoAuthenticationProvider it is used to connect to the database
        //provider.setUserDetailsService(userDetailsService);
        // the above line is used in the springboot 6 and less there it need in latest it is not need
        // and new DaoAuthenticationProvider constructor need userDetailsService as argument
        // provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); // for the plain password
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       /* here the customized code can written in 2 ways 1. Lamda expression way 2.Imperative way */

        // Lamda expression way
        http.csrf(customizer->customizer.disable());
        http.authorizeHttpRequests(request->request
                .requestMatchers("/register","/login").permitAll()
                .anyRequest().authenticated());
        // http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return  config.getAuthenticationManager();
    }// this object is need for the jwt purpose

    /* @Bean
    // inmemory user details
    public UserDetailsService userDetailsService(){
        UserDetails user= User
                .withDefaultPasswordEncoder()
                .username("kiran")
                .password("kiran")
                .roles("USER")
                .build();
        UserDetails admin= User
                .withDefaultPasswordEncoder()
                .username("chinna")
                .password("chinna")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user,admin);
    } */
}

