package com.example.getherinjava.dto;

import java.util.Map;

public class ObjectResponse {
    private String message;
    private boolean success;
    private Map<String,String> data;

    public ObjectResponse(String message, boolean success, Map<String, String> data) {
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

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
