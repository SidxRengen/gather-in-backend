package com.example.getherinjava.dto.response;

import com.example.getherinjava.entry.Group;

import java.util.List;

public class AllGroupResponse {
    private String message;
    private boolean success;
    private List<Group> groupList;

    public String getMessage() {
        return message;
    }

    public AllGroupResponse(String message, boolean success, List<Group> groupList) {
        this.message = message;
        this.success = success;
        this.groupList = groupList;
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

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
}
