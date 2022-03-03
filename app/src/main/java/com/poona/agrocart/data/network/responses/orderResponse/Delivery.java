package com.poona.agrocart.data.network.responses.orderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.order_summary.model.DeliverySlot;

import java.util.ArrayList;

public class Delivery {
    @SerializedName("delivery_date")
    @Expose
    public String deliveryDate;
    @SerializedName("delivery_slots")
    @Expose
    public ArrayList<DeliverySlot> deliverySlots = null;

}
