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
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.galleryResponse.GalleryResponse;
import com.poona.agrocart.data.network.responses.myOrderResponse.OrderCancelReasonResponse;
import com.poona.agrocart.databinding.FragmentOrderViewBinding;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class OrderViewDetailsViewHolder extends AndroidViewModel {

    private static final String TAG = OrderViewDetailsViewHolder.class.getSimpleName();
    public OrderViewDetailsViewHolder(@NonNull Application application) {
        super(application);
    }


    public LiveData<BaseResponse> getRatingAndFeedBack(ProgressDialog progressDialog, Context context
            , HashMap<String, String> ratingAndFeedBackInputParameter, OrderViewFragment fragmentOrderViewBinding) {

        MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(fragmentOrderViewBinding.context)
                .create(ApiInterface.class)
                .getSubmitRatingResponseOrder(ratingAndFeedBackInputParameter)
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
                           // ((NetworkExceptionListener) fragmentOrderViewBinding).onNetworkException(1,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return baseResponseMutableLiveData;


    }

    public LiveData<OrderCancelReasonResponse> getOrderCancelReasonResponse(ProgressDialog progressDialog, Context context
            , OrderViewFragment orderViewFragment) {

        MutableLiveData<OrderCancelReasonResponse> orderCancelReasonResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getOrderCancelReasonResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<OrderCancelReasonResponse>() {
                    @Override
                    public void onSuccess(@NonNull OrderCancelReasonResponse baseResponse) {
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
                        OrderCancelReasonResponse baseResponse = new OrderCancelReasonResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), OrderCancelReasonResponse.class);

                            orderCancelReasonResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) orderViewFragment)
                                    .onNetworkException(0, "");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return orderCancelReasonResponseMutableLiveData;
    }


    public LiveData<BaseResponse> getOrderCancelSuccessfullyResponse(ProgressDialog progressDialog, Context context
            , HashMap<String, String> orderCancelSuccessfullyInputParameter, OrderViewFragment orderViewFragment) {
        MutableLiveData<BaseResponse> orderCancelReasonResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getOrderCancelSuccessfullyResponse(orderCancelSuccessfullyInputParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@NonNull BaseResponse baseResponse) {
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
                        BaseResponse baseResponse = new BaseResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), BaseResponse.class);

                            orderCancelReasonResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) orderViewFragment)
                                    .onNetworkException(0, "");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return orderCancelReasonResponseMutableLiveData;
    }
}
