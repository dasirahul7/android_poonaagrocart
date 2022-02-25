package com.poona.agrocart.data.network.responses.homeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("category_type")
    @Expose
    private String categoryType;
    @SerializedName("category_sequence")
    @Expose
    private String categorySequence;

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategorySequence() {
        return categorySequence;
    }

    public void setCategorySequence(String categorySequence) {
        this.categorySequence = categorySequence;
    }
}
