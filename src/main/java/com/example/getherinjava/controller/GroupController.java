package com.example.getherinjava.controller;

import com.example.getherinjava.dto.request.AddMembersRequest;
import com.example.getherinjava.dto.request.GroupRequest;
import com.example.getherinjava.dto.response.*;
import com.example.getherinjava.entry.Group;
import com.example.getherinjava.entry.User;
import com.example.getherinjava.repository.GroupMessageRepository;
import com.example.getherinjava.repository.GroupRepository;
import com.example.getherinjava.repository.UserRepository;
import com.example.getherinjava.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/group")
public class GroupController {

    GroupRepository groupRepository;
    UserRepository userRepository;
    UserService userService;
    GroupMessageRepository groupMessageRepository;
    public GroupController(GroupRepository groupRepository,UserRepository userRepository,   UserService userService,GroupMessageRepository groupMessageRepository){
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.userService= userService;
        this.groupMessageRepository = groupMessageRepository;
    }
    String photoUrl = null;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createGroup(
            @RequestPart("data") GroupRequest groupRequest,
            @RequestPart("file") MultipartFile file,
            Authentication authentication
    ) {
        User currentUser = userRepository
                .findByEmail(authentication.getName())
                .orElse(null);

        if (currentUser == null) {
            return new ResponseEntity<>(
                    new ObjectResponse("Unauthorized", false, new HashMap<>()),
                    HttpStatus.UNAUTHORIZED
            );
        }

        String photoUrl = null;

        if (file != null && !file.isEmpty()) {

            if (!file.getContentType().startsWith("image/")) {
                return new ResponseEntity<>(
                        new ObjectResponse("Only image files allowed", false, new HashMap<>()),
                        HttpStatus.BAD_REQUEST
                );
            }

            if (file.getSize() > 2 * 1024 * 1024) {
                return new ResponseEntity<>(
                        new ObjectResponse("Max image size is 2MB", false, new HashMap<>()),
                        HttpStatus.BAD_REQUEST
                );
            }

            photoUrl = userService.uploadImage(file);
        }

        List<User> admins = List.of(currentUser);
        List<User> members = List.of();

        Group newGroup = new Group(
                groupRequest.getName(),
                groupRequest.getDescription(),
                photoUrl,
                members,
                admins
        );

        groupRepository.save(newGroup);

        return new ResponseEntity<>(
                new ObjectResponseDTO("Group created successfully", true, newGroup),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable Long groupId){
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group==null){
            ObjectResponse objectResponse= new ObjectResponse("group not exists with this id",false, new HashMap<>());
            return new ResponseEntity<>(objectResponse,HttpStatus.BAD_REQUEST);
        }
        System.out.println("group info "+groupId+" : "+group.toString());
        GeneralResponse groupResponse = new GeneralResponse("group has been fetched successfully",true,group);
        return new ResponseEntity<>(groupResponse,HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserGroup(Authentication authentication) {

        if (authentication == null) {
            return new ResponseEntity<>(
                    new ObjectResponse("Unauthorized", false, new HashMap<>()),
                    HttpStatus.UNAUTHORIZED
            );
        }

        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(
                    new ObjectResponse("User does not exist", false, new HashMap<>()),
                    HttpStatus.UNAUTHORIZED
            );
        }
        if(groupMessageRepository.findAll().isEmpty()){
            return new ResponseEntity<>(
                    new ArrayResponse("No Active Group is found", true, List.of()),
                    HttpStatus.OK
            );
        }
        List<Object[]> rawData = groupRepository.getActiveGroup(userEmail);
        if (rawData == null || rawData.isEmpty()) {
            return ResponseEntity.ok(
                    new ArrayObjectResponse(
                            "No active groups found",
                            true,
                            Collections.emptyList()
                    )
            );
        }
        List<Map<String, Object>> response = rawData.stream().map(obj -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", (Long) obj[0]);
            m.put("userName", (String) obj[1]);
            m.put("photo", (String) obj[2]);
            m.put("timestamp", obj[3]);
            m.put("description", obj[4]);
            return m;
        }).toList();

        ArrayObjectResponse arrayResponse =
                new ArrayObjectResponse("All groups fetched successfully", true,response);

        return new ResponseEntity<>(arrayResponse, HttpStatus.OK);
    }

    @PostMapping("/addMembers/{group_id}")
    public ResponseEntity<?> addGroupMembers(@PathVariable Long group_id,@RequestBody AddMembersRequest addMembersRequest){
        Group group = groupRepository.findById(group_id).orElse(null);
        if (group == null) {
            return ResponseEntity.badRequest()
                    .body(new ObjectResponse("Group not found", false, new HashMap<>()));
        }

        List<User> updatedMembers = new ArrayList<>();

        for (String email : addMembersRequest.getMembers()) {
            userRepository.findByEmail(email).ifPresent(updatedMembers::add);
        }

        group.setMembers(updatedMembers);
        groupRepository.save(group);

        return ResponseEntity.ok(
                new ObjectResponse("Group members updated successfully", true, new HashMap<>())
        );
    }

}
