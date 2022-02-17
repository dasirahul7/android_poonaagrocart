package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class IntroScreenResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private ArrayList<Intro> introScreenItem;

    public ArrayList<Intro> getIntroScreenItem() {
        return introScreenItem;
    }

    public void setIntroScreenItem(ArrayList<Intro> introScreenItem) {
        this.introScreenItem = introScreenItem;
    }
    public static class Intro {
        @SerializedName("id")
        @Expose
        String id;
        @SerializedName("title")
        @Expose
        String Title;
        @SerializedName("description")
        @Expose
        String Description;
        @SerializedName("banner_image")
        @Expose
        String imgFile;

//    public Intro(String id, String title, String description, Integer imgFile) {
//        this.id = id;
//        Title = title;
//        Description = description;
//        this.imgFile = imgFile;
//    }

        public String getImgFile() {
            return imgFile;
        }

        public void setImgFile(String imgFile) {
            this.imgFile = imgFile;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }
    }

}
