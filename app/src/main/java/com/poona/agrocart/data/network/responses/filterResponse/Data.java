
package com.poona.agrocart.data.network.responses.filterResponse;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("category_list")
    @Expose
    private ArrayList<CategoryFilterList> categoryList = null;
    @SerializedName("brand_list")
    @Expose
    private ArrayList<BrandFliterList> brandList = null;
    @SerializedName("price_filter_list")
    @Expose
    private ArrayList<SortByFilterList> priceFilterList = null;

    public ArrayList<CategoryFilterList> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<CategoryFilterList> categoryList) {
        this.categoryList = categoryList;
    }

    public ArrayList<BrandFliterList> getBrandList() {
        return brandList;
    }

    public void setBrandList(ArrayList<BrandFliterList> brandList) {
        this.brandList = brandList;
    }

    public ArrayList<SortByFilterList> getPriceFilterList() {
        return priceFilterList;
    }

    public void setPriceFilterList(ArrayList<SortByFilterList> priceFilterList) {
        this.priceFilterList = priceFilterList;
    }

}
