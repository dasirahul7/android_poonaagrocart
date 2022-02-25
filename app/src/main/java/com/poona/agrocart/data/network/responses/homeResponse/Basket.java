package com.poona.agrocart.data.network.responses.homeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Basket{
    @SerializedName("basket_id")
    @Expose
    private String basketId;
    @SerializedName("basket_name")
    @Expose
    private String basketName;
    @SerializedName("basket_rate")
    @Expose
    private String basketRate;
    @SerializedName("subscription_basket_rate")
    @Expose
    private String subscriptionBasketRate;
    @SerializedName("is_o3")
    @Expose
    private String isO3;
    @SerializedName("search_keywords")
    @Expose
    private String searchKeywords;
    @SerializedName("feature_img")
    @Expose
    private String featureImg;
    @SerializedName("is_favourite")
    @Expose
    private Integer isFavourite;
    @SerializedName("in_cart")
    @Expose
    private Integer inCart;
    @SerializedName("quantity")
    @Expose
    private String quantity;

    public String getBasketId() {
        return basketId;
    }

    public void setBasketId(String basketId) {
        this.basketId = basketId;
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

    public String getSubscriptionBasketRate() {
        return subscriptionBasketRate;
    }

    public void setSubscriptionBasketRate(String subscriptionBasketRate) {
        this.subscriptionBasketRate = subscriptionBasketRate;
    }

    public String getIsO3() {
        return isO3;
    }

    public void setIsO3(String isO3) {
        this.isO3 = isO3;
    }

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public String getFeatureImg() {
        return featureImg;
    }

    public void setFeatureImg(String featureImg) {
        this.featureImg = featureImg;
    }

    public Integer getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Integer isFavourite) {
        this.isFavourite = isFavourite;
    }

    public Integer getInCart() {
        return inCart;
    }

    public void setInCart(Integer inCart) {
        this.inCart = inCart;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

