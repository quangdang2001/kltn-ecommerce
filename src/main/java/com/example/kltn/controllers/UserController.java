package com.example.kltn.controllers;


import com.example.kltn.models.User;
import com.example.kltn.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserRepo userRepo;
//    @GetMapping("/user")
//    public ResponseEntity<?> saveUser(){
//
//
//    }
}
