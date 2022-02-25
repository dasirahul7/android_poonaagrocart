package com.poona.agrocart.data.network.responses.homeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreBanner{
    @SerializedName("store_banner_id")
    @Expose
    private String storeBannerId;
    @SerializedName("banner_image")
    @Expose
    private String bannerImage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;

    public String getStoreBannerId() {
        return storeBannerId;
    }

    public void setStoreBannerId(String storeBannerId) {
        this.storeBannerId = storeBannerId;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

