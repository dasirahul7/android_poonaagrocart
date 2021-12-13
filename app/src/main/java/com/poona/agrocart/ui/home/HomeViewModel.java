package com.poona.agrocart.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.ui.home.model.Banner;
import com.poona.agrocart.ui.home.model.Product;

import java.util.List;


public class HomeViewModel extends AndroidViewModel {
    private MutableLiveData<List<Banner>> banners;
    private MutableLiveData<List<Product>> products;

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }
}