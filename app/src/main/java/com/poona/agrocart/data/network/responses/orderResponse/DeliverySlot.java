package com.poona.agrocart.data.network.responses.orderResponse;

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
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("slot_no")
    @Expose
    public String slotNo;
    @SerializedName("slot_time")
    @Expose
    public String slotTime;
    @SerializedName("is_available")
    @Expose
    public Integer isAvailable;

}
