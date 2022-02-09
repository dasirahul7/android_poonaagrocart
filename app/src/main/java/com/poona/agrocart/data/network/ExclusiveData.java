package com.poona.agrocart.data.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class ExclusiveData {
    @SerializedName("exclusive_list")
    @Expose
    private ArrayList<Product> exclusivesList;

    public ArrayList<Product> getExclusivesList() {
        return exclusivesList;
    }

    public void setExclusivesList(ArrayList<Product> exclusivesList) {
        this.exclusivesList = exclusivesList;
    }
}
