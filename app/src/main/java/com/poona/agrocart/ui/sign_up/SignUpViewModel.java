package com.poona.agrocart.ui.sign_up;

import android.app.Application;
import android.app.ProgressDialog;
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
import com.poona.agrocart.data.network.responses.BaseResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class SignUpViewModel extends AndroidViewModel {
    private static final String TAG = SignUpViewModel.class.getSimpleName();
    public MutableLiveData<String> userName;
    public MutableLiveData<String> countryCode;
    public MutableLiveData<String> mobileNo;
    public MutableLiveData<String> emailId;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        userName = new MutableLiveData<>();
        countryCode = new MutableLiveData<>();
        mobileNo = new MutableLiveData<>();
        emailId = new MutableLiveData<>();
    }

    public LiveData<BaseResponse> submitRegistrationApi(ProgressDialog progressDialog,
                                                        HashMap<String, String> registrationMap,
                                                        SignUpFragment signUpFragment) {
        MutableLiveData<BaseResponse> registrationApiResponse = new MutableLiveData<>();

        ApiClientAuth.getClient(signUpFragment.getContext())
                .create(ApiInterface.class)
                .getRegistrationResponse(registrationMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse baseResponse) {
                        progressDialog.dismiss();
                        registrationApiResponse.setValue(baseResponse);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new BaseResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            registrationApiResponse.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) signUpFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return registrationApiResponse;
    }
}
