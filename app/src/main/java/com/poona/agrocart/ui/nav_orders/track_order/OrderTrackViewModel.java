package com.poona.agrocart.ui.nav_orders.track_order;

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
import com.poona.agrocart.data.network.responses.myOrderResponse.orderTrack.ProductOrderTrackResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class OrderTrackViewModel extends AndroidViewModel {
    private final static String TAG = OrderTrackViewModel.class.getSimpleName();

    public MutableLiveData<String> orderId;
    public MutableLiveData<String> expectedDate;
    public MutableLiveData<String> cus_name;
    public MutableLiveData<String> cus_phone;
    public MutableLiveData<String> cus_address;
    public MutableLiveData<String> place_date;
    public MutableLiveData<String> confirmed_date;
    public MutableLiveData<String> packing_date;
    public MutableLiveData<String> delivered_date;

    public OrderTrackViewModel(@NonNull Application application) {
        super(application);

       /* orderMutableLiveData = new MutableLiveData<>();
        Order trackOrder = new Order(application.getString(R.string._paac002),
                application.getString(R.string.sep_30_2021_9_00_am_12_00pm),
                application.getString(R.string.ayush_shah),
                application.getString(R.string._91_986_095_3315),
                application.getString(R.string.nand_nivas_building_floor_3_b_3_lane_no_13_bhatrau_nivas_vishrantwadi_pune_411015));
        orderMutableLiveData.postValue(trackOrder);*/

     orderId = new MutableLiveData<>();
     expectedDate = new MutableLiveData<>();
     cus_name = new MutableLiveData<>();
     cus_phone = new MutableLiveData<>();
     cus_address = new MutableLiveData<>();
     place_date = new MutableLiveData<>();
     confirmed_date = new MutableLiveData<>();
     packing_date = new MutableLiveData<>();
     delivered_date = new MutableLiveData<>();


     orderId.setValue("");
     expectedDate.setValue("");
     cus_name.setValue("");
     cus_phone.setValue("");
     cus_address.setValue("");
     place_date.setValue("");
     confirmed_date.setValue("");
     packing_date.setValue("");
     delivered_date.setValue("");
    }

  /*  public MutableLiveData<Order> getOrderMutableLiveData() {
        return orderMutableLiveData;
    }*/

    public LiveData<ProductOrderTrackResponse> getOrderTrackApi(ProgressDialog progressDialog, Context context
            , HashMap<String, String> orderTrackInputParameter, OrderTrackFragment orderTrackFragment) {

        MutableLiveData<ProductOrderTrackResponse> productOrderTrackResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getOrderTrackResponse(orderTrackInputParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProductOrderTrackResponse>() {
                    @Override
                    public void onSuccess(@NonNull ProductOrderTrackResponse baseResponse) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        productOrderTrackResponseMutableLiveData.setValue(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }

                        Gson gson = new GsonBuilder().create();
                        ProductOrderTrackResponse baseResponse = new ProductOrderTrackResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), ProductOrderTrackResponse.class);

                            productOrderTrackResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) orderTrackFragment)
                                    .onNetworkException(0, "");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return productOrderTrackResponseMutableLiveData;

    }
}
