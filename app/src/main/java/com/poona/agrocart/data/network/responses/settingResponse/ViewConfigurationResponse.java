package com.poona.agrocart.data.network.responses.settingResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewConfigurationResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ViewConfiguration> data = null;

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

    public List<ViewConfiguration> getData() {
        return data;
    }

    public void setData(List<ViewConfiguration> data) {
        this.data = data;
    }

    public static class ViewConfiguration {

        @SerializedName("configuration_name")
        @Expose
        private String configurationName;
        @SerializedName("configuration_value")
        @Expose
        private String configurationValue;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("email_notification_status")
        @Expose
        private String emailNotificationStatus;
        @SerializedName("app_notification_status")
        @Expose
        private String appNotificationStatus;

        public String getConfigurationName() {
            return configurationName;
        }

        public void setConfigurationName(String configurationName) {
            this.configurationName = configurationName;
        }

        public String getConfigurationValue() {
            return configurationValue;
        }

        public void setConfigurationValue(String configurationValue) {
            this.configurationValue = configurationValue;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEmailNotificationStatus() {
            return emailNotificationStatus;
        }

        public void setEmailNotificationStatus(String emailNotificationStatus) {
            this.emailNotificationStatus = emailNotificationStatus;
        }

        public String getAppNotificationStatus() {
            return appNotificationStatus;
        }

        public void setAppNotificationStatus(String appNotificationStatus) {
            this.appNotificationStatus = appNotificationStatus;
        }

    }


}
