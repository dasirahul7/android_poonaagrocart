package com.poona.agrocart.ui.nav_my_cart;

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
import com.poona.agrocart.data.network.responses.cartResponse.MyCartResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class MyCartViewModel extends AndroidViewModel {
    private static final String TAG = MyCartViewModel.class.getSimpleName();

    public MutableLiveData<String> totalItems;
    public MutableLiveData<String> totalTotal;
    public MutableLiveData<String> totalSaved;

    public MyCartViewModel(@NonNull Application application) {
        super(application);

        totalItems = new MutableLiveData<>();
        totalTotal = new MutableLiveData<>();
        totalSaved = new MutableLiveData<>();

        totalItems.setValue("");
        totalTotal.setValue("");
        totalSaved.setValue("");
    }

    public LiveData<MyCartResponse> getMyCartListingResponse(ProgressDialog progressDialog,
                                                             HashMap<String, String> hashMap,
                                                             MyCartFragment myCartFragment) {
        MutableLiveData<MyCartResponse> myCartResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(myCartFragment.getContext())
                .create(ApiInterface.class)
                .getMyCartListingResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyCartResponse>() {
                    @Override
                    public void onSuccess(MyCartResponse favouriteLisResponse) {
                        if (favouriteLisResponse != null) {
                            progressDialog.dismiss();
                            myCartResponseMutableLiveData.setValue(favouriteLisResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        MyCartResponse response = new MyCartResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    MyCartResponse.class);

                            myCartResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) myCartFragment).onNetworkException(0,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return myCartResponseMutableLiveData;

    }

    public LiveData<BaseResponse> deleteCartItemResponse(ProgressDialog progressDialog,
                                                         HashMap<String, String> hashMap,
                                                         MyCartFragment myCartFragment) {
        MutableLiveData<BaseResponse> myCartResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(myCartFragment.getContext())
                .create(ApiInterface.class)
                .deleteCartItemResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            myCartResponseMutableLiveData.setValue(baseResponse);
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

                            myCartResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) myCartFragment).onNetworkException(1,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return myCartResponseMutableLiveData;

    }

    public LiveData<BaseResponse> deleteCartListResponse(ProgressDialog progressDialog,
                                                             HashMap<String, String> hashMap,
                                                             MyCartFragment myCartFragment) {
        MutableLiveData<BaseResponse> myCartResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(myCartFragment.getContext())
                .create(ApiInterface.class)
                .deleteCartListResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            myCartResponseMutableLiveData.setValue(baseResponse);
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

                            myCartResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) myCartFragment).onNetworkException(2,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return myCartResponseMutableLiveData;
    }
}