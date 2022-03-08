
package com.poona.agrocart.data.network.responses.myOrderResponse.subscriptionBasketDetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscribeBasketCustomerDetailsResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("basket_subscription_details")
    @Expose
    private List<BasketSubscriptionDetail> basketSubscriptionDetails = null;

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

    public List<BasketSubscriptionDetail> getBasketSubscriptionDetails() {
        return basketSubscriptionDetails;
    }

    public void setBasketSubscriptionDetails(List<BasketSubscriptionDetail> basketSubscriptionDetails) {
        this.basketSubscriptionDetails = basketSubscriptionDetails;
    }

}
