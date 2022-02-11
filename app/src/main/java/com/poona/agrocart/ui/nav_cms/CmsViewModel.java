package com.poona.agrocart.ui.nav_cms;

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
import com.poona.agrocart.data.network.reponses.CmsResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class CmsViewModel extends AndroidViewModel {

    private static final String TAG = CmsViewModel.class.getSimpleName();

    public MutableLiveData<String> aboutUsContent;

    public CmsViewModel(@NonNull Application application) {
        super(application);

        aboutUsContent = new MutableLiveData<>();

        aboutUsContent.setValue("");

    }

    public LiveData<CmsResponse> getCmsResponse(ProgressDialog progressDialog,
                                                Context context,
                                                CmsFragment cmsFragment) {

            MutableLiveData<CmsResponse> aboutUsResponseMutableLiveData = new MutableLiveData<>();

            ApiClientAuth.getClient(context)
                    .create(ApiInterface.class)
                    .getCmsResponse()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CmsResponse>() {
                        @Override
                        public void onSuccess(@NonNull CmsResponse baseResponse) {
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
                            CmsResponse baseResponse = new CmsResponse();
                            try {
                                baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), CmsResponse.class);

                                aboutUsResponseMutableLiveData.setValue(baseResponse);
                            } catch (Exception exception) {
                                Log.e(TAG, exception.getMessage());
                               /* ((NetworkExceptionListener) cmsFragment)
                                        .onNetworkException(0);*/
                            }
                            Log.e(TAG, e.getMessage());
                        }
                    });

            return aboutUsResponseMutableLiveData;

    }
}