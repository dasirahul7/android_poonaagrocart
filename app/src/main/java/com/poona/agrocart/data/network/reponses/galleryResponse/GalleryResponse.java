
package com.poona.agrocart.data.network.reponses.galleryResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.reponses.BaseResponse;

import java.util.List;

public class GalleryResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private Gallery data;

    public Gallery getData() {
        return data;
    }

    public void setData(Gallery data) {
        this.data = data;
    }

    public class Gallery {

        @SerializedName("gallery_images")
        @Expose
        private List<GalleryImage> galleryImage = null;
        @SerializedName("gallery_videos")
        @Expose
        private List<GalleryVideo> galleryVideo = null;

        public List<GalleryImage> getGalleryImage() {
            return galleryImage;
        }

        public void setGalleryImage(List<GalleryImage> galleryImage) {
            this.galleryImage = galleryImage;
        }

        public List<GalleryVideo> getGalleryVideo() {
            return galleryVideo;
        }

        public void setGalleryVideo(List<GalleryVideo> galleryVideo) {
            this.galleryVideo = galleryVideo;
        }

    }

}
