
package com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyOrderDetial {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_code")
    @Expose
    private String orderCode;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("total_quantity")
    @Expose
    private Object totalQuantity;
    @SerializedName("pending_date")
    @Expose
    private String pendingDate;
    @SerializedName("delivery_code")
    @Expose
    private String deliveryCode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("order_address_text")
    @Expose
    private String orderAddressText;
    @SerializedName("order_area_name")
    @Expose
    private String orderAreaName;
    @SerializedName("order_city_name")
    @Expose
    private String orderCityName;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("transaction_id")
    @Expose
    private Object transactionId;
    @SerializedName("invoice_file")
    @Expose
    private Object invoiceFile;
    @SerializedName("product_amount")
    @Expose
    private Object productAmount;
    @SerializedName("paid_amount")
    @Expose
    private Object paidAmount;
    @SerializedName("delivery_charges")
    @Expose
    private Object deliveryCharges;
    @SerializedName("discount")
    @Expose
    private Object discount;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("star_rating")
    @Expose
    private Object starRating;
    @SerializedName("feedback")
    @Expose
    private Object feedback;
    @SerializedName("should_deliver_on_date")
    @Expose
    private String shouldDeliverOnDate;
    @SerializedName("deliery_slot_start_and_end_time")
    @Expose
    private String delierySlotStartAndEndTime;
    @SerializedName("items_details")
    @Expose
    private List<ItemsDetail> itemsDetails = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Object getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Object totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getPendingDate() {
        return pendingDate;
    }

    public void setPendingDate(String pendingDate) {
        this.pendingDate = pendingDate;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrderAddressText() {
        return orderAddressText;
    }

    public void setOrderAddressText(String orderAddressText) {
        this.orderAddressText = orderAddressText;
    }

    public String getOrderAreaName() {
        return orderAreaName;
    }

    public void setOrderAreaName(String orderAreaName) {
        this.orderAreaName = orderAreaName;
    }

    public String getOrderCityName() {
        return orderCityName;
    }

    public void setOrderCityName(String orderCityName) {
        this.orderCityName = orderCityName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Object getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Object transactionId) {
        this.transactionId = transactionId;
    }

    public Object getInvoiceFile() {
        return invoiceFile;
    }

    public void setInvoiceFile(Object invoiceFile) {
        this.invoiceFile = invoiceFile;
    }

    public Object getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Object productAmount) {
        this.productAmount = productAmount;
    }

    public Object getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Object paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Object getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(Object deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public Object getDiscount() {
        return discount;
    }

    public void setDiscount(Object discount) {
        this.discount = discount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Object getStarRating() {
        return starRating;
    }

    public void setStarRating(Object starRating) {
        this.starRating = starRating;
    }

    public Object getFeedback() {
        return feedback;
    }

    public void setFeedback(Object feedback) {
        this.feedback = feedback;
    }

    public String getShouldDeliverOnDate() {
        return shouldDeliverOnDate;
    }

    public void setShouldDeliverOnDate(String shouldDeliverOnDate) {
        this.shouldDeliverOnDate = shouldDeliverOnDate;
    }

    public String getDelierySlotStartAndEndTime() {
        return delierySlotStartAndEndTime;
    }

    public void setDelierySlotStartAndEndTime(String delierySlotStartAndEndTime) {
        this.delierySlotStartAndEndTime = delierySlotStartAndEndTime;
    }

    public List<ItemsDetail> getItemsDetails() {
        return itemsDetails;
    }

    public void setItemsDetails(List<ItemsDetail> itemsDetails) {
        this.itemsDetails = itemsDetails;
    }

}
