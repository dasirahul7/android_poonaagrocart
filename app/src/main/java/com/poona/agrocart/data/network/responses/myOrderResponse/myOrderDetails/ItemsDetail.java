
package com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemsDetail  {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("item_type") //item_type
    @Expose
    private String itemType;
    @SerializedName("product_name") //product_name
    @Expose
    private String productName;
    @SerializedName("basket_name") //basket_name
    @Expose
    private String basketName;
    @SerializedName("weight") //weight
    @Expose
    private String weight;
    @SerializedName("unit") //unit
    @Expose
    private String unit;
    @SerializedName("cost") //cost
    @Expose
    private String cost;
    @SerializedName("offer_cost") //offer_cost
    @Expose
    private String offerCost;
    @SerializedName("quantity") //quantity
    @Expose
    private String quantity;
    @SerializedName("feature_img") //feature_img
    @Expose
    private String featureImg;
    @SerializedName("should_deliver_on_date")
    @Expose
    private String shouldDeliverOnDate;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("deliery_slot_start_and_end_time")
    @Expose
    private String delierySlotStartAndEndTime;
    @SerializedName("total_price") //total_price
    @Expose
    private String totalPrice;

   /* protected ItemsDetail(Parcel in) {
        orderId = in.readString();
        itemType = in.readString();
        productName = in.readString();
        basketName = in.readString();
        weight = in.readString();
        unit = in.readString();
        cost = in.readString();
        offerCost = in.readString();
        quantity = in.readString();
        featureImg = in.readString();
        shouldDeliverOnDate = in.readString();
        orderStatus = in.readString();
        delierySlotStartAndEndTime = in.readString();
        totalPrice = in.readString();
    }*/


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getOfferCost() {
        return offerCost;
    }

    public void setOfferCost(String offerCost) {
        this.offerCost = offerCost;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getFeatureImg() {
        return featureImg;
    }

    public void setFeatureImg(String featureImg) {
        this.featureImg = featureImg;
    }

    public String getShouldDeliverOnDate() {
        return shouldDeliverOnDate;
    }

    public void setShouldDeliverOnDate(String shouldDeliverOnDate) {
        this.shouldDeliverOnDate = shouldDeliverOnDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDelierySlotStartAndEndTime() {
        return delierySlotStartAndEndTime;
    }

    public void setDelierySlotStartAndEndTime(String delierySlotStartAndEndTime) {
        this.delierySlotStartAndEndTime = delierySlotStartAndEndTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


}
