package com.example.getherinjava.controller;

import com.example.getherinjava.dto.response.ObjectResponse;
import com.example.getherinjava.dto.socket.GroupMessageRequest;
import com.example.getherinjava.entry.Group;
import com.example.getherinjava.entry.GroupMessage;
import com.example.getherinjava.entry.User;
import com.example.getherinjava.repository.GroupMessageRepository;
import com.example.getherinjava.repository.GroupRepository;
import com.example.getherinjava.repository.UserRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GroupMessageSocketController {
    public SimpMessagingTemplate simpMessagingTemplate;
    public UserRepository userRepository;
    public GroupMessageRepository groupMessageRepository;
    public GroupRepository groupRepository;

    public GroupMessageSocketController(SimpMessagingTemplate simpMessagingTemplate,UserRepository userRepository,GroupRepository groupRepository,GroupMessageRepository groupMessageRepository){
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupMessageRepository = groupMessageRepository;
    }

    @MessageMapping("/send/group/message")
    public void sendGroupMessage(GroupMessageRequest groupMessageRequest){
        User sender= userRepository.findByEmail(groupMessageRequest.getSender_email()).orElse(null);
        if(sender==null){
            return;
        }
        Group currentGroup =  groupRepository.findById(groupMessageRequest.getGroup_id()).orElse(null);
        if(currentGroup==null){
            return;
        }
        System.out.println(groupMessageRequest.getContent());
        GroupMessage groupMessage = new GroupMessage(currentGroup,groupMessageRequest.content,sender);
        groupMessageRepository.save(groupMessage);
        Map<String,String> msg = new HashMap<>();
        msg.put("senderEmail",groupMessage.getSender().getEmail());
        msg.put("senderUserName",groupMessage.getSender().getUserName());
        msg.put("content",groupMessage.getContent());
        msg.put("photo",groupMessage.getSender().getPhotoUrl());
        msg.put("timestamp",groupMessage.getTimestamp().toString());
        simpMessagingTemplate.convertAndSend("/queue/group/message/"+currentGroup.getId(),msg);
    }
}
