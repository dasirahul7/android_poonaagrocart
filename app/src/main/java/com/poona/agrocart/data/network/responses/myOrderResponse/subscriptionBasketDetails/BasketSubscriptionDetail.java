
package com.poona.agrocart.data.network.responses.myOrderResponse.subscriptionBasketDetails;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BasketSubscriptionDetail {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_subscription_id")
    @Expose
    private String orderSubscriptionId;
    @SerializedName("order_code")
    @Expose
    private String orderCode;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
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
    private Object transactionId;
    @SerializedName("invoice_file")
    @Expose
    private Object invoiceFile;
    @SerializedName("product_amount")
    @Expose
    private String productAmount;
    @SerializedName("delivery_charges")
    @Expose
    private String deliveryCharges;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("paid_amount")
    @Expose
    private String paidAmount;
    @SerializedName("no_of_subscription")
    @Expose
    private String noOfSubscription;
    @SerializedName("items_details")
    @Expose
    private List<ItemsDetail> itemsDetails = null;
    @SerializedName("rating")
    @Expose
    private Rating rating;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;

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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
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

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getNoOfSubscription() {
        return noOfSubscription;
    }

    public void setNoOfSubscription(String noOfSubscription) {
        this.noOfSubscription = noOfSubscription;
    }

    public List<ItemsDetail> getItemsDetails() {
        return itemsDetails;
    }

    public void setItemsDetails(List<ItemsDetail> itemsDetails) {
        this.itemsDetails = itemsDetails;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
