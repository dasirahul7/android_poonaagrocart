package com.poona.agrocart.ui.product_detail;

import android.app.Application;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.ProductDetailsResponse;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.ui.nav_favourites.FavouriteItemsFragment;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class ProductDetailViewModel extends AndroidViewModel {
    public static final String TAG = ProductDetailViewModel.class.getSimpleName();
    public MutableLiveData<String> productName;
    public MutableLiveData<String> productBrand;
    public MutableLiveData<Boolean> isOrganic;
    public MutableLiveData<String> productLocation;
    public LiveData<ProductListResponse.ProductUnit> productUnitLiveData;
    public MutableLiveData<ProductListResponse.ProductUnit> unitMutableLiveData;
    public MutableLiveData<String> sellingPrice;
    public MutableLiveData<String> offerPrice;
    public MutableLiveData<String> offer;
    public MutableLiveData<String> specialOffer;
    public MutableLiveData<Boolean> isInCart;
    public MutableLiveData<Boolean> isInFav;
    public MutableLiveData<String> productQuantity;
    public MutableLiveData<String> productDetail;
    public MutableLiveData<String> productAbout;
    public MutableLiveData<String> productBenefit;
    public MutableLiveData<String> productStorageUses;
    public MutableLiveData<String> productOtherInfo;
    public MutableLiveData<String> productWeightPolicy;
    public MutableLiveData<String> productNutrition;
    public MutableLiveData<String> productNoOfRating;
    public MutableLiveData<String> averageRating;
    public MutableLiveData<Integer> alreadyPurchased;

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
        productName = new MutableLiveData<>();
        productBrand = new MutableLiveData<>();
        isOrganic = new MutableLiveData<>();
        productLocation = new MutableLiveData<>();
        unitMutableLiveData = new MutableLiveData<>();
        productUnitLiveData = new MutableLiveData<>();
        sellingPrice = new MutableLiveData<>();
        offerPrice = new MutableLiveData<>();
        offer = new MutableLiveData<>();
        specialOffer = new MutableLiveData<>();
        isInCart = new MutableLiveData<>();
        isInFav = new MutableLiveData<>();
        productQuantity = new MutableLiveData<>();
        productDetail = new MutableLiveData<>();
        productAbout = new MutableLiveData<>();
        productBenefit = new MutableLiveData<>();productBenefit.setValue(null);
        productStorageUses = new MutableLiveData<>();productStorageUses.setValue(null);
        productOtherInfo = new MutableLiveData<>();productOtherInfo.setValue(null);
        productWeightPolicy = new MutableLiveData<>();productWeightPolicy.setValue(null);
        productNutrition = new MutableLiveData<>();productNutrition.setValue(null);
        productNoOfRating = new MutableLiveData<>();productNoOfRating.setValue(null);
        averageRating = new MutableLiveData<>();averageRating.setValue(null);
        alreadyPurchased = new MutableLiveData<>();alreadyPurchased.setValue(null);
        productName.setValue(null);
        productLocation.setValue(null);
        productAbout.setValue(null);
        productDetail.setValue(null);
        productQuantity.setValue(null);
        offer.setValue(null);
        specialOffer.setValue(null);
        offerPrice.setValue(null);
        isInCart.setValue(null);
        isInFav.setValue(null);
        unitMutableLiveData.setValue(null);
        sellingPrice.setValue(null);

    }

    //Product Details API
    public LiveData<ProductDetailsResponse> productDetailsResponseLiveData(ProgressDialog progressDialog,
                                                                           HashMap<String, String> hashMap,
                                                                           ProductDetailFragment productDetailFragment) {
        MutableLiveData<ProductDetailsResponse> productDetailsResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(productDetailFragment.getContext())
                .create(ApiInterface.class)
                .getProductDetailsResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProductDetailsResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ProductDetailsResponse productDetailsResponse) {
                        if (productDetailsResponse != null) {
                            progressDialog.dismiss();
                            productDetailsResponseMutableLiveData.setValue(productDetailsResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        ProductDetailsResponse response = new ProductDetailsResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    ProductDetailsResponse.class);

                            productDetailsResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) productDetailFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return productDetailsResponseMutableLiveData;
    }

    /*Add to Favourite*/

    public LiveData<BaseResponse> addToFavourite(ProgressDialog progressDialog,
                                                 HashMap<String, String> hashMap,
                                                 ProductDetailFragment productDetailFragment) {
        MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(productDetailFragment.getContext())
                .create(ApiInterface.class)
                .addToFavouriteResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            baseResponseMutableLiveData.setValue(baseResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            baseResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) productDetailFragment).onNetworkException(1, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return baseResponseMutableLiveData;
    }

    /*Remove from favourite*/
    public LiveData<BaseResponse> removeFromFavoriteResponse(ProgressDialog progressDialog,
                                                             HashMap<String, String> hashMap,
                                                             ProductDetailFragment productDetailFragment) {
        MutableLiveData<BaseResponse> removeFromFavouriteResponseObserver = new MutableLiveData<>();

        ApiClientAuth.getClient(productDetailFragment.getContext())
                .create(ApiInterface.class)
                .removeFromFavouriteResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            removeFromFavouriteResponseObserver.setValue(baseResponse);
                            Log.e(TAG, "Remove from Favourite onSuccess: " + new Gson().toJson(baseResponse));
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            removeFromFavouriteResponseObserver.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) productDetailFragment).onNetworkException(1, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return removeFromFavouriteResponseObserver;

    }


    //Add To Cart Product
    public LiveData<BaseResponse> addToCartProductLiveData(ProgressDialog progressDialog,
                                                           HashMap<String, String> hashMap,
                                                           ProductDetailFragment productDetailFragment) {
        MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(productDetailFragment.getContext())
                .create(ApiInterface.class)
                .addToCartProductResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            Log.e(TAG, "add to cart onSuccess: " + new Gson().toJson(baseResponse));
                            baseResponseMutableLiveData.setValue(baseResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            baseResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) productDetailFragment).onNetworkException(2, "");
                        }
                    }
                });
        return baseResponseMutableLiveData;
    }

    public LiveData<BaseResponse> callSubmitRatingResponseApi(ProgressDialog progressDialog, HashMap<String, String> submitRatingInputParameter,
                                                              ProductDetailFragment productDetailFragment) {


            MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();
            ApiClientAuth.getClient(productDetailFragment.getContext())
                    .create(ApiInterface.class)
                    .getSubmitRatingResponseProduct(submitRatingInputParameter)
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
//                            ((NetworkExceptionListener) homeFragment).onNetworkException(1,"");
                            }

                            Log.e(TAG, e.getMessage());
                        }
                    });
            return baseResponseMutableLiveData;
        }
}
