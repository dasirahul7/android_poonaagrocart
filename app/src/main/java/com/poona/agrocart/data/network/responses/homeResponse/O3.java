package com.poona.agrocart.data.network.responses.homeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class O3 {
    @SerializedName("o3_id")
    @Expose
    private String o3Id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video_upload")
    @Expose
    private String videoUpload;
    @SerializedName("video_image")
    @Expose
    private String videoImage;

    public String getO3Id() {
        return o3Id;
    }

    public void setO3Id(String o3Id) {
        this.o3Id = o3Id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUpload() {
        return videoUpload;
    }

    public void setVideoUpload(String videoUpload) {
        this.videoUpload = videoUpload;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }
}
