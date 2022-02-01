package com.poona.agrocart.ui.home.model;

import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeasonalProduct {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("seasonal_product_visible_to")
    @Expose
    private String seasonalProductVisibleTo;
    @SerializedName("seasonal_product_name")
    @Expose
    private String seasonalProductName;
    @SerializedName("product_image")
    @Expose
    private String product_image;
    @SerializedName("product_ads_amage")
    @Expose
    private String productAdsAmage;
    @SerializedName("product_details")
    @Expose
    private String productDetails;
    @SerializedName("status")
    @Expose
    private String status;
    public String sProductPlace ="Alphanso",type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeasonalProductVisibleTo() {
        return seasonalProductVisibleTo;
    }

    public void setSeasonalProductVisibleTo(String seasonalProductVisibleTo) {
        this.seasonalProductVisibleTo = seasonalProductVisibleTo;
    }

    public String getSeasonalProductName() {
        return seasonalProductName;
    }

    public void setSeasonalProductName(String seasonalProductName) {
        this.seasonalProductName = seasonalProductName;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getsProductImage() {
        return product_image;
    }

    public void setsProductImage(String sProductImage) {
        this.product_image = sProductImage;
    }

    public String getProductAdsAmage() {
        return productAdsAmage;
    }

    public void setProductAdsAmage(String productAdsAmage) {
        this.productAdsAmage = productAdsAmage;
    }

    public String getProductDetails() {
        return Html.fromHtml(productDetails).toString();
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getsProductPlace() {
        return sProductPlace;
    }

    public void setsProductPlace(String sProductPlace) {
        this.sProductPlace = sProductPlace;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
