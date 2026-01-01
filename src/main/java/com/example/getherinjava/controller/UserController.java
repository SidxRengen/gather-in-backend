package com.example.getherinjava.controller;

import com.example.getherinjava.dto.ArrayResponse;
import com.example.getherinjava.dto.ObjectResponse;
import com.example.getherinjava.dto.UserResponse;
import com.example.getherinjava.entry.User;
import com.example.getherinjava.repository.MessageRepository;
import com.example.getherinjava.repository.UserRepository;
import com.example.getherinjava.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    UserService userService;

    public UserController(UserRepository userRepository,MessageRepository messageRepository,UserService userService){
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @GetMapping("/allUsers")
    public ResponseEntity<?> getAllUsers(){
        List<User> allUsers= userRepository.findAll();
        List<Map<String, String>> users = allUsers.stream()
                .map(user -> {
                    Map<String, String> m = new HashMap<>();
                    m.put("userName", user.getUserName());
                    m.put("email", user.getEmail());
                    m.put("photo", user.getPhotoUrl());
                    return m;
                })
                .toList();
        ArrayResponse responseBody = new ArrayResponse("all users has been fetched successfully!",true,users);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/activeUsers")
    public ResponseEntity<?> getActiveChats(Authentication authentication) {

        String userEmail = authentication.getName();

        if (!userRepository.existsByEmail(userEmail)) {
            return new ResponseEntity<>(
                    new ArrayResponse("User not found", false, List.of()),
                    HttpStatus.NOT_FOUND
            );
        }

        Map<String, Map<String, Object>> latestChats = new HashMap<>();

        List<Object[]> senderUsers = messageRepository.findSenderUser(userEmail);
        List<Object[]> receiverUsers = messageRepository.findReceiverUser(userEmail);


        for (Object[] row : receiverUsers) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userName", (String) row[0]);
            userMap.put("email", (String) row[1]);
            userMap.put("photoUrl", (String) row[2]);
            userMap.put("timestamp", (LocalDateTime) row[3]);

            latestChats.put((String) row[1], userMap);
        }


        for (Object[] row : senderUsers) {
            String email = (String) row[1];
            LocalDateTime newTime = (LocalDateTime) row[3];

            latestChats.merge(
                    email,
                    new HashMap<>() {{
                        put("userName", (String) row[0]);
                        put("email", email);
                        put("photoUrl", (String) row[2]);
                        put("timestamp", newTime);
                    }},
                    (oldVal, newVal) -> {
                        LocalDateTime oldTime = (LocalDateTime) oldVal.get("timestamp");
                        return oldTime.isAfter(newTime) ? oldVal : newVal;
                    }
            );
        }

        List<Map<String, String>> response = new ArrayList<>();

        for (Map<String, Object> user : latestChats.values()) {
            Map<String, String> obj = new HashMap<>();

            obj.put("userName", (String) user.get("userName"));
            obj.put("email", (String) user.get("email"));
            obj.put("photo", (String) user.get("photoUrl"));
            obj.put(
                    "timestamp",
                    ((LocalDateTime) user.get("timestamp")).toString()
            );

            response.add(obj);
        }

        return ResponseEntity.ok(
                new ArrayResponse(
                        "All active users fetched successfully",
                        true,
                        response
                )
        );
    }
    @PostMapping("/uploadPhoto")
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        String email = authentication.getName();

        if (file.isEmpty()) {
            ArrayResponse responseBody = new ArrayResponse("File is empty", false, new ArrayList<>());
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        if (!file.getContentType().startsWith("image/")) {
            ArrayResponse responseBody = new ArrayResponse("Only images allowed", false, new ArrayList<>());
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            ArrayResponse responseBody = new ArrayResponse("Max size 2MB", false, new ArrayList<>());
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        String photoUrl = userService.uploadImage(file);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPhotoUrl(photoUrl);
        userRepository.save(user);

        ArrayResponse responseBody = new ArrayResponse("Image Uploaded Successfully!", true, new ArrayList<>());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfileInfo(Authentication authentication){
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        if (user==null){
            return new ResponseEntity<>(new ObjectResponse("user profile has been fetched successfully!",true,new HashMap<>()),HttpStatus.BAD_REQUEST);
        }
        Map<String,String> m = new HashMap<>();
        m.put("userName",user.getUserName());
        m.put("email",user.getEmail());
        m.put("photo",user.getPhotoUrl());
        m.put("timestamp",user.getTimestamp().toString());
        return new ResponseEntity<>(new ObjectResponse("user profile has been fetched successfully!",true,m),HttpStatus.OK);
    }
}
