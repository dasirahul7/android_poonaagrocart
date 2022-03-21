package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class GetUnitResponse extends BaseResponse{

    @SerializedName("data")
    @Expose
    private ArrayList<UnitData> UnitList;

    public ArrayList<UnitData> getUnitList() {
        return UnitList;
    }

    public void setUnitList(ArrayList<UnitData> unitList) {
        UnitList = unitList;
    }

    public class UnitData {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("unit_name")
        @Expose
        private String unitName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }
    }
}
