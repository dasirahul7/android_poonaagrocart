package com.poona.agrocart.ui.nav_offers;

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
import com.poona.agrocart.data.network.responses.CouponResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class CouponViewModel extends AndroidViewModel {

    private static final String TAG = CouponViewModel.class.getSimpleName();

    public CouponViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<CouponResponse> couponResponseLiveData(ProgressDialog progressDialog,
                                                           HashMap<String, String> hashMap,
                                                           CouponFragment couponFragment) {
        MutableLiveData<CouponResponse> couponResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(couponFragment.getContext())
                .create(ApiInterface.class)
                .couponListResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CouponResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull CouponResponse couponResponse) {
                        if (couponResponse != null) {
                            progressDialog.dismiss();
                            couponResponseMutableLiveData.setValue(couponResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();

                        CouponResponse couponResponse = new CouponResponse();
                        try {
                            couponResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    CouponResponse.class);
                            couponResponseMutableLiveData.setValue(couponResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) couponFragment).onNetworkException(5, "");
                        }

                    }
                });
        return couponResponseMutableLiveData;

    }

}