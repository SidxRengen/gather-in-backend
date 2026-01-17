package com.example.getherinjava.dto.response;

import com.example.getherinjava.entry.Group;
import com.example.getherinjava.entry.User;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

public class GroupResponse {
    private String message;
    private boolean success;
    private Group group;

    public GroupResponse(String message, boolean success, Group group) {
        this.message = message;
        this.success = success;
        this.group = group;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
