package com.poona.agrocart.ui.sign_in.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("UA_mobile")
    String uMobile;

    @SerializedName("UA_otp")
    String uOtp;

    public String getuMobile() {
        return uMobile;
    }

    public void setuMobile(String uMobile) {
        this.uMobile = uMobile;
    }

    public String getuOtp() {
        return uOtp;
    }

    public void setuOtp(String uOtp) {
        this.uOtp = uOtp;
    }
}
