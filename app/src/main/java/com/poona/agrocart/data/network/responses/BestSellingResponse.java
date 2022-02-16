package com.poona.agrocart.data.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BestSellingResponse extends BaseResponse{
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
        private ArrayList<ProductListResponse.Product> bestSellingProductList = null;

        public ArrayList<ProductListResponse.Product> getBestSellingProductList() {
            return bestSellingProductList;
        }

        public void setBestSellingProductList(ArrayList<ProductListResponse.Product> bestSellingProductList) {
            this.bestSellingProductList = bestSellingProductList;
        }
    }

}
