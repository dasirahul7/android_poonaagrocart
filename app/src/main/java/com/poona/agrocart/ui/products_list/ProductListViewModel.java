package com.poona.agrocart.ui.products_list;

import android.app.Application;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.ExclusiveResponse;
import com.poona.agrocart.data.network.reponses.BasketResponse;
import com.poona.agrocart.data.network.reponses.BestSellingResponse;
import com.poona.agrocart.data.network.reponses.ProductListByResponse;
import com.poona.agrocart.ui.home.model.ProductOld;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class ProductListViewModel extends AndroidViewModel {


    public static final String TAG = ProductListViewModel.class.getSimpleName();

    public ProductListViewModel(Application application) {
        super(application);

    }


    /*Product list by category API*/
    public LiveData<ProductListByResponse> productListByResponseLiveData(ProgressDialog progressDialog,
                                                                         HashMap<String, String> hashMap,
                                                                         ProductListFragment productListFragment,
                                                                         String apiFrom){
        MutableLiveData<ProductListByResponse> productListByResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(productListFragment.getContext())
                .create(ApiInterface.class)
                .productsByCategoryResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProductListByResponse>() {
                    @Override
                    public void onSuccess(@NonNull ProductListByResponse productListByResponse) {
                       if (productListByResponse!=null){
                           progressDialog.dismiss();
                           productListByResponseMutableLiveData.setValue(productListByResponse);
                       }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        ProductListByResponse response = new ProductListByResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    ProductListByResponse.class);

                            productListByResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) productListFragment).onNetworkException(0, apiFrom);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });

        return productListByResponseMutableLiveData;
    }

    /*All Basket list API Response*/

    public LiveData<BasketResponse> basketResponseLiveData(ProgressDialog progressDialog,
                                                           HashMap<String, String> hashMap,
                                                           ProductListFragment productListFragment,
                                                           String listType) {
        MutableLiveData<BasketResponse> basketResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(productListFragment.getContext())
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
                            ((NetworkExceptionListener) productListFragment).onNetworkException(1, listType);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return basketResponseMutableLiveData;
    }

    /*All BestSelling Response here*/
    public LiveData<BestSellingResponse> allBestSellingResponseLiveData(ProgressDialog progressDialog,
                                                                     HashMap<String, String> hashMap,
                                                                     ProductListFragment productListFragment,
                                                                     String listType) {
        MutableLiveData<BestSellingResponse> bestSellingResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(productListFragment.getContext())
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
                            ((NetworkExceptionListener) productListFragment).onNetworkException(2,listType);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return bestSellingResponseMutableLiveData;
    }
    
    /*All Exclusive Response here*/

    public LiveData<ExclusiveResponse> allExclusiveResponseLiveData(ProgressDialog progressDialog,
                                                                 HashMap<String, String> hashMap,
                                                                 ProductListFragment productListFragment,
                                                                 String loadType) {
        MutableLiveData<ExclusiveResponse> exclusiveResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(productListFragment.getContext())
                .create(ApiInterface.class)
                .homeExclusiveResponseSingle(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ExclusiveResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ExclusiveResponse exclusiveResponse) {
                        if (exclusiveResponse != null) {
                            Log.d(TAG, "Product onSuccess: " + exclusiveResponse.getExclusiveData().getExclusivesList().get(0).getProductId());
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
                            ((NetworkExceptionListener) productListFragment).onNetworkException(3,loadType);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return exclusiveResponseMutableLiveData;
    }


}
