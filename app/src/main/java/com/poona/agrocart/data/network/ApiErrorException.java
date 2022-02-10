package com.poona.agrocart.data.network;

public interface ApiErrorException {
    void onApiErrorException(int from, String type);
}
