package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.BaseFragment;

import java.util.ArrayList;

public class BasketDetailsResponse extends BaseFragment {
@SerializedName("basket_details")
    @Expose
    private ArrayList<BasketResponse.Basket> basketArrayList;

    public ArrayList<BasketResponse.Basket> getBasketArrayList() {
        return basketArrayList;
    }

    public void setBasketArrayList(ArrayList<BasketResponse.Basket> basketArrayList) {
        this.basketArrayList = basketArrayList;
    }
}
