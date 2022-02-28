package com.poona.agrocart.data.network.responses.cartResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.BaseResponse;

import java.util.ArrayList;

public class MyCartResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<CartData> data = null;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("cart_items")
    @Expose
    private Integer cartItems;

    public ArrayList<CartData> getData() {
        return data;
    }

    public void setData(ArrayList<CartData> data) {
        this.data = data;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getCartItems() {
        return cartItems;
    }

    public void setCartItems(Integer cartItems) {
        this.cartItems = cartItems;
    }
}
