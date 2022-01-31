package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.BannerData;

public class BannerResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private BannerData bannerData;

    public BannerData getData() {
        return bannerData;
    }

    public void setData(BannerData data) {
        this.bannerData = data;
    }
}
