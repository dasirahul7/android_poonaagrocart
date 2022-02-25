package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.responses.homeResponse.Product;

import java.util.ArrayList;

public class BestSellingResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private BestSellingData bestSellingData;

    public BestSellingData getBestSellingData() {
        return bestSellingData;
    }

    public void setBestSellingData(BestSellingData bestSellingData) {
        this.bestSellingData = bestSellingData;
    }

    public class BestSellingData {
        @SerializedName("best_selling_product_list")
        @Expose
        private ArrayList<Product> bestSellingProductList = null;

        public ArrayList<Product> getBestSellingProductList() {
            return bestSellingProductList;
        }

        public void setBestSellingProductList(ArrayList<Product> bestSellingProductList) {
            this.bestSellingProductList = bestSellingProductList;
        }
    }

}
