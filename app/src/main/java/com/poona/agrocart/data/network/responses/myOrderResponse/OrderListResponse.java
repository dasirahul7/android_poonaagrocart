
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
    @SerializedName("order_list")
    @Expose
    private List<Order> orderList = null;

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

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }


    public class Order {

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
        @SerializedName("created_at")
        @Expose
        private String createdAt;

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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }

}
