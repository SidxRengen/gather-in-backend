package com.example.getherinjava.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArrayObjectResponse {
    private String message;
    private boolean success;
    private List<Map<String,Object>> data = new ArrayList<>();

    public ArrayObjectResponse(String message, boolean success, List<Map<String,Object>> data1) {
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

    public List<Map<String,Object>> getData() {
        return data;
    }

    public void setData(List<Map<String,Object>> data) {
        this.data = data;
    }
}
