package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BannerData {
    @SerializedName("banner_details")
    @Expose
    private ArrayList<BannerDetails> bannerDetailsList;

    public ArrayList<BannerDetails> getBannerDetailsList() {
        return bannerDetailsList;
    }

    public void setBannerDetailsList(ArrayList<BannerDetails> bannerDetailsList) {
        this.bannerDetailsList = bannerDetailsList;
    }
}
