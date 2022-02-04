
package com.poona.agrocart.data.network.reponses;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.StoreBanner;

public class StoreBannerResponse extends BaseResponse{

    @SerializedName("data")
    @Expose
    private ArrayList<StoreBanner> storeBanners = null;

    public ArrayList<StoreBanner> getStoreBanners() {
        return storeBanners;
    }

    public void setStoreBanners(ArrayList<StoreBanner> storeBanners) {
        this.storeBanners = storeBanners;
    }
}
