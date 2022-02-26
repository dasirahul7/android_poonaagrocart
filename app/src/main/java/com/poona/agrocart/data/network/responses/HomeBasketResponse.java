package com.poona.agrocart.data.network.responses;

import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.homeResponse.Basket;

import java.util.ArrayList;
import java.util.List;

public class HomeBasketResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private BasketData data;

    public BasketData getData() {
        return data;
    }

    public void setData(BasketData data) {
        this.data = data;
    }

    public class BasketData {
        @SerializedName("basket_list")
        @Expose
        private ArrayList<Basket> baskets;

        public ArrayList<Basket> getBaskets() {
            return baskets;
        }

        public void setBaskets(ArrayList<Basket> baskets) {
            this.baskets = baskets;
        }
    }


    public class BasketImges {

        @SerializedName("basket_img")
        @Expose
        private String basketImg;

        public String getBasketImg() {
            return basketImg;
        }

        public void setBasketImg(String basketImg) {
            this.basketImg = basketImg;
        }
    }
}
