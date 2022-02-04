
package com.poona.agrocart.data.network.reponses;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.nav_offers.Coupon;
import com.poona.agrocart.ui.nav_offers.Coupons;

public class CouponResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private ArrayList<Coupon> coupons = null;

    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }
}
