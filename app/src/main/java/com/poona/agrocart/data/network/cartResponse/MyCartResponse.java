
package com.poona.agrocart.data.network.cartResponse;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.BaseResponse;

public class MyCartResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<CartData> data = null;

    public ArrayList<CartData> getData() {
        return data;
    }

    public void setData(ArrayList<CartData> data) {
        this.data = data;
    }

}
