package com.poona.agrocart.data.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.home.model.Exclusive;

import java.util.ArrayList;

public class ExclusiveData {
    @SerializedName("exclusive_list")
    @Expose
    private ArrayList<Exclusive> exclusivesList;

    public ArrayList<Exclusive> getExclusivesList() {
        return exclusivesList;
    }

    public void setExclusivesList(ArrayList<Exclusive> exclusivesList) {
        this.exclusivesList = exclusivesList;
    }
}
