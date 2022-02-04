package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.Exclusive;

import java.util.ArrayList;

public class ProductResponseDt {
    @SerializedName("product_list")
    @Expose
    private ArrayList<Exclusive> productList = null;

    public ArrayList<Exclusive> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Exclusive> productList) {
        this.productList = productList;
    }
}
