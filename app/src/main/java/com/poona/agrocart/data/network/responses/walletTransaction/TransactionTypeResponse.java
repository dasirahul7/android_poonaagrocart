package com.poona.agrocart.data.network.responses.walletTransaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TransactionTypeResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("wallet_transaction_type_list")
    @Expose
    private ArrayList<WalletTransactionType> walletTransactionTypeList = null;

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

    public ArrayList<WalletTransactionType> getWalletTransactionTypeList() {
        return walletTransactionTypeList;
    }

    public void setWalletTransactionTypeList(ArrayList<WalletTransactionType> walletTransactionTypeList) {
        this.walletTransactionTypeList = walletTransactionTypeList;
    }
}
