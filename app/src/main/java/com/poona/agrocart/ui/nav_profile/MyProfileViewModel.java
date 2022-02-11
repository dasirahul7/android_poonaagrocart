package com.poona.agrocart.ui.nav_profile;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.reponses.AreaResponse;
import com.poona.agrocart.data.network.reponses.CityResponse;
import com.poona.agrocart.data.network.reponses.ProfileResponse;
import com.poona.agrocart.data.network.reponses.StateResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyProfileViewModel extends AndroidViewModel
{
    public MutableLiveData<String> name;
    public MutableLiveData<String> mobileNo;
    public MutableLiveData<String> alternateMobileNo;
    public MutableLiveData<String> emailId;
    public MutableLiveData<String> state;
    public MutableLiveData<String> city;
    public MutableLiveData<String> area;
    public MutableLiveData<String> gender;
    public MutableLiveData<String> dateOfBirth;

    public MyProfileViewModel(@NonNull Application application)
    {
        super(application);

        name = new MutableLiveData<>();
        mobileNo = new MutableLiveData<>();
        alternateMobileNo = new MutableLiveData<>();
        emailId = new MutableLiveData<>();
        state = new MutableLiveData<>();
        city = new MutableLiveData<>();
        area = new MutableLiveData<>();
        gender = new MutableLiveData<>();
        dateOfBirth = new MutableLiveData<>();

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
}