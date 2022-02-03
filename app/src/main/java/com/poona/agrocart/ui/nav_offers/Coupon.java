package com.poona.agrocart.ui.nav_offers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupon {
    @SerializedName("id")
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
    @SerializedName("no_of_users")
    @Expose
    private String noOfUsers;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("terms_and_cond")
    @Expose
    private String termsAndCond;
    @SerializedName("coupon_image")
    @Expose
    private String couponImage;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("weight_id")
    @Expose
    private String weightId;
    @SerializedName("unit_id")
    @Expose
    private String unitId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    private String minUnit = "Order min Rs."
            ,percent = " %",max ="Max ",priceUnit ="Rs.";

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getMinUnit() {
        return minUnit;
    }

    public void setMinUnit(String minUnit) {
        this.minUnit = minUnit;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

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
        return discountPercentage.replace(".00","");
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

    public String getNoOfUsers() {
        return noOfUsers;
    }

    public void setNoOfUsers(String noOfUsers) {
        this.noOfUsers = noOfUsers;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTermsAndCond() {
        return termsAndCond;
    }

    public void setTermsAndCond(String termsAndCond) {
        this.termsAndCond = termsAndCond;
    }

    public String getCouponImage() {
        return couponImage;
    }

    public void setCouponImage(String couponImage) {
        this.couponImage = couponImage;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getWeightId() {
        return weightId;
    }

    public void setWeightId(String weightId) {
        this.weightId = weightId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

}
