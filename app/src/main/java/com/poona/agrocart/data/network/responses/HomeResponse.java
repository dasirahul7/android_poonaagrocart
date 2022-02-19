package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HomeResponse extends BaseResponse implements Serializable {
    private final static long serialVersionUID = -1361486807372423920L;
    @SerializedName("response")
    @Expose
    private ResponseData response;

    public ResponseData getResponse() {
        return response;
    }

    public void setResponse(ResponseData response) {
        this.response = response;
    }

}
