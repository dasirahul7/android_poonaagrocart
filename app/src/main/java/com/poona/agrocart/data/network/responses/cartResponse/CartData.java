package com.poona.agrocart.data.network.responses.cartResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartData {

    @SerializedName("item_type")
    @Expose
    private String itemType;
    @SerializedName("cart_id")
    @Expose
    private String cartId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("pu_id")
    @Expose
    private String puId;
    @SerializedName("unit_id")
    @Expose
    private String unitId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("price_per_quantity")
    @Expose
    private String pricePerQuantity;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("feature_img")
    @Expose
    private String featureImg;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("basket_id_fk")
    @Expose
    private String basketIdFk;
    @SerializedName("basket_name")
    @Expose
    private String basketName;
    @SerializedName("basket_rate")
    @Expose
    private String basketRate;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPuId() {
        return puId;
    }

    public void setPuId(String puId) {
        this.puId = puId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPricePerQuantity() {
        return pricePerQuantity;
    }

    public void setPricePerQuantity(String pricePerQuantity) {
        this.pricePerQuantity = pricePerQuantity;
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

    public String getFeatureImg() {
        return featureImg;
    }

    public void setFeatureImg(String featureImg) {
        this.featureImg = featureImg;
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

    public String getBasketIdFk() {
        return basketIdFk;
    }

    public void setBasketIdFk(String basketIdFk) {
        this.basketIdFk = basketIdFk;
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
}
