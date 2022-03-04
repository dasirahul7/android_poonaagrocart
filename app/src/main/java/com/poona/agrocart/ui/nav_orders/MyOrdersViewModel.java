package com.poona.agrocart.ui.nav_orders;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.myOrderResponse.OrderCancelReasonResponse;
import com.poona.agrocart.data.network.responses.myOrderResponse.OrderListResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class MyOrdersViewModel extends ViewModel {
    public static final String TAG = MyOrdersViewModel.class.getSimpleName();
    private final MutableLiveData<String> mText;

    public MyOrdersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<OrderListResponse> getOrderListResponse(ProgressDialog progressDialog, Context context
            , MyOrdersFragment myOrdersFragment) {

        MutableLiveData<OrderListResponse> orderCancelReasonResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getOrderListResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<OrderListResponse>() {
                    @Override
                    public void onSuccess(@NonNull OrderListResponse baseResponse) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        orderCancelReasonResponseMutableLiveData.setValue(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        Gson gson = new GsonBuilder().create();
                        OrderListResponse baseResponse = new OrderListResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), OrderListResponse.class);

                            orderCancelReasonResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) myOrdersFragment)
                                    .onNetworkException(0, "");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return orderCancelReasonResponseMutableLiveData;
    }
}