package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PinCodeResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("pincode_list")
    @Expose
    private ArrayList<PinCode> pinCode = null;

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

    public ArrayList<PinCode> getPinCode() {
        return pinCode;
    }

    public void setPinCode(ArrayList<PinCode> pinCode) {
        this.pinCode = pinCode;
    }

    public class PinCode {
        @SerializedName("pin_code")
        @Expose
        public String pinCode;

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }
    }
}
