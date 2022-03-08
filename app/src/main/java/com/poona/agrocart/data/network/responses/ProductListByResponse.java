package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductListByResponse extends BaseResponse {
    @SerializedName("total_count")
    @Expose
    private int totalCount;
    @SerializedName("data")
    @Expose
    private ProductListResponse.ProductResponseDt productListResponseDt;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ProductListResponse.ProductResponseDt getProductListResponseDt() {
        return productListResponseDt;
    }

    public void setProductListResponseDt(ProductListResponse.ProductResponseDt productListResponseDt) {
        this.productListResponseDt = productListResponseDt;
    }
}
