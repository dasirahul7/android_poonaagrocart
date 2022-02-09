
package com.poona.agrocart.ui.nav_stores.model.store_details;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreDetail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("store_code")
    @Expose
    private String storeCode;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("contact_person_name")
    @Expose
    private String contactPersonName;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("map_link")
    @Expose
    private String mapLink;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("area_id")
    @Expose
    private String areaId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("store_image")
    @Expose
    private String storeImage;
    @SerializedName("about_store")
    @Expose
    private String aboutStore;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("area_name")
    @Expose
    private String areaName;
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

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getAboutStore() {
        return aboutStore;
    }

    public void setAboutStore(String aboutStore) {
        this.aboutStore = aboutStore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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
