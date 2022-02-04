package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.Exclusive;

import java.util.ArrayList;

public class BestSellingData {
    @SerializedName("best_selling_product_list")
    @Expose
    private ArrayList<Exclusive> bestSellingProductList = null;

    public ArrayList<Exclusive> getBestSellingProductList() {
        return bestSellingProductList;
    }

    public void setBestSellingProductList(ArrayList<Exclusive> bestSellingProductList) {
        this.bestSellingProductList = bestSellingProductList;
    }
}
