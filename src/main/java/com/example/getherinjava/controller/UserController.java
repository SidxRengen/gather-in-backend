package com.example.getherinjava.controller;

import com.example.getherinjava.dto.ArrayResponse;
import com.example.getherinjava.dto.UserResponse;
import com.example.getherinjava.entry.User;
import com.example.getherinjava.repository.MessageRepository;
import com.example.getherinjava.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    UserRepository userRepository;
    MessageRepository messageRepository;

    public UserController(UserRepository userRepository,MessageRepository messageRepository){
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers(){
        List<User> allUsers= userRepository.findAll();
        List<Map<String, String>> users = allUsers.stream()
                .map(user -> {
                    Map<String, String> m = new HashMap<>();
                    m.put("userName", user.getUserName());
                    m.put("email", user.getEmail());
                    return m;
                })
                .toList();
        ArrayResponse responseBody = new ArrayResponse("all users has been fetched successfully!",true,users);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/activeUsers")
    public ResponseEntity<?> getActiveChats(Authentication authentication){
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail).orElse(null);
        if(currentUser==null){
            UserResponse responseBody = new UserResponse("user not found",true,new ArrayList<>());
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
        Map<List<String>, LocalDateTime> m = new HashMap<>();
        List<Object[]> activeSenderUser = messageRepository.findSenderUser(userEmail);
        List<Object[]> activeReceiverUser = messageRepository.findReceiverUser(userEmail);
        for(Object[] row: activeReceiverUser){
            List<String> l = List.of((String) row[0],(String) row[1]);
            m.put(l,(LocalDateTime)row[2]);

        }
        for(Object[] row: activeSenderUser){
            List<String> l = List.of((String) row[0],(String) row[1]);
            if(m.containsKey(l)&&m.get(l).isBefore((LocalDateTime)row[2])){
                m.put(l,(LocalDateTime)row[2]);
            }
        }
        List<Map<String, String>> response = new ArrayList<>();
        for(Map.Entry<List<String>, LocalDateTime> i:m.entrySet()){
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email",i.getKey().get(1));
            responseObj.put("userName",i.getKey().get(0));
            responseObj.put("timestamp",i.getValue().toString());
            response.add(responseObj);
        }
        ArrayResponse responseBody = new ArrayResponse("all users has been fetched successfully!",true,response);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
