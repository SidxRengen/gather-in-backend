package com.example.getherinjava.dto;

import com.example.getherinjava.entry.Message;
import com.example.getherinjava.entry.User;

import java.util.ArrayList;
import java.util.List;

public class UserResponse {
    private String message;
    private boolean success;
    private List<User> data = new ArrayList<>();

    public UserResponse(String message, boolean success, List<User> data) {
        this.message = message;
        this.success = success;
        this.data = data;
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

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}
