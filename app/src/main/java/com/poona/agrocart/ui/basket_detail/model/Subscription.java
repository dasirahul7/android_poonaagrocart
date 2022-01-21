package com.poona.agrocart.ui.basket_detail.model;

import java.util.ArrayList;

public class Subscription {
    String subPrice;
    String subUnit;
    String subMsg;
    ArrayList<String> subTypes;
    String subQty;
    String subStartDate;
    String subUnitPrice;
    String subTotalAmount;

    public Subscription() {
    }

    public Subscription(String subPrice, String subUnit, String subMsg, ArrayList<String> subTypes, String subQty, String subStartDate, String subUnitPrice, String subTotalAmount) {
        this.subPrice = subPrice;
        this.subUnit = subUnit;
        this.subMsg = subMsg;
        this.subTypes = subTypes;
        this.subQty = subQty;
        this.subStartDate = subStartDate;
        this.subUnitPrice = subUnitPrice;
        this.subTotalAmount = subTotalAmount;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getSubPrice() {
        return subPrice;
    }

    public void setSubPrice(String subPrice) {
        this.subPrice = subPrice;
    }

    public String getSubUnit() {
        return subUnit;
    }

    public void setSubUnit(String subUnit) {
        this.subUnit = subUnit;
    }

    public ArrayList<String> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(ArrayList<String> subTypes) {
        this.subTypes = subTypes;
    }

    public String getSubQty() {
        return subQty;
    }

    public void setSubQty(String subQty) {
        this.subQty = subQty;
    }

    public String getSubStartDate() {
        return subStartDate;
    }

    public void setSubStartDate(String subStartDate) {
        this.subStartDate = subStartDate;
    }

    public String getSubUnitPrice() {
        return subUnitPrice;
    }

    public void setSubUnitPrice(String subUnitPrice) {
        this.subUnitPrice = subUnitPrice;
    }

    public String getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(String subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }
}
