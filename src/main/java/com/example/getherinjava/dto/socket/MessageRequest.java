package com.example.getherinjava.dto.socket;

import jakarta.validation.constraints.NotBlank;

public class MessageRequest {
    @NotBlank(message = "receiver email cannot be blank")
    private  String receiverEmail;
    @NotBlank(message = "sender email cannot be blank")
    private  String senderEmail;
    @NotBlank(message = "content cannot be blank")
    private String content;

    public MessageRequest(String receiverEmail,String senderEmail,String content){
        this.content = content;
        this.receiverEmail = receiverEmail;
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
