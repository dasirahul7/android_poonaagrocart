package com.poona.agrocart.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.Banner;

import java.util.ArrayList;

public class BannerData {
    @SerializedName("banner_details")
    @Expose
    private ArrayList<Banner> banners;

    public ArrayList<Banner> getBanners() {
        return banners;
    }

    public void setBannerDetailsList(ArrayList<Banner> bannerDetailsList) {
        this.banners = bannerDetailsList;
    }
}
