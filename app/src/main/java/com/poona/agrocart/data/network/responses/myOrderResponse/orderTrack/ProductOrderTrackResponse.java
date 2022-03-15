
package com.poona.agrocart.data.network.responses.myOrderResponse.orderTrack;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductOrderTrackResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order_detials")
    @Expose
    private List<OrderTrack> orderDetials = null;

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

    public List<OrderTrack> getOrderDetials() {
        return orderDetials;
    }

    public void setOrderDetials(List<OrderTrack> orderDetials) {
        this.orderDetials = orderDetials;
    }

}
