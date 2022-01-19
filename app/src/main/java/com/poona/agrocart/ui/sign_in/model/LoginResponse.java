package com.poona.agrocart.ui.sign_in.model;

import android.service.autofill.UserData;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    Integer status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    User user;

    @SerializedName("token")
    String token;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
