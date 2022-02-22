package com.poona.agrocart.ui.nav_profile;

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
import com.poona.agrocart.data.network.responses.AreaResponse;
import com.poona.agrocart.data.network.responses.CityResponse;
import com.poona.agrocart.data.network.responses.ProfileResponse;
import com.poona.agrocart.data.network.responses.StateResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;

public class MyProfileViewModel extends AndroidViewModel {
    private static final String TAG = MyProfileViewModel.class.getSimpleName();
    public MutableLiveData<String> profilePhoto;
    public MutableLiveData<String> name;
    public MutableLiveData<String> mobileNo;
    public MutableLiveData<String> alternateMobileNo;
    public MutableLiveData<String> emailId;
    public MutableLiveData<String> state;
    public MutableLiveData<String> city;
    public MutableLiveData<String> area;
    public MutableLiveData<String> gender;
    public MutableLiveData<String> dateOfBirth;

    private final Context context;

    public MyProfileViewModel(@NonNull Application application) {
        super(application);

        context = application.getApplicationContext();

        profilePhoto = new MutableLiveData<>();
        name = new MutableLiveData<>();
        mobileNo = new MutableLiveData<>();
        alternateMobileNo = new MutableLiveData<>();
        emailId = new MutableLiveData<>();
        state = new MutableLiveData<>();
        city = new MutableLiveData<>();
        area = new MutableLiveData<>();
        gender = new MutableLiveData<>();
        dateOfBirth = new MutableLiveData<>();

        profilePhoto.setValue("");
        name.setValue("");
        mobileNo.setValue("");
        alternateMobileNo.setValue("");
        emailId.setValue("");
        state.setValue("");
        city.setValue("");
        area.setValue("");
        gender.setValue("");
        dateOfBirth.setValue("");
    }

    public LiveData<ProfileResponse> updateProfileResponse(ProgressDialog progressDialog,
                                                           EditProfileFragment editProfileFragment,
                                                           HashMap<String, RequestBody> profileMap,
                                                           MultipartBody.Part profilePhoto) {
        MutableLiveData<ProfileResponse> responseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(editProfileFragment.getContext())
                .create(ApiInterface.class)
                .updateProfileResponse(profileMap, profilePhoto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProfileResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ProfileResponse profileResponse) {
                        progressDialog.dismiss();
                        responseMutableLiveData.setValue(profileResponse);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        ProfileResponse response = new ProfileResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    ProfileResponse.class);

                            responseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) editProfileFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return responseMutableLiveData;
    }

    public Observable<List<String>> getProfileStateResponses(Context context,
                                                             HashMap<String, String> cityHashMap) {
        Observable<ProfileResponse> profileResponseObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getProfileObservableResponse(cityHashMap);
        Observable<StateResponse> stateResponseObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getStateObservableResponse();

        @SuppressLint("LongLogTag") Observable<List<String>> observableResult =
                Observable.zip(
                        profileResponseObservable.subscribeOn(Schedulers.io()),
                        stateResponseObservable.subscribeOn(Schedulers.io()),
                        (profileResponse, stateResponse) -> {
                            //Log.e("Profile Api ResponseData", new Gson().toJson(cityResponse));
                            //Log.e("State Api ResponseData", new Gson().toJson(areaResponse));

                            List<String> list = new ArrayList();
                            list.add(new Gson().toJson(profileResponse));
                            list.add(new Gson().toJson(stateResponse));
                            return list;
                        });

        return observableResult;
    }

    public Observable<List<String>> getCityAreaResponses(Context context,
                                                         HashMap<String, String> cityHashMap,
                                                         HashMap<String, String> areaHashMap) {
        Observable<CityResponse> cityResponseObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getCityObservableResponse(cityHashMap);
        Observable<AreaResponse> areaResponseObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getAreaObservableResponse(areaHashMap);

        @SuppressLint("LongLogTag") Observable<List<String>> observableResult =
                Observable.zip(
                        cityResponseObservable.subscribeOn(Schedulers.io()),
                        areaResponseObservable.subscribeOn(Schedulers.io()),
                        (cityResponse, areaResponse) -> {
                            //Log.e("City Api ResponseData", new Gson().toJson(cityResponse));
                            //Log.e("Area Api ResponseData", new Gson().toJson(areaResponse));

                            List<String> list = new ArrayList();
                            list.add(new Gson().toJson(cityResponse));
                            list.add(new Gson().toJson(areaResponse));
                            return list;
                        });

        return observableResult;
    }

    public LiveData<CityResponse> getCityResponse(ProgressDialog progressDialog,
                                                  EditProfileFragment editProfileFragment,
                                                  HashMap<String, String> areaParameters) {
        MutableLiveData<CityResponse> responseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(editProfileFragment.getContext())
                .create(ApiInterface.class)
                .getCityResponse(areaParameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CityResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull CityResponse cityResponse) {
                        progressDialog.dismiss();
                        responseMutableLiveData.setValue(cityResponse);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        CityResponse response = new CityResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    CityResponse.class);

                            responseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            //((NetworkExceptionListener) editProfileFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return responseMutableLiveData;
    }

    public LiveData<AreaResponse> getAreaResponse(ProgressDialog progressDialog,
                                                  EditProfileFragment editProfileFragment,
                                                  HashMap<String, String> areaParameters) {
        MutableLiveData<AreaResponse> responseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(editProfileFragment.getContext())
                .create(ApiInterface.class)
                .getAreaResponse(areaParameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AreaResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull AreaResponse areaResponse) {
                        progressDialog.dismiss();
                        responseMutableLiveData.setValue(areaResponse);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        AreaResponse response = new AreaResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    AreaResponse.class);

                            responseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            //((NetworkExceptionListener) editProfileFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return responseMutableLiveData;
    }
}