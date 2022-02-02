package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductListResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private ProductResponseDt productResponseDt;

    public ProductResponseDt getProductResponseDt() {
        return productResponseDt;
    }

    public void setProductResponseDt(ProductResponseDt productResponseDt) {
        this.productResponseDt = productResponseDt;
    }
}
