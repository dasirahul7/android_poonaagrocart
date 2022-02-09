package com.poona.agrocart.ui.search;

import android.app.Application;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.CategoryResponse;
import com.poona.agrocart.data.network.reponses.ProductListByResponse;
import com.poona.agrocart.data.network.reponses.ProductListResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class SearchViewModel extends AndroidViewModel {
    public static final String TAG = SearchViewModel.class.getSimpleName();

    public SearchViewModel(@NonNull Application application) {
        super(application);
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
                            ((NetworkExceptionListener) searchFragment).onNetworkException(0);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return productListResponseMutableLiveData;
    }


    //Search Category
    public LiveData<CategoryResponse> searchCategoryResponse(ProgressDialog progressDialog,
                                                             HashMap<String, String> hashMap,
                                                             SearchFragment searchFragment) {
        MutableLiveData<CategoryResponse> categoryResponseMutableLiveData = new MutableLiveData<>();
        Observer<CategoryResponse> categoryResponseObserver = categoryResponse -> {
            ApiClientAuth.getClient(searchFragment.getContext())
                    .create(ApiInterface.class)
                    .homeCategoryResponse(hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CategoryResponse>() {
                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull CategoryResponse categoryResponse) {
                            if (categoryResponse != null) {
                                Log.d(TAG, "categoryResponse onSuccess: " + categoryResponse.getCategoryData().getCategoryList().size());
                                progressDialog.dismiss();
                                categoryResponseMutableLiveData.setValue(categoryResponse);
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            progressDialog.dismiss();

                            Gson gson = new GsonBuilder().create();
                            CategoryResponse response = new CategoryResponse();
                            try {
                                response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                        CategoryResponse.class);

                                categoryResponseMutableLiveData.setValue(response);
                            } catch (Exception exception) {
                                Log.e(TAG, exception.getMessage());
                                ((NetworkExceptionListener) searchFragment).onNetworkException(1);
                            }

                            Log.e(TAG, e.getMessage());
                        }
                    });
        };
        return categoryResponseMutableLiveData;

    }
    /*Search By category*/
    public LiveData<ProductListByResponse> searchProductByCategory(ProgressDialog progressDialog,
                                                                  HashMap<String, String> hashMap,
                                                                  SearchFragment searchFragment) {
        MutableLiveData<ProductListByResponse> productListByResponseMutableLiveData = new MutableLiveData<>();
        Observer<ProductListByResponse> productListByResponseObserver = productListByResponse -> {
            ApiClientAuth.getClient(searchFragment.getContext())
                    .create(ApiInterface.class)
                    .productsByCategoryResponse(hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<ProductListByResponse>() {
                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ProductListByResponse productListByResponse1) {
                            if (productListByResponse != null) {
                                Log.d(TAG, "Search productListByResponse onSuccess: " + productListByResponse.getMessage());
                                progressDialog.dismiss();
                                productListByResponseMutableLiveData.setValue(productListByResponse);
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            progressDialog.dismiss();

                            Gson gson = new GsonBuilder().create();
                            ProductListByResponse response = new ProductListByResponse();
                            try {
                                response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                        ProductListByResponse.class);

                                productListByResponseMutableLiveData.setValue(response);
                            } catch (Exception exception) {
                                Log.e(TAG, exception.getMessage());
                                ((NetworkExceptionListener) searchFragment).onNetworkException(1);
                            }

                            Log.e(TAG, e.getMessage());
                        }
                    });
        };
        return productListByResponseMutableLiveData;

    }

}

