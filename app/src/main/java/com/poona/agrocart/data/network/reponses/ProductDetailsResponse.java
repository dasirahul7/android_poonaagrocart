package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.product_detail.model.ProductDetail;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsResponse extends BaseResponse {
@SerializedName("product_details")
    @Expose
    private ArrayList<ProductDetails> productArrayList;

    public ArrayList<ProductDetails> getProductArrayList() {
        return productArrayList;
    }

    public void setProductArrayList(ArrayList<ProductDetails> productArrayList) {
        this.productArrayList = productArrayList;
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
        @SerializedName("category_image")
        @Expose
        private String categoryImage;
        @SerializedName("product_imgs")
        @Expose
        private List<ProductImg> productImgs = null;
        @SerializedName("product_units")
        @Expose
        private List<ProductListResponse.ProductUnit> productUnits = null;
        private String priceUnit= "Rs.";
        private String offerMsg = "Buy"+productUnits.get(0).getWeight()+productUnits.get(0).getUnitName()+priceUnit+productUnits.get(0).getOfferPrice();

        public String getOfferMsg() {
            return offerMsg;
        }

        public void setOfferMsg(String offerMsg) {
            this.offerMsg = offerMsg;
        }

        public String getPriceUnit() {
            return priceUnit;
        }

        public void setPriceUnit(String priceUnit) {
            this.priceUnit = priceUnit;
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

        public String getSuVisibleTo() {
            return suVisibleTo;
        }

        public void setSuVisibleTo(String suVisibleTo) {
            this.suVisibleTo = suVisibleTo;
        }

        public String getStoragesUses() {
            return storagesUses;
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
            return otherProuctInfo;
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
            return weightPolicy;
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
}
