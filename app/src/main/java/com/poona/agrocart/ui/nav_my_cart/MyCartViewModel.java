package com.poona.agrocart.ui.nav_my_cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.ui.home.model.ProductOld;

import java.util.ArrayList;

public class MyCartViewModel extends AndroidViewModel {

    public MutableLiveData<ArrayList<ProductOld>> liveProductList = new MutableLiveData<>();

    public MyCartViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<ProductOld>> getCartList(){
        return liveProductList;
    }
}
