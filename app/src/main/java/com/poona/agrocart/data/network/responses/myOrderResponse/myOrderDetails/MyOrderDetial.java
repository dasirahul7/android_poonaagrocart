
package com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyOrderDetial {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_code")
    @Expose
    private String orderCode;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("total_quantity")
    @Expose
    private String totalQuantity;
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
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("invoice_file")
    @Expose
    private String invoiceFile;
    @SerializedName("product_amount")
    @Expose
    private String productAmount;
    @SerializedName("paid_amount")
    @Expose
    private String paidAmount;
    @SerializedName("delivery_charges")
    @Expose
    private String deliveryCharges;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("star_rating")
    @Expose
    private String starRating;
    @SerializedName("feedback")
    @Expose
    private String feedback;
    @SerializedName("should_deliver_on_date")
    @Expose
    private String shouldDeliverOnDate;
    @SerializedName("deliery_slot_start_and_end_time")
    @Expose
    private String delierySlotStartAndEndTime;
    @SerializedName("items_details")
    @Expose
    private List<ItemsDetail> itemsDetails = null;
    @SerializedName("rating")
    @Expose
    private MyOrderRating rating;
    @SerializedName("reviews")
    @Expose
    private List<MyOrderReview> reviews = null;

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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getInvoiceFile() {
        return invoiceFile;
    }

    public void setInvoiceFile(String invoiceFile) {
        this.invoiceFile = invoiceFile;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
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

    public MyOrderRating getRating() {
        return rating;
    }

    public void setRating(MyOrderRating rating) {
        this.rating = rating;
    }

    public List<MyOrderReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MyOrderReview> reviews) {
        this.reviews = reviews;
    }

}

