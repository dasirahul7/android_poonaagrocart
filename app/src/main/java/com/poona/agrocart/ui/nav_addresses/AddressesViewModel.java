package com.poona.agrocart.ui.nav_addresses;

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
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.AddressesResponse;
import com.poona.agrocart.data.network.responses.AreaResponse;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.CityResponse;
import com.poona.agrocart.data.network.responses.PinCodeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class AddressesViewModel extends AndroidViewModel {
    public MutableLiveData<String> addressType;
    public MutableLiveData<String> name;
    public MutableLiveData<String> mobile;
    public MutableLiveData<String> city;
    public MutableLiveData<String> area;
    public MutableLiveData<String> pinCode;
    public MutableLiveData<String> apartmentName;
    public MutableLiveData<String> houseNumber;
    public MutableLiveData<String> landmark;
    public MutableLiveData<String> mapAddress;
    public MutableLiveData<String> street;
    private final String TAG = AddressesViewModel.class.getSimpleName();

    public AddressesViewModel(@NonNull Application application) {
        super(application);

        addressType = new MutableLiveData<>();
        name = new MutableLiveData<>();
        mobile = new MutableLiveData<>();
        city = new MutableLiveData<>();
        area = new MutableLiveData<>();
        pinCode = new MutableLiveData<>();
        houseNumber = new MutableLiveData<>();
        apartmentName = new MutableLiveData<>();
        street = new MutableLiveData<>();
        landmark = new MutableLiveData<>();
        mapAddress = new MutableLiveData<>();

        addressType.setValue("");
        name.setValue("");
        mobile.setValue("");
        city.setValue("");
        area.setValue("");
        pinCode.setValue("");
        houseNumber.setValue("");
        apartmentName.setValue("");
        street.setValue("");
        landmark.setValue("");
        mapAddress.setValue("");
    }

    public LiveData<AddressesResponse> getAddressesResponse(ProgressDialog progressDialog,
                                                            AddressesFragment addressesFragment) {
        MutableLiveData<AddressesResponse> responseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(addressesFragment.getContext())
                .create(ApiInterface.class)
                .getAddressesListResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AddressesResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull AddressesResponse response) {
                        progressDialog.dismiss();
                        responseMutableLiveData.setValue(response);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        AddressesResponse response = new AddressesResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    AddressesResponse.class);

                            responseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) addressesFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return responseMutableLiveData;
    }

    public LiveData<CityResponse> getCityResponse(ProgressDialog progressDialog,
                                                  AddAddressFragment addAddressFragment) {
        MutableLiveData<CityResponse> cityResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(addAddressFragment.getContext())
                .create(ApiInterface.class)
                .getCityResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CityResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull CityResponse cityResponse) {
                        if (cityResponse != null) {
                            progressDialog.dismiss();
                            cityResponseMutableLiveData.setValue(cityResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        CityResponse cityResponse = new CityResponse();
                        try {
                            cityResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    CityResponse.class);

                            cityResponseMutableLiveData.setValue(cityResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) addAddressFragment).onNetworkException(0, "");
                        }

                    }
                });

        return cityResponseMutableLiveData;
    }

    public LiveData<AreaResponse> getAreaResponse(ProgressDialog progressDialog,
                                                  AddAddressFragment addAddressFragment,
                                                  HashMap<String, String> hashmap) {
        MutableLiveData<AreaResponse> areaResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(addAddressFragment.getContext())
                .create(ApiInterface.class)
                .getAreaResponse(hashmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AreaResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull AreaResponse areaResponse) {
                        if (areaResponse != null) {
                            progressDialog.dismiss();
                            areaResponseMutableLiveData.setValue(areaResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        AreaResponse response = new AreaResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    AreaResponse.class);

                            areaResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) addAddressFragment).onNetworkException(1, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return areaResponseMutableLiveData;
    }

    public LiveData<PinCodeResponse> getPinCodeResponse(ProgressDialog progressDialog,
                                                  AddAddressFragment addAddressFragment,
                                                  HashMap<String, String> hashmap) {
        MutableLiveData<PinCodeResponse> pinCodeResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(addAddressFragment.getContext())
                .create(ApiInterface.class)
                .getPinCodeResponse(hashmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<PinCodeResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull PinCodeResponse pinCodeResponse) {
                        if (pinCodeResponse != null) {
                            progressDialog.dismiss();
                            pinCodeResponseMutableLiveData.setValue(pinCodeResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        PinCodeResponse pinCodeResponse = new PinCodeResponse();
                        try {
                            pinCodeResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    PinCodeResponse.class);

                            pinCodeResponseMutableLiveData.setValue(pinCodeResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) addAddressFragment).onNetworkException(1, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return pinCodeResponseMutableLiveData;
    }

    public LiveData<BaseResponse> addAddressResponse(ProgressDialog progressDialog,
                                                     AddAddressFragment addAddressFragment,
                                                     HashMap<String, String> hashmap) {
        MutableLiveData<BaseResponse> responseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(addAddressFragment.getContext())
                .create(ApiInterface.class)
                .addAddressResponse(hashmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            responseMutableLiveData.setValue(baseResponse);
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

                            responseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) addAddressFragment).onNetworkException(2, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return responseMutableLiveData;
    }

    public LiveData<BaseResponse> updateAddressResponse(ProgressDialog progressDialog,
                                                        AddAddressFragment addAddressFragment,
                                                        HashMap<String, String> hashmap) {
        MutableLiveData<BaseResponse> responseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(addAddressFragment.getContext())
                .create(ApiInterface.class)
                .updateAddressResponse(hashmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            responseMutableLiveData.setValue(baseResponse);
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

                            responseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) addAddressFragment).onNetworkException(4, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return responseMutableLiveData;
    }

    public LiveData<BaseResponse> deleteAddressResponse(ProgressDialog progressDialog,
                                                        AddressesFragment addAddressFragment,
                                                        HashMap<String, String> hashmap) {
        MutableLiveData<BaseResponse> responseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(addAddressFragment.getContext())
                .create(ApiInterface.class)
                .deleteAddressResponse(hashmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            responseMutableLiveData.setValue(baseResponse);
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

                            responseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) addAddressFragment).onNetworkException(1, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return responseMutableLiveData;
    }

    public LiveData<BaseResponse> checkPinCodeAvailableResponse(ProgressDialog progressDialog,
                                                                AddAddressFragment addAddressFragment,
                                                                HashMap<String, String> hashmap) {
        MutableLiveData<BaseResponse> responseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(addAddressFragment.getContext())
                .create(ApiInterface.class)
                .checkPinCodeAvailableResponse(hashmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            responseMutableLiveData.setValue(baseResponse);
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

                            responseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) addAddressFragment).onNetworkException(3, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return responseMutableLiveData;
    }

    public LiveData<BaseResponse> setDefaultAddressResponse(ProgressDialog progressDialog,
                                                            AddressesFragment addAddressFragment,
                                                            HashMap<String, String> hashmap) {
        MutableLiveData<BaseResponse> responseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(addAddressFragment.getContext())
                .create(ApiInterface.class)
                .setDefaultAddressResponse(hashmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                        if (baseResponse != null) {
                            progressDialog.dismiss();
                            responseMutableLiveData.setValue(baseResponse);
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

                            responseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) addAddressFragment).onNetworkException(2, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return responseMutableLiveData;
    }

    public Observable<List<String>> getCityAreaPinCodeResponses(Context context, HashMap<String, String> areaHashMap, HashMap<String, String> pinCodeHashMap) {
        Observable<CityResponse> cityResponseObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getCityObservableResponse();
        Observable<AreaResponse> areaResponseObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getAreaObservableResponse(areaHashMap);
        Observable<PinCodeResponse> pinCodeResponseObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getPinCodeObservableResponse(pinCodeHashMap);

        @SuppressLint("LongLogTag") Observable<List<String>> observableResult =
                Observable.zip(
                        cityResponseObservable.subscribeOn(Schedulers.io()),
                        areaResponseObservable.subscribeOn(Schedulers.io()),
                        pinCodeResponseObservable.subscribeOn(Schedulers.io()),
                        (cityResponse, areaResponse,pinCodeResponse) -> {
                            //Log.e("City Api ResponseData", new Gson().toJson(cityResponse));
                            //Log.e("Area Api ResponseData", new Gson().toJson(areaResponse));

                            List<String> list = new ArrayList();
                            list.add(new Gson().toJson(cityResponse));
                            list.add(new Gson().toJson(areaResponse));
                            list.add(new Gson().toJson(pinCodeResponse));
                            return list;
                        });

        return observableResult;
    }
}