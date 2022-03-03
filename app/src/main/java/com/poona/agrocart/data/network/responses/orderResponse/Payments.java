package com.poona.agrocart.data.network.responses.orderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payments {
    @SerializedName("payment_mode_id")
    @Expose
    public String paymentModeId;
    @SerializedName("payment_type")
    @Expose
    public String paymentType;
    @SerializedName("payment_mode_status")
    @Expose
    public String paymentModeStatus;

}
