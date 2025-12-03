package com.example.spring_sec_demo.controller;

import com.example.spring_sec_demo.model.Users;
import com.example.spring_sec_demo.service.JwtService;
import com.example.spring_sec_demo.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public Users register(@RequestBody Users users){
        return usersService.saveUser(users);
    }

    @PostMapping("/login")
    public  String login(@RequestBody Users users){
        Authentication authentication=authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(),users.getPassword()));
        if(authentication.isAuthenticated()) {
            return jwtService.generateJwtToken(users.getUsername());
        }else{
            return "Login Failed";
        }
    }
}
