package com.example.getherinjava.controller;

import com.example.getherinjava.dto.request.MessageRequest;
import com.example.getherinjava.dto.response.ObjectResponse;
import com.example.getherinjava.dto.response.ArrayResponse;
import com.example.getherinjava.entry.Message;
import com.example.getherinjava.entry.User;
import com.example.getherinjava.repository.MessageRepository;
import com.example.getherinjava.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public MessageController(UserRepository userRepository,MessageRepository messageRepository){
        this.messageRepository = messageRepository;
        this.userRepository= userRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(Authentication authentication,@Valid @RequestBody MessageRequest messageRequest){
        User sender =  userRepository.findByEmail(authentication.getName()).orElse(null);
        User receiver = userRepository.findByEmail(messageRequest.getReceiverEmail()).orElse(null);
        String content = messageRequest.getContent();
        if(receiver==null){
            ObjectResponse objectResponse = new ObjectResponse("No email found with this email!",false,new HashMap<>());
            return new ResponseEntity<>(objectResponse, HttpStatus.BAD_REQUEST);
        }
        Message newMessage = new Message(content,sender,receiver);
        messageRepository.save(newMessage);
        ObjectResponse objectResponse = new ObjectResponse("Message has beed Send!",true,new HashMap<>());
        return new ResponseEntity<>(objectResponse, HttpStatus.OK);
    }

    @GetMapping("/chat/{receiver_email}")
    public ResponseEntity<?> sendMessage(Authentication authentication, @PathVariable String receiver_email){
        User sender =  userRepository.findByEmail(authentication.getName()).orElse(null);
        User receiver = userRepository.findByEmail(receiver_email).orElse(null);
        if(receiver==null){
            ObjectResponse objectResponse = new ObjectResponse("No user found with this email!",false,new HashMap<>());
            return new ResponseEntity<>(objectResponse, HttpStatus.BAD_REQUEST);
        }
        List<Message> messages = messageRepository.findAll();
        List<Map<String,String>> userMessages = new ArrayList<>();
        for(Message message:messages){
            if ((message.getReceiver()==sender&&message.getSender()==receiver)||(message.getSender()==sender&&message.getReceiver()==receiver)){
                Map<String,String> msg = new HashMap<>();
                msg.put("senderEmail",message.getSender().getEmail());
                msg.put("senderUserName",message.getSender().getUserName());
                msg.put("content",message.getContent());
                msg.put("photo",message.getSender().getPhotoUrl());
                msg.put("timestamp",message.getTimestamp().toString());
                userMessages.add(msg);
            }
        }
        ArrayResponse arrayResponse = new ArrayResponse("Message has been fetched successfully!",true,userMessages);
        return new ResponseEntity<>(arrayResponse, HttpStatus.OK);
    }

}
