
package com.poona.agrocart.data.network.responses;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    public class StoreBanner {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("banner_image")
        @Expose
        private String bannerImage;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("status")
        @Expose
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

}
