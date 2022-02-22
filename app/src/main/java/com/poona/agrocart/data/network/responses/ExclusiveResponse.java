package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
        private ArrayList<ProductListResponse.Product> exclusivesList;

        public ArrayList<ProductListResponse.Product> getExclusivesList() {
            return exclusivesList;
        }

        public void setExclusivesList(ArrayList<ProductListResponse.Product> exclusivesList) {
            this.exclusivesList = exclusivesList;
        }
    }

}
