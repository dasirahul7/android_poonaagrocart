
package com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MyOrderDetailsResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("discount_message")
    @Expose
    private String discountMessage;
    @SerializedName("order_detials")
    @Expose
    private List<MyOrderDetial> orderDetials = null;

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

    public List<MyOrderDetial> getOrderDetials() {
        return orderDetials;
    }

    public void setOrderDetials(List<MyOrderDetial> orderDetials) {
        this.orderDetials = orderDetials;
    }

    public String getDiscountMessage() {
        return discountMessage;
    }

    public void setDiscountMessage(String discountMessage) {
        this.discountMessage = discountMessage;
    }
}
