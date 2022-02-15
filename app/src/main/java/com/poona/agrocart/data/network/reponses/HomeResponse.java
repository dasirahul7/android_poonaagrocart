
package com.poona.agrocart.data.network.reponses;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeResponse extends BaseResponse implements Serializable
{
    @SerializedName("response")
    @Expose
    private ResponseData response;
    private final static long serialVersionUID = -1361486807372423920L;

    public ResponseData getResponse() {
        return response;
    }

    public void setResponse(ResponseData response) {
        this.response = response;
    }

}
