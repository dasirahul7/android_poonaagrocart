
package com.poona.agrocart.data.network.responses.filterResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterListResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("count_category")
    @Expose
    private String countCategory;
    @SerializedName("count_brand")
    @Expose
    private String countBrand;
    @SerializedName("count_price_filter")
    @Expose
    private String countPriceFilter;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCountCategory() {
        return countCategory;
    }

    public void setCountCategory(String countCategory) {
        this.countCategory = countCategory;
    }

    public String getCountBrand() {
        return countBrand;
    }

    public void setCountBrand(String countBrand) {
        this.countBrand = countBrand;
    }

    public String getCountPriceFilter() {
        return countPriceFilter;
    }

    public void setCountPriceFilter(String countPriceFilter) {
        this.countPriceFilter = countPriceFilter;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
