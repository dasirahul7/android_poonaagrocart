package com.poona.agrocart.ui.nav_faq;

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
import com.poona.agrocart.ui.nav_faq.model.FaqListResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class FaQViewModel extends AndroidViewModel {
    private static final String TAG = FaQViewModel.class.getSimpleName();

    public FaQViewModel(@NonNull Application application) {
        super(application);


    }

    public LiveData<FaqListResponse> getAddFaqsResponse(ProgressDialog progressDialog, Context context, FaQFragment faQFragment) {
        MutableLiveData<FaqListResponse> addFaqsResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getAddFaqs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FaqListResponse>() {
                    @Override
                    public void onSuccess(@NonNull FaqListResponse baseResponse) {
                        progressDialog.dismiss();
                        addFaqsResponseMutableLiveData.setValue(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        FaqListResponse baseResponse = new FaqListResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), FaqListResponse.class);

                            addFaqsResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) faQFragment).onNetworkException(0, "");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return addFaqsResponseMutableLiveData;
    }

    // TODO: Implement the ViewModel
}