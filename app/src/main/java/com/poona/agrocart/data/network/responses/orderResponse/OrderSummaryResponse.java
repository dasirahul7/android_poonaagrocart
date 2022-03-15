package com.poona.agrocart.data.network.responses.orderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.AddressesResponse;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.Coupon;

import java.util.ArrayList;

public class OrderSummaryResponse {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("items_details")
    @Expose
    public ArrayList<ItemsDetail> itemsDetails = null;
    @SerializedName("sub_total")
    @Expose
    public Integer subTotal;
    @SerializedName("discount")
    @Expose
    public Integer discount;
    @SerializedName("delivery_charges")
    @Expose
    public Integer deliveryCharges;
    @SerializedName("total_amount")
    @Expose
    public Integer totalAmount;
    @SerializedName("address")
    @Expose
    public ArrayList<AddressesResponse.Address> address = null;
    @SerializedName("delivery")
    @Expose
    public Delivery delivery;
    @SerializedName("payment_mode")
    @Expose
    public ArrayList<Payments> paymentMode = null;
    @SerializedName("coupon_code_list")
    @Expose
    public ArrayList<Coupon> couponCodeList = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ItemsDetail> getItemsDetails() {
        return itemsDetails;
    }

    public void setItemsDetails(ArrayList<ItemsDetail> itemsDetails) {
        this.itemsDetails = itemsDetails;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Integer getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(Integer deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<AddressesResponse.Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<AddressesResponse.Address> address) {
        this.address = address;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public ArrayList<Payments> getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(ArrayList<Payments> paymentMode) {
        this.paymentMode = paymentMode;
    }

    public ArrayList<Coupon> getCouponCodeList() {
        return couponCodeList;
    }

    public void setCouponCodeList(ArrayList<Coupon> couponCodeList) {
        this.couponCodeList = couponCodeList;
    }
}
