package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AddressesResponse extends BaseResponse implements Serializable {
    @SerializedName("data")
    @Expose
    private List<Address> addresses = null;

    @SerializedName("state_details")
    @Expose
    private StateDetails stateDetails = null;

    public StateDetails getStateDetails() {
        return stateDetails;
    }

    public void setStateDetails(StateDetails stateDetails) {
        this.stateDetails = stateDetails;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public static class Address implements Serializable {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("usertype")
        @Expose
        private String usertype;
        @SerializedName("address_primary_id")
        @Expose
        private String addressPrimaryId;
        @SerializedName("address_type")
        @Expose
        private String addressType;
        @SerializedName("is_default")
        @Expose
        private String isDefault;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("appartment_name")
        @Expose
        private String appartmentName;
        @SerializedName("house_no")
        @Expose
        private String houseNo;
        @SerializedName("street")
        @Expose
        private String street;
        @SerializedName("landmark")
        @Expose
        private String landmark;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("map_address")
        @Expose
        private String mapAddress;
        @SerializedName("state_id")
        @Expose
        private String stateId;
        @SerializedName("city_id")
        @Expose
        private String cityId;
        @SerializedName("area_id")
        @Expose
        private String areaId;
        @SerializedName("state_name")
        @Expose
        private String stateName;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("area_name")
        @Expose
        private String areaName;
        private String fullAddress;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsertype() {
            return usertype;
        }

        public void setUsertype(String usertype) {
            this.usertype = usertype;
        }

        public String getAddressPrimaryId() {
            return addressPrimaryId;
        }

        public void setAddressPrimaryId(String addressPrimaryId) {
            this.addressPrimaryId = addressPrimaryId;
        }

        public String getAddressType() {
            return addressType;
        }

        public void setAddressType(String addressType) {
            this.addressType = addressType;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getAppartmentName() {
            return appartmentName;
        }

        public void setAppartmentName(String appartmentName) {
            this.appartmentName = appartmentName;
        }

        public String getHouseNo() {
            return houseNo;
        }

        public void setHouseNo(String houseNo) {
            this.houseNo = houseNo;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getLandmark() {
            return landmark;
        }

        public void setLandmark(String landmark) {
            this.landmark = landmark;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getMapAddress() {
            return mapAddress;
        }

        public void setMapAddress(String mapAddress) {
            this.mapAddress = mapAddress;
        }

        public String getStateId() {
            return stateId;
        }

        public void setStateId(String stateId) {
            this.stateId = stateId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getFullAddress() {
            return fullAddress;
        }

        public void setFullAddress(String fullAddress) {
            this.fullAddress = fullAddress;
        }
    }

    public static class StateDetails {
        @SerializedName("state_id")
        @Expose
        private String stateId;
        @SerializedName("stateName")
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
}