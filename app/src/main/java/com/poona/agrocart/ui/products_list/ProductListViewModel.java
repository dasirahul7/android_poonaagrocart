package com.poona.agrocart.ui.products_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.R;
import com.poona.agrocart.ui.home.model.Basket;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class ProductListViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Product>> vegesMutableLiveData;
    private MutableLiveData<ArrayList<Product>> fruitMutableLiveData;
    private MutableLiveData<ArrayList<Basket>> basketMutableLiveData;

    public ProductListViewModel(Application application) {
        super(application);
        initList();
    }

    private void initList() {
        ArrayList<Product> vegetableArrayList = new ArrayList<>();
        ArrayList<Product> fruitsArrayList = new ArrayList<>();
        ArrayList<Basket> basketArrayList = new ArrayList<>();
        vegesMutableLiveData = new MutableLiveData<>();
        fruitMutableLiveData = new MutableLiveData<>();
        basketMutableLiveData = new MutableLiveData<>();
        //Add Veges
        for (int i = 0; i < 6; i++) {
            Product vegetable = new Product("123", "Ginger", "1kg",
                    "10", "100", getApplication().getString(R.string.img_ginger), "Pune");
            if (i == 3)
                vegetable.setWeight("0");
            vegetableArrayList.add(vegetable);
        }
        vegesMutableLiveData.setValue(vegetableArrayList);
        //Add fruits
        for (int i = 0; i < 6; i++) {
            Product fruit = new Product("123", "Apple", i + "kg",
                    "10", "40", getApplication().getString(R.string.img_red_apple), "Pune");
            if (i == 3)
                fruit.setWeight("0");
            if (i == 4)
                fruit.setOrganic(true);
            fruitsArrayList.add(fruit);
        }
        fruitMutableLiveData.setValue(fruitsArrayList);

        //Add Baskets
//        for (int i = 0; i < 10; i++) {
//            Basket basket = new Basket("Sprout Baskets", "Rs.500", getApplication().getString(R.string.basket_sprout_img));
//            if (i == 1) {
//                {
//                    basket.setName("Nutrition Baskets");
//                    basket.setPrice("Rs. 700");
//                    basket.setImg(getApplication().getString(R.string.basket_nutrition_img));
//                }
//                if (i == 2) {
//                    {
//                        basket.setName("Diet Baskets");
//                        basket.setPrice("Rs. 900");
//                        basket.setImg(getApplication().getString(R.string.basket_diet_img));
//                    }
//                }
//            }
//            basketArrayList.add(basket);
//        }
        basketMutableLiveData.setValue(basketArrayList);
    }

    public MutableLiveData<ArrayList<Product>> getVegesMutableLiveData() {
        return vegesMutableLiveData;
    }

    public MutableLiveData<ArrayList<Product>> getFruitMutableLiveData() {
        return fruitMutableLiveData;
    }

    public MutableLiveData<ArrayList<Basket>> getBasketMutableLiveData() {
        return basketMutableLiveData;
    }
}
