package com.poona.agrocart.data.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.reponses.BaseResponse;

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
}
