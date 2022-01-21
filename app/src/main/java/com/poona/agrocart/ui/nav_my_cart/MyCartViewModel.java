package com.poona.agrocart.ui.nav_my_cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class MyCartViewModel extends AndroidViewModel {

    public MutableLiveData<ArrayList<Product>> liveProductList = new MutableLiveData<>();

    public MyCartViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<Product>> getCartList(){
        return liveProductList;
    }
}
