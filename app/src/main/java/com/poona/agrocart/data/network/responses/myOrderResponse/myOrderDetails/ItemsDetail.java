
package com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemsDetail {

    @SerializedName("item_type")
    @Expose
    private String itemType;
    @SerializedName("basket_name")
    @Expose
    private Object basketName;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("unit")
    @Expose
    private String unit;
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
    private Object featureImg;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Object getBasketName() {
        return basketName;
    }

    public void setBasketName(Object basketName) {
        this.basketName = basketName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public Object getFeatureImg() {
        return featureImg;
    }

    public void setFeatureImg(Object featureImg) {
        this.featureImg = featureImg;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

}
