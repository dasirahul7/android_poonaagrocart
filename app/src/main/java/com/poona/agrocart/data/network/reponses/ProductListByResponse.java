package com.poona.agrocart.data.network.reponses;

import androidx.core.app.NotificationCompat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductListByResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private ProductListResponseDt productListResponseDt;

    public ProductListResponseDt getProductListResponseDt() {
        return productListResponseDt;
    }

    public void setProductListResponseDt(ProductListResponseDt productListResponseDt) {
        this.productListResponseDt = productListResponseDt;
    }
}
