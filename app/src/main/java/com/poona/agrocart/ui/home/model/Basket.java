package com.poona.agrocart.ui.home.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.R;

public class Basket {
    @SerializedName("id")
    @Expose
    private String id;
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
    @SerializedName("pd_visible_to")
    @Expose
    private String pdVisibleTo;
    @SerializedName("product_details")
    @Expose
    private String productDetails;
    @SerializedName("ap_visible_to")
    @Expose
    private String apVisibleTo;
    @SerializedName("about_product")
    @Expose
    private String aboutProduct;
    @SerializedName("b_visible_to")
    @Expose
    private String bVisibleTo;
    @SerializedName("benifit")
    @Expose
    private String benifit;
    @SerializedName("su_visible_to")
    @Expose
    private Object suVisibleTo;
    @SerializedName("storages_uses")
    @Expose
    private String storagesUses;
    @SerializedName("opi_visible_to")
    @Expose
    private Object opiVisibleTo;
    @SerializedName("other_prouct_info")
    @Expose
    private String otherProuctInfo;
    @SerializedName("wp_visible_to")
    @Expose
    private Object wpVisibleTo;
    @SerializedName("weight_policy")
    @Expose
    private String weightPolicy;
    @SerializedName("n_visible_to")
    @Expose
    private Object nVisibleTo;
    @SerializedName("nutrition")
    @Expose
    private String nutrition;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("gst_name")
    @Expose
    private String gstName;
    @SerializedName("hsn_code")
    @Expose
    private String hsnCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPdVisibleTo() {
        return pdVisibleTo;
    }

    public void setPdVisibleTo(String pdVisibleTo) {
        this.pdVisibleTo = pdVisibleTo;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public String getApVisibleTo() {
        return apVisibleTo;
    }

    public void setApVisibleTo(String apVisibleTo) {
        this.apVisibleTo = apVisibleTo;
    }

    public String getAboutProduct() {
        return aboutProduct;
    }

    public void setAboutProduct(String aboutProduct) {
        this.aboutProduct = aboutProduct;
    }

    public String getbVisibleTo() {
        return bVisibleTo;
    }

    public void setbVisibleTo(String bVisibleTo) {
        this.bVisibleTo = bVisibleTo;
    }

    public String getBenifit() {
        return benifit;
    }

    public void setBenifit(String benifit) {
        this.benifit = benifit;
    }

    public Object getSuVisibleTo() {
        return suVisibleTo;
    }

    public void setSuVisibleTo(Object suVisibleTo) {
        this.suVisibleTo = suVisibleTo;
    }

    public String getStoragesUses() {
        return storagesUses;
    }

    public void setStoragesUses(String storagesUses) {
        this.storagesUses = storagesUses;
    }

    public Object getOpiVisibleTo() {
        return opiVisibleTo;
    }

    public void setOpiVisibleTo(Object opiVisibleTo) {
        this.opiVisibleTo = opiVisibleTo;
    }

    public String getOtherProuctInfo() {
        return otherProuctInfo;
    }

    public void setOtherProuctInfo(String otherProuctInfo) {
        this.otherProuctInfo = otherProuctInfo;
    }

    public Object getWpVisibleTo() {
        return wpVisibleTo;
    }

    public void setWpVisibleTo(Object wpVisibleTo) {
        this.wpVisibleTo = wpVisibleTo;
    }

    public String getWeightPolicy() {
        return weightPolicy;
    }

    public void setWeightPolicy(String weightPolicy) {
        this.weightPolicy = weightPolicy;
    }

    public Object getnVisibleTo() {
        return nVisibleTo;
    }

    public void setnVisibleTo(Object nVisibleTo) {
        this.nVisibleTo = nVisibleTo;
    }

    public String getNutrition() {
        return nutrition;
    }

    public void setNutrition(String nutrition) {
        this.nutrition = nutrition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGstName() {
        return gstName;
    }

    public void setGstName(String gstName) {
        this.gstName = gstName;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    //    @BindingAdapter("setImage")
//    public static void loadImage(ImageView view, String img){
//        Glide.with(view.getContext())
//                .load(img)
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.img_bell_pepper_red).into(view);
//    }
}
