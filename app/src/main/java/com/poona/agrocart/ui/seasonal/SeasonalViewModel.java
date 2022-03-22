package com.poona.agrocart.ui.seasonal;

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
import com.poona.agrocart.data.network.responses.GetUnitResponse;
import com.poona.agrocart.data.network.responses.SeasonalProductResponse;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class SeasonalViewModel extends AndroidViewModel {

    public MutableLiveData<String> productNameMutable;
    public MutableLiveData<String> productImageMutable;
    public MutableLiveData<String> productDetailMutable;
    public MutableLiveData<ArrayList<GetUnitResponse.UnitData>> arrayListUnitLiveData = new MutableLiveData<>();
    public SeasonalViewModel(@NonNull Application application) {
        super(application);
        productNameMutable = new MutableLiveData<>();
        productImageMutable = new MutableLiveData<>();
        productDetailMutable = new MutableLiveData<>();
        productNameMutable.setValue(null);
        productImageMutable.setValue(null);
        productDetailMutable.setValue(null);
        arrayListUnitLiveData.setValue(null);
    }


    /*Get Seasonal Product Detail */
    public LiveData<SeasonalProductResponse> getSeasonalResponseLiveData(ProgressDialog progressDialog,
                                                                     HashMap<String,String> hashMap,
                                                                     SeasonalRegFragment seasonalRegFragment){
        MutableLiveData<SeasonalProductResponse> seasonalProductResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(seasonalRegFragment.context)
                .create(ApiInterface.class)
                .seasonalProductDetails(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SeasonalProductResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull SeasonalProductResponse seasonalProductResponse) {
                        if (seasonalProductResponse!=null){
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            seasonalProductResponseMutableLiveData.setValue(seasonalProductResponse);
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        SeasonalProductResponse seasonalProductResponse = new SeasonalProductResponse();
                        try {
                            seasonalProductResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    SeasonalProductResponse.class);
                            seasonalProductResponseMutableLiveData.setValue(seasonalProductResponse);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            ((NetworkExceptionListener) seasonalRegFragment).onNetworkException(0,"");
                        }
                    }
                });
        return seasonalProductResponseMutableLiveData;
    }

    public LiveData<BaseResponse> seasonalProductRegistrationResponse(ProgressDialog progressDialog,
                                                                       HashMap<String,String> hashMap,
                                                                       SeasonalRegFragment seasonalRegFragment){

        MutableLiveData<BaseResponse> seasonalProductRegMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(seasonalRegFragment.context)
                .create(ApiInterface.class)
                .seasonalProductRegistration(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse response) {
                        if (response!=null){
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            seasonalProductRegMutableLiveData.setValue(response);
                            Log.d("TAG", "Seasonal register onSuccess: " + new Gson().toJson(response));
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        BaseResponse baseResponse = new BaseResponse();
                        try {
                          baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                  BaseResponse.class);
                          seasonalProductRegMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            ((NetworkExceptionListener) seasonalRegFragment).onNetworkException(2,"");
                        }
                    }
                });
        return seasonalProductRegMutableLiveData;

    }
}
