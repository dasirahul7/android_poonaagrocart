package com.poona.agrocart.data.network.responses.orderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.BaseResponse;

import java.util.ArrayList;

public class ApplyCouponResponse {
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("sub_total")
    @Expose
    public Integer subTotal;
    @SerializedName("discount")
    @Expose
    public float discount;
    @SerializedName("delivery_charges")
    @Expose
    public Integer deliveryCharges;
    @SerializedName("total_amount")
    @Expose
    public float totalAmount;
    @SerializedName("coupon_id")
    @Expose
    public String couponId;
    @SerializedName("items_details")
    @Expose
    private ArrayList<ItemsDetail> itemsDetails = null;

    public ArrayList<ItemsDetail> getItemsDetails() {
        return itemsDetails;
    }

    public void setItemsDetails(ArrayList<ItemsDetail> itemsDetails) {
        this.itemsDetails = itemsDetails;
    }
}
