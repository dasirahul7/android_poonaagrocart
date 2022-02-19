package com.poona.agrocart.ui.nav_settings;

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
import com.poona.agrocart.data.network.responses.settingResponse.UpdateConfigurationResponse;
import com.poona.agrocart.data.network.responses.settingResponse.ViewConfigurationResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class SettingViewModel extends AndroidViewModel {
    private static final String TAG = SettingViewModel.class.getSimpleName();

    public MutableLiveData<String> appVersion;
    public MutableLiveData<String> version;

    public SettingViewModel(@NonNull Application application) {
        super(application);

        appVersion  = new MutableLiveData<>();
        version  = new MutableLiveData<>();

        appVersion.setValue("");
        version.setValue("");
    }

    public LiveData<UpdateConfigurationResponse> getNotificationUpdate(ProgressDialog progressDialog, Context context
            , HashMap<String, Integer> notificationInputParameter, SettingsFragment settingsFragment) {
        MutableLiveData<UpdateConfigurationResponse> notificationSettingResponseMutableLiveData = new MutableLiveData<>();

                 ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getNotificationSetting(notificationInputParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UpdateConfigurationResponse>() {
                    @Override
                    public void onSuccess(@NonNull UpdateConfigurationResponse baseResponse) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                        if (baseResponse != null) {
                            notificationSettingResponseMutableLiveData.setValue(baseResponse);
                        } else {
                            notificationSettingResponseMutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }

                        Gson gson = new GsonBuilder().create();
                        UpdateConfigurationResponse baseResponse = new UpdateConfigurationResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), UpdateConfigurationResponse.class);

                            notificationSettingResponseMutableLiveData.setValue(baseResponse);
                        }catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) settingsFragment)
                                    .onNetworkException(0, "");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return notificationSettingResponseMutableLiveData;
    }

    public LiveData<ViewConfigurationResponse> getUpdatedNotification(ProgressDialog progressDialog
            , Context context, SettingsFragment settingsFragment) {
        MutableLiveData<ViewConfigurationResponse> updateConfigurationResponseMutableLiveData = new MutableLiveData<>();

                 ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getUpdatedNotification()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ViewConfigurationResponse>() {
                    @Override
                    public void onSuccess(@NonNull ViewConfigurationResponse baseResponse) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                        if (baseResponse != null) {
                            updateConfigurationResponseMutableLiveData.setValue(baseResponse);
                        } else {
                            updateConfigurationResponseMutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }

                        Gson gson = new GsonBuilder().create();
                        ViewConfigurationResponse baseResponse = new ViewConfigurationResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), ViewConfigurationResponse.class);

                            updateConfigurationResponseMutableLiveData.setValue(baseResponse);
                        }catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) settingsFragment)
                                    .onNetworkException(0, "");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return updateConfigurationResponseMutableLiveData;

    }

   /* public LiveData<ViewConfigurationResponse> getUpdatedNotification(ProgressDialog progressDialog
            , Context context, SettingsFragment settingsFragment) {
        MutableLiveData<ViewConfigurationResponse> updateConfigurationResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getUpdatedNotification()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ViewConfigurationResponse>() {
                    @Override
                    public void onSuccess(@NonNull ViewConfigurationResponse baseResponse) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                        if (baseResponse != null) {
                            updateConfigurationResponseMutableLiveData.setValue(baseResponse);
                        } else {
                            updateConfigurationResponseMutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }

                        Gson gson = new GsonBuilder().create();
                        ViewConfigurationResponse baseResponse = new ViewConfigurationResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), ViewConfigurationResponse.class);

                            updateConfigurationResponseMutableLiveData.setValue(baseResponse);
                        }catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) settingsFragment)
                                    .onNetworkException(0, "");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return updateConfigurationResponseMutableLiveData;
    }*/
}
