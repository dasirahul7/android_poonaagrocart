package com.poona.agrocart.ui.home;

import android.annotation.SuppressLint;
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
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BannerResponse;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.BasketResponse;
import com.poona.agrocart.data.network.responses.BestSellingResponse;
import com.poona.agrocart.data.network.responses.CategoryResponse;
import com.poona.agrocart.data.network.responses.ExclusiveResponse;
import com.poona.agrocart.data.network.responses.HomeResponse;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.data.network.responses.ProfileResponse;
import com.poona.agrocart.data.network.responses.SeasonalProductResponse;
import com.poona.agrocart.data.network.responses.StoreBannerResponse;
import com.poona.agrocart.ui.home.model.ProductOld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;


public class HomeViewModel extends AndroidViewModel {
    private final String TAG = HomeViewModel.class.getSimpleName();

    private final MutableLiveData<ProfileResponse.Profile> profileMutableLiveData;
    private final MutableLiveData<ArrayList<ProductOld>> liveDataCartProduct;

    private final MutableLiveData<ArrayList<ProductOld>> savesProductInBasket;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        //init all mutable liveData
        liveDataCartProduct = new MutableLiveData<>();
        savesProductInBasket = new MutableLiveData<>();
        profileMutableLiveData = new MutableLiveData<>();
        profileMutableLiveData.setValue(null);
        initCartItems();
    }

    private void initCartItems() {
        String PID = AppConstants.pId + "OP";
        ArrayList cartItemList = new ArrayList();
        for (int i = 0; i < 4; i++) {
            ProductOld cartProductOld = new ProductOld(PID + i, "Vegetable", "1kg",
                    "10", "65", getApplication().getString(R.string.img_potato),
                    "Pune", "Green");
            if (i == 1)
                cartProductOld.setImg(getApplication().getString(R.string.img_beat));
            if (i == 2)
                cartProductOld.setImg(getApplication().getString(R.string.img_beat));
            cartItemList.add(cartProductOld);
            if (i == 3) {
                cartProductOld.setOrganic(true);
            }
        }
        liveDataCartProduct.setValue(cartItemList);
    }

    //Banner ResponseData here
    @SuppressLint("CheckResult")
    public LiveData<BannerResponse> bannerResponseLiveData(ProgressDialog progressDialog,
                                                           HomeFragment homeFragment) {

        MutableLiveData<BannerResponse> bannerResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeBannerResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BannerResponse>() {

                    @Override
                    public void onSuccess(BannerResponse response) {
                        if (response != null) {
                            progressDialog.dismiss();
                            bannerResponseMutableLiveData.setValue(response);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        BannerResponse response = new BannerResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BannerResponse.class);

                            bannerResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return bannerResponseMutableLiveData;
    }

    //Category ResponseData here
    @SuppressLint("CheckResult")
    public LiveData<CategoryResponse> categoryResponseLiveData(ProgressDialog progressDialog,
                                                               HashMap<String, String> hashMap,
                                                               HomeFragment homeFragment) {

        MutableLiveData<CategoryResponse> categoryResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeCategoryResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CategoryResponse>() {

                    @Override
                    public void onSuccess(CategoryResponse response) {
                        if (response != null) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            categoryResponseMutableLiveData.setValue(response);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        CategoryResponse response = new CategoryResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    CategoryResponse.class);

                            categoryResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(1, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return categoryResponseMutableLiveData;
    }

    // BasketList ResponseData here
    public LiveData<BasketResponse> basketResponseLiveData(ProgressDialog progressDialog,
                                                           HashMap<String, String> hashMap,
                                                           HomeFragment homeFragment) {
        MutableLiveData<BasketResponse> basketResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeBasketResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BasketResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BasketResponse basketResponse) {
                        if (basketResponse != null) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            basketResponseMutableLiveData.setValue(basketResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        BasketResponse response = new BasketResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BasketResponse.class);

                            basketResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(2, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return basketResponseMutableLiveData;
    }

    //ExclusiveList ResponseData here
    public LiveData<ExclusiveResponse> exclusiveResponseLiveData(ProgressDialog progressDialog,
                                                                 HashMap<String, String> hashMap,
                                                                 HomeFragment homeFragment) {
        MutableLiveData<ExclusiveResponse> exclusiveResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeExclusiveResponseSingle(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ExclusiveResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ExclusiveResponse exclusiveResponse) {
                        if (exclusiveResponse != null) {
                            Log.d(TAG, "Product onSuccess: " + new Gson().toJson(exclusiveResponse));
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            exclusiveResponseMutableLiveData.setValue(exclusiveResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        ExclusiveResponse response = new ExclusiveResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    ExclusiveResponse.class);

                            exclusiveResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(3, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return exclusiveResponseMutableLiveData;
    }

    //BestSelling ResponseData here
    public LiveData<BestSellingResponse> bestSellingResponseLiveData(ProgressDialog progressDialog,
                                                                     HashMap<String, String> hashMap,
                                                                     HomeFragment homeFragment) {
        MutableLiveData<BestSellingResponse> bestSellingResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeBestSellingResponseSingle(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BestSellingResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BestSellingResponse bestSellingResponse) {
                        if (bestSellingResponse != null) {
                            Log.d(TAG, "BestSelling onSuccess: " + bestSellingResponse.getMessage());
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            bestSellingResponseMutableLiveData.setValue(bestSellingResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        BestSellingResponse response = new BestSellingResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BestSellingResponse.class);

                            bestSellingResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(4, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return bestSellingResponseMutableLiveData;
    }

    //Seasonal Products ResponseData here
    public LiveData<SeasonalProductResponse> seasonalResponseLiveData(ProgressDialog progressDialog,
                                                                      HashMap<String, String> hashMap,
                                                                      HomeFragment homeFragment) {
        MutableLiveData<SeasonalProductResponse> seasonalProductResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeSeasonalListResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SeasonalProductResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull SeasonalProductResponse seasonalProductResponse) {
                        if (seasonalProductResponse != null) {
                            Log.d(TAG, "Seasonal onSuccess: " + seasonalProductResponse.getMessage());
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            seasonalProductResponseMutableLiveData.setValue(seasonalProductResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        SeasonalProductResponse response = new SeasonalProductResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    SeasonalProductResponse.class);

                            seasonalProductResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(5, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return seasonalProductResponseMutableLiveData;
    }

    //ProductOld list ResponseData here
    public LiveData<ProductListResponse> homeProductListResponseLiveData(ProgressDialog progressDialog,
                                                                         HashMap<String, String> hashMap,
                                                                         HomeFragment homeFragment) {
        MutableLiveData<ProductListResponse> productListResponseMutableLiveData = new MutableLiveData();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeProductListResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProductListResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ProductListResponse productListResponse) {
                        if (productListResponse != null) {
                            Log.d(TAG, "productListResponse onSuccess: " + productListResponse.getMessage());
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            productListResponseMutableLiveData.setValue(productListResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        ProductListResponse response = new ProductListResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    ProductListResponse.class);

                            productListResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(6, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return productListResponseMutableLiveData;
    }

    //Store Banner ResponseData here

    public LiveData<StoreBannerResponse> storeBannerResponseLiveData(ProgressDialog progressDialog,
                                                                     HomeFragment homeFragment) {
        MutableLiveData<StoreBannerResponse> storeBannerMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .homeStoreBannerResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<StoreBannerResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull StoreBannerResponse storeBannerResponse) {
                        if (storeBannerResponse != null) {
                            Log.e(TAG, "storeBannerResponse onSuccess: " + storeBannerResponse.getMessage());
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            storeBannerMutableLiveData.setValue(storeBannerResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        StoreBannerResponse storeBannerResponse = new StoreBannerResponse();
                        try {
                            storeBannerResponse = gson.fromJson(((HttpException) e).response().errorBody().toString(),
                                    StoreBannerResponse.class);

                            storeBannerMutableLiveData.setValue(storeBannerResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(7, "");
                        }
                    }
                });
        return storeBannerMutableLiveData;
    }

    /*Add to Product in CART API*/
    public LiveData<BaseResponse> addToCartProductLiveData(HashMap<String, String> hashMap,
                                                           HomeFragment homeFragment) {
        MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .addToCartProductResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            Log.e(TAG, "add to cart onSuccess: " + new Gson().toJson(baseResponse));
                            baseResponseMutableLiveData.setValue(baseResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().toString(),
                                    BaseResponse.class);

                            baseResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(7, "");
                        }
                    }
                });
        return baseResponseMutableLiveData;
    }

    public MutableLiveData<ArrayList<ProductOld>> getLiveDataCartProduct() {
        return liveDataCartProduct;
    }

    public MutableLiveData<ArrayList<ProductOld>> getSavesProductInBasket() {
        return savesProductInBasket;
    }

    public LiveData<HomeResponse> homeResponseLiveData(ProgressDialog progressDialog,
                                                       HashMap<String, String> hashMap,
                                                       HomeFragment homeFragment) {
        MutableLiveData<HomeResponse> homeResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .getHomeAllData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<HomeResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull HomeResponse homeResponse) {
                        if (homeResponse != null) {
                            progressDialog.dismiss();
                            Log.e(TAG, "add to cart onSuccess: " + new Gson().toJson(homeResponse));
                            homeResponseMutableLiveData.setValue(homeResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        HomeResponse response = new HomeResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().toString(),
                                    HomeResponse.class);

                            homeResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(8, "");
                        }
                    }
                });
        return homeResponseMutableLiveData;
    }

    //View Profile
    public LiveData<ProfileResponse> getViewProfileResponse(ProgressDialog progressDialog,
                                                            HashMap<String, String> hashMap,
                                                            HomeFragment homeFragment) {
        MutableLiveData<ProfileResponse> responseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(homeFragment.getContext())
                .create(ApiInterface.class)
                .getViewProfileResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProfileResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ProfileResponse profileResponse) {
                        if (profileResponse != null) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Log.e(TAG, "onSuccess: " + new Gson().toJson(profileResponse));
                            responseMutableLiveData.setValue(profileResponse);
                            profileMutableLiveData.setValue(profileResponse.getProfile());
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        ProfileResponse response = new ProfileResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().toString(),
                                    ProfileResponse.class);
                            responseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeFragment).onNetworkException(9, "");
                        }
                    }
                });

        return responseMutableLiveData;
    }

    public Observable<List<String>> getHomepageResponses(Context context,
                                                         HashMap<String, String> homeHashMap
                                                         ) {
        Observable<HomeResponse> homeAllDataObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getHomeAllDataObservable(homeHashMap);

        Observable<BannerResponse> homeBannerObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).homeScreenBannerResponse();

        Observable<StoreBannerResponse> homeStoreBannerObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).homeStoreBannerObservable();

        Observable<CategoryResponse> homeCategoryObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).homeCategoryObservable(homeHashMap);

        Observable<BasketResponse> homeBasketObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).homeBasketObservable(homeHashMap);

        Observable<ExclusiveResponse> homeExclusiveObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).homeExclusiveObservable(homeHashMap);

        Observable<BestSellingResponse> homeBestSellingObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).homeBestSellingObservable(homeHashMap);

        Observable<SeasonalProductResponse> homeSeasonalObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).homeSeasonalObservable(homeHashMap);

        Observable<ProductListResponse> homeProductListObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).homeProductListObservable(homeHashMap);

        @SuppressLint("LongLogTag") Observable<List<String>> observableResult =
                Observable.zip(
                        homeAllDataObservable.subscribeOn(Schedulers.io()),
                        homeBannerObservable.subscribeOn(Schedulers.io()),
                        homeStoreBannerObservable.subscribeOn(Schedulers.io()),
                        homeCategoryObservable.subscribeOn(Schedulers.io()),
                        homeBasketObservable.subscribeOn(Schedulers.io()),
                        homeExclusiveObservable.subscribeOn(Schedulers.io()),
                        homeBestSellingObservable.subscribeOn(Schedulers.io()),
                        homeSeasonalObservable.subscribeOn(Schedulers.io()),
                        homeProductListObservable.subscribeOn(Schedulers.io()),
                        (homeResponse, bannerResponse, storeBannerResponse,
                         categoryResponse, basketResponse, exclusiveResponse,
                         bestSellingResponse, seasonalProductResponse, productListResponse) -> {
                            Log.e("Home Api Response", new Gson().toJson(homeResponse));
                            Log.e("Banner Api Response", new Gson().toJson(bannerResponse));
                            Log.e("StoreBanner Api Response", new Gson().toJson(storeBannerResponse));
                            Log.e("Category Api Response", new Gson().toJson(categoryResponse));
                            Log.e("Basket Api Response", new Gson().toJson(basketResponse));
                            Log.e("Exclusive Api Response", new Gson().toJson(exclusiveResponse));
                            Log.e("Best Selling Api Response", new Gson().toJson(bestSellingResponse));
                            Log.e("Seasonal Product Api Response", new Gson().toJson(seasonalProductResponse));
                            Log.e("ProductList Api Response", new Gson().toJson(productListResponse));

                            List<String> list = new ArrayList();
                            list.add(new Gson().toJson(homeResponse));
                            list.add(new Gson().toJson(bannerResponse));
                            list.add(new Gson().toJson(storeBannerResponse));
                            list.add(new Gson().toJson(categoryResponse));
                            list.add(new Gson().toJson(basketResponse));
                            list.add(new Gson().toJson(exclusiveResponse));
                            list.add(new Gson().toJson(bestSellingResponse));
                            list.add(new Gson().toJson(seasonalProductResponse));
                            list.add(new Gson().toJson(productListResponse));
                            return list;
                        });

        return observableResult;
    }
}