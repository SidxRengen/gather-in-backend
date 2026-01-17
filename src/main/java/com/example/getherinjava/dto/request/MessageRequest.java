package com.example.getherinjava.dto.request;

import jakarta.validation.constraints.NotBlank;


public class MessageRequest {
    @NotBlank(message = "receiver email cannot be blank")
    private String receiverEmail;

    @NotBlank(message = "content cannot be blank")
    private String content;

    public MessageRequest(String receiverEmail, String content) {
        this.receiverEmail = receiverEmail;
        this.content = content;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
