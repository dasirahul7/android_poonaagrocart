package com.poona.agrocart.ui.seasonal;

import android.app.Application;
import android.app.ProgressDialog;
import android.widget.ProgressBar;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.Api;
import com.google.common.graph.MutableValueGraph;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.GetUnitResponse;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class SeasonalViewModel extends AndroidViewModel {

    public MutableLiveData<ArrayList<GetUnitResponse.UnitData>> arrayListUnitLiveData = new MutableLiveData<>();
    public SeasonalViewModel(@NonNull Application application) {
        super(application);
        arrayListUnitLiveData.setValue(null);
    }


    /*Get Unit lists*/

//    public LiveData<GetUnitResponse> getUnitResponseLiveData(ProgressDialog progressDialog,
//                                                             SeasonalRegFragment seasonalRegFragment){
//        MutableLiveData<GetUnitResponse> getUnitResponseMutableLiveData = new MutableLiveData<>();
//
//        ApiClientAuth.getClient(seasonalRegFragment.context)
//                .create(ApiInterface.class)
//                .getUnitResponse()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<GetUnitResponse>() {
//                    @Override
//                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull GetUnitResponse getUnitResponse) {
//                        if (getUnitResponse!=null){
//                            if (progressDialog!=null)
//                                progressDialog.dismiss();
//                            getUnitResponseMutableLiveData.setValue(getUnitResponse);
//                        }
//                    }
//                    @Override
//                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//                        Gson gson = new GsonBuilder().create();
//                        GetUnitResponse getUnitResponse = new GetUnitResponse();
//                        try {
//                            getUnitResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
//                                    GetUnitResponse.class);
//                            getUnitResponseMutableLiveData.setValue(getUnitResponse);
//                        } catch (Exception exception) {
//                            exception.printStackTrace();
//                            ((NetworkExceptionListener) seasonalRegFragment).onNetworkException(0,"");
//                        }
//                    }
//                });
//        return getUnitResponseMutableLiveData;
//    }

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
                            ((NetworkExceptionListener) seasonalRegFragment).onNetworkException(1,"");
                        }
                    }
                });
        return seasonalProductRegMutableLiveData;

    }
}
