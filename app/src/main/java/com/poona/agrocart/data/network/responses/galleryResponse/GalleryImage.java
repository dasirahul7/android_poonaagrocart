
package com.poona.agrocart.data.network.responses.galleryResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GalleryImage {

    @SerializedName("gallery_image")
    @Expose
    private String galleryImage;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("status")
    @Expose
    private String status;

    public String getGalleryImage() {
        return galleryImage;
    }

    public void setGalleryImage(String galleryImage) {
        this.galleryImage = galleryImage;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
