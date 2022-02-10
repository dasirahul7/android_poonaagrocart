package com.poona.agrocart.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Product {
    @SerializedName("sequence_no")
    @Expose
    private String sequenceNo;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("special_offer")
    @Expose
    private String specialOffer;
    @SerializedName("is_o3")
    @Expose
    private String isO3;
    @SerializedName("product_visible_to")
    @Expose
    private String productVisibleTo;
    @SerializedName("feature_img")
    @Expose
    private String featureImg;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("product_units")
    @Expose
    private ArrayList<ProductUnit> productUnits = null;
    private String priceUnit= "Rs.";

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpecialOffer() {
        return specialOffer;
    }

    public void setSpecialOffer(String specialOffer) {
        this.specialOffer = specialOffer;
    }

    public String getIsO3() {
        return isO3;
    }

    public void setIsO3(String isO3) {
        this.isO3 = isO3;
    }

    public String getProductVisibleTo() {
        return productVisibleTo;
    }

    public void setProductVisibleTo(String productVisibleTo) {
        this.productVisibleTo = productVisibleTo;
    }

    public String getFeatureImg() {
        return featureImg;
    }

    public void setFeatureImg(String featureImg) {
        this.featureImg = featureImg;
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

    public ArrayList<ProductUnit> getProductUnits() {
        return productUnits;
    }

    public void setProductUnits(ArrayList<ProductUnit> productUnits) {
        this.productUnits = productUnits;
    }
}
