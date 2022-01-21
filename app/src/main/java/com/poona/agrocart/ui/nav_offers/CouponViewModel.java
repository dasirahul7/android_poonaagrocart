package com.poona.agrocart.ui.nav_offers;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CouponViewModel extends ViewModel {
MutableLiveData<ArrayList<Coupons>> liveCoupons = new MutableLiveData<>();

    public CouponViewModel() {
        ArrayList<Coupons> coupons = new ArrayList<>();
    for (int i=0;i<20;i++){
        Coupons coupon = new Coupons(i,"OFF250","10%","25 November 2021","Order min Rs. 500 ","Order min Rs. 500 ","Max 100Rs.");
        coupons.add(coupon);
    }
        liveCoupons.setValue(coupons);
    }
}