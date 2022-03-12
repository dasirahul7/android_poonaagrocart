package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.search.model.CommonSearchItem;

import java.util.List;

public class CommonSearchResponse extends BaseResponse{
    @SerializedName("product_count")
    @Expose
    private String productCount;
    @SerializedName("basket_count")
    @Expose
    private String basketCount;
    @SerializedName("category_count")
    @Expose
    private String categoryCount;
    @SerializedName("product_list")
    @Expose
    private List<CommonSearchItem> productList = null;
    @SerializedName("basket_list")
    @Expose
    private List<CommonSearchItem> basketList = null;

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getBasketCount() {
        return basketCount;
    }

    public void setBasketCount(String basketCount) {
        this.basketCount = basketCount;
    }

    public String getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(String categoryCount) {
        this.categoryCount = categoryCount;
    }

    public List<CommonSearchItem> getProductList() {
        return productList;
    }

    public void setProductList(List<CommonSearchItem> productList) {
        this.productList = productList;
    }

    public List<CommonSearchItem> getBasketList() {
        return basketList;
    }

    public void setBasketList(List<CommonSearchItem> basketList) {
        this.basketList = basketList;
    }
}
