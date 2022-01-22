package com.poona.agrocart.ui.verify_otp;

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
import com.poona.agrocart.data.network.reponses.VerifyOtpResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class VerifyOtpViewModel extends AndroidViewModel {
    private String TAG = VerifyOtpViewModel.class.getSimpleName();
    public MutableLiveData<String> userMobileMsg;
    public MutableLiveData<String> otp;
    public VerifyOtpViewModel(@NonNull Application application) {
        super(application);
        userMobileMsg = new MutableLiveData<>();
        otp = new MutableLiveData<>();
    }

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
                            ((NetworkExceptionListener) verifyOtpFragment).onNetworkException(0);
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });

        return verifyOtpResponseMutableLiveData;
    }
}
