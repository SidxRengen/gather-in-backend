package com.example.getherinjava.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArrayResponse {
    private String message;
    private boolean success;
    private List<Map<String,String>> data = new ArrayList<>();

    public ArrayResponse(String message, boolean success, List<Map<String,String>> data1) {
        this.message = message;
        this.success = success;
        this.data = data1;
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

    public List<Map<String,String>> getData() {
        return data;
    }

    public void setData(List<Map<String,String>> data) {
        this.data = data;
    }
}
