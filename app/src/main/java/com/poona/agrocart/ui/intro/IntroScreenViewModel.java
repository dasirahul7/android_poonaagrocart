package com.poona.agrocart.ui.intro;

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
import com.poona.agrocart.data.network.responses.IntroScreenResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class IntroScreenViewModel extends AndroidViewModel {
    private static final String TAG = IntroScreenViewModel.class.getSimpleName();

    public IntroScreenViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<IntroScreenResponse> getIntroScreenResponse(ProgressDialog progressDialog, IntroScreenFragment introScreenFragment) {
        MutableLiveData<IntroScreenResponse> introScreenResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(introScreenFragment.getContext())
                .create(ApiInterface.class)
                .getIntroScreenResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<IntroScreenResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull IntroScreenResponse introScreenResponse) {
                        if (introScreenResponse != null) {
                            progressDialog.dismiss();
                            introScreenResponseMutableLiveData.setValue(introScreenResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        IntroScreenResponse response = new IntroScreenResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    IntroScreenResponse.class);

                            introScreenResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) introScreenFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return introScreenResponseMutableLiveData;
    }
}
