package com.poona.agrocart.data.network.responses.walletTransaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WalletTransactionListResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("wallet_balance")
    @Expose
    private String walletBalance;
    @SerializedName("wallet_transaction_list")
    @Expose
    private ArrayList<WalletTransaction> walletTransactionList = null;

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

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public ArrayList<WalletTransaction> getWalletTransactionList() {
        return walletTransactionList;
    }

    public void setWalletTransactionList(ArrayList<WalletTransaction> walletTransactionList) {
        this.walletTransactionList = walletTransactionList;
    }
}
