
package com.poona.agrocart.data.network.responses.myOrderResponse;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscribeBasketListCustomerResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order_detials")
    @Expose
    private List<SubscribeBasketListCustomer> orderDetials = null;

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

    public List<SubscribeBasketListCustomer> getOrderDetials() {
        return orderDetials;
    }

    public void setOrderDetials(List<SubscribeBasketListCustomer> orderDetials) {
        this.orderDetials = orderDetials;
    }
    public class SubscribeBasketListCustomer {

        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("order_subscription_id")
        @Expose
        private String orderSubscriptionId;
        @SerializedName("order_code")
        @Expose
        private String orderCode;
        @SerializedName("basket_name")
        @Expose
        private String basketName;
        @SerializedName("paid_amount")
        @Expose
        private String paidAmount;
        @SerializedName("total_quantity")
        @Expose
        private String totalQuantity;
        @SerializedName("transaction_id")
        @Expose
        private String transactionId;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("invoice_file")
        @Expose
        private String invoiceFile;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderSubscriptionId() {
            return orderSubscriptionId;
        }

        public void setOrderSubscriptionId(String orderSubscriptionId) {
            this.orderSubscriptionId = orderSubscriptionId;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getBasketName() {
            return basketName;
        }

        public void setBasketName(String basketName) {
            this.basketName = basketName;
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

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getInvoiceFile() {
            return invoiceFile;
        }

        public void setInvoiceFile(String invoiceFile) {
            this.invoiceFile = invoiceFile;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }

}
