package com.example.getherinjava.controller;


import com.example.getherinjava.dto.ResponseBody;
import com.example.getherinjava.dto.socket.MessageRequest;
import com.example.getherinjava.entry.Message;
import com.example.getherinjava.entry.User;
import com.example.getherinjava.repository.MessageRepository;
import com.example.getherinjava.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class MessageSocketController {

    public SimpMessagingTemplate simpMessagingTemplate;
    public MessageRepository messageRepository;
    public UserRepository userRepository;

    public MessageSocketController(MessageRepository messageRepository,SimpMessagingTemplate simpMessagingTemplate,UserRepository userRepository){
        this.messageRepository = messageRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userRepository = userRepository;
    }

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload MessageRequest messageRequest){
        System.out.println("Sender email: " + messageRequest.getSenderEmail());
        System.out.println("Receiver email: " + messageRequest.getReceiverEmail());

        User sender = userRepository
                .findByEmail(messageRequest.getSenderEmail())
                .orElse(null);

        User receiver = userRepository
                .findByEmail(messageRequest.getReceiverEmail())
                .orElse(null);

        System.out.println("Sender found: " + sender);
        System.out.println("Receiver found: " + receiver);

        if (sender == null || receiver == null) {
            System.out.println("❌ Sender or receiver not found. Aborting message save.");
            return;
        }

        Message message = new Message(messageRequest.getContent(),sender,receiver);
        messageRepository.save(message);
        Map<String,String> msg = new HashMap<>();
        msg.put("senderEmail",message.getSender().getEmail());
        msg.put("senderUserName",message.getSender().getUserName());
        msg.put("content",message.getContent());
        simpMessagingTemplate.convertAndSend("/queue/messages/"+messageRequest.getReceiverEmail(), msg);
        simpMessagingTemplate.convertAndSend("/queue/messages/"+messageRequest.getSenderEmail(), msg);
    }
}
