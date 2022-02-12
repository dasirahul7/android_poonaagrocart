package com.poona.agrocart.ui.sign_up;

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
import com.poona.agrocart.data.network.reponses.AreaResponse;
import com.poona.agrocart.data.network.reponses.BaseResponse;
import com.poona.agrocart.data.network.reponses.CityResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class SelectLocationViewModel extends AndroidViewModel {
    private static final String TAG = SelectLocationViewModel.class.getSimpleName();

    public SelectLocationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<AreaResponse> getAreaResponse(ProgressDialog progressDialog,
                                                  SelectLocationFragment selectLocationFragment) {
        MutableLiveData<AreaResponse> areaResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(selectLocationFragment.getContext())
                .create(ApiInterface.class)
                .getAreaResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AreaResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull AreaResponse areaResponse) {
                        if (areaResponse != null) {
                            progressDialog.dismiss();
                            Log.d(TAG, "onSuccess: " + areaResponse.getArea().size());
                            areaResponseMutableLiveData.setValue(areaResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        AreaResponse response = new AreaResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    AreaResponse.class);

                            areaResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) selectLocationFragment).onNetworkException(0,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return areaResponseMutableLiveData;
    }

    public LiveData<CityResponse> getCityResponse(ProgressDialog progressDialog,
                                                  SelectLocationFragment selectLocationFragment) {
        MutableLiveData<CityResponse> cityResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(selectLocationFragment.getContext())
                .create(ApiInterface.class)
                .getCityResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CityResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull CityResponse cityResponse) {
                        if (cityResponse != null) {
                            progressDialog.dismiss();
                            cityResponseMutableLiveData.setValue(cityResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        CityResponse cityResponse = new CityResponse();
                        try {
                            cityResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    CityResponse.class);

                            cityResponseMutableLiveData.setValue(cityResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) selectLocationFragment).onNetworkException(1,"");
                        }

                    }
                });

        return cityResponseMutableLiveData;
    }

    public LiveData<BaseResponse> updateLocation(ProgressDialog progressDialog,
                                                 HashMap<String, String> updateLocationMap,
                                                 SelectLocationFragment selectLocationFragment) {
        MutableLiveData<BaseResponse> updateLocationMutableLiveData = new MutableLiveData<>();

            ApiClientAuth.getClient(selectLocationFragment.getContext())
                    .create(ApiInterface.class)
                    .updateLocationResponse(updateLocationMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                            if (baseResponse!=null){
                                progressDialog.dismiss();
                                updateLocationMutableLiveData.setValue(baseResponse);
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            progressDialog.dismiss();

                            Gson gson = new GsonBuilder().create();
                            BaseResponse baseResponse = new AreaResponse();
                             try {
                                 baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                         BaseResponse.class);
                                 updateLocationMutableLiveData.setValue(baseResponse);
                             } catch (Exception exception) {
                                 Log.e(TAG, exception.getMessage());
                                 ((NetworkExceptionListener) selectLocationFragment).onNetworkException(2,"");
                             }
                            Log.e(TAG, e.getMessage());
                        }
                    });

            return updateLocationMutableLiveData;
    }
}
