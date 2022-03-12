package com.poona.agrocart.ui.search.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonSearchItem {
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
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_visible_to")
    @Expose
    private String productVisibleTo;
    @SerializedName("special_offer")
    @Expose
    private String specialOffer;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("pu_id")
    @Expose
    private String puId;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("perc_discount")
    @Expose
    private String percDiscount;
    @SerializedName("selling_price")
    @Expose
    private String sellingPrice;
    @SerializedName("offer_price")
    @Expose
    private String offerPrice;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("unit_limit")
    @Expose
    private String unitLimit;

    private String itemType;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

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

    public String getProductVisibleTo() {
        return productVisibleTo;
    }

    public void setProductVisibleTo(String productVisibleTo) {
        this.productVisibleTo = productVisibleTo;
    }

    public String getSpecialOffer() {
        return specialOffer;
    }

    public void setSpecialOffer(String specialOffer) {
        this.specialOffer = specialOffer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPercDiscount() {
        return percDiscount;
    }

    public void setPercDiscount(String percDiscount) {
        this.percDiscount = percDiscount;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitLimit() {
        return unitLimit;
    }

    public void setUnitLimit(String unitLimit) {
        this.unitLimit = unitLimit;
    }
}
