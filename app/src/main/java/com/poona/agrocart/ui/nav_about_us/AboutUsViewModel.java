package com.poona.agrocart.ui.nav_about_us;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.ui.nav_about_us.model.CmsPagesDataResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class AboutUsViewModel extends AndroidViewModel {

    private static final String TAG = AboutUsViewModel.class.getSimpleName();

    public MutableLiveData<String> aboutUsContent;

    public AboutUsViewModel(@NonNull Application application) {
        super(application);

        aboutUsContent = new MutableLiveData<>();

        aboutUsContent.setValue("");

    }

    public LiveData<CmsPagesDataResponse> getAboutUsResponse(ProgressDialog progressDialog, Context context
            , AboutUsFragment aboutUsFragment) {

            MutableLiveData<CmsPagesDataResponse> aboutUsResponseMutableLiveData = new MutableLiveData<>();

            ApiClientAuth.getClient(context)
                    .create(ApiInterface.class)
                    .getAboutUsResponse()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CmsPagesDataResponse>() {
                        @Override
                        public void onSuccess(@NonNull CmsPagesDataResponse baseResponse) {
                            if (progressDialog != null){
                                progressDialog.dismiss();
                            }
                            if (baseResponse != null){
                                aboutUsResponseMutableLiveData.setValue(baseResponse);
                            }else {
                                aboutUsResponseMutableLiveData.setValue(null);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (progressDialog != null){
                                progressDialog.dismiss();
                            }

                            Gson gson = new GsonBuilder().create();
                            CmsPagesDataResponse baseResponse = new CmsPagesDataResponse();
                            try {
                                baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), CmsPagesDataResponse.class);

                                aboutUsResponseMutableLiveData.setValue(baseResponse);
                            } catch (Exception exception) {
                                Log.e(TAG, exception.getMessage());
                               /* ((NetworkExceptionListener) aboutUsFragment)
                                        .onNetworkException(0);*/
                            }
                            Log.e(TAG, e.getMessage());
                        }
                    });

            return aboutUsResponseMutableLiveData;

    }

    // TODO: Implement the ViewModel
}