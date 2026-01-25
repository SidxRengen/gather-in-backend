package com.example.getherinjava.dto.socket;

import com.example.getherinjava.entry.Group;
import com.example.getherinjava.entry.User;
import jakarta.validation.constraints.NotBlank;

public class GroupMessageRequest {
    @NotBlank(message = "message cannot be blank")
    public String content;

    @NotBlank(message = "user is not valid")
    public String sender_email;

    @NotBlank(message = "Please select a valid group")
    public Long group_id;
    public String image;
    public GroupMessageRequest(String content, Long group_id,String sender_email,String image) {
        this.content = content;
        this.group_id = group_id;
        this.sender_email = sender_email;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSender_email() {
        return sender_email;
    }

    public void setSender_email(String sender_email) {
        this.sender_email = sender_email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }
}
