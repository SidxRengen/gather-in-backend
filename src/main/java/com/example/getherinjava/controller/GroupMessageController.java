package com.example.getherinjava.controller;

import com.example.getherinjava.dto.response.GeneralResponse;
import com.example.getherinjava.entry.GroupMessage;
import com.example.getherinjava.repository.GroupMessageRepository;
import com.example.getherinjava.repository.GroupRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group/message")
public class GroupMessageController {
    public GroupRepository groupRepository;
    public GroupMessageRepository groupMessageRepository;
    public GroupMessageController(GroupRepository groupRepository, GroupMessageRepository groupMessageRepository){
        this.groupRepository = groupRepository;
        this.groupMessageRepository = groupMessageRepository;
    }
    @GetMapping("/{group_id}")
    public ResponseEntity<?> receiveMessage(@PathVariable Long group_id){
        List<GroupMessage> allMessages=  groupMessageRepository.findAll();

        List<Map<String,String>> groupMessages = allMessages.stream().map(msg->{
            if (msg.getGroup().getId() == group_id){
                Map<String,String> newMsg = new HashMap<>();
                newMsg.put("senderEmail",msg.getSender().getEmail());
                newMsg.put("senderUserName",msg.getSender().getUserName());
                newMsg.put("content",msg.getContent());
                newMsg.put("photo",msg.getSender().getPhotoUrl());
                newMsg.put("timestamp",msg.getTimestamp().toString());
                return newMsg;
            }
            return null;
        }).toList();
        GeneralResponse generalResponse = new GeneralResponse("all message of this group has been fetched",true,groupMessages);
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }
}
