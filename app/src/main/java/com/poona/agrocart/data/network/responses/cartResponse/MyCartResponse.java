package com.poona.agrocart.data.network.responses.cartResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.BaseResponse;

import java.util.ArrayList;

public class MyCartResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<CartData> data = null;
    @SerializedName("saved_amount")
    @Expose
    private float savedAmount;

    public float getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(float savedAmount) {
        this.savedAmount = savedAmount;
    }

    public ArrayList<CartData> getData() {
        return data;
    }

    public void setData(ArrayList<CartData> data) {
        this.data = data;
    }

}
