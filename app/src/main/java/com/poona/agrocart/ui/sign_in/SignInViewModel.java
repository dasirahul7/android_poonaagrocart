package com.poona.agrocart.ui.sign_in;

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
import com.poona.agrocart.data.network.reponses.BaseResponse;
import com.poona.agrocart.data.network.reponses.SignInResponse;
import com.poona.agrocart.ui.home.HomeActivity;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class SignInViewModel extends AndroidViewModel {

    private String TAG = SignInViewModel.class.getSimpleName();

    public SignInViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("CheckResult")
    public LiveData<SignInResponse> submitSignInDetails(ProgressDialog progressDialog,
                                                        HashMap<String, String> hashMap,
                                                        SignInFragment signInFragment) {
        MutableLiveData<SignInResponse> signInResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(signInFragment.getContext())
                .create(ApiInterface.class)
                .getSignInResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SignInResponse>() {

                    @Override
                    public void onSuccess(SignInResponse response) {
                        progressDialog.dismiss();
                        signInResponseMutableLiveData.setValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        SignInResponse response = new SignInResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    SignInResponse.class);

                            signInResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) signInFragment).onNetworkException(0,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });

        return signInResponseMutableLiveData;
    }
    public LiveData<BaseResponse> signOutApiResponse(ProgressDialog progressDialog,
                                                      HashMap<String, String> hashMap,
                                                      HomeActivity homeActivity) {
        MutableLiveData<BaseResponse> signOutResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(homeActivity.getApplicationContext())
                .create(ApiInterface.class)
                .signOutResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {

                    @Override
                    public void onSuccess(BaseResponse response) {
                        progressDialog.dismiss();
                        signOutResponseMutableLiveData.setValue(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        BaseResponse response = new SignInResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    BaseResponse.class);

                            signOutResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) homeActivity).onNetworkException(0,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });

        return signOutResponseMutableLiveData;
    }
}