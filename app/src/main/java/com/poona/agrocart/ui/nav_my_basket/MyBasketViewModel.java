package com.poona.agrocart.ui.nav_my_basket;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.poona.agrocart.data.network.responses.myOrderResponse.SubscribeBasketListCustomerResponse;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.MyOrderDetailsResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class MyBasketViewModel extends AndroidViewModel {
    public static final String TAG = MyBasketViewModel.class.getSimpleName();
    public MyBasketViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<SubscribeBasketListCustomerResponse> getSubScriptionBasketListApi(ProgressDialog progressDialog, Context context
            , MyBasketFragment myBasketFragment) {

        MutableLiveData<SubscribeBasketListCustomerResponse> subscribeBasketListCustomerResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getSubscriptionBasketListResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SubscribeBasketListCustomerResponse>() {
                    @Override
                    public void onSuccess(@NonNull SubscribeBasketListCustomerResponse baseResponse) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        subscribeBasketListCustomerResponseMutableLiveData.setValue(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        Gson gson = new GsonBuilder().create();
                        SubscribeBasketListCustomerResponse baseResponse = new SubscribeBasketListCustomerResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), SubscribeBasketListCustomerResponse.class);

                            subscribeBasketListCustomerResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) myBasketFragment)
                                    .onNetworkException(0, "");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return subscribeBasketListCustomerResponseMutableLiveData;
    }
}
