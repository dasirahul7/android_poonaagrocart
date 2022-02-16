
package com.poona.agrocart.data.network.responses.favoutiteResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favourite {

    @SerializedName("item_type")
    @Expose
    private String itemType;
    @SerializedName("favourite_id")
    @Expose
    private String favouriteId;
    @SerializedName("basket_id")
    @Expose
    private String basketId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("basket_name")
    @Expose
    private String basketName;
    @SerializedName("basket_rate")
    @Expose
    private String basketRate;
    @SerializedName("feature_img")
    @Expose
    private String featureImg;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("pu_id")
    @Expose
    private String puId;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(String favouriteId) {
        this.favouriteId = favouriteId;
    }

    public String getBasketId() {
        return basketId;
    }

    public void setBasketId(String basketId) {
        this.basketId = basketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    public String getBasketRate() {
        return basketRate;
    }

    public void setBasketRate(String basketRate) {
        this.basketRate = basketRate;
    }

    public String getFeatureImg() {
        return featureImg;
    }

    public void setFeatureImg(String featureImg) {
        this.featureImg = featureImg;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

}
