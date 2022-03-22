package com.poona.agrocart.ui.payment;

import android.app.Application;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.ui.order_summary.OrderSummaryFragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class PaymentViewModel extends AndroidViewModel {
    private static final String TAG = PaymentViewModel.class.getSimpleName();

    public PaymentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseResponse> getOrderPlaceAPIResponse(ProgressDialog progressDialog,
                                                           HashMap<String,String> hashMap,
                                                           PaymentActivity paymentActivity){
        MutableLiveData<BaseResponse> placeOrderResponseMutableLive = new MutableLiveData<>();

        ApiClientAuth.getClient(paymentActivity.getApplicationContext())
                .create(ApiInterface.class)
                .getOrderPlaceResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse response) {
                        if (response!=null){
                            if (progressDialog!=null){
                                progressDialog.dismiss();
                                placeOrderResponseMutableLive.setValue(response);
                            }
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        BaseResponse baseResponse =new BaseResponse();
                        try {
                            baseResponse = gson.fromJson(Objects.requireNonNull(Objects.requireNonNull(((HttpException) e).response()).errorBody()).string(),BaseResponse.class);
                            placeOrderResponseMutableLive.setValue(baseResponse);
                        }catch (JsonSyntaxException | IOException exception){
                            Log.e(TAG, "onError: "+exception.getMessage() );
                            ((NetworkExceptionListener) paymentActivity).onNetworkException(2,"");
                        }
                    }
                });
        return placeOrderResponseMutableLive;
    }

}
