package com.example.spring_sec_demo.service;

import com.example.spring_sec_demo.model.UserDetailsImpl;
import com.example.spring_sec_demo.model.Users;
import com.example.spring_sec_demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users=userRepo.findByUsername(username);
        if(users==null){
            throw new UsernameNotFoundException("404 User");
        }
        return new UserDetailsImpl(users);
    }
}
