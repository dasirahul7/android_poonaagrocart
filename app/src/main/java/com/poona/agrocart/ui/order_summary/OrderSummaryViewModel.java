package com.poona.agrocart.ui.order_summary;

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
import com.poona.agrocart.data.network.responses.AddressesResponse;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.Coupon;
import com.poona.agrocart.data.network.responses.orderResponse.ApplyCouponResponse;
import com.poona.agrocart.data.network.responses.orderResponse.Delivery;
import com.poona.agrocart.data.network.responses.orderResponse.ItemsDetail;
import com.poona.agrocart.data.network.responses.orderResponse.OrderSummaryResponse;
import com.poona.agrocart.data.network.responses.orderResponse.Payments;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.ui.order_summary.model.Address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class OrderSummaryViewModel extends AndroidViewModel {
    private static String TAG = OrderSummaryViewModel.class.getSimpleName();
    public MutableLiveData<String> customerNameMutable = new MutableLiveData<>();
    public MutableLiveData<String> customerPhoneMutable = new MutableLiveData<>();
    public MutableLiveData<String> deliveryAddressMutable= new MutableLiveData<>();
    public MutableLiveData<String> deliveryDateMutable= new MutableLiveData<>();
    public MutableLiveData<String> deliverySlotMutable= new MutableLiveData<>();
    public MutableLiveData<String> availableCouponMutable= new MutableLiveData<>();
    public MutableLiveData<String> enteredCouponMutable= new MutableLiveData<>();
    public MutableLiveData<String> couponMessageMutable= new MutableLiveData<>();
    public MutableLiveData<String> subTotalMutable= new MutableLiveData<>();
    public MutableLiveData<String> discountMutable= new MutableLiveData<>();
    public MutableLiveData<String> finalTotalMutable= new MutableLiveData<>();
    public MutableLiveData<String> deliveryChargesMutable= new MutableLiveData<>();
    public MutableLiveData<String> youWillSaveMutable= new MutableLiveData<>();
    public MutableLiveData<String> availableWalletMutable= new MutableLiveData<>();
    public MutableLiveData<String> paymentCashMutable= new MutableLiveData<>();
    public MutableLiveData<String> paymentOnlineMutable= new MutableLiveData<>();
    public MutableLiveData<String> paymentWalletMutable= new MutableLiveData<>();
    public MutableLiveData<ArrayList<AddressesResponse.Address>> arrayAddressListMutableLiveData= new MutableLiveData<>();
    public MutableLiveData<ArrayList<Delivery>> arrayDeliveryListMutableLiveData= new MutableLiveData<>();
    public MutableLiveData<ArrayList<Coupon>> arrayCouponListMutableLiveData= new MutableLiveData<>();
    public MutableLiveData<ArrayList<ItemsDetail>> arrayItemListMutableLiveData= new MutableLiveData<>();
    public MutableLiveData<ArrayList<Payments>> arrayPaymentListMutableLiveData= new MutableLiveData<>();

    public OrderSummaryViewModel(@NonNull Application application) {
        super(application);
        customerNameMutable.setValue(null);
        customerPhoneMutable.setValue(null);
        deliveryAddressMutable.setValue(null);
        deliveryDateMutable.setValue(null);
        deliverySlotMutable.setValue(null);
        availableCouponMutable.setValue(null);
        enteredCouponMutable.setValue(null);
        couponMessageMutable.setValue(null);
        subTotalMutable.setValue(null);
        discountMutable.setValue(null);
        finalTotalMutable.setValue(null);
        deliveryChargesMutable.setValue(null);
        youWillSaveMutable.setValue(null);
        availableWalletMutable.setValue(null);
        paymentCashMutable.setValue(null);
        paymentOnlineMutable.setValue(null);
        paymentWalletMutable.setValue(null);
        arrayAddressListMutableLiveData.setValue(null);
        arrayDeliveryListMutableLiveData.setValue(null);
        arrayCouponListMutableLiveData.setValue(null);
        arrayItemListMutableLiveData.setValue(null);
        arrayPaymentListMutableLiveData.setValue(null);
    }

    public void initViewModel(OrderSummaryResponse orderSummaryResponse, Context context) {
        AppSharedPreferences preferences = new AppSharedPreferences(context);
        /*Address and name */
        customerNameMutable.setValue(preferences.getUserName());
        customerPhoneMutable.setValue(preferences.getUserMobile());
        customerPhoneMutable.setValue(preferences.getUserMobile());
        deliveryAddressMutable.setValue(preferences.getDeliveryAddress());
        /*Delivery date and time*/
        deliveryDateMutable.setValue(orderSummaryResponse.delivery.get(0).deliveryDate);
        deliverySlotMutable.setValue(orderSummaryResponse.delivery.get(0).deliverySlots.get(0).slotStartTime+" - "+orderSummaryResponse.delivery.get(0).deliverySlots.get(0).slotEndTime);
        deliverySlotMutable.setValue(orderSummaryResponse.delivery.get(0).deliverySlots.get(0).slotStartTime+" - "+orderSummaryResponse.delivery.get(0).deliverySlots.get(0).slotEndTime);
        /*Delivery charge and total amount*/
        subTotalMutable.setValue(String.valueOf(orderSummaryResponse.subTotal).trim());
        discountMutable.setValue(String.valueOf(orderSummaryResponse.discount).trim());
        deliveryChargesMutable.setValue(String.valueOf(orderSummaryResponse.deliveryCharges).trim());
        finalTotalMutable.setValue(String.valueOf(orderSummaryResponse.totalAmount).trim());
        youWillSaveMutable.setValue(String.valueOf(orderSummaryResponse.discount).trim());

        /*Arraylist initialization*/
        arrayAddressListMutableLiveData.setValue(orderSummaryResponse.address);
        arrayDeliveryListMutableLiveData.setValue(orderSummaryResponse.delivery);
        arrayCouponListMutableLiveData.setValue(orderSummaryResponse.couponCodeList);
        arrayItemListMutableLiveData.setValue(orderSummaryResponse.itemsDetails);
        arrayPaymentListMutableLiveData.setValue(orderSummaryResponse.paymentMode);

    }



    /*Order Summary API*/
    public LiveData<OrderSummaryResponse> getOrderSummaryResponse(ProgressDialog progressDialog,
                                                                  OrderSummaryFragment orderSummaryFragment) {
        MutableLiveData<OrderSummaryResponse> orderSummaryResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(orderSummaryFragment.getContext())
                .create(ApiInterface.class)
                .getOrderSummaryResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<OrderSummaryResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull OrderSummaryResponse orderSummaryResponse) {
                        if (orderSummaryResponse!=null){
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            orderSummaryResponseMutableLiveData.setValue(orderSummaryResponse);
                            Log.e(TAG, "Order Summary onSuccess: "+new Gson().toJson(orderSummaryResponse) );
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        OrderSummaryResponse errorResponse = new OrderSummaryResponse();
                        try {
                            errorResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    OrderSummaryResponse.class);
                            orderSummaryResponseMutableLiveData.setValue(errorResponse);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                            Log.e(TAG, "onError: "+ioException.getMessage() );
                            ((NetworkExceptionListener) orderSummaryFragment).onNetworkException(0,"");
                        }
                    }
                });
        return orderSummaryResponseMutableLiveData;
    }

    /*Apply Coupon API*/
    public LiveData<ApplyCouponResponse> getApplyCouponResponse(ProgressDialog progressDialog,
                                                                 HashMap<String,String> hashMap,
                                                                 OrderSummaryFragment orderSummaryFragment){

        MutableLiveData<ApplyCouponResponse> applyCouponResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(orderSummaryFragment.getContext())
                .create(ApiInterface.class)
                .getApplyCouponResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ApplyCouponResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ApplyCouponResponse applyCouponResponse) {
                        if (applyCouponResponse!=null){
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            applyCouponResponseMutableLiveData.setValue(applyCouponResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        ApplyCouponResponse errorResponse = new ApplyCouponResponse();
                        try {
                            errorResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    ApplyCouponResponse.class);
                            applyCouponResponseMutableLiveData.setValue(errorResponse);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                            Log.e(TAG, ioException.getMessage());
                            ((NetworkExceptionListener) orderSummaryFragment).onNetworkException(1, "");
                        }
                    }
                });
        return applyCouponResponseMutableLiveData;
    }
}
