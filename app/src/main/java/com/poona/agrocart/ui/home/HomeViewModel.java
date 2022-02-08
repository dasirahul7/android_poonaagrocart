package com.poona.agrocart.ui.home;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.ExclusiveResponse;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.BannerResponse;
import com.poona.agrocart.data.network.reponses.BasketResponse;
import com.poona.agrocart.data.network.reponses.BestSellingResponse;
import com.poona.agrocart.data.network.reponses.CategoryResponse;
import com.poona.agrocart.data.network.reponses.ProductListResponse;
import com.poona.agrocart.data.network.reponses.ProductResponseDt;
import com.poona.agrocart.data.network.reponses.SeasonalProductResponse;
import com.poona.agrocart.data.network.reponses.StoreBannerResponse;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.ui.home.model.Banner;
import com.poona.agrocart.ui.home.model.Basket;
import com.poona.agrocart.ui.home.model.Category;
import com.poona.agrocart.ui.home.model.Product;
import com.poona.agrocart.ui.home.model.SeasonalProduct;
import com.poona.agrocart.ui.home.model.StoreBanner;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;


public class HomeViewModel extends AndroidViewModel {
    private String TAG = HomeViewModel.class.getSimpleName();

    private MutableLiveData<ArrayList<Product>> liveDataCartProduct;
    //    private MutableLiveData<ArrayList<Category>> liveDataCategory;

    private MutableLiveData<ArrayList<Product>> savesProductInBasket;



