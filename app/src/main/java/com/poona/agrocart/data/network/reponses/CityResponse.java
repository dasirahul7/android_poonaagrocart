package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public class CityData {
        @SerializedName("city_data")
        @Expose
        private ArrayList<City> cityArrayList;

        public ArrayList<City> getCityArrayList() {
            return cityArrayList;
        }
    }
    public class City {
        @SerializedName("id")
        @Expose
        private String cityId;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("code")
        @Expose
        private Integer cityCode;
        @SerializedName("status")
        @Expose
        private Integer cityStatus;
        @SerializedName("state_name")
        @Expose
        private String stateName;

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public Integer getCityCode() {
            return cityCode;
        }

        public void setCityCode(Integer cityCode) {
            this.cityCode = cityCode;
        }

        public Integer getCityStatus() {
            return cityStatus;
        }

        public void setCityStatus(Integer cityStatus) {
            this.cityStatus = cityStatus;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }
    }

}
