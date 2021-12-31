package com.poona.agrocart.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.ui.home.model.Banner;
import com.poona.agrocart.ui.home.model.Basket;
import com.poona.agrocart.ui.home.model.Category;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;


public class HomeViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Banner>> liveDataBanner;
    private MutableLiveData<ArrayList<Product>> liveDataBestSelling;
    private MutableLiveData<ArrayList<Product>> liveDataOffer;
    private MutableLiveData<ArrayList<Product>> liveDataCartProduct;
    private MutableLiveData<ArrayList<Category>> liveDataCategory;
    private MutableLiveData<ArrayList<Basket>> liveDataBaskets;
    private MutableLiveData<ArrayList<Product>> savesProductInBasket;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        //init all mutable liveData
        liveDataBanner = new MutableLiveData<>();
        liveDataBestSelling = new MutableLiveData<>();
        liveDataOffer = new MutableLiveData<>();
        liveDataCartProduct = new MutableLiveData<>();
        liveDataCategory = new MutableLiveData<>();
        liveDataBaskets = new MutableLiveData<>();
        savesProductInBasket = new MutableLiveData<>();
        //init arraylist
        getBasketData();
        initBanner();
        initCategory();
        initBestSelling();
        initOfferProduct();
        initBasketItems();
        initCartItems();
    }

    private void getBasketData() {
        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
        ArrayList<Product> basketList = preferences.getArrayList(AppConstants.CART_LIST);
        savesProductInBasket.setValue(basketList);
    }

    private void initCartItems() {
        String PID = AppConstants.pId+"OP";
        ArrayList cartItemList = new ArrayList();
        for (int i = 0; i < 4; i++) {
            Product cartProduct = new Product(PID+i, "Vegetable", "1kg", "10% Off",
                    "Rs 65", "Rs 35", getApplication().getString(R.string.img_potato),
                    "Pune");
            if (i==1)
                cartProduct.setImg(getApplication().getString(R.string.img_beat));
            if (i==2)
                cartProduct.setImg(getApplication().getString(R.string.img_carrot));
            cartItemList.add(cartProduct);
            if (i == 3) {
                cartProduct.setOrganic(true);
            }
        }
        liveDataCartProduct.setValue(cartItemList);
    }



    private void initBasketItems() {
        ArrayList<Basket> baskets = new ArrayList<>();
        Basket basket = new Basket("Fruit\n" +
                "Baskets", "Rs 15000", "https://www.linkpicture.com/q/1-200284.png");
        for (int i = 0; i < 4; i++)
            baskets.add(basket);

        liveDataBaskets.setValue(baskets);
    }

    private void initOfferProduct() {
        String PID = AppConstants.pId+"OP";
        ArrayList<Product> offerProducts = new ArrayList<>();
        Product offerProduct = new Product(PID+"1", "Red Apple", "1Kg"
                , "10% Off", "Rs150", "Rs. 140", "https://www.linkpicture.com/q/pngfuel-1-1.png", "Pune");
        Product offerProduct1 = new Product(PID+"2", "Organic Bananas", "12 pcs"
                , "10% Off", "Rs 45", "Rs. 35", "https://www.linkpicture.com/q/banana_2.png", "Pune");
        offerProduct1.setOrganic(true);
        Product offerProduct2 = new Product(PID+"3", "Red Apple", "1Kg"
                , "10% Off", "Rs150", "Rs. 140", "https://www.linkpicture.com/q/pngfuel-1-1.png", "Pune");
        Product offerProduct3 = new Product(PID+"4", "Organic Bananas", "12 pcs"
                , "10% Off", "Rs 45", "Rs. 35", "https://www.linkpicture.com/q/banana_2.png", "Pune");
        offerProduct3.setOrganic(true);
        offerProducts.add(offerProduct);
        offerProducts.add(offerProduct1);
        offerProducts.add(offerProduct2);
        offerProducts.add(offerProduct3);
        liveDataOffer.setValue(offerProducts);
    }

    private void initBestSelling() {
        ArrayList<Product> sellingProducts = new ArrayList<>();
        String PID = AppConstants.pId+"BP";
        Product product = new Product(PID+"1", "Bell Pepper Red", "1Kg"
                , "10% Off", "Rs25", "Rs. 15", "https://www.linkpicture.com/q/capsicon.png", "Pune");
        Product product1 = new Product(PID+"2", "Ginger", "250 gms"
                , "10% Off", "Rs 110", "Rs. 140", "https://www.linkpicture.com/q/ginger.png", "Pune");
        product1.setOrganic(true);
        Product product2 = new Product(PID+"3", "Bell Pepper Red", "1Kg"
                , "10% Off", "Rs25", "Rs. 15", "https://www.linkpicture.com/q/capsicon.png", "Pune");
        Product product3 = new Product(PID+"4", "Ginger", "500 gms"
                , "10% Off", "Rs280", "Rs. 250", "https://www.linkpicture.com/q/ginger.png", "Pune");
        Product product4 = new Product(PID+"5", "Ginger", ""
                , "", "", "", "https://www.linkpicture.com/q/ginger.png", "Pune");
        product4.setQty("0");
        Product product5 = new Product(PID+"6", "Ginger", ""
                , "", "", "", "https://www.linkpicture.com/q/ginger.png", "Pune");
        product5.setQty("0");
        sellingProducts.add(product);
        sellingProducts.add(product1);
        sellingProducts.add(product2);
        sellingProducts.add(product3);
        sellingProducts.add(product4);
        sellingProducts.add(product5);
        liveDataBestSelling.setValue(sellingProducts);
    }

    private void initCategory() {
        ArrayList<Category> categories = new ArrayList<>();
        Category category = new Category("1", "Green Vegetables", getApplication().getString(R.string.img_green_leafy));
        Category category1 = new Category("2", "Fruit Vegetables", getApplication().getString(R.string.img_tomato));
        Category category2 = new Category("3", "Green Vegetables", getApplication().getString(R.string.img_green_leafy));
        Category category3 = new Category("4", "Fruit Vegetables", getApplication().getString(R.string.img_tomato));

        categories.add(category);
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        liveDataCategory.setValue(categories);
    }

    private void initBanner() {
        ArrayList<Banner> banners = new ArrayList<>();
        Banner banner = new Banner("1", "Banner1", "https://www.linkpicture.com/q/banner_img.jpg");
        for (int i = 0; i < 3; i++) {
            banners.add(banner);
        }
        liveDataBanner.setValue(banners);
    }

    public MutableLiveData<ArrayList<Banner>> getLiveDataBanner() {
        return liveDataBanner;
    }

    public MutableLiveData<ArrayList<Category>> getLiveDataCategory() {
        return liveDataCategory;
    }

    public MutableLiveData<ArrayList<Product>> getLiveDataBestSelling() {
        return liveDataBestSelling;
    }

    public MutableLiveData<ArrayList<Product>> getLiveDataOffer() {
        return liveDataOffer;
    }

    public MutableLiveData<ArrayList<Basket>> getLiveDataBaskets() {
        return liveDataBaskets;
    }

    public MutableLiveData<ArrayList<Product>> getLiveDataCartProduct() {
        return liveDataCartProduct;
    }

    public MutableLiveData<ArrayList<Product>> getSavesProductInBasket() {
        return savesProductInBasket;
    }
}