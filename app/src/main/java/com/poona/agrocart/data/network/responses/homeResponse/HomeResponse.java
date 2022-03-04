package com.poona.agrocart.data.network.responses.homeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.BaseResponse;

import java.io.Serializable;

public class HomeResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("message")
    @Expose
    private String message;
    /* for cart items*/
    @Expose
    @SerializedName("cart_items")
    private int cartItems;
    @Expose
    @SerializedName("total_amount")
    private float totalAmount;
    @SerializedName("response")
    @Expose
    private HomeResponseData homeResponseData;

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getCartItems() {
        return cartItems;
    }

    public void setCartItems(int cartItems) {
        this.cartItems = cartItems;
    }

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

    public HomeResponseData getHomeResponseData() {
        return homeResponseData;
    }

    public void setHomeResponseData(HomeResponseData homeResponseData) {
        this.homeResponseData = homeResponseData;
    }

    /*Home response Data class*/

    /*The Banner class*/


    /*Category class*/

    /*The Basket class*/
    /*HomeProduct class*/


    /*O3 Class*/

    /*Seasonal Call*/


    /*Store Banner Class*/
    /*User Data Class*/

}
