package com.example.getherinjava.controller;

import com.example.getherinjava.dto.MessageResponse;
import com.example.getherinjava.dto.UserResponse;
import com.example.getherinjava.entry.User;
import com.example.getherinjava.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers(){
        List<User> allUsers= userRepository.findAll();
        UserResponse responseBody = new UserResponse("all users has been fetched successfully!",true,allUsers);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/activeChats")
    public ResponseEntity<?> getActiveChats(Authentication authentication){
        String userEmail = authentication.getName();
        List<User> allUsers= userRepository.findAll();
        UserResponse responseBody = new UserResponse("all users has been fetched successfully!",true,allUsers);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
