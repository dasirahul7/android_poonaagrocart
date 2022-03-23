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
@SerializedName("wallet_balance")
    @Expose
    public String balance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(String paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentModeStatus() {
        return paymentModeStatus;
    }

    public void setPaymentModeStatus(String paymentModeStatus) {
        this.paymentModeStatus = paymentModeStatus;
    }
}
