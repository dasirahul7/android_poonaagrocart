package com.poona.agrocart.ui.nav_offers;

public class Coupons {
    int id;
    String couponCode,discount,validDate,type,minimumAmount;

    public Coupons(int id, String couponCode, String discount, String validDate, String type, String minimumAmount) {
        this.id = id;
        this.couponCode = couponCode;
        this.discount = discount;
        this.validDate = validDate;
        this.type = type;
        this.minimumAmount = minimumAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        this.minimumAmount = minimumAmount;
    }
}
