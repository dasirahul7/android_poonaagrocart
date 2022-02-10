package com.poona.agrocart.ui.nav_favourites;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.ui.home.model.ProductOld;

import java.util.ArrayList;

public class FavouriteViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<ProductOld>> favouriteItemMutableLiveData;
    public FavouriteViewModel(@NonNull Application application) {
        super(application);
        favouriteItemMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<ProductOld>> getFavouriteItemMutableLiveData() { return favouriteItemMutableLiveData; }
}
