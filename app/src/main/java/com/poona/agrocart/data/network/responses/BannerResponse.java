package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BannerResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private BannerData bannerData;

    public BannerData getData() {
        return bannerData;
    }

    public void setData(BannerData data) {
        this.bannerData = data;
    }

    public static class Banner {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("adv_type")
        @Expose
        private String advType;
        @SerializedName("from_date")
        @Expose
        private String fromDate;
        @SerializedName("to_date")
        @Expose
        private String toDate;
        @SerializedName("adv_sequence_number")
        @Expose
        private String advSequenceNumber;
        @SerializedName("adv_url")
        @Expose
        private String advUrl;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("weight_id")
        @Expose
        private String weightId;
        @SerializedName("unit_id")
        @Expose
        private String unitId;
        @SerializedName("adv_image")
        @Expose
        private String advImage;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("weight")
        @Expose
        private String weight;
        @SerializedName("unit_name")
        @Expose
        private String unitName;
        @SerializedName("banner_state")
        @Expose
        private ArrayList<BannerState> bannerState = null;
        @SerializedName("banner_city")
        @Expose
        private ArrayList<BannerCity> bannerCity = null;
        @SerializedName("banner_area")
        @Expose
        private ArrayList<BannerArea> bannerArea = null;

        private boolean isDummy;

        public Banner() {
        }

        public Banner(String id, String userType, String advUrl, String advImage) {
            this.id = id;
            this.userType = userType;
            this.advUrl = advUrl;
            this.advImage = advImage;
        }

        public boolean isDummy() {
            return isDummy;
        }

        public void setDummy(boolean dummy) {
            isDummy = dummy;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getAdvType() {
            return advType;
        }

        public void setAdvType(String advType) {
            this.advType = advType;
        }

        public String getFromDate() {
            return fromDate;
        }

        public void setFromDate(String fromDate) {
            this.fromDate = fromDate;
        }

        public String getToDate() {
            return toDate;
        }

        public void setToDate(String toDate) {
            this.toDate = toDate;
        }

        public String getAdvSequenceNumber() {
            return advSequenceNumber;
        }

        public void setAdvSequenceNumber(String advSequenceNumber) {
            this.advSequenceNumber = advSequenceNumber;
        }

        public String getAdvUrl() {
            return advUrl;
        }

        public void setAdvUrl(String advUrl) {
            this.advUrl = advUrl;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getWeightId() {
            return weightId;
        }

        public void setWeightId(String weightId) {
            this.weightId = weightId;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public String getAdvImage() {
            return advImage;
        }

        public void setAdvImage(String advImage) {
            this.advImage = advImage;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public ArrayList<BannerState> getBannerState() {
            return bannerState;
        }

        public void setBannerState(ArrayList<BannerState> bannerState) {
            this.bannerState = bannerState;
        }

        public ArrayList<BannerCity> getBannerCity() {
            return bannerCity;
        }

        public void setBannerCity(ArrayList<BannerCity> bannerCity) {
            this.bannerCity = bannerCity;
        }

        public ArrayList<BannerArea> getBannerArea() {
            return bannerArea;
        }

        public void setBannerArea(ArrayList<BannerArea> bannerArea) {
            this.bannerArea = bannerArea;
        }
    }

    public static class BannerState extends Banner {
        @SerializedName("state_id")
        @Expose
        private String stateId;
        @SerializedName("state_name")
        @Expose
        private String stateName;

        public String getStateId() {
            return stateId;
        }

        public void setStateId(String stateId) {
            this.stateId = stateId;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }
    }

    public static class BannerArea {
        @SerializedName("area_id")
        @Expose
        private String areaId;
        @SerializedName("area_name")
        @Expose
        private String areaName;

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }
    }

    public static class BannerCity {
        @SerializedName("city_id")
        @Expose
        private String cityId;
        @SerializedName("city_name")
        @Expose
        private String cityName;

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
    }

    public class BannerData {
        @SerializedName("banner_details")
        @Expose
        private ArrayList<Banner> banners;

        public ArrayList<Banner> getBanners() {
            return banners;
        }

        public void setBannerDetailsList(ArrayList<Banner> bannerDetailsList) {
            this.banners = bannerDetailsList;
        }
    }
}
