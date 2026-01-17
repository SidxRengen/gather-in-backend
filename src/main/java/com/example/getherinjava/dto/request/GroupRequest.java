package com.example.getherinjava.dto.request;


import com.example.getherinjava.controller.GroupController;
import com.example.getherinjava.entry.User;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class GroupRequest {

    @NotBlank(message = "please provide a group name")
    private String name;


    @NotBlank(message = "please provide a group description")
    private String description;

    private String photoUrl;

    public String getName() {
        return name;
    }
    public GroupRequest(){};
    public GroupRequest(String name, String description, String photoUrl, List<User> members, List<User> admins) {
        this.name = name;
        this.description = description;
        this.photoUrl = photoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
