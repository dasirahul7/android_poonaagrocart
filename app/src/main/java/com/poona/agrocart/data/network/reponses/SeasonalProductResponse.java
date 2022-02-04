package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.SeasonalProduct;

import java.util.ArrayList;

public class SeasonalProductResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private ArrayList<SeasonalProduct> seasonalProducts = null;

    public ArrayList<SeasonalProduct> getSeasonalProducts() {
        return seasonalProducts;
    }

    public void setSeasonalProducts(ArrayList<SeasonalProduct> seasonalProducts) {
        this.seasonalProducts = seasonalProducts;
    }
}
