package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.Basket;

import java.util.ArrayList;

public class BasketData{
    @SerializedName("basket_details")
    @Expose
    private ArrayList<Basket> baskets;

    public ArrayList<Basket> getBaskets() {
        return baskets;
    }

    public void setBaskets(ArrayList<Basket> baskets) {
        this.baskets = baskets;
    }
}