    public HomeViewModel(@NonNull Application application) {
        super(application);
        //init all mutable liveData
        liveDataCartProduct = new MutableLiveData<>();
        savesProductInBasket = new MutableLiveData<>();
        initCartItems();
//        initSeasonalBanner();
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


    //Banner Response here
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
                        if (response != null) {
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

    //Category Response here
    @SuppressLint("CheckResult")
    public LiveData<CategoryResponse> categoryResponseLiveData(ProgressDialog progressDialog,
                                                               HashMap<String, String> hashMap,
                                                               HomeFragment homeFragment) {

        MutableLiveData<CategoryResponse> categoryResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeCategoryResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CategoryResponse>() {

                    @Override
                    public void onSuccess(CategoryResponse response) {
                        if (response != null) {
                            Log.d(TAG, "onSuccess: " + response.getMessage());
                            progressDialog.dismiss();
                            categoryResponseMutableLiveData.setValue(response);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        CategoryResponse response = new CategoryResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    CategoryResponse.class);

                            categoryResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(1);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return categoryResponseMutableLiveData;
    }

    // BasketList Response here
    public LiveData<BasketResponse> basketResponseLiveData(ProgressDialog progressDialog,
                                                           HashMap<String, String> hashMap,
                                                           HomeFragment homeFragment) {
        MutableLiveData<BasketResponse> basketResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeBasketResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BasketResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BasketResponse basketResponse) {
                        if (basketResponse != null) {
                            progressDialog.dismiss();
                            basketResponseMutableLiveData.setValue(basketResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        BasketResponse response = new BasketResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BasketResponse.class);

                            basketResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(2);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return basketResponseMutableLiveData;
    }

    //ExclusiveList Response here
    public LiveData<ExclusiveResponse> exclusiveResponseLiveData(ProgressDialog progressDialog,
                                                                 HashMap<String, String> hashMap,
                                                                 HomeFragment homeFragment) {
        MutableLiveData<ExclusiveResponse> exclusiveResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeExclusiveResponseSingle(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ExclusiveResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ExclusiveResponse exclusiveResponse) {
                        if (exclusiveResponse != null) {
                            Log.d(TAG, "Exclusive onSuccess: " + exclusiveResponse.getExclusiveData().getExclusivesList().get(0).getId());

                            progressDialog.dismiss();
                            exclusiveResponseMutableLiveData.setValue(exclusiveResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        ExclusiveResponse response = new ExclusiveResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    ExclusiveResponse.class);

                            exclusiveResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(3);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return exclusiveResponseMutableLiveData;
    }

    //BestSelling Response here
    public LiveData<BestSellingResponse> bestSellingResponseLiveData(ProgressDialog progressDialog,
                                                                     HashMap<String, String> hashMap,
                                                                     HomeFragment homeFragment) {
        MutableLiveData<BestSellingResponse> bestSellingResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeBestSellingResponseSingle(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BestSellingResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BestSellingResponse bestSellingResponse) {
                        if (bestSellingResponse != null) {
                            Log.d(TAG, "BestSelling onSuccess: " + bestSellingResponse.getBestSellingData().getBestSellingProductList().size());
                            progressDialog.dismiss();
                            bestSellingResponseMutableLiveData.setValue(bestSellingResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        BestSellingResponse response = new BestSellingResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BestSellingResponse.class);

                            bestSellingResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(4);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return bestSellingResponseMutableLiveData;
    }

    //Seasonal Products Response here
    public LiveData<SeasonalProductResponse> seasonalResponseLiveData(ProgressDialog progressDialog,
                                                                      HashMap<String, String> hashMap,
                                                                      HomeFragment homeFragment) {
        MutableLiveData<SeasonalProductResponse> seasonalProductResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeSeasonalListResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SeasonalProductResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull SeasonalProductResponse seasonalProductResponse) {
                        if (seasonalProductResponse != null) {
                            Log.d(TAG, "Seasonal onSuccess: " + seasonalProductResponse.getSeasonalProducts().size());
                            progressDialog.dismiss();
                            seasonalProductResponseMutableLiveData.setValue(seasonalProductResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        SeasonalProductResponse response = new SeasonalProductResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    SeasonalProductResponse.class);

                            seasonalProductResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(5);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return seasonalProductResponseMutableLiveData;
    }

    //Product list Response here
    public LiveData<ProductListResponse> homeProductListResponseLiveData(ProgressDialog progressDialog,
                                                                         HashMap<String, String> hashMap,
                                                                         HomeFragment homeFragment) {
        MutableLiveData<ProductListResponse> productListResponseMutableLiveData = new MutableLiveData();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeProductListResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProductListResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ProductListResponse productListResponse) {
                        if (productListResponse != null) {
                            Log.d(TAG, "productListResponse onSuccess: " + productListResponse.getProductResponseDt().getProductList().size());
                            progressDialog.dismiss();
                            productListResponseMutableLiveData.setValue(productListResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        ProductListResponse response = new ProductListResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    ProductListResponse.class);

                            productListResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(6);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return productListResponseMutableLiveData;
    }

    //Store Banner Response here

    public LiveData<StoreBannerResponse> storeBannerResponseLiveData(ProgressDialog progressDialog,
                                                                     HomeFragment homeFragment){
        MutableLiveData<StoreBannerResponse> storeBannerMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeStoreBannerResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<StoreBannerResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull StoreBannerResponse storeBannerResponse) {
                        if (storeBannerResponse!=null){
                            Log.e(TAG, "storeBannerResponse onSuccess: "+storeBannerResponse.getStoreBanners().size());
                            progressDialog.dismiss();
                            storeBannerMutableLiveData.setValue(storeBannerResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        StoreBannerResponse storeBannerResponse = new StoreBannerResponse();
                        try{
                            storeBannerResponse = gson.fromJson(((HttpException) e).response().errorBody().toString(),
                                    StoreBannerResponse.class);

                            storeBannerMutableLiveData.setValue(storeBannerResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(7);
                        }
                    }
                });
        return storeBannerMutableLiveData;
    }



    public MutableLiveData<ArrayList<Product>> getLiveDataCartProduct() {
        return liveDataCartProduct;
    }

    public MutableLiveData<ArrayList<Product>> getSavesProductInBasket() {
        return savesProductInBasket;
    }


}