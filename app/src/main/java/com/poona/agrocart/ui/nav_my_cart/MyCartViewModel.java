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
    private AppSharedPreferences mSessionManager;

    public MyCartViewModel(@NonNull Application application) {
        super(application);
//        liveProductList.setValue(null);
//        init();
    }

    private void init() {
        mSessionManager = new AppSharedPreferences(getApplication());
        ArrayList<Product> cartList = new ArrayList<>();
//        cartList = mSessionManager.getArrayList(AppConstants.CART_LIST);
        for (int i = 0; i < 10; i++) {
            Product cartItem = new Product();
            cartItem.setName("Bell Pepper Red");
            cartItem.setWeight("1kg");
            cartItem.setPrice("Rs.30");
            cartItem.setOfferPrice("RS.25");
            cartItem.setImg("https://www.linkpicture.com/q/capsicon.png");
            cartItem.setLocation("Vishrantwadi");
            cartList.add(cartItem);
        }
        liveProductList.setValue(cartList);
    }

    public MutableLiveData<ArrayList<Product>> getCartList(){
        return liveProductList;
    }
}
