package com.poona.agrocart.ui.nav_orders.track_order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.R;
import com.poona.agrocart.ui.nav_orders.model.Order;

public class OrderTrackViewModel extends AndroidViewModel
{
    private MutableLiveData<Order> orderMutableLiveData;

    public OrderTrackViewModel(@NonNull Application application)
    {
        super(application);
        orderMutableLiveData = new MutableLiveData<>();
        Order trackOrder = new Order(application.getString(R.string._paac002),
                application.getString(R.string.sep_30_2021_9_00_am_12_00pm),
                application.getString(R.string.ayush_shah),
                application.getString(R.string._91_986_095_3315),
                application.getString(R.string.nand_nivas_building_floor_3_b_3_lane_no_13_bhatrau_nivas_vishrantwadi_pune_411015));
        orderMutableLiveData.postValue(trackOrder);
    }

    public MutableLiveData<Order> getOrderMutableLiveData() {
        return orderMutableLiveData;
    }
}
