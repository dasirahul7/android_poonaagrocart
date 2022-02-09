package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.Basket;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class ProductListResponseDt {
    @SerializedName("basket_list")
    @Expose
    private ArrayList<Basket> basketList = null;
    @SerializedName("product_list")
    @Expose
    private ArrayList<Product> productList = null;

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public ArrayList<Basket> getBasketList() {
        return basketList;
    }
}
