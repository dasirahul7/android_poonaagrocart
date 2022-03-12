package com.poona.agrocart.ui.search;

import android.app.Application;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.CommonSearchResponse;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.ui.search.model.CommonSearchItem;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class SearchViewModel extends AndroidViewModel {
    public static final String TAG = SearchViewModel.class.getSimpleName();
    public MutableLiveData<ArrayList<CommonSearchItem>> arrayListMutableSearchLiveData;
    public SearchViewModel(@NonNull Application application) {
        super(application);
        arrayListMutableSearchLiveData = new MutableLiveData<>();
        arrayListMutableSearchLiveData.setValue(null);
    }

    //Add to cart Product
    public LiveData<BaseResponse> addToCartProductLiveData(HashMap<String, String> hashMap,
                                                           SearchFragment homeFragment) {
        MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .addToCartProductResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            Log.e(TAG, "add to cart onSuccess: " + new Gson().toJson(baseResponse));
                            baseResponseMutableLiveData.setValue(baseResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            baseResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(2, "");
                        }
                    }
                });
        return baseResponseMutableLiveData;
    }

    //Add to cart Basket

    public LiveData<BaseResponse> addToCartBasketLiveData(HashMap<String,String> hashMap,
                                                          SearchFragment searchFragment){
        MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(searchFragment.getContext())
                .create(ApiInterface.class)
                .addToBasketResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse response) {
                        if (response != null) {
                            Log.e(TAG, "add to cart onSuccess: " + new Gson().toJson(response));
                            baseResponseMutableLiveData.setValue(response);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            baseResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) searchFragment).onNetworkException(2, "");
                        }
                    }
                })  ;
        return baseResponseMutableLiveData;
    }

    /*Global Search here*/
    public LiveData<CommonSearchResponse> getCommonSearchLiveData(ProgressDialog progressDialog,
                                                                  HashMap<String,String> hashMap,
                                                                  SearchFragment searchFragment){

        MutableLiveData<CommonSearchResponse> commonSearchResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(searchFragment.context)
                .create(ApiInterface.class)
                .getCommonSearchResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CommonSearchResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull CommonSearchResponse commonSearchResponse) {
                      if (commonSearchResponse!=null){
                          if (progressDialog!=null)
                              progressDialog.dismiss();
                          commonSearchResponseMutableLiveData.setValue(commonSearchResponse);
                      }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        CommonSearchResponse commonSearchResponse = new CommonSearchResponse();
                        try {

                            commonSearchResponse = gson.fromJson(((HttpException)e).response().errorBody().string(),
                                    CommonSearchResponse.class);
                            commonSearchResponseMutableLiveData.setValue(commonSearchResponse);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            ((NetworkExceptionListener) searchFragment).onNetworkException(0, "");
                        }
                    }
                });
        return commonSearchResponseMutableLiveData;
    }

    // Search ProductOld here
    public LiveData<ProductListResponse> searchListResponseLiveData(ProgressDialog progressDialog,
                                                                    HashMap<String, String> hashMap,
                                                                    SearchFragment searchFragment) {
        MutableLiveData<ProductListResponse> productListResponseMutableLiveData = new MutableLiveData();

        ApiClientAuth.getClient(searchFragment.getContext())
                .create(ApiInterface.class)
                .homeProductListResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProductListResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ProductListResponse productListResponse) {
                        if (productListResponse != null) {
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
                            ((NetworkExceptionListener) searchFragment).onNetworkException(1, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return productListResponseMutableLiveData;
    }
}

