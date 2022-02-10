package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.common.model.Areas;

import java.util.ArrayList;
public class AreaData {
    @SerializedName("area_data")
    @Expose
    private ArrayList<Areas> area;

    public ArrayList<Areas> getArea() {
        return area;
    }

    public void setArea(ArrayList<Areas> area) {
        this.area = area;
    }
}

