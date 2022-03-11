package com.poona.agrocart.data.network.responses;


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
        /*"data": {
          "id": "49",
                  "verified": "1",
                  "mobile": "8224858710",
                  "status": "1",
                  "usertype": "2",
                  "state_id": "4",
                  "state_name": "Madhya Pradesh",
                  "city_id": "4",
                  "city_name": "Rourkela city",
                  "area_id": "7",
                  "area_name": "Kurla one"
      }*/
        @SerializedName("id")
        @Expose
        private String userId;
        @SerializedName("verified")
        @Expose
        private Integer verified;
        @SerializedName("mobile")
        @Expose
        private String userMobile;
        @SerializedName("status")
        @Expose
        private String userStatus;
        @SerializedName("usertype")
        @Expose
        private String userType;
        @SerializedName("state_name")
        @Expose
        private String stateName;
        @SerializedName("state_id")
        @Expose
        private String stateId;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("city_id")
        @Expose
        private String cityId;
        @SerializedName("area_name")
        @Expose
        private String areaName;
        @SerializedName("area_id")
        @Expose
        private String areaId;

        public String getStateId() {
            return stateId;
        }

        public void setStateId(String stateId) {
            this.stateId = stateId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public Integer getVerified() {
            return verified;
        }

        public void setVerified(Integer verified) {
            this.verified = verified;
        }

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
