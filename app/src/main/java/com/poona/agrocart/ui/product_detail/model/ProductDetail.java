package com.poona.agrocart.ui.product_detail.model;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ProductDetail {
    public String productName;
    public String productLocation;
    public ArrayList<String> weightOfProduct;
    public ArrayList<String> productImages;
    public String productImage;
    public String price;
    public String quantity;
    public String productDetailBrief;
    public String aboutProduct;
    public String benefitProduct;
    public String storageUses;
    public String otherProductInfo;
    public String validityWeightPolicy;
    public String nutritionDetailBrief;
    public String productReview;
    public String ratings;
    public Boolean basket;

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getAboutProduct() {
        return aboutProduct;
    }

    public void setAboutProduct(String aboutProduct) {
        this.aboutProduct = aboutProduct;
    }

    public String getBenefitProduct() {
        return benefitProduct;
    }

    public void setBenefitProduct(String benefitProduct) {
        this.benefitProduct = benefitProduct;
    }

    public String getStorageUses() {
        return storageUses;
    }

    public void setStorageUses(String storageUses) {
        this.storageUses = storageUses;
    }

    public String getOtherProductInfo() {
        return otherProductInfo;
    }

    public void setOtherProductInfo(String otherProductInfo) {
        this.otherProductInfo = otherProductInfo;
    }

    public String getValidityWeightPolicy() {
        return validityWeightPolicy;
    }

    public void setValidityWeightPolicy(String validityWeightPolicy) {
        this.validityWeightPolicy = validityWeightPolicy;
    }

    public String getProductReview() {
        return productReview;
    }

    public void setProductReview(String productReview) {
        this.productReview = productReview;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public ArrayList<String> getWeightOfProduct() {
        return weightOfProduct;
    }

    public void setWeightOfProduct(ArrayList<String> weightOfProduct) {
        this.weightOfProduct = weightOfProduct;
    }

    public ArrayList<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(ArrayList<String> productImages) {
        this.productImages = productImages;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductDetailBrief() {
        return productDetailBrief;
    }

    public void setProductDetailBrief(String productDetailBrief) {
        this.productDetailBrief = productDetailBrief;
    }

    public String getNutritionDetailBrief() {
        return nutritionDetailBrief;
    }

    public void setNutritionDetailBrief(String nutritionDetailBrief) {
        this.nutritionDetailBrief = nutritionDetailBrief;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public Boolean getBasket() {
        return basket;
    }

    public void setBasket(Boolean basket) {
        this.basket = basket;
    }
}