
package com.poona.agrocart.data.network.responses.myOrderResponse.orderTrack;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderTrack {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_code")
    @Expose
    private String orderCode;
    @SerializedName("should_deliver_on_date")
    @Expose
    private String shouldDeliverOnDate;
    @SerializedName("deliery_slot_start_and_end_time")
    @Expose
    private String delierySlotStartAndEndTime;
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
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("pending_date")
    @Expose
    private String pendingDate;
    @SerializedName("confirm_date")
    @Expose
    private String confirmDate;
    @SerializedName("in_process_date")
    @Expose
    private String inProcessDate;
    @SerializedName("dispatch_date")
    @Expose
    private String dispatchDate;
    @SerializedName("completed_date")
    @Expose
    private String completedDate;
    @SerializedName("cancelled_date")
    @Expose
    private String cancelledDate;

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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPendingDate() {
        return pendingDate;
    }

    public void setPendingDate(String pendingDate) {
        this.pendingDate = pendingDate;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getInProcessDate() {
        return inProcessDate;
    }

    public void setInProcessDate(String inProcessDate) {
        this.inProcessDate = inProcessDate;
    }

    public String getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(String dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(String cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

}
