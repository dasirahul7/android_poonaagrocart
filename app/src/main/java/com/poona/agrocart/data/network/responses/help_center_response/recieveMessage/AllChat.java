package com.poona.agrocart.data.network.responses.help_center_response.recieveMessage;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllChat {

    @SerializedName("session_chat_id")
    @Expose
    private String sessionChatId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("image")
    @Expose
    private String image;

    public String getSessionChatId() {
        return sessionChatId;
    }

    public void setSessionChatId(String sessionChatId) {
        this.sessionChatId = sessionChatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
