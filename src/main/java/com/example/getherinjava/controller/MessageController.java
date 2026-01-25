package com.example.getherinjava.controller;

import com.example.getherinjava.dto.request.MessageRequest;
import com.example.getherinjava.dto.response.GeneralResponse;
import com.example.getherinjava.dto.response.ObjectResponse;
import com.example.getherinjava.dto.response.ArrayResponse;
import com.example.getherinjava.entry.Message;
import com.example.getherinjava.entry.User;
import com.example.getherinjava.repository.MessageRepository;
import com.example.getherinjava.repository.UserRepository;
import com.example.getherinjava.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    public MessageController(UserRepository userRepository,MessageRepository messageRepository,UserService userService){
        this.messageRepository = messageRepository;
        this.userRepository= userRepository;
        this.userService = userService;
    }

//    @PostMapping("/send")
//    public ResponseEntity<?> sendMessage(Authentication authentication,@Valid @RequestBody MessageRequest messageRequest){
//        User sender =  userRepository.findByEmail(authentication.getName()).orElse(null);
//        User receiver = userRepository.findByEmail(messageRequest.getReceiverEmail()).orElse(null);
//        String content = messageRequest.getContent();
//        if(receiver==null){
//            ObjectResponse objectResponse = new ObjectResponse("No email found with this email!",false,new HashMap<>());
//            return new ResponseEntity<>(objectResponse, HttpStatus.BAD_REQUEST);
//        }
//        Message newMessage = new Message(content,sender,receiver);
//        messageRepository.save(newMessage);
//        ObjectResponse objectResponse = new ObjectResponse("Message has beed Send!",true,new HashMap<>());
//        return new ResponseEntity<>(objectResponse, HttpStatus.OK);
//    }

    @GetMapping("/chat/{receiver_email}")
    public ResponseEntity<?> sendMessage(Authentication authentication, @PathVariable String receiver_email){
        User sender =  userRepository.findByEmail(authentication.getName()).orElse(null);
        User receiver = userRepository.findByEmail(receiver_email).orElse(null);
        if(receiver==null){
            ObjectResponse objectResponse = new ObjectResponse("No user found with this email!",false,new HashMap<>());
            return new ResponseEntity<>(objectResponse, HttpStatus.BAD_REQUEST);
        }
        List<Message> messages = messageRepository.findAll();
//        System.out.println(messages);
        List<Map<String,String>> userMessages = new ArrayList<>();
        for(Message message:messages){
            if ((message.getReceiver()==sender&&message.getSender()==receiver)||(message.getSender()==sender&&message.getReceiver()==receiver)){
                Map<String,String> msg = new HashMap<>();
                msg.put("senderEmail",message.getSender().getEmail());
                msg.put("senderUserName",message.getSender().getUserName());
                msg.put("content",message.getContent());
                msg.put("photo",message.getSender().getPhotoUrl());
                msg.put("image",message.getImage());
                msg.put("timestamp",message.getTimestamp().toString());
                userMessages.add(msg);
            }
        }
        ArrayResponse arrayResponse = new ArrayResponse("Message has been fetched successfully!",true,userMessages);
        return new ResponseEntity<>(arrayResponse, HttpStatus.OK);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        String email = authentication.getName();

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new GeneralResponse("File is empty", false, Map.of()));
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest()
                    .body(new GeneralResponse("Only images allowed", false, Map.of()));
        }

        if (file.getSize() > 4 * 1024 * 1024) {
            return ResponseEntity.badRequest()
                    .body(new GeneralResponse("Max size 4MB", false, Map.of()));
        }

        String photoUrl = userService.uploadImage(file);

        return ResponseEntity.ok(
                new GeneralResponse(
                        "photoUrl has been created!",
                        true,
                        Map.of("photoUrl", photoUrl)
                )
        );
    }
}
