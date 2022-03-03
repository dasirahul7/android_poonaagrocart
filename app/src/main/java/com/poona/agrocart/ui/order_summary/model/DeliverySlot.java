package com.poona.agrocart.ui.order_summary.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliverySlot {
    @SerializedName("delivery_days_id")
    @Expose
    public String deliveryDaysId;
    @SerializedName("delivery_day")
    @Expose
    public String deliveryDay;
    @SerializedName("slot_id")
    @Expose
    public String slotId;
    @SerializedName("slot_start_time")
    @Expose
    public String slotStartTime;
    @SerializedName("slot_end_time")
    @Expose
    public String slotEndTime;
    @SerializedName("available_delivaries")
    @Expose
    public String availableDelivaries;
    @SerializedName("is_delivery_available")
    @Expose
    public Integer isDeliveryAvailable;
    String SlotName;
    boolean active;

    public DeliverySlot(String slotName, boolean active) {
        SlotName = slotName;
        this.active = active;
    }

    public String getSlotName() {
        return SlotName;
    }

    public void setSlotName(String slotName) {
        SlotName = slotName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
