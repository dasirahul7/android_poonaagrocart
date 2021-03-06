package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasketDetailsResponse extends BaseResponse {
    @SerializedName("basket_details")
    @Expose
    private BasketResponse.Basket basketDetail;

    public BasketResponse.Basket getBasketDetail() {
        return basketDetail;
    }

    public void setBasketDetail(BasketResponse.Basket basketDetail) {
        this.basketDetail = basketDetail;
    }
}
