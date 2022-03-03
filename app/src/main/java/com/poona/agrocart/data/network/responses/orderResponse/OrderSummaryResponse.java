package com.poona.agrocart.data.network.responses.orderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.AddressesResponse;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.Coupon;
import com.poona.agrocart.ui.order_summary.model.DeliverySlot;

import java.util.ArrayList;

public class OrderSummaryResponse {
    @SerializedName("status")
    @Expose
    public int status;
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
    public float discount;
    @SerializedName("delivery_charges")
    @Expose
    public Integer deliveryCharges;
    @SerializedName("total_amount")
    @Expose
    public float totalAmount;
    @SerializedName("address")
    @Expose
    public ArrayList<AddressesResponse.Address> address = null;
    @SerializedName("delivery")
    @Expose
    public ArrayList<Delivery> delivery = null;
    @SerializedName("payment_mode")
    @Expose
    public ArrayList<Payments> paymentMode = null;
    @SerializedName("coupon_code_list")
    @Expose
    public ArrayList<Coupon> couponCodeList = null;
}
