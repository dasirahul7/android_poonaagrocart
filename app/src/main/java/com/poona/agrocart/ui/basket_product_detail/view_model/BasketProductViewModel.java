package com.poona.agrocart.ui.basket_product_detail.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class BasketProductViewModel extends AndroidViewModel
{
    public MutableLiveData<String> productName;
    public MutableLiveData<String> weightOfProduct;
    public MutableLiveData<String> price;
    public MutableLiveData<String> quantity;
    public MutableLiveData<String> productDetailBrief;
    public MutableLiveData<String> nutritionDetailBrief;
    public MutableLiveData<String> ratings;

    public BasketProductViewModel(@NonNull Application application)
    {
        super(application);

        productName=new MutableLiveData<>();
        weightOfProduct=new MutableLiveData<>();
        price=new MutableLiveData<>();
        quantity=new MutableLiveData<>();
        productDetailBrief=new MutableLiveData<>();
        nutritionDetailBrief=new MutableLiveData<>();
        ratings=new MutableLiveData<>();

        productName.setValue("");
        weightOfProduct.setValue("");
        price.setValue("");
        quantity.setValue("");
        productDetailBrief.setValue("");
        nutritionDetailBrief.setValue("");
        ratings.setValue("");
    }
}
