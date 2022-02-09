package com.poona.agrocart.ui.nav_stores;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.ui.nav_stores.model.OurStoreListData;
import com.poona.agrocart.ui.nav_stores.model.Store;
import com.poona.agrocart.ui.nav_stores.model.store_details.StoreDetail;

public class StoreDetailViewModel extends AndroidViewModel {
    public MutableLiveData<StoreDetail> storeMutableLiveData;

    public StoreDetailViewModel(Application application) {
        super(application);
        storeMutableLiveData = new MutableLiveData<>();
        storeMutableLiveData.setValue(null);
    }
}