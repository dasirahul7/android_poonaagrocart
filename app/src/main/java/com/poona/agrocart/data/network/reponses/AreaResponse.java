package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AreaResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private ArrayList<Areas> area;

    public ArrayList<Areas> getArea() {
        return area;
    }

    public void setArea(ArrayList<Areas> area) {
        this.area = area;
    }


    public class Areas {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("area_name")
        @Expose
        private String areaName;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("state_name")
        @Expose
        private String stateName;

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }


}
