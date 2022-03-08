
package com.poona.agrocart.data.network.responses.myOrderResponse.subscriptionBasketDetails;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscriptionBasketItemsDetail {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("item_type")
    @Expose
    private String itemType;
    @SerializedName("basket_name")
    @Expose
    private String basketName;
    @SerializedName("weight")
    @Expose
    private Object weight;
    @SerializedName("unit")
    @Expose
    private Object unit;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("offer_cost")
    @Expose
    private String offerCost;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("feature_img")
    @Expose
    private String featureImg;
    @SerializedName("should_deliver_on_date")
    @Expose
    private String shouldDeliverOnDate;
    @SerializedName("deliery_slot_start_and_end_time")
    @Expose
    private String delierySlotStartAndEndTime;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;

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

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public Object getUnit() {
        return unit;
    }

    public void setUnit(Object unit) {
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

}
