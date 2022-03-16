package com.poona.agrocart.ui.basket_detail;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.BasketDetailsResponse;
import com.poona.agrocart.data.network.responses.Review;
import com.poona.agrocart.data.network.responses.orderResponse.Delivery;
import com.poona.agrocart.data.network.responses.orderResponse.DeliverySlot;
import com.poona.agrocart.data.network.responses.orderResponse.OrderSummaryResponse;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class BasketDetailViewModel extends AndroidViewModel {
    public static final String TAG = BasketDetailViewModel.class.getSimpleName();
    public MutableLiveData<String> basketName;
    public MutableLiveData<String> basketRate;
    public MutableLiveData<String> basketSubRate;
    public MutableLiveData<Boolean> isOrganic;
    public MutableLiveData<ArrayList<BasketDetailsResponse.BasketProduct>> basketProducts;
    public MutableLiveData<String> basketTotalGrams;
    public MutableLiveData<Boolean> isInCart;
    public MutableLiveData<Boolean> isInFav;
    public MutableLiveData<String> basketQuantity;
    public MutableLiveData<String> basketDetail;
    public MutableLiveData<String> basketAbout;
    public MutableLiveData<String> basketBenefit;
    public MutableLiveData<String> basketStorageUses;
    public MutableLiveData<String> basketOtherInfo;
    public MutableLiveData<String> basketWeightPolicy;
    public MutableLiveData<String> basketNutrition;
    public MutableLiveData<String> basketAvgRating;
    public MutableLiveData<String> basketNoOfRatings;
    public MutableLiveData<Float> yourRating;
    public MutableLiveData<Integer> alreadyPurchased;
    public MutableLiveData<ArrayList<Review>> reviewLiveData;
    public MutableLiveData<BasketDetailsResponse.Rating> basketRating;

    /*Subscription here*/
    public MutableLiveData<DeliverySlot> subscriptionSlotMutable;
    public MutableLiveData<String> subscriptionStartDateMutable;
    public MutableLiveData<ArrayList<BasketDetailsResponse.SubscriptionType>> subscriptionTypeMutableList;
    public MutableLiveData<ArrayList<DeliverySlot>> subscriptionDeliverySlotsLists;
    public MutableLiveData<BasketDetailsResponse.Rating> getBasketRating() {
        return basketRating;
    }

    public void setBasketRating(MutableLiveData<BasketDetailsResponse.Rating> basketRating) {
        this.basketRating = basketRating;
    }

    public BasketDetailViewModel(Application application) {
        super(application);
        basketName = new MutableLiveData<>();
        basketRate = new MutableLiveData<>();
        basketSubRate = new MutableLiveData<>();
        isOrganic = new MutableLiveData<>();
        basketTotalGrams = new MutableLiveData<>();
        isInCart = new MutableLiveData<>();
        isInFav = new MutableLiveData<>();
        basketQuantity = new MutableLiveData<>();
        basketDetail = new MutableLiveData<>();
        basketAbout = new MutableLiveData<>();
        basketBenefit = new MutableLiveData<>();
        basketStorageUses = new MutableLiveData<>();
        basketOtherInfo = new MutableLiveData<>();
        basketWeightPolicy = new MutableLiveData<>();
        basketNutrition = new MutableLiveData<>();
        basketAvgRating = new MutableLiveData<>();
        basketProducts = new MutableLiveData<ArrayList<BasketDetailsResponse.BasketProduct>>();
        reviewLiveData = new MutableLiveData<>();
        basketRating = new MutableLiveData<>();
        basketNoOfRatings = new MutableLiveData<>();
        yourRating = new MutableLiveData<>();
        alreadyPurchased = new MutableLiveData<>();
        basketName.setValue(null);
        basketRate.setValue(null);
        basketSubRate.setValue(null);
        isOrganic.setValue(null);
        basketTotalGrams.setValue(null);
        isInCart.setValue(null);
        isInFav.setValue(null);
        basketQuantity.setValue(null);
        basketDetail.setValue(null);
        basketAbout.setValue(null);
        basketBenefit.setValue(null);
        basketStorageUses.setValue(null);
        basketOtherInfo.setValue(null);
        basketWeightPolicy.setValue(null);
        basketNutrition.setValue(null);
        basketAvgRating.setValue(null);
        basketProducts.setValue(null);
        reviewLiveData.setValue(null);
        basketRating.setValue(null);
        basketNoOfRatings.setValue(null);
        yourRating.setValue(null);
        alreadyPurchased.setValue(null);
        /*subscription*/
        subscriptionSlotMutable= new MutableLiveData<>();
        subscriptionStartDateMutable= new MutableLiveData<>();
        subscriptionTypeMutableList= new MutableLiveData<>();
        subscriptionDeliverySlotsLists= new MutableLiveData<>();
        subscriptionSlotMutable.setValue(null);
        subscriptionStartDateMutable.setValue(null);
        subscriptionTypeMutableList.setValue(null);
        subscriptionDeliverySlotsLists.setValue(null);
    }

    /*Get BasketDetails API here*/
    public LiveData<BasketDetailsResponse> basketDetailsResponseLiveData(ProgressDialog progressDialog,
                                                                         HashMap<String, String> hashMap,
                                                                         BasketDetailFragment basketDetailFragment) {
        MutableLiveData<BasketDetailsResponse> basketDetailsResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(basketDetailFragment.getContext())
                .create(ApiInterface.class)
                .getBasketDetailsResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BasketDetailsResponse>() {
                    @Override
                    public void onSuccess(@NonNull BasketDetailsResponse basketDetailsResponse) {
                        if (basketDetailsResponse != null) {
                            progressDialog.dismiss();
                            basketDetailsResponseMutableLiveData.setValue(basketDetailsResponse);
                            Log.e(TAG, "Basket Details onSuccess: " + new Gson().toJson(basketDetailsResponse));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        BasketDetailsResponse response = new BasketDetailsResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BasketDetailsResponse.class);

                            basketDetailsResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) basketDetailFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return basketDetailsResponseMutableLiveData;

    }


    /*Add to CART Basket*/
    public LiveData<BaseResponse> addToBasketResponse(ProgressDialog progressDialog,
                                                      HashMap<String, String> hashMap,
                                                      BasketDetailFragment basketDetailFragment) {
        MutableLiveData<BaseResponse> addToBasketResponseObserver = new MutableLiveData<>();

        ApiClientAuth.getClient(basketDetailFragment.getContext())
                .create(ApiInterface.class)
                .addToBasketResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            addToBasketResponseObserver.setValue(baseResponse);
                            Log.e(TAG, "Add to Basket onSuccess: " + new Gson().toJson(baseResponse));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            addToBasketResponseObserver.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) basketDetailFragment).onNetworkException(1, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return addToBasketResponseObserver;

    }

    /*Add To Favourite Basket*/
    public LiveData<BaseResponse> addToFavoriteBasketResponse(ProgressDialog progressDialog,
                                                              HashMap<String, String> hashMap,
                                                              BasketDetailFragment basketDetailFragment) {
        MutableLiveData<BaseResponse> addToFavouriteResponseObserver = new MutableLiveData<>();

        ApiClientAuth.getClient(basketDetailFragment.getContext())
                .create(ApiInterface.class)
                .addToFavouriteResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            addToFavouriteResponseObserver.setValue(baseResponse);
                            Log.e(TAG, "Add to Favourite onSuccess: " + new Gson().toJson(baseResponse));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            addToFavouriteResponseObserver.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) basketDetailFragment).onNetworkException(2, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return addToFavouriteResponseObserver;

    }

    /*Remove from favrouite*/
    public LiveData<BaseResponse> removeFromFavoriteResponse(ProgressDialog progressDialog,
                                                             HashMap<String, String> hashMap,
                                                             BasketDetailFragment basketDetailFragment) {
        MutableLiveData<BaseResponse> removeFromFavouriteResponseObserver = new MutableLiveData<>();

        ApiClientAuth.getClient(basketDetailFragment.getContext())
                .create(ApiInterface.class)
                .removeFromFavouriteResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            removeFromFavouriteResponseObserver.setValue(baseResponse);
                            Log.e(TAG, "Remove from Favourite onSuccess: " + new Gson().toJson(baseResponse));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            removeFromFavouriteResponseObserver.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) basketDetailFragment).onNetworkException(3, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return removeFromFavouriteResponseObserver;

    }

    public LiveData<BaseResponse> callSubmitRatingResponseApi(ProgressDialog progressDialog, HashMap<String, String> submitRatingInputParameter,
                                                              BasketDetailFragment basketDetailFragment) {


        MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(basketDetailFragment.getContext())
                .create(ApiInterface.class)
                .getSubmitRatingResponseBasket(submitRatingInputParameter)
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
                            ((NetworkExceptionListener) basketDetailFragment).onNetworkException(4,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return baseResponseMutableLiveData;
    }


    public LiveData<BaseResponse> getSubscrideBasketApi(Context context, ProgressDialog progressDialog
            , HashMap<String, String> subscriptionBasketInputParameter, BasketDetailFragment basketDetailFragment) {

        MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getSubscriptionBasketDetailsApi(subscriptionBasketInputParameter)
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
                            ((NetworkExceptionListener) basketDetailFragment).onNetworkException(5,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return baseResponseMutableLiveData;


    }

    public LiveData<OrderSummaryResponse> deliverySlotResponseLiveData(ProgressDialog progressDialog, HashMap<String, String> deliverySlotParam, BasketDetailFragment basketDetailFragment) {
        MutableLiveData<OrderSummaryResponse> deliverySlotByDateResponseMutable = new MutableLiveData<>();

        ApiClientAuth.getClient(basketDetailFragment.context)
                .create(ApiInterface.class)
                .getDeliverySlotByDateResponse(deliverySlotParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<OrderSummaryResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull OrderSummaryResponse deliverySlotByDateResponse) {
                        if (deliverySlotByDateResponse!=null){

                            if (progressDialog!=null) {
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
                            deliverySlotResponse = gson.fromJson(((HttpException)e).response().errorBody().string(),
                                    OrderSummaryResponse.class);
                            deliverySlotByDateResponseMutable.setValue(deliverySlotResponse);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            ((NetworkExceptionListener)basketDetailFragment).onNetworkException(3,"");
                        }
                    }
                });
        return deliverySlotByDateResponseMutable;
    }
}