package com.poona.agrocart.ui.offer_coupon;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CouponViewModel extends ViewModel {
MutableLiveData<ArrayList<Coupons>> liveCoupons = new MutableLiveData<>();

    public CouponViewModel() {
        ArrayList<Coupons> coupons = new ArrayList<>();
        Coupons coupon = new Coupons("OFF250","10%","25 November 2021","Order min Rs. 500 ","Order min Rs. 500 ");
    for (int i=0;i<5;i++)
        coupons.add(coupon);
    liveCoupons.setValue(coupons);
    }
}