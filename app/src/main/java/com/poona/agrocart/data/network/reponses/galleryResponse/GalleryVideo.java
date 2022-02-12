
package com.poona.agrocart.data.network.reponses.galleryResponse;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GalleryVideo {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("video_image")
    @Expose
    private String videoImage;
    @SerializedName("created_on")
    @Expose
    private String createdOn;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}
