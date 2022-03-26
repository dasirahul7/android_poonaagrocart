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
import com.google.gson.JsonSyntaxException;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.AddressesResponse;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.Coupon;
import com.poona.agrocart.data.network.responses.orderResponse.ApplyCouponResponse;
import com.poona.agrocart.data.network.responses.orderResponse.Delivery;
import com.poona.agrocart.data.network.responses.orderResponse.ItemsDetail;
import com.poona.agrocart.data.network.responses.orderResponse.OrderSuccessResponse;
import com.poona.agrocart.data.network.responses.orderResponse.OrderSummaryResponse;
import com.poona.agrocart.data.network.responses.orderResponse.Payments;
import com.poona.agrocart.data.network.responses.payment.RazorPayCredentialResponse;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class OrderSummaryViewModel extends AndroidViewModel {
    private static String TAG = OrderSummaryViewModel.class.getSimpleName();
    public MutableLiveData<String> customerNameMutable = new MutableLiveData<>();
    public MutableLiveData<String> customerPhoneMutable = new MutableLiveData<>();
    public MutableLiveData<String> deliveryAddressMutable = new MutableLiveData<>();
    public MutableLiveData<String> deliveryDateMutable = new MutableLiveData<>();
    public MutableLiveData<String> deliverySlotMutable = new MutableLiveData<>();
    public MutableLiveData<String> availableCouponMutable = new MutableLiveData<>();
    public MutableLiveData<String> enteredCouponMutable = new MutableLiveData<>();
    public MutableLiveData<String> couponMessageMutable = new MutableLiveData<>();
    public MutableLiveData<String> subTotalMutable = new MutableLiveData<>();
    public MutableLiveData<String> discountMutable = new MutableLiveData<>();
    public MutableLiveData<String> finalTotalMutable = new MutableLiveData<>();
    public MutableLiveData<String> deliveryChargesMutable = new MutableLiveData<>();
    public MutableLiveData<String> youWillSaveMutable = new MutableLiveData<>();
    public MutableLiveData<String> availableWalletMutable = new MutableLiveData<>();
    public MutableLiveData<String> paymentCashMutable = new MutableLiveData<>();
    public MutableLiveData<String> paymentOnlineMutable = new MutableLiveData<>();
    public MutableLiveData<String> paymentWalletMutable = new MutableLiveData<>();
    public MutableLiveData<ArrayList<AddressesResponse.Address>> arrayAddressListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Delivery> arrayDeliveryListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Coupon>> arrayCouponListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ItemsDetail>> arrayItemListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Payments>> arrayPaymentListMutableLiveData = new MutableLiveData<>();
    /*for subscription*/
    public MutableLiveData<String> subscribeNowIdMutable = new MutableLiveData<>();
    public MutableLiveData<String> subscriptionTypeMutable = new MutableLiveData<>();
    public MutableLiveData<String> noOfBasketMutable = new MutableLiveData<>();
    public MutableLiveData<String> basketIdMutable = new MutableLiveData<>();

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

    /*init Order - summary here*/
    public void initViewModel(OrderSummaryResponse orderSummaryResponse, Context context) {
        AppSharedPreferences preferences = new AppSharedPreferences(context);
        /*Address and name */
        customerNameMutable.setValue(preferences.getUserName());
        customerPhoneMutable.setValue(preferences.getUserMobile());
        customerPhoneMutable.setValue(preferences.getUserMobile());
        deliveryAddressMutable.setValue(preferences.getDeliveryAddress());
        /*Delivery date and time*/
        deliveryDateMutable.setValue(orderSummaryResponse.delivery.deliveryDate);
        preferences.setDeliveryDate(orderSummaryResponse.delivery.deliveryDate);
        try {
            if (orderSummaryResponse.delivery.deliverySlots.size() > 0) {
                deliverySlotMutable.setValue(orderSummaryResponse.delivery.deliverySlots.get(0).slotTime);
                preferences.setDeliverySlot(orderSummaryResponse.delivery.deliverySlots.get(0).slotId);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        /*Delivery charge and total amount*/
        subTotalMutable.setValue(String.valueOf(orderSummaryResponse.subTotal).trim());
//        discountMutable.setValue(String.valueOf(orderSummaryResponse.discount).trim());
        deliveryChargesMutable.setValue(String.valueOf(orderSummaryResponse.deliveryCharges).trim());
        finalTotalMutable.setValue(String.valueOf(orderSummaryResponse.totalAmount).trim());
        youWillSaveMutable.setValue(String.valueOf(orderSummaryResponse.discount).trim());

        /*Arraylist initialization*/
        arrayAddressListMutableLiveData.setValue(orderSummaryResponse.address);
        arrayDeliveryListMutableLiveData.setValue(orderSummaryResponse.delivery);
        arrayCouponListMutableLiveData.setValue(orderSummaryResponse.couponCodeList);
        arrayItemListMutableLiveData.setValue(orderSummaryResponse.itemsDetails);
        arrayPaymentListMutableLiveData.setValue(orderSummaryResponse.paymentMode);
        availableWalletMutable.setValue(orderSummaryResponse.walletBalance);

    }


    /*init subscription summary here*/
    public void initSubscriptionSummary(OrderSummaryResponse orderSummaryResponse,
                                        Context context) {
        AppSharedPreferences preferences = new AppSharedPreferences(context);
        /*Address and name */
        customerNameMutable.setValue(preferences.getUserName());
        customerPhoneMutable.setValue(preferences.getUserMobile());
        customerPhoneMutable.setValue(preferences.getUserMobile());
        deliveryAddressMutable.setValue(preferences.getDeliveryAddress());
        /*Delivery date and time*/
        deliveryDateMutable.setValue(orderSummaryResponse.getItemsDetails().get(0).subStartDate);
        preferences.setDeliveryDate(orderSummaryResponse.getItemsDetails().get(0).subStartDate);
        preferences.setDeliverySlot(orderSummaryResponse.getItemsDetails().get(0).subSlotId);
        deliverySlotMutable.setValue(orderSummaryResponse.getItemsDetails().get(0).subSlotTime);
        /*Delivery charge and total amount*/
        subTotalMutable.setValue(String.valueOf(orderSummaryResponse.subTotal).trim());
        deliveryChargesMutable.setValue(String.valueOf(orderSummaryResponse.deliveryCharges).trim());
        finalTotalMutable.setValue(String.valueOf(orderSummaryResponse.totalAmount).trim());
        youWillSaveMutable.setValue(String.valueOf(orderSummaryResponse.discount));

        /*Arraylist initialization*/
        arrayAddressListMutableLiveData.setValue(orderSummaryResponse.address);
        arrayCouponListMutableLiveData.setValue(orderSummaryResponse.couponCodeList);
        arrayItemListMutableLiveData.setValue(orderSummaryResponse.itemsDetails);
        arrayPaymentListMutableLiveData.setValue(orderSummaryResponse.paymentMode);
        availableWalletMutable.setValue(orderSummaryResponse.walletBalance);
        /*init extra data for subscription*/
        subscribeNowIdMutable.setValue(orderSummaryResponse.getItemsDetails().get(0).subscribeNowId);
        subscriptionTypeMutable.setValue(orderSummaryResponse.getItemsDetails().get(0).subscriptionType);
        noOfBasketMutable.setValue(orderSummaryResponse.getItemsDetails().get(0).noOfSubscription);
        basketIdMutable.setValue(orderSummaryResponse.getItemsDetails().get(0).basketId);
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
                        if (orderSummaryResponse != null) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            orderSummaryResponseMutableLiveData.setValue(orderSummaryResponse);
                            Log.e(TAG, "Order Summary onSuccess: " + new Gson().toJson(orderSummaryResponse));
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        OrderSummaryResponse errorResponse = new OrderSummaryResponse();
                        try {
                            errorResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    OrderSummaryResponse.class);
                            orderSummaryResponseMutableLiveData.setValue(errorResponse);
                        } catch (Exception ioException) {
                            ioException.printStackTrace();
                            Log.e(TAG, "onError: " + ioException.getMessage());
                            ((NetworkExceptionListener) orderSummaryFragment).onNetworkException(0, "");
                        }
                    }
                });
        return orderSummaryResponseMutableLiveData;
    }

    /*Subscription Order Summary API*/
    public LiveData<OrderSummaryResponse> getSubscriptionSummaryResponse(ProgressDialog progressDialog,
                                                                         HashMap<String, String> hashMap,
                                                                         OrderSummaryFragment orderSummaryFragment) {

        MutableLiveData<OrderSummaryResponse> basketSummaryResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(orderSummaryFragment.getContext())
                .create(ApiInterface.class)
                .getSubscriptionSummaryResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<OrderSummaryResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull OrderSummaryResponse orderSummaryResponse) {
                        if (orderSummaryResponse != null) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            basketSummaryResponseMutableLiveData.setValue(orderSummaryResponse);
                            Log.e(TAG, "Basket Summary onSuccess: " + new Gson().toJson(orderSummaryResponse));
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        OrderSummaryResponse errorResponse = new OrderSummaryResponse();
                        try {
                            errorResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    OrderSummaryResponse.class);
                            basketSummaryResponseMutableLiveData.setValue(errorResponse);
                        } catch (Exception ioException) {
                            ioException.printStackTrace();
                            Log.e(TAG, "onError: " + ioException.getMessage());
                            ((NetworkExceptionListener) orderSummaryFragment).onNetworkException(5, "");
                        }
                    }
                });
        return basketSummaryResponseMutableLiveData;
    }

    /*Apply Coupon API*/
    public LiveData<ApplyCouponResponse> getApplyCouponResponse(ProgressDialog progressDialog,
                                                                HashMap<String, String> hashMap,
                                                                OrderSummaryFragment orderSummaryFragment) {

        MutableLiveData<ApplyCouponResponse> applyCouponResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(orderSummaryFragment.getContext())
                .create(ApiInterface.class)
                .getApplyCouponResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ApplyCouponResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ApplyCouponResponse applyCouponResponse) {
                        if (applyCouponResponse != null) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            applyCouponResponseMutableLiveData.setValue(applyCouponResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        ApplyCouponResponse errorResponse = new ApplyCouponResponse();
                        try {
                            errorResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    ApplyCouponResponse.class);
                            applyCouponResponseMutableLiveData.setValue(errorResponse);
                        } catch (Exception ioException) {
                            ioException.printStackTrace();
                            Log.e(TAG, ioException.getMessage());
                            ((NetworkExceptionListener) orderSummaryFragment).onNetworkException(1, "");
                        }
                    }
                });
        return applyCouponResponseMutableLiveData;
    }

    /*Order place api*/
    public LiveData<OrderSuccessResponse> getOrderPlaceAPIResponse(ProgressDialog progressDialog,
                                                           HashMap<String, String> hashMap,
                                                           OrderSummaryFragment orderSummaryFragment) {
        MutableLiveData<OrderSuccessResponse> placeOrderResponseMutableLive = new MutableLiveData<>();

        ApiClientAuth.getClient(orderSummaryFragment.getContext())
                .create(ApiInterface.class)
                .getOrderPlaceResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<OrderSuccessResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull OrderSuccessResponse response) {
                        if (response != null) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                placeOrderResponseMutableLive.setValue(response);
                            }
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        OrderSuccessResponse baseResponse = new OrderSuccessResponse();
                        try {
                            baseResponse = gson.fromJson(Objects.requireNonNull(Objects.requireNonNull(((HttpException) e).response()).errorBody()).string(), OrderSuccessResponse.class);
                            placeOrderResponseMutableLive.setValue(baseResponse);
                        } catch (JsonSyntaxException | IOException exception) {
                            Log.e(TAG, "onError: " + exception.getMessage());
                            ((NetworkExceptionListener) orderSummaryFragment).onNetworkException(2, "");
                        }
                    }
                });
        return placeOrderResponseMutableLive;
    }

    /*Subscription Order done API*/
    public LiveData<OrderSuccessResponse> getSubscriptionBasketCustomerResponse(ProgressDialog progressDialog,
                                                                       HashMap<String,String> hashMap,
                                                                       OrderSummaryFragment orderSummaryFragment){
        MutableLiveData<OrderSuccessResponse> subscribeBasketMutableLive = new MutableLiveData<>();

        ApiClientAuth.getClient(orderSummaryFragment.getContext())
                .create(ApiInterface.class)
                .getSubscriptionBasketCustomerApi(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<OrderSuccessResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull OrderSuccessResponse response) {
                        if (response != null) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                subscribeBasketMutableLive.setValue(response);
                            }
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        OrderSuccessResponse baseResponse = new OrderSuccessResponse();
                        try {
                            baseResponse = gson.fromJson(Objects.requireNonNull(Objects.requireNonNull(((HttpException) e).response()).errorBody()).string(), OrderSuccessResponse.class);
                            subscribeBasketMutableLive.setValue(baseResponse);
                        } catch (JsonSyntaxException | IOException exception) {
                            Log.e(TAG, "onError: " + exception.getMessage());
                            ((NetworkExceptionListener) orderSummaryFragment).onNetworkException(2, "");
                        }
                    }
                });
        return subscribeBasketMutableLive;
    }

    /*Get Delivery Slo By Date on 14-Mar-2022*/
    public LiveData<OrderSummaryResponse> deliverySlotResponseLiveData(ProgressDialog progressDialog,
                                                                       HashMap<String, String> hashMap,
                                                                       OrderSummaryFragment orderSummaryFragment) {
        MutableLiveData<OrderSummaryResponse> deliverySlotByDateResponseMutable = new MutableLiveData<>();

        ApiClientAuth.getClient(orderSummaryFragment.context)
                .create(ApiInterface.class)
                .getDeliverySlotByDateResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<OrderSummaryResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull OrderSummaryResponse deliverySlotByDateResponse) {
                        if (deliverySlotByDateResponse != null) {

                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                deliverySlotByDateResponseMutable.setValue(deliverySlotByDateResponse);
                            }
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        OrderSummaryResponse deliverySlotResponse = new OrderSummaryResponse();
                        try {
                            deliverySlotResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    OrderSummaryResponse.class);
                            deliverySlotByDateResponseMutable.setValue(deliverySlotResponse);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            ((NetworkExceptionListener) orderSummaryFragment).onNetworkException(3, "");
                        }
                    }
                });
        return deliverySlotByDateResponseMutable;
    }

    /*Get Payment Credentials*/
    public LiveData<RazorPayCredentialResponse> getRazorPayCredentialResponse(ProgressDialog progressDialog,
                                                                              Context context) {

        MutableLiveData<RazorPayCredentialResponse> razorPayCredentialResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getRazorPayCredentialResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RazorPayCredentialResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull RazorPayCredentialResponse razorPayCredentialResponse) {
                        if (razorPayCredentialResponse != null) {
                            if (progressDialog != null) {
                                razorPayCredentialResponseMutableLiveData.setValue(razorPayCredentialResponse);
                            }
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        RazorPayCredentialResponse payCredentialResponse = new RazorPayCredentialResponse();
                        try {

                            payCredentialResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    RazorPayCredentialResponse.class);
                            razorPayCredentialResponseMutableLiveData.setValue(payCredentialResponse);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            ((NetworkExceptionListener) context).onNetworkException(4, "");
                        }
                    }
                });
        return razorPayCredentialResponseMutableLiveData;
    }


}
