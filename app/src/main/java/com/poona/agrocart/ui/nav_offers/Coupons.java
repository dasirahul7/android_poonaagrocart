package com.poona.agrocart.ui.nav_offers;

public class Coupons {
    int id;
    String title, couponCode, discount, maxDiscount, validDate, type, minimumAmount, validMsg;

    public Coupons(int id, String couponCode, String discount, String validDate, String type, String minimumAmount, String maxDiscount) {
        this.id = id;
        this.couponCode = couponCode;
        this.discount = discount;
        this.validDate = validDate;
        this.type = type;
        this.minimumAmount = minimumAmount;
        this.maxDiscount = maxDiscount;
    }

    public Coupons(int id, String title, String couponCode, String validMsg) {
        this.id = id;
        this.title = title;
        this.couponCode = couponCode;
        this.validMsg = validMsg;
    }

    public String getValidMsg() {
        return validMsg;
    }

    public void setValidMsg(String validMsg) {
        this.validMsg = validMsg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(String maxDiscount) {
        this.maxDiscount = maxDiscount;
    }
}
