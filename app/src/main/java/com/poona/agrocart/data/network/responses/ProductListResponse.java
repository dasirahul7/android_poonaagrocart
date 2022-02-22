package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductListResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private ProductResponseDt productResponseDt;

    public ProductResponseDt getProductResponseDt() {
        return productResponseDt;
    }

    public void setProductResponseDt(ProductResponseDt productResponseDt) {
        this.productResponseDt = productResponseDt;
    }

    public class ProductResponseDt {
        @SerializedName("product_list")
        @Expose
        private ArrayList<ProductListResponse.Product> productList = null;
        @SerializedName("basket_list")
        @Expose
        private ArrayList<BasketResponse.Basket> basketList = null;

        public ArrayList<BasketResponse.Basket> getBasketList() {
            return basketList;
        }

        public void setBasketList(ArrayList<BasketResponse.Basket> basketList) {
            this.basketList = basketList;
        }

        public ArrayList<ProductListResponse.Product> getProductList() {
            return productList;
        }

        public void setProductList(ArrayList<ProductListResponse.Product> productList) {
            this.productList = productList;
        }
    }

    public class Product {
        @SerializedName("sequence_no")
        @Expose
        private String sequenceNo;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("special_offer")
        @Expose
        private String specialOffer;
        @SerializedName("is_o3")
        @Expose
        private String isO3;
        @SerializedName("product_visible_to")
        @Expose
        private String productVisibleTo;
        @SerializedName("feature_img")
        @Expose
        private String featureImg;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("product_units")
        @Expose
        private ArrayList<ProductUnit> productUnits = null;
        @SerializedName("is_favourite")
        private int isFavourite;
        @SerializedName("in_cart")
        private int inCart;
        @SerializedName("quantity")
        private int quantity;
        private ProductUnit unit;
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

        public ProductUnit getUnit() {
            return unit;
        }

        public void setUnit(ProductUnit unit) {
            this.unit = unit;
        }

        public String getAccurateWeight() {
            return accurateWeight;
        }

        public void setAccurateWeight(String accurateWeight) {
            this.accurateWeight = accurateWeight;
        }

        public String getSequenceNo() {
            return sequenceNo;
        }

        public void setSequenceNo(String sequenceNo) {
            this.sequenceNo = sequenceNo;
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

        public void setProductId(String id) {
            this.productId = id;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSpecialOffer() {
            return specialOffer;
        }

        public void setSpecialOffer(String specialOffer) {
            this.specialOffer = specialOffer;
        }

        public String getIsO3() {
            return isO3;
        }

        public void setIsO3(String isO3) {
            this.isO3 = isO3;
        }

        public String getProductVisibleTo() {
            return productVisibleTo;
        }

        public void setProductVisibleTo(String productVisibleTo) {
            this.productVisibleTo = productVisibleTo;
        }

        public String getFeatureImg() {
            return featureImg;
        }

        public void setFeatureImg(String featureImg) {
            this.featureImg = featureImg;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public ArrayList<ProductUnit> getProductUnits() {
            return productUnits;
        }

        public void setProductUnits(ArrayList<ProductUnit> productUnits) {
            this.productUnits = productUnits;
        }

    }

    public class ProductUnit {
        @SerializedName("pu_id")
        private String pId;
        @SerializedName("weight")
        @Expose
        private String weight;
        @SerializedName("perc_discount")
        @Expose
        private String percDiscount;
        @SerializedName("special_offer")
        @Expose
        private String specialOffer;
        @SerializedName("selling_price")
        @Expose
        private String sellingPrice;
        @SerializedName("offer_price")
        @Expose
        private String offerPrice;
        @SerializedName("unit_name")
        @Expose
        private String unitName;
        @SerializedName("in_cart")
        private int inCart;

        public int getInCart() {
            return inCart;
        }

        public void setInCart(int inCart) {
            this.inCart = inCart;
        }

        public String getpId() {
            return pId;
        }

        public void setpId(String pId) {
            this.pId = pId;
        }

        public String getSpecialOffer() {
            return specialOffer;
        }

        public void setSpecialOffer(String specialOffer) {
            this.specialOffer = specialOffer;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getPercDiscount() {
            return percDiscount + " %off";
        }

        public void setPercDiscount(String percDiscount) {
            this.percDiscount = percDiscount;
        }

        public String getSellingPrice() {
            return sellingPrice;
        }

        public void setSellingPrice(String sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        public String getOfferPrice() {
            return offerPrice;
        }

        public void setOfferPrice(String offerPrice) {
            this.offerPrice = offerPrice;
        }

        public String getUnitName() {
            return " " + unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String pWeight() {
            return this.weight + this.unitName;
        }
    }


}
