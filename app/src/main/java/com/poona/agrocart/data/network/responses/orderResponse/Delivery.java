package com.poona.agrocart.data.network.responses.orderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Delivery {
    @SerializedName("delivery_date")
    @Expose
    public String deliveryDate;
    @SerializedName("delivery_slots")
    @Expose
    public ArrayList<DeliverySlot> deliverySlots = null;

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public ArrayList<DeliverySlot> getDeliverySlots() {
        return deliverySlots;
    }

    public void setDeliverySlots(ArrayList<DeliverySlot> deliverySlots) {
        this.deliverySlots = deliverySlots;
    }
}
