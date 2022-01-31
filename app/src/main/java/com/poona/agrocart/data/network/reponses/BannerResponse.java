package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannerResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private BannerData data;

    public BannerData getData() {
        return data;
    }

    public void setData(BannerData data) {
        this.data = data;
    }
}
