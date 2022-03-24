package com.poona.agrocart.data.network.responses.walletTransaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletTransaction {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("wallet_id")
    @Expose
    private String walletId;
    @SerializedName("order_code")
    @Expose
    private String orderCode;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("invoice_file")
    @Expose
    private String invoiceFile;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("paid_amount")
    @Expose
    private String paidAmount;
    @SerializedName("transaction_amount")
    @Expose
    private String transactionAmount;
    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("order_subscription_id")
    @Expose
    private String orderSubscriptionId;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getInvoiceFile() {
        return invoiceFile;
    }

    public void setInvoiceFile(String invoiceFile) {
        this.invoiceFile = invoiceFile;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderSubscriptionId() {
        return orderSubscriptionId;
    }

    public void setOrderSubscriptionId(String orderSubscriptionId) {
        this.orderSubscriptionId = orderSubscriptionId;
    }
}
