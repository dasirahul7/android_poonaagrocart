package com.poona.agrocart.ui.nav_orders.model;

public class OrderDetails {
    String successTitleMsg, successMsg;

    public OrderDetails(String successTitleMsg, String successMsg) {
        this.successTitleMsg = successTitleMsg;
        this.successMsg = successMsg;
    }

    public String getSuccessTitleMsg() {
        return successTitleMsg;
    }

    public void setSuccessTitleMsg(String successTitleMsg) {
        this.successTitleMsg = successTitleMsg;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }
}
