
package com.poona.agrocart.data.network.responses.help_center_response.recieveMessage;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecieveMessageResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private RecieveMessage data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RecieveMessage getData() {
        return data;
    }

    public void setData(RecieveMessage data) {
        this.data = data;
    }

}
