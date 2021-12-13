package com.poona.agrocart.ui.our_stores;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.ui.our_stores.model.Store;

public class StoreDetailViewModel extends AndroidViewModel {
    public MutableLiveData<Store> storeMutableLiveData;

    public StoreDetailViewModel(Application application) {
        super(application);
        storeMutableLiveData = new MutableLiveData<>();
        storeMutableLiveData.setValue(null);
    }
}