package com.poona.agrocart.ui.home;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.BannerResponse;
import com.poona.agrocart.data.network.reponses.BannerResponse;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.ui.home.model.Banner;
import com.poona.agrocart.ui.home.model.Basket;
import com.poona.agrocart.ui.home.model.Category;
import com.poona.agrocart.ui.home.model.Product;
import com.poona.agrocart.ui.home.model.SeasonalProduct;
import com.poona.agrocart.ui.sign_in.SignInFragment;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;


public class HomeViewModel extends AndroidViewModel {
    private String TAG = HomeViewModel.class.getSimpleName();

    private MutableLiveData<ArrayList<Banner>> liveDataBanner;
    private MutableLiveData<ArrayList<Product>> liveDataBestSelling;
    private MutableLiveData<ArrayList<Product>> liveDataOffer;
    private MutableLiveData<ArrayList<Product>> liveDataCartProduct;
    private MutableLiveData<ArrayList<Category>> liveDataCategory;
    private MutableLiveData<ArrayList<Basket>> liveDataBaskets;
    private MutableLiveData<ArrayList<Product>> savesProductInBasket;
    private MutableLiveData<ArrayList<SeasonalProduct>> liveSeasonProducts;


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
        liveSeasonProducts = new MutableLiveData<>();
        //init arraylist
        getBasketData();
        initBanner();
        initCategory();
        initBestSelling();
        initOfferProduct();
        initBasketItems();
        initCartItems();
        initSeasonalBanner();
    }

    private void initSeasonalBanner() {
        ArrayList<SeasonalProduct> seasonalProducts = new ArrayList<>();
        SeasonalProduct seasonalProduct = new SeasonalProduct();
        seasonalProduct.setsProductName("Alphanso");
        seasonalProduct.setsProductPlace("Ratnagiri");
        seasonalProduct.setsProductQuality("Fresh farm mangoes");
        seasonalProduct.setsProductImage(getApplication().getString(R.string.url_mango_img));
        seasonalProduct.setType("Yellow");
        SeasonalProduct seasonalProduct1 = new SeasonalProduct();
        seasonalProduct1.setsProductName("Grapes");
        seasonalProduct1.setsProductPlace("Nashik");
        seasonalProduct1.setsProductQuality("Fresh farm grapes");
        seasonalProduct1.setsProductImage(getApplication().getString(R.string.url_grapes_img));
        seasonalProduct1.setType("Green");
        SeasonalProduct seasonalProduct2 = new SeasonalProduct();
        seasonalProduct2.setsProductName("Alphanso");
        seasonalProduct2.setsProductPlace("Ratnagiri");
        seasonalProduct2.setsProductQuality("Fresh farm mangoes");
        seasonalProduct2.setsProductImage(getApplication().getString(R.string.url_mango_img));
        seasonalProduct2.setType("Yellow");
        SeasonalProduct seasonalProduct3 = new SeasonalProduct();
        seasonalProduct3.setsProductName("Grapes");
        seasonalProduct3.setsProductPlace("Nashik");
        seasonalProduct3.setsProductQuality("Fresh farm grapes");
        seasonalProduct3.setsProductImage(getApplication().getString(R.string.url_grapes_img));
        seasonalProduct3.setType("Green");
        seasonalProducts.add(seasonalProduct);
        seasonalProducts.add(seasonalProduct1);
        seasonalProducts.add(seasonalProduct2);
        seasonalProducts.add(seasonalProduct3);
        seasonalProducts.add(seasonalProduct);
        seasonalProducts.add(seasonalProduct1);
        seasonalProducts.add(seasonalProduct2);
        seasonalProducts.add(seasonalProduct3);
        liveSeasonProducts.setValue(seasonalProducts);
    }

    private void getBasketData() {
        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
        ArrayList<Product> basketList = preferences.getSavedCartList(AppConstants.CART_LIST);
        savesProductInBasket.setValue(basketList);
    }

    private void initCartItems() {
        String PID = AppConstants.pId + "OP";
        ArrayList cartItemList = new ArrayList();
        for (int i = 0; i < 4; i++) {
            Product cartProduct = new Product(PID + i, "Vegetable", "1kg",
                    "10", "65", getApplication().getString(R.string.img_potato),
                    "Pune", "Green");
            if (i == 1)
                cartProduct.setImg(getApplication().getString(R.string.img_beat));
            if (i == 2)
                cartProduct.setImg(getApplication().getString(R.string.img_beat));
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
        String PID = AppConstants.pId + "OP";
        ArrayList<Product> offerProducts = new ArrayList<>();
        Product offerProduct = new Product(PID + "1", "Red Apple", "1Kg"
                , "10", "150", "https://www.linkpicture.com/q/pngfuel-1-1.png", "Pune", "Kashmir");
        Product offerProduct1 = new Product(PID + "2", "Organic Bananas", "12 pcs"
                , "10", "45", "https://www.linkpicture.com/q/banana_2.png", "Pune", "Kashmir");
        offerProduct1.setOrganic(true);
        Product offerProduct2 = new Product(PID + "3", "Red Apple", "1Kg"
                , "10", "150", "https://www.linkpicture.com/q/pngfuel-1-1.png", "Pune", "Kashmir");
        Product offerProduct3 = new Product(PID + "4", "Organic Bananas", "12 pcs"
                , "10", "45", "https://www.linkpicture.com/q/banana_2.png", "Pune", "Kashmir");
        offerProduct3.setOrganic(true);
        offerProducts.add(offerProduct);
        offerProducts.add(offerProduct1);
        offerProducts.add(offerProduct2);
        offerProducts.add(offerProduct3);
        liveDataOffer.setValue(offerProducts);
    }

    private void initBestSelling() {
        ArrayList<Product> sellingProducts = new ArrayList<>();
        String PID = AppConstants.pId + "BP";
        Product product = new Product(PID + "1", "Bell Pepper Red", "1Kg"
                , "10", "25", "https://www.linkpicture.com/q/capsicon.png",
                "Pune", "Walmart");
        Product product1 = new Product(PID + "2", "Ginger", "250 gms"
                , "10", "110", "https://www.linkpicture.com/q/ginger.png",
                "Pune", "Walmart");
        product1.setOrganic(true);
        Product product2 = new Product(PID + "3", "Bell Pepper Red", "1Kg"
                , "10", "25", "https://www.linkpicture.com/q/capsicon.png",
                "Pune", "Walmart");
        Product product3 = new Product(PID + "4", "Ginger", "500 gms"
                , "10", "280", "https://www.linkpicture.com/q/ginger.png",
                "Pune", "Walmart");
        Product product4 = new Product(PID + "5", "Ginger", ""
                , "", "", "https://www.linkpicture.com/q/ginger.png",
                "Pune", "Walmart");
        product4.setWeight("0");
        Product product5 = new Product(PID + "6", "Ginger", ""
                , "", "", "https://www.linkpicture.com/q/ginger.png",
                "Pune", "Walmart");
        product5.setWeight("0");
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
    //Home Banner API
    @SuppressLint("CheckResult")
    public LiveData<BannerResponse> bannerResponseLiveData(ProgressDialog progressDialog,
                                                           HashMap<String, String> hashMap,
                                                           HomeFragment homeFragment) {

        MutableLiveData<BannerResponse> bannerResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeBannerResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BannerResponse>() {

                    @Override
                    public void onSuccess(BannerResponse response) {
                        if (response!=null){
                            Log.d(TAG, "onSuccess: "+response.getData().getBannerDetailsList().get(0).getId());
                            progressDialog.dismiss();
                            bannerResponseMutableLiveData.setValue(response);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        BannerResponse response = new BannerResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BannerResponse.class);

                            bannerResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(0);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return bannerResponseMutableLiveData;
    }

    public MutableLiveData<ArrayList<SeasonalProduct>> getLiveSeasonProducts() {
        return liveSeasonProducts;
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