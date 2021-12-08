package com.poona.agrocart.ui.product_detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ProductDetailViewModel extends AndroidViewModel
{
    public MutableLiveData<String> productName;
    public MutableLiveData<String> productLocation;
    public MutableLiveData<List<String>> weightOfProduct;
    public MutableLiveData<String> price;
    public MutableLiveData<String> quantity;
    public MutableLiveData<String> productDetailBrief;
    public MutableLiveData<String> nutritionDetailBrief;
    public MutableLiveData<String> ratings;
    public MutableLiveData<Boolean> basket;

    public ProductDetailViewModel(@NonNull Application application)
    {
        super(application);

        productName=new MutableLiveData<>();
        basket=new MutableLiveData<>();
        productLocation=new MutableLiveData<>();
        weightOfProduct=new MutableLiveData<>();
        price=new MutableLiveData<>();
        quantity=new MutableLiveData<>();
        productDetailBrief=new MutableLiveData<>();
        nutritionDetailBrief=new MutableLiveData<>();
        ratings=new MutableLiveData<>();

        basket.setValue(null);
        productName.setValue("");
        productLocation.setValue("");
        weightOfProduct.setValue(null);
        price.setValue("");
        quantity.setValue("");
        productDetailBrief.setValue("");
        nutritionDetailBrief.setValue("");
        ratings.setValue("");
    }


}
