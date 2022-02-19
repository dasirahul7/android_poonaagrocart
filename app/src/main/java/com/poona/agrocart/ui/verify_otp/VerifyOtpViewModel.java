package com.poona.agrocart.ui.verify_otp;

import android.annotation.SuppressLint;
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
import com.poona.agrocart.data.network.responses.SignInResponse;
import com.poona.agrocart.data.network.responses.VerifyOtpResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class VerifyOtpViewModel extends AndroidViewModel {
    public MutableLiveData<String> userMobileMsg;
    public MutableLiveData<String> otp;
    private final String TAG = VerifyOtpViewModel.class.getSimpleName();

    public VerifyOtpViewModel(@NonNull Application application) {
        super(application);
        userMobileMsg = new MutableLiveData<>();
        otp = new MutableLiveData<>();
    }

    //Verify Otp API ResponseData
    public LiveData<VerifyOtpResponse> submitVerifyOtp(ProgressDialog progressDialog,
                                                       HashMap<String, String> hashMap,
                                                       VerifyOtpFragment verifyOtpFragment) {
        MutableLiveData<VerifyOtpResponse> verifyOtpResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(verifyOtpFragment.getContext())
                .create(ApiInterface.class)
                .getVerifyOtpResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<VerifyOtpResponse>() {

                    @Override
                    public void onSuccess(VerifyOtpResponse response) {
                        progressDialog.dismiss();
                        verifyOtpResponseMutableLiveData.setValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        VerifyOtpResponse response = new VerifyOtpResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    VerifyOtpResponse.class);

                            verifyOtpResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) verifyOtpFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });

        return verifyOtpResponseMutableLiveData;
    }


    @SuppressLint("CheckResult")
    public LiveData<SignInResponse> resendOtpApi(ProgressDialog progressDialog,
                                                 HashMap<String, String> hashMap,
                                                 VerifyOtpFragment verifyOtpFragment) {
        MutableLiveData<SignInResponse> resendOtpResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(verifyOtpFragment.getContext())
                .create(ApiInterface.class)
                .getResendOtpResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SignInResponse>() {

                    @Override
                    public void onSuccess(SignInResponse response) {
                        progressDialog.dismiss();
                        resendOtpResponseMutableLiveData.setValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        SignInResponse response = new SignInResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    SignInResponse.class);

                            resendOtpResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) verifyOtpFragment).onNetworkException(1, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });

        return resendOtpResponseMutableLiveData;
    }
}
