package com.poona.agrocart.data.network.responses.splashResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SplashData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("gif_image")
    @Expose
    private String gifImage;
    @SerializedName("logo_image")
    @Expose
    private String logoImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGifImage() {
        return gifImage;
    }

    public void setGifImage(String gifImage) {
        this.gifImage = gifImage;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }
}
