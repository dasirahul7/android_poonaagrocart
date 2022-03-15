
package com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscribeBasketItemListResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("total_count")
    @Expose
    private String totalCount;
    @SerializedName("basket_subscription_details")
    @Expose
    private BasketSubscriptionList basketSubscriptionDetails;

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

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public BasketSubscriptionList getBasketSubscriptionDetails() {
        return basketSubscriptionDetails;
    }

    public void setBasketSubscriptionDetails(BasketSubscriptionList basketSubscriptionDetails) {
        this.basketSubscriptionDetails = basketSubscriptionDetails;
    }

}
