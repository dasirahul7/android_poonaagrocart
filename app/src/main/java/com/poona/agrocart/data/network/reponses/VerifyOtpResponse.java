package com.poona.agrocart.data.network.reponses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpResponse {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private OtpData user;

    public OtpData getUser() {
        return user;
    }

    public void setUser(OtpData user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class OtpData {
        /*"id": "48",
                "mobile": "9340768580",
                "status": "1",
                "usertype": "2"*/
        @SerializedName("id")
        @Expose
        private String userId;
        @SerializedName("mobile")
        @Expose
        private String userMobile;
        @SerializedName("status")
        @Expose
        private String userStatus;
        @SerializedName("usertype")
        @Expose
        private String userType;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserMobile() {
            return userMobile;
        }

        public void setUserMobile(String userMobile) {
            this.userMobile = userMobile;
        }

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
    }
}
