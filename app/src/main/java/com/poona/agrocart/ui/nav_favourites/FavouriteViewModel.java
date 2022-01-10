package com.poona.agrocart.ui.nav_favourites;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class FavouriteViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<Product>> favouriteItemMutableLiveData;
    public FavouriteViewModel(@NonNull Application application) {
        super(application);
        favouriteItemMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Product>> getFavouriteItemMutableLiveData() { return favouriteItemMutableLiveData; }
}
