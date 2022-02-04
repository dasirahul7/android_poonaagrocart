package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BestSellingResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private BestSellingData bestSellingData;

    public BestSellingData getBestSellingData() {
        return bestSellingData;
    }

    public void setBestSellingData(BestSellingData bestSellingData) {
        this.bestSellingData = bestSellingData;
    }
}
