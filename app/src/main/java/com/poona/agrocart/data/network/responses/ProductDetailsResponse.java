package com.poona.agrocart.data.network.responses;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.R;

import java.io.Serializable;
import java.util.List;

public class ProductDetailsResponse extends BaseResponse {
    @SerializedName("product_details")
    @Expose
    private ProductDetails productDetails;

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails = productDetails;
    }

    public class ProductDetails {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("product_visible_to")
        @Expose
        private String productVisibleTo;
        @SerializedName("brand_id_fk")
        @Expose
        private String brandIdFk;
        @SerializedName("special_offer")
        @Expose
        private String specialOffer;
        @SerializedName("gst_id_fk")
        @Expose
        private String gstIdFk;
        @SerializedName("hsn_id_fk")
        @Expose
        private String hsnIdFk;
        @SerializedName("is_o3")
        @Expose
        private String isO3;
        @SerializedName("location")
        @Expose
        private String location;
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
        @SerializedName("brand_name")
        @Expose
        private String brandName;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("category_sequence")
        @Expose
        private String categorySequence;
        @SerializedName("is_favourite")
        @Expose
        private int isFavourite;
        @SerializedName("in_cart")
        @Expose
        private int isCart;
        @SerializedName("quantity")
        @Expose
        private int quantity;
        @SerializedName("category_image")
        @Expose
        private String categoryImage;
        @SerializedName("product_imgs")
        @Expose
        private List<ProductImg> productImgs = null;
        @SerializedName("product_units")
        @Expose
        private List<ProductListResponse.ProductUnit> productUnits = null;

        @SerializedName("rating")
        @Expose
        private Rating rating;
        @SerializedName("reviews")
        @Expose
        private List<Review> reviews = null;

        private ProductListResponse.ProductUnit unit;

        public int getIsFavourite() {
            return isFavourite;
        }

        public void setIsFavourite(int isFavourite) {
            this.isFavourite = isFavourite;
        }

        public int getIsCart() {
            return isCart;
        }

        public void setIsCart(int isCart) {
            this.isCart = isCart;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public ProductListResponse.ProductUnit getUnit() {
            return unit;
        }

        public void setUnit(ProductListResponse.ProductUnit unit) {
            this.unit = unit;
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

        public String getProductVisibleTo() {
            return productVisibleTo;
        }

        public void setProductVisibleTo(String productVisibleTo) {
            this.productVisibleTo = productVisibleTo;
        }

        public String getBrandIdFk() {
            return brandIdFk;
        }

        public void setBrandIdFk(String brandIdFk) {
            this.brandIdFk = brandIdFk;
        }

        public String getSpecialOffer() {
            return specialOffer;
        }

        public void setSpecialOffer(String specialOffer) {
            this.specialOffer = specialOffer;
        }

        public String getGstIdFk() {
            return gstIdFk;
        }

        public void setGstIdFk(String gstIdFk) {
            this.gstIdFk = gstIdFk;
        }

        public String getHsnIdFk() {
            return hsnIdFk;
        }

        public void setHsnIdFk(String hsnIdFk) {
            this.hsnIdFk = hsnIdFk;
        }

        public String getIsO3() {
            return isO3;
        }

        public void setIsO3(String isO3) {
            this.isO3 = isO3;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
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

        public String getSuVisibleTo() {
            return suVisibleTo;
        }

        public void setSuVisibleTo(String suVisibleTo) {
            this.suVisibleTo = suVisibleTo;
        }

        public String getStoragesUses() {
            return Html.fromHtml(storagesUses).toString().trim();
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

        public String getOtherProuctInfo() {
            return Html.fromHtml(otherProuctInfo).toString().trim();
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
            return Html.fromHtml(weightPolicy).toString().trim();
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

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategorySequence() {
            return categorySequence;
        }

        public void setCategorySequence(String categorySequence) {
            this.categorySequence = categorySequence;
        }

        public String getCategoryImage() {
            return categoryImage;
        }

        public void setCategoryImage(String categoryImage) {
            this.categoryImage = categoryImage;
        }

        public List<ProductImg> getProductImgs() {
            return productImgs;
        }

        public void setProductImgs(List<ProductImg> productImgs) {
            this.productImgs = productImgs;
        }

        public List<ProductListResponse.ProductUnit> getProductUnits() {
            return productUnits;
        }

        public void setProductUnits(List<ProductListResponse.ProductUnit> productUnits) {
            this.productUnits = productUnits;
        }


        public Rating getRating() {
            return rating;
        }

        public void setRating(Rating rating) {
            this.rating = rating;
        }

        public List<Review> getReviews() {
            return reviews;
        }

        public void setReviews(List<Review> reviews) {
            this.reviews = reviews;
        }
    }

    public class ProductImg {
        @SerializedName("product_img")
        @Expose
        private String productImg;

        public String getProductImg() {
            return productImg;
        }

        public void setProductImg(String productImg) {
            this.productImg = productImg;
        }
    }


    @BindingAdapter("setImage")
    public static void setImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(view);
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
}
