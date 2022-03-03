package com.poona.agrocart.data.network.responses;

import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupon {
    @SerializedName("coupon_id")
    @Expose
    private String id;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("discount_percentage")
    @Expose
    private String discountPercentage;
    @SerializedName("fromdate")
    @Expose
    private String fromdate;
    @SerializedName("todate")
    @Expose
    private String todate;
    @SerializedName("min_order_amount")
    @Expose
    private String minOrderAmount;
    @SerializedName("discount_max_amount")
    @Expose
    private String discountMaxAmount;

    @SerializedName("terms_and_cond")
    @Expose
    private String termsAndCond;
//        private String minUnit = "Order min Rs."
//                ,percent = " %",max ="Max ",priceUnit ="Rs.";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDiscountPercentage() {
        return discountPercentage.replace(".00", "");
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(String minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public String getDiscountMaxAmount() {
        return discountMaxAmount;
    }

    public void setDiscountMaxAmount(String discountMaxAmount) {
        this.discountMaxAmount = discountMaxAmount;
    }


    public String getTermsAndCond() {
        return String.valueOf(Html.fromHtml(termsAndCond));
    }

    public void setTermsAndCond(String termsAndCond) {
        this.termsAndCond = termsAndCond;
    }







}
