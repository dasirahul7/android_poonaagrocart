package com.poona.agrocart.data.network.responses.help_center_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendMessage {

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
