package com.poona.agrocart.ui.my_cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MyCartViewModel extends AndroidViewModel
{
    public MutableLiveData<String> name;
    public MutableLiveData<String> weight;
    public MutableLiveData<String> price;
    public MutableLiveData<String> finalPrice;
    public MutableLiveData<String> quantity;


    public MyCartViewModel(@NonNull Application application)
    {
        super(application);

        name=new MutableLiveData<>();
        weight=new MutableLiveData<>();
        price=new MutableLiveData<>();
        finalPrice=new MutableLiveData<>();
        quantity=new MutableLiveData<>();

        name.setValue("");
        weight.setValue("");
        price.setValue("");
        quantity.setValue("");
        finalPrice.setValue("");
    }
}
