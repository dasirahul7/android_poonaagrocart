package com.poona.agrocart.data.network.responses;

import android.os.Build;
import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.orderResponse.DeliverySlot;

import java.util.ArrayList;

public class BasketDetailsResponse extends BaseResponse {
    @SerializedName("basket_details")
    @Expose
    private BasketDetails basketDetail;
    @SerializedName("subscription")
    @Expose
    private Subscription subscription;

    public BasketDetails getBasketDetail() {
        return basketDetail;
    }

    public void setBasketDetail(BasketDetails basketDetail) {
        this.basketDetail = basketDetail;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public class BasketDetails{
        @SerializedName("basket_id")
        @Expose
        private String basketId;
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
        private String suVisibleTo;
        @SerializedName("storages_uses")
        @Expose
        private String storagesUses;
        @SerializedName("opi_visible_to")
        @Expose
        private String opiVisibleTo;
        @SerializedName("average_rating")
        @Expose
        private String averageRating;
        @SerializedName("no_or_users_rated")
        @Expose
        private String noOrUsersRated;
        @SerializedName("other_prouct_info")
        @Expose
        private String otherProuctInfo;
        @SerializedName("wp_visible_to")
        @Expose
        private String wpVisibleTo;
        @SerializedName("weight_policy")
        @Expose
        private String weightPolicy;
        @SerializedName("n_visible_to")
        @Expose
        private String nVisibleTo;
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
        private ArrayList<BasketProduct> basketProduct = null;
        @SerializedName("basket_imgs")
        @Expose
        private ArrayList<BasketImg> basketImgs = null;
        @SerializedName("is_favourite")
        @Expose
        private Integer isFavourite;
        @SerializedName("in_cart")
        @Expose
        private Integer inCart;
        @SerializedName("already_purchased")
        @Expose
        private Integer alreadyPurchased;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("rating")
        @Expose
        private Rating basketRating;
        @SerializedName("reviews")
        @Expose
        private ArrayList<Review> reviews = null;

        public Integer getAlreadyPurchased() {
            return alreadyPurchased;
        }


        public Subscription getSubscription() {
            return subscription;
        }

        public String getBasketId() {
            return basketId;
        }

        public void setBasketId(String basketId) {
            this.basketId = basketId;
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                return String.valueOf(Html.fromHtml(productDetails, Html.FROM_HTML_MODE_COMPACT)).trim();
            else return String.valueOf(Html.fromHtml(productDetails)).trim();
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                return String.valueOf(Html.fromHtml(aboutProduct, Html.FROM_HTML_MODE_COMPACT)).trim();
            else return String.valueOf(Html.fromHtml(aboutProduct)).trim();
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                return String.valueOf(Html.fromHtml(benifit, Html.FROM_HTML_MODE_COMPACT)).trim();
            else return String.valueOf(Html.fromHtml(benifit)).trim();
        }

        public void setBenifit(String benifit) {
            this.benifit = benifit;
        }

        public String getSuVisibleTo() {
            return suVisibleTo;
        }

        public void setSuVisibleTo(String suVisibleTo) {
            this.suVisibleTo = suVisibleTo;
        }

        public String getStoragesUses() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                return String.valueOf(Html.fromHtml(storagesUses, Html.FROM_HTML_MODE_COMPACT)).trim();
            else return String.valueOf(Html.fromHtml(storagesUses)).trim();
        }

        public void setStoragesUses(String storagesUses) {
            this.storagesUses = storagesUses;
        }

        public String getOpiVisibleTo() {
            return opiVisibleTo;
        }

        public void setOpiVisibleTo(String opiVisibleTo) {
            this.opiVisibleTo = opiVisibleTo;
        }

        public String getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(String averageRating) {
            this.averageRating = averageRating;
        }

        public String getNoOrUsersRated() {
            return noOrUsersRated;
        }

        public void setNoOrUsersRated(String noOrUsersRated) {
            this.noOrUsersRated = noOrUsersRated;
        }

        public String getOtherProuctInfo() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return String.valueOf(Html.fromHtml(otherProuctInfo,Html.FROM_HTML_MODE_COMPACT)).trim();
            }else return String.valueOf(Html.fromHtml(otherProuctInfo)).trim();
        }

        public void setOtherProuctInfo(String otherProuctInfo) {
            this.otherProuctInfo = otherProuctInfo;
        }

        public String getWpVisibleTo() {
            return wpVisibleTo;
        }

        public void setWpVisibleTo(String wpVisibleTo) {
            this.wpVisibleTo = wpVisibleTo;
        }

        public String getWeightPolicy() {
            return String.valueOf(Html.fromHtml(weightPolicy)).trim();
        }

        public void setWeightPolicy(String weightPolicy) {
            this.weightPolicy = weightPolicy;
        }

        public String getnVisibleTo() {
            return nVisibleTo;
        }

        public void setnVisibleTo(String nVisibleTo) {
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

        public ArrayList<BasketProduct> getBasketProduct() {
            return basketProduct;
        }

        public void setBasketProduct(ArrayList<BasketProduct> basketProduct) {
            this.basketProduct = basketProduct;
        }

        public ArrayList<BasketImg> getBasketImgs() {
            return basketImgs;
        }

        public void setBasketImgs(ArrayList<BasketImg> basketImgs) {
            this.basketImgs = basketImgs;
        }

        public Integer getIsFavourite() {
            return isFavourite;
        }

        public void setIsFavourite(Integer isFavourite) {
            this.isFavourite = isFavourite;
        }

        public Integer getInCart() {
            return inCart;
        }

        public void setInCart(Integer inCart) {
            this.inCart = inCart;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public Rating getBasketRating() {
            return basketRating;
        }

        public void setBasketRating(Rating basketRating) {
            this.basketRating = basketRating;
        }

        public ArrayList<Review> getReviews() {
            return reviews;
        }

        public void setReviews(ArrayList<Review> reviews) {
            this.reviews = reviews;
        }
    }

    public class Rating {

        @SerializedName("rating_id")
        @Expose
        private String ratingId;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("review")
        @Expose
        private String review;
        @SerializedName("average_rating")
        @Expose
        private Double averageRating;

        public String getRatingId() {
            return ratingId;
        }

        public void setRatingId(String ratingId) {
            this.ratingId = ratingId;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public Double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(Double averageRating) {
            this.averageRating = averageRating;
        }

    }

    public class BasketImg {

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

    public class BasketProduct {
        @SerializedName("bp_id")
        @Expose
        private String bpId;
        @SerializedName("product_name")
        @Expose
        private String productName;
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

    public static class Subscription {
        private Float basketRate;
        @SerializedName("subscription_types")
        @Expose
        private ArrayList<SubscriptionType> subscriptionTypes = null;
        @SerializedName("delivery_date")
        @Expose
        private String deliveryDate;
        @SerializedName("delivery_slots")
        @Expose
        private ArrayList<DeliverySlot> deliverySlots = null;

        public Float getBasketRate() {
            return basketRate;
        }

        public void setBasketRate(Float basketRate) {
            this.basketRate = basketRate;
        }

        public ArrayList<SubscriptionType> getSubscriptionTypes() {
            return subscriptionTypes;
        }

        public void setSubscriptionTypes(ArrayList<SubscriptionType> subscriptionTypes) {
            this.subscriptionTypes = subscriptionTypes;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public ArrayList<DeliverySlot> getDeliverySlots() {
            return deliverySlots;
        }

        public void setDeliverySlots(ArrayList<DeliverySlot> deliverySlots) {
            this.deliverySlots = deliverySlots;
        }

        public Subscription() {
        }

    }
    public class SubscriptionType {
        @SerializedName("subscription_type")
        @Expose
        private String subscriptionType;
        @SerializedName("subscription_type_name")
        @Expose
        private String subscriptionTypeName;

        public String getSubscriptionType() {
            return subscriptionType;
        }

        public void setSubscriptionType(String subscriptionType) {
            this.subscriptionType = subscriptionType;
        }

        public String getSubscriptionTypeName() {
            return subscriptionTypeName;
        }

        public void setSubscriptionTypeName(String subscriptionTypeName) {
            this.subscriptionTypeName = subscriptionTypeName;
        }
    }

}
