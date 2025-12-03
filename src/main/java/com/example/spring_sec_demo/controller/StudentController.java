package com.example.spring_sec_demo.controller;

import com.example.spring_sec_demo.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {
    List<Student> students=new ArrayList<>(List.of(new Student(1,"Kiran","Java"),new Student(2,"Kumat","SpringBoot"),new Student(3,"Sri","JS")));

    @GetMapping("/students")
    public List<Student> getAllStudents(){
        return students;
    }

    // here we accessing the csrf token because for the post,put,delete it require it
    // this is using just because to restrict the request from the malicious websites or 3rd party sites
    // we restrict it in 2ways 1 is the below ways
    /*1. we can access csrf token that by passing attribute name "_csrf" and we returning that object
    in the postman we can send and use that token in the headers with key=X-CSRF-TOKEN,value=token */

    /* 2. Restricting and allowing only the same site to access the resources and not allowing other sites to access them
       use the server.servlet.session.cookie.same=strict (by default lax) in application.properties file
    */
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }
    @PostMapping("/new-student")
    public void addStudent(@RequestBody Student student){
        students.add(student);
    }
}
