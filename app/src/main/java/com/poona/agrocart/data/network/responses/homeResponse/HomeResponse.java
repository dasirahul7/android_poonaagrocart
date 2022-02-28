package com.poona.agrocart.data.network.responses.homeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.BaseResponse;

import java.io.Serializable;

public class HomeResponse extends BaseResponse implements Serializable {
    @SerializedName("cart_items")
    @Expose
    private int cartItems;
    @SerializedName("response")
    @Expose
    private HomeResponseData homeResponseData;

    public HomeResponseData getHomeResponseData() {
        return homeResponseData;
    }

    public void setHomeResponseData(HomeResponseData homeResponseData) {
        this.homeResponseData = homeResponseData;
    }

    public int getCartItems() {
        return cartItems;
    }

    public void setCartItems(int cartItems) {
        this.cartItems = cartItems;
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
