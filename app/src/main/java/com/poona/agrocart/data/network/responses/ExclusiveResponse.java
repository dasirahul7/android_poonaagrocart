package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.homeResponse.Product;

import java.util.ArrayList;
import java.util.List;

public class ExclusiveResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private ExclusiveData exclusiveData;

    public ExclusiveData getExclusiveData() {
        return exclusiveData;
    }

    public void setExclusiveData(ExclusiveData exclusiveData) {
        this.exclusiveData = exclusiveData;
    }

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

}
