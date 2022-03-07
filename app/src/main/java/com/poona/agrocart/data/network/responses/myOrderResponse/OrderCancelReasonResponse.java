
package com.poona.agrocart.data.network.responses.myOrderResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderCancelReasonResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order_detials")
    @Expose
    private List<OrderCancelReason> orderDetials = null;

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

    public List<OrderCancelReason> getOrderDetials() {
        return orderDetials;
    }

    public void setOrderDetials(List<OrderCancelReason> orderDetials) {
        this.orderDetials = orderDetials;
    }
    public class OrderCancelReason {

        @SerializedName("cancel_id")
        @Expose
        private String cancelId;
        @SerializedName("cancelled_reason")
        @Expose
        private String cancelledReason;
        @SerializedName("status")
        @Expose
        private String status;

        public String getCancelId() {
            return cancelId;
        }

        public void setCancelId(String cancelId) {
            this.cancelId = cancelId;
        }

        public String getCancelledReason() {
            return cancelledReason;
        }

        public void setCancelledReason(String cancelledReason) {
            this.cancelledReason = cancelledReason;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
}
