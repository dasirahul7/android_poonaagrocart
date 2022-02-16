package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AreaResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private List<Area> areas = null;

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public static class Area {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("area_name")
        @Expose
        private String areaName;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("state_name")
        @Expose
        private String stateName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
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

    }
}