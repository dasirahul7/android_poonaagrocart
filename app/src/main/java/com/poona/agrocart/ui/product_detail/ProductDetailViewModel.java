package com.poona.agrocart.ui.product_detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.ui.product_detail.model.ProductDetail;

public class ProductDetailViewModel extends AndroidViewModel
{
    public MutableLiveData<ProductDetail> productDetailMutableLiveData;

    public ProductDetailViewModel(@NonNull Application application)
    {
        super(application);
        productDetailMutableLiveData = new MutableLiveData<>();
        productDetailMutableLiveData.setValue(null);
    }


}
