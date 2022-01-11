package com.poona.agrocart.ui.nav_orders.order_details;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.poona.agrocart.ui.nav_orders.model.OrderDetails;

public class OrderDetailsViewModel extends ViewModel {
    public MutableLiveData<OrderDetails> orderDetailsLiveData;

    public OrderDetailsViewModel() {
        orderDetailsLiveData = new MutableLiveData<>();
        orderDetailsLiveData.setValue(null);
    }
}