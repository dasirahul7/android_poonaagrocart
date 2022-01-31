package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasketResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private BasketData data;

    public BasketData getData() {
        return data;
    }

    public void setData(BasketData data) {
        this.data = data;
    }
}
