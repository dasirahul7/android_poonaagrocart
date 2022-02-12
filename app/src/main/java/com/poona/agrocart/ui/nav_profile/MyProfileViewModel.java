package com.poona.agrocart.ui.nav_profile;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.poona.agrocart.data.network.reponses.AreaResponse;
import com.poona.agrocart.data.network.reponses.BaseResponse;
import com.poona.agrocart.data.network.reponses.CityResponse;
import com.poona.agrocart.data.network.reponses.ProfileResponse;
import com.poona.agrocart.data.network.reponses.StateResponse;
import com.poona.agrocart.ui.sign_up.SignUpFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
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

    public MyProfileViewModel(@NonNull Application application) {
        super(application);

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

    public Observable<List<String>> getCommonApiResponses(Context context, HashMap<String, String> profileHashMapData) {
        Observable<ProfileResponse> profileResponseObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getProfileObservableResponse(profileHashMapData);
        Observable<StateResponse> stateResponseObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getStateObservableResponse();
        Observable<CityResponse> cityResponseObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getCityObservableResponse();
        Observable<AreaResponse> areaResponseObservable = ApiClientAuth
                .getClient(context).create(ApiInterface.class).getAreaObservableResponse();

        @SuppressLint("LongLogTag") Observable<List<String>> observableResult =
                Observable.zip(
                        profileResponseObservable.subscribeOn(Schedulers.io()),
                        stateResponseObservable.subscribeOn(Schedulers.io()),
                        cityResponseObservable.subscribeOn(Schedulers.io()),
                        areaResponseObservable.subscribeOn(Schedulers.io()),
                        (profileResponse, stateResponse, cityResponse, areaResponse) -> {
                            //Log.e("Profile Api Response", new Gson().toJson(profileResponse));
                            //Log.e("State Api Response", new Gson().toJson(stateResponse));
                            //Log.e("City Api Response", new Gson().toJson(cityResponse));
                            //Log.e("Area Api Response", new Gson().toJson(areaResponse));

                            List<String> list = new ArrayList();
                            list.add(new Gson().toJson(profileResponse));
                            list.add(new Gson().toJson(stateResponse));
                            list.add(new Gson().toJson(cityResponse));
                            list.add(new Gson().toJson(areaResponse));
                            return list;
                        });

        return observableResult;
    }

    public LiveData<ProfileResponse> updateProfileResponse(ProgressDialog progressDialog,
                                                        HashMap<String, String> profileMap,
                                                        MyProfileFragment myProfileFragment) {
        MutableLiveData<ProfileResponse> registrationApiResponse = new MutableLiveData<>();

        ApiClientAuth.getClient(myProfileFragment.getContext())
                .create(ApiInterface.class)
                .updateProfileResponse(profileMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProfileResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ProfileResponse profileResponse) {
                        progressDialog.dismiss();
                        registrationApiResponse.setValue(profileResponse);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        ProfileResponse response = new ProfileResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    ProfileResponse.class);

                            registrationApiResponse.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) myProfileFragment).onNetworkException(0);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return registrationApiResponse;
    }
}