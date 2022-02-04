package com.poona.agrocart.data.network.reponses;

import androidx.arch.core.internal.SafeIterableMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.common.model.Areas;

import java.util.ArrayList;

public class AreaResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private AreaData areaData;

    public AreaData getAreaData() {
        return areaData;
    }

    public void setAreaData(AreaData areaData) {
        this.areaData = areaData;
    }


}
