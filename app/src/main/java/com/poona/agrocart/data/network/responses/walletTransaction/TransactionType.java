package com.poona.agrocart.data.network.responses.walletTransaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionType {
    @SerializedName("wallet_transaction_type_id")
    @Expose
    private String walletTransactionTypeId;
    @SerializedName("wallet_transaction_type")
    @Expose
    private String walletTransactionType;
    @SerializedName("status")
    @Expose
    private String status;

    public String getWalletTransactionTypeId() {
        return walletTransactionTypeId;
    }

    public void setWalletTransactionTypeId(String walletTransactionTypeId) {
        this.walletTransactionTypeId = walletTransactionTypeId;
    }

    public String getWalletTransactionType() {
        return walletTransactionType;
    }

    public void setWalletTransactionType(String walletTransactionType) {
        this.walletTransactionType = walletTransactionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
