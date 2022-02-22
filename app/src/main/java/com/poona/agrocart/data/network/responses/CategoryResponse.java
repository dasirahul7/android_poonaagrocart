package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private CategoryData categoryData;

    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(CategoryData categoryData) {
        this.categoryData = categoryData;
    }

    public class CategoryData {
        @SerializedName("category_list")
        @Expose
        private ArrayList<Category> categoryList = null;

        public ArrayList<Category> getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(ArrayList<Category> categoryList) {
            this.categoryList = categoryList;
        }
    }

    public class Category {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("category_type")
        @Expose
        private String categoryType;
        @SerializedName("category_image")
        @Expose
        private String categoryImage;
        @SerializedName("category_sequence")
        @Expose
        private String categorySequence;
        @SerializedName("status")
        @Expose
        private String status;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

}
