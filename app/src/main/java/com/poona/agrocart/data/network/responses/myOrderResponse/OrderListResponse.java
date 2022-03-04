
package com.poona.agrocart.data.network.responses.myOrderResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderListResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order_detials")
    @Expose
    private List<OrderList> orderDetials = null;

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

    public List<OrderList> getOrderDetials() {
        return orderDetials;
    }

    public void setOrderDetials(List<OrderList> orderDetials) {
        this.orderDetials = orderDetials;
    }
    public class OrderList {

        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("order_code")
        @Expose
        private String orderCode;
        @SerializedName("order_status")
        @Expose
        private String orderStatus;
        @SerializedName("paid_amount")
        @Expose
        private String paidAmount;
        @SerializedName("total_quantity")
        @Expose
        private String totalQuantity;
        @SerializedName("pending_date")
        @Expose
        private String pendingDate;

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

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getPaidAmount() {
            return paidAmount;
        }

        public void setPaidAmount(String paidAmount) {
            this.paidAmount = paidAmount;
        }

        public String getTotalQuantity() {
            return totalQuantity;
        }

        public void setTotalQuantity(String totalQuantity) {
            this.totalQuantity = totalQuantity;
        }

        public String getPendingDate() {
            return pendingDate;
        }

        public void setPendingDate(String pendingDate) {
            this.pendingDate = pendingDate;
        }

    }

}
