package com.poona.agrocart.ui.nav_orders.track_order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.R;

public class OrderTrackViewModel extends AndroidViewModel
{
    public MutableLiveData<String> orderId;
    public MutableLiveData<String> expectedDeliveryDate;
    public MutableLiveData<String> customerName;
    public MutableLiveData<String> mobileNumber;
    public MutableLiveData<String> address;

    public OrderTrackViewModel(@NonNull Application application)
    {
        super(application);

        orderId=new MutableLiveData<>();
        expectedDeliveryDate=new MutableLiveData<>();
        customerName=new MutableLiveData<>();
        mobileNumber=new MutableLiveData<>();
        address=new MutableLiveData<>();

        orderId.setValue(application.getString(R.string.order_id));
        expectedDeliveryDate.setValue(application.getString(R.string.sep_30_2021_9_00_am_12_00pm));
        customerName.setValue(application.getString(R.string.ayush_shah));
        mobileNumber.setValue(application.getString(R.string._91_986_095_3315));
        address.setValue(application.getString(R.string.nand_nivas_building_floor_3_b_3_lane_no_13_bhatrau_nivas_vishrantwadi_pune_411015));
    }
}
