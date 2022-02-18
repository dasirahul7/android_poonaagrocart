
package com.poona.agrocart.data.network.responses;

import android.service.autofill.UserData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseData implements Serializable
{

    @SerializedName("user_data")
    @Expose
    private List<SignInResponse.User> userData = null;
    @SerializedName("banner_details")
    @Expose
    private ArrayList<BannerResponse.Banner> bannerDetails = null;
    @SerializedName("category_data")
    @Expose
    private List<CategoryResponse.Category> categoryData = null;
    @SerializedName("exclusive_list")
    @Expose
    private List<ProductListResponse.Product> exclusiveList = null;
    @SerializedName("basket_list")
    @Expose
    private List<BasketResponse.Basket> basketList = null;
    @SerializedName("best_selling_product_list")
    @Expose
    private List<ProductListResponse.Product> bestSellingProductList = null;
    @SerializedName("seasonal_product")
    @Expose
    private List<SeasonalProductResponse.SeasonalProduct> seasonalProduct = null;
//    @SerializedName("o_3")
//    @Expose
//    private List<O3> o3 = null;
    @SerializedName("store_banner")
    @Expose
    private List<StoreBannerResponse.StoreBanner> storeBanner = null;
    @SerializedName("product_list")
    @Expose
    private List<ProductListResponse.Product> productList = null;

    public List<SignInResponse.User> getUserData() {
        return userData;
    }

    public void setUserData(List<SignInResponse.User> userData) {
        this.userData = userData;
    }

    public ArrayList<BannerResponse.Banner> getBannerDetails() {
        return bannerDetails;
    }

    public void setBannerDetails(ArrayList<BannerResponse.Banner> bannerDetails) {
        this.bannerDetails = bannerDetails;
    }

    public List<CategoryResponse.Category> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(List<CategoryResponse.Category> categoryData) {
        this.categoryData = categoryData;
    }

    public List<ProductListResponse.Product> getExclusiveList() {
        return exclusiveList;
    }

    public void setExclusiveList(List<ProductListResponse.Product> exclusiveList) {
        this.exclusiveList = exclusiveList;
    }

    public List<BasketResponse.Basket> getBasketList() {
        return basketList;
    }

    public void setBasketList(List<BasketResponse.Basket> basketList) {
        this.basketList = basketList;
    }

    public List<ProductListResponse.Product> getBestSellingProductList() {
        return bestSellingProductList;
    }

    public void setBestSellingProductList(List<ProductListResponse.Product> bestSellingProductList) {
        this.bestSellingProductList = bestSellingProductList;
    }

    public List<SeasonalProductResponse.SeasonalProduct> getSeasonalProduct() {
        return seasonalProduct;
    }

    public void setSeasonalProduct(List<SeasonalProductResponse.SeasonalProduct> seasonalProduct) {
        this.seasonalProduct = seasonalProduct;
    }

//    public List<O3> getO3() {
//        return o3;
//    }
//
//    public void setO3(List<O3> o3) {
//        this.o3 = o3;
//    }

    public List<StoreBannerResponse.StoreBanner> getStoreBanner() {
        return storeBanner;
    }

    public void setStoreBanner(List<StoreBannerResponse.StoreBanner> storeBanner) {
        this.storeBanner = storeBanner;
    }

    public List<ProductListResponse.Product> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListResponse.Product> productList) {
        this.productList = productList;
    }

}
