package com.example.spring_sec_demo.service;
import com.example.spring_sec_demo.model.Users;
import com.example.spring_sec_demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
    public Users saveUser(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        return  userRepo.save(user);
    }
}
