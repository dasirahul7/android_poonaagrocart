package com.poona.agrocart.data.network.responses.homeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HomeResponseData {
        @SerializedName("user_data")
        @Expose
        private UserDetails userData = null;
        @SerializedName("banner_details")
        @Expose
        private ArrayList<Banner> bannerDetails = null;
        @SerializedName("category_data")
        @Expose
        private ArrayList<Category> categoryData = null;
        @SerializedName("exclusive_list")
        @Expose
        private ArrayList<Product> exclusiveList = null;
        @SerializedName("basket_list")
        @Expose
        private ArrayList<Basket> basketList = null;
        @SerializedName("best_selling_product_list")
        @Expose
        private ArrayList<Product> bestSellingProductList = null;
        @SerializedName("seasonal_product")
        @Expose
        private ArrayList<SeasonalProduct> seasonalProduct = null;
        @SerializedName("o_3")
        @Expose
        private ArrayList<O3> o3 = null;
        @SerializedName("store_banner")
        @Expose
        private ArrayList<StoreBanner> storeBanner = null;
        @SerializedName("product_list")
        @Expose
        private ArrayList<Product> productList = null;

    public UserDetails getUserData() {
        return userData;
    }

    public void setUserData(UserDetails userData) {
        this.userData = userData;
    }

    public ArrayList<Banner> getBannerDetails() {
        return bannerDetails;
    }

    public void setBannerDetails(ArrayList<Banner> bannerDetails) {
        this.bannerDetails = bannerDetails;
    }

    public ArrayList<Category> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(ArrayList<Category> categoryData) {
        this.categoryData = categoryData;
    }

    public ArrayList<Product> getExclusiveList() {
        return exclusiveList;
    }

    public void setExclusiveList(ArrayList<Product> exclusiveList) {
        this.exclusiveList = exclusiveList;
    }

    public ArrayList<Basket> getBasketList() {
        return basketList;
    }

    public void setBasketList(ArrayList<Basket> basketList) {
        this.basketList = basketList;
    }

    public ArrayList<Product> getBestSellingProductList() {
        return bestSellingProductList;
    }

    public void setBestSellingProductList(ArrayList<Product> bestSellingProductList) {
        this.bestSellingProductList = bestSellingProductList;
    }

    public ArrayList<SeasonalProduct> getSeasonalProduct() {
        return seasonalProduct;
    }

    public void setSeasonalProduct(ArrayList<SeasonalProduct> seasonalProduct) {
        this.seasonalProduct = seasonalProduct;
    }

    public ArrayList<O3> getO3() {
        return o3;
    }

    public void setO3(ArrayList<O3> o3) {
        this.o3 = o3;
    }

    public ArrayList<StoreBanner> getStoreBanner() {
        return storeBanner;
    }

    public void setStoreBanner(ArrayList<StoreBanner> storeBanner) {
        this.storeBanner = storeBanner;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }
}
