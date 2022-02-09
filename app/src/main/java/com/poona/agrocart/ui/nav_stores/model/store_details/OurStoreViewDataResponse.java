
package com.poona.agrocart.ui.nav_stores.model.store_details;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OurStoreViewDataResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("store_details")
    @Expose
    private List<StoreDetail> storeDetails = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StoreDetail> getStoreDetails() {
        return storeDetails;
    }

    public void setStoreDetails(List<StoreDetail> storeDetails) {
        this.storeDetails = storeDetails;
    }

}
