package com.poona.agrocart.data.network;

public interface NetworkExceptionListener {
    void onNetworkException(int from, String type);
}