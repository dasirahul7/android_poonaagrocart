package com.poona.agrocart.ui.nav_orders.order_view;

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
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.SubscribeBasketItemListResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class BasketItemViewHolder extends AndroidViewModel {
    private final static String TAG = BasketItemViewHolder.class.getSimpleName();

    public BasketItemViewHolder(@NonNull Application application) {
        super(application);
    }

    public LiveData<SubscribeBasketItemListResponse> getSubscriptionBasketItemList(Context context, ProgressDialog progressDialog
            , HashMap<String, String> basketItemInputParameter, BasketItemFragment basketItemFragment) {
        MutableLiveData<SubscribeBasketItemListResponse> subscribeBasketItemListResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getMySubscriptionBasketItemListResponse(basketItemInputParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SubscribeBasketItemListResponse>() {
                    @Override
                    public void onSuccess(@NonNull SubscribeBasketItemListResponse baseResponse) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        subscribeBasketItemListResponseMutableLiveData.setValue(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        Gson gson = new GsonBuilder().create();
                        SubscribeBasketItemListResponse baseResponse = new SubscribeBasketItemListResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), SubscribeBasketItemListResponse.class);

                            subscribeBasketItemListResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) basketItemFragment)
                                    .onNetworkException(5, "");

                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return subscribeBasketItemListResponseMutableLiveData;
    }
}
