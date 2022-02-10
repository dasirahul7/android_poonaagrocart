package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.common.model.City;

import java.util.ArrayList;

public class CityResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private CityData cityResponseData;

    public CityData getCityResponseData() {
        return cityResponseData;
    }

    public void setCityResponseData(CityData cityResponseData) {
        this.cityResponseData = cityResponseData;
    }


}
