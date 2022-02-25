package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.homeResponse.Banner;
import com.poona.agrocart.data.network.responses.homeResponse.HomeResponse;

import java.util.ArrayList;

public class BannerResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private BannerData bannerData;

    public BannerData getData() {
        return bannerData;
    }

    public void setData(BannerData data) {
        this.bannerData = data;
    }

       /*BannerData class*/
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
}
