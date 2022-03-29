package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.homeResponse.Basket;
import com.poona.agrocart.data.network.responses.homeResponse.Product;

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

    public  class ProductResponseDt {
        @SerializedName("product_list")
        @Expose
        private ArrayList<Product> productList = null;
        @SerializedName("basket_list")
        @Expose
        private ArrayList<BasketResponse.Basket> basketList = null;

        public ArrayList<BasketResponse.Basket> getBasketList() {
            return basketList;
        }

        public void setBasketList(ArrayList<BasketResponse.Basket> basketList) {
            this.basketList = basketList;
        }

        public ArrayList<Product> getProductList() {
            return productList;
        }

        public void setProductList(ArrayList<Product> productList) {
            this.productList = productList;
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
        @SerializedName("qty")
        private int qty;

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

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
            return percDiscount;
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
