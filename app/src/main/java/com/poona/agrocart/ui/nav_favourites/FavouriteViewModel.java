package com.poona.agrocart.ui.nav_favourites;

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
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.favoutiteResponse.FavouriteListResponse;
import com.poona.agrocart.data.network.responses.favoutiteResponse.RemoveFavouriteListResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class FavouriteViewModel extends AndroidViewModel {
    private final String TAG = FavouriteViewModel.class.getSimpleName();

    public FavouriteViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<FavouriteListResponse> favouriteLisResponseLiveData(ProgressDialog progressDialog,
                                                                        FavouriteItemsFragment favouriteItemsFragment) {
        MutableLiveData<FavouriteListResponse> favouriteLisResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(favouriteItemsFragment.getContext())
                .create(ApiInterface.class)
                .getFavouriteList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FavouriteListResponse>() {
                    @Override
                    public void onSuccess(FavouriteListResponse favouriteLisResponse) {
                        if (favouriteLisResponse != null) {
                            progressDialog.dismiss();
                            favouriteLisResponseMutableLiveData.setValue(favouriteLisResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        FavouriteListResponse response = new FavouriteListResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    FavouriteListResponse.class);

                            favouriteLisResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
//                            ((NetworkExceptionListener) homeFragment).onNetworkException(1,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return favouriteLisResponseMutableLiveData;

    }

    public LiveData<RemoveFavouriteListResponse> removeFavouriteLisResponseLiveData(ProgressDialog progressDialog,
                                                                                    HashMap<String, String> removeFavourite, FavouriteItemsFragment favouriteItemsFragment) {

        MutableLiveData<RemoveFavouriteListResponse> removeFavouriteListResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(favouriteItemsFragment.getContext())
                .create(ApiInterface.class)
                .getRemoveFavouriteList(removeFavourite)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RemoveFavouriteListResponse>() {
                    @Override
                    public void onSuccess(RemoveFavouriteListResponse favouriteLisResponse) {
                        if (favouriteLisResponse != null) {
                            progressDialog.dismiss();
                            removeFavouriteListResponseMutableLiveData.setValue(favouriteLisResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        RemoveFavouriteListResponse response = new RemoveFavouriteListResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    RemoveFavouriteListResponse.class);

                            removeFavouriteListResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
//                            ((NetworkExceptionListener) homeFragment).onNetworkException(1,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return removeFavouriteListResponseMutableLiveData;
    }

    public LiveData<BaseResponse> callAddToCartFromBasketFavouriteList(ProgressDialog progressDialog,
                                                                 HashMap<String, String> addToCartFromInputParameter,
                                                                 FavouriteItemsFragment favouriteItemsFragment) {
        MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(favouriteItemsFragment.getContext())
                .create(ApiInterface.class)
                .getAddToCartFavouriteBasket(addToCartFromInputParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            baseResponseMutableLiveData.setValue(baseResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            baseResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
//                            ((NetworkExceptionListener) homeFragment).onNetworkException(1,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return baseResponseMutableLiveData;
    }

    public LiveData<BaseResponse> callAddToCartFromProductFavouriteList(ProgressDialog progressDialog,
                                                                        HashMap<String, String> addToCartFromProductInputParameter,
                                                                        FavouriteItemsFragment favouriteItemsFragment) {
        MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(favouriteItemsFragment.getContext())
                .create(ApiInterface.class)
                .getAddToCartFavouriteProduct(addToCartFromProductInputParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            baseResponseMutableLiveData.setValue(baseResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            baseResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
//                            ((NetworkExceptionListener) homeFragment).onNetworkException(1,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return baseResponseMutableLiveData;
    }
}
