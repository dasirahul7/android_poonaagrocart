package com.poona.agrocart.data.network.responses.splashResponse;

import android.window.SplashScreen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SplashResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private SplashData splashData;

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

    public SplashData getSplashData() {
        return splashData;
    }

    public void setSplashData(SplashData splashData) {
        this.splashData = splashData;
    }
}
