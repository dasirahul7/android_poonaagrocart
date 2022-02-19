package com.poona.agrocart.ui.order_summary.model;

public class ProductAndPrice {
    private String productName, dividedPrice, finalPrice;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDividedPrice() {
        return dividedPrice;
    }

    public void setDividedPrice(String dividedPrice) {
        this.dividedPrice = dividedPrice;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }
}
