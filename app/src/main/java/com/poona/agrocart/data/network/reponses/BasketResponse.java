package com.poona.agrocart.data.network.reponses;

import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BasketResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private BasketData data;

    public BasketData getData() {
        return data;
    }

    public void setData(BasketData data) {
        this.data = data;
    }
    public class BasketData{
        @SerializedName("basket_list")
        @Expose
        private ArrayList<Basket> baskets;

        public ArrayList<Basket> getBaskets() {
            return baskets;
        }

        public void setBaskets(ArrayList<Basket> baskets) {
            this.baskets = baskets;
        }
    }
    public class Basket {
        @SerializedName("basket_id")
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
        @SerializedName("basket_product")
        @Expose
        private ArrayList<BasketProduct> basketUnits;
        @SerializedName("basket_imgs")
        @Expose
        private List<BasketImges> basketImges;
        @SerializedName("is_favourite")
        private int isFavourite;
        @SerializedName("in_cart")
        private int inCart;
        @SerializedName("quantity")
        private int quantity;
        private BasketProduct basketUnit;
        private String accurateWeight;

        public int getIsFavourite() {
            return isFavourite;
        }

        public void setIsFavourite(int isFavourite) {
            this.isFavourite = isFavourite;
        }

        public int getInCart() {
            return inCart;
        }

        public void setInCart(int inCart) {
            this.inCart = inCart;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public List<BasketImges> getBasketImges() {
            return basketImges;
        }

        public void setBasketImges(List<BasketImges> basketImges) {
            this.basketImges = basketImges;
        }

        public ArrayList<BasketProduct> getBasketUnits() {
            return basketUnits;
        }

        public void setBasketUnits(ArrayList<BasketProduct> basketUnits) {
            this.basketUnits = basketUnits;
        }

        public BasketProduct getBasketUnit() {
            return basketUnit;
        }

        public void setBasketUnit(BasketProduct basketUnit) {
            this.basketUnit = basketUnit;
        }

        public String getAccurateWeight() {
            return accurateWeight;
        }

        public void setAccurateWeight(String accurateWeight) {
            this.accurateWeight = accurateWeight;
        }

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
            return Html.fromHtml(productDetails).toString().trim();
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
            return Html.fromHtml(aboutProduct).toString().trim();
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
            return Html.fromHtml(benifit).toString().trim();
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
            return Html.fromHtml(storagesUses).toString().trim();
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
            return Html.fromHtml(otherProuctInfo).toString().trim();
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
            return Html.fromHtml(weightPolicy).toString().trim();
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
            return Html.fromHtml(nutrition).toString().trim();
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

    }

    public class BasketProduct {
        @SerializedName("bp_id")
        @Expose
        private String bpId;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("basket_id_fk")
        @Expose
        private String basketIdFk;
        @SerializedName("weight_and_unit")
        @Expose
        private String weightAndUnit;
        @SerializedName("status")
        @Expose
        private String status;

        public String getBpId() {
            return bpId;
        }

        public void setBpId(String bpId) {
            this.bpId = bpId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getBasketIdFk() {
            return basketIdFk;
        }

        public void setBasketIdFk(String basketIdFk) {
            this.basketIdFk = basketIdFk;
        }

        public String getWeightAndUnit() {
            return weightAndUnit;
        }

        public void setWeightAndUnit(String weightAndUnit) {
            this.weightAndUnit = weightAndUnit;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public class BasketImges {

        @SerializedName("basket_img")
        @Expose
        private String basketImg;

        public String getBasketImg() {
            return basketImg;
        }

        public void setBasketImg(String basketImg) {
            this.basketImg = basketImg;
        }
    }
}
