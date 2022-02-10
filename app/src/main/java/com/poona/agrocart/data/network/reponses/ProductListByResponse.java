package com.poona.agrocart.data.network.reponses;

import androidx.core.app.NotificationCompat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductListByResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private ProductListResponse.ProductResponseDt productListResponseDt;

    public ProductListResponse.ProductResponseDt getProductListResponseDt() {
        return productListResponseDt;
    }

    public void setProductListResponseDt(ProductListResponse.ProductResponseDt productListResponseDt) {
        this.productListResponseDt = productListResponseDt;
    }
}
