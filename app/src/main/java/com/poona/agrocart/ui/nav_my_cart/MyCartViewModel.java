package com.poona.agrocart.ui.nav_my_cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class MyCartViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Product>> liveProductList = new MutableLiveData<>();

    public MyCartViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        ArrayList<Product> cartList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product cartItem = new Product();
            cartItem.setName("Bell Pepper Red");
            cartItem.setQty("1kg");
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
