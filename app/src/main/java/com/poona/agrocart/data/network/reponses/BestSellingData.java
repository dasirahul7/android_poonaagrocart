package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class BestSellingData {
    @SerializedName("best_selling_product_list")
    @Expose
    private ArrayList<Product> bestSellingProductList = null;

    public ArrayList<Product> getBestSellingProductList() {
        return bestSellingProductList;
    }

    public void setBestSellingProductList(ArrayList<Product> bestSellingProductList) {
        this.bestSellingProductList = bestSellingProductList;
    }
}
