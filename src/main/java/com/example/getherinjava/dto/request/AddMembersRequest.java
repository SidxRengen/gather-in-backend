package com.example.getherinjava.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class AddMembersRequest {
    @NotBlank(message = "members list cannot be null")
    List<String> members = new ArrayList<>();

    public AddMembersRequest(List<String> members) {
        this.members = members;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
