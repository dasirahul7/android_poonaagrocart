package com.poona.agrocart.data.network.responses.orderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderSuccessResponse {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    /*after order or subscription done*/
    @SerializedName("subscription_id")
    @Expose
    private String subscriptionId;
    @SerializedName("order_id")
    @Expose
    private String orderId;

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

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
