package com.poona.agrocart.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductUnit {
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("perc_discount")
    @Expose
    private String percDiscount;
    @SerializedName("selling_price")
    @Expose
    private String sellingPrice;
    @SerializedName("offer_price")
    @Expose
    private String offerPrice;
    @SerializedName("unit_name")
    @Expose
    private String unitName;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPercDiscount() {
        return percDiscount;
    }

    public void setPercDiscount(String percDiscount) {
        this.percDiscount = percDiscount;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
