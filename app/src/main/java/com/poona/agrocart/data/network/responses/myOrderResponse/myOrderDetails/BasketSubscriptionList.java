
package com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class BasketSubscriptionList {

    @SerializedName("items_details")
    @Expose
    private List<com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.ItemsDetail> itemsDetails = null;

    public List<com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.ItemsDetail> getItemsDetails() {
        return itemsDetails;
    }

    public void setItemsDetails(List<com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.ItemsDetail> itemsDetails) {
        this.itemsDetails = itemsDetails;
    }

}
