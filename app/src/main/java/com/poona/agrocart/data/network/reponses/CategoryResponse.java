package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.Category;
import com.poona.agrocart.ui.home.model.CategoryData;

import java.util.ArrayList;

public class CategoryResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private CategoryData categoryData;

    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(CategoryData categoryData) {
        this.categoryData = categoryData;
    }
}
