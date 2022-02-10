package com.poona.agrocart.ui.nav_our_privacy;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
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

public class OurPolicyViewModel extends ViewModel {
    private static final String TAG = OurPolicyViewModel.class.getSimpleName();

   public MutableLiveData<String> policyData1;
   public MutableLiveData<String> policyData2;
   public MutableLiveData<String> policyData3;
   public MutableLiveData<String> privacyPolicyData;

    public OurPolicyViewModel() {
        policyData1 = new MutableLiveData<>();
        policyData2 = new MutableLiveData<>();
        policyData3 = new MutableLiveData<>();
        privacyPolicyData = new MutableLiveData<>();
        policyData1.setValue(null);
        policyData2.setValue(null);
        policyData3.setValue(null);
        privacyPolicyData.setValue(null);
    }


    /*Privacy Policy*/
    public LiveData<CmsPagesDataResponse> getPrivacyPolicyResponse(ProgressDialog progressDialog, Context context, PrivacyPolicyFragment privacyPolicyFragment) {
        MutableLiveData<CmsPagesDataResponse> cmsPagesDataResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getPrivacyPolicyResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CmsPagesDataResponse>() {
                    @Override
                    public void onSuccess(@NonNull CmsPagesDataResponse baseResponse) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                        if (baseResponse != null){
                            cmsPagesDataResponseMutableLiveData.setValue(baseResponse);
                        }else {
                            cmsPagesDataResponseMutableLiveData.setValue(null);
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

                            cmsPagesDataResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                               /* ((NetworkExceptionListener) aboutUsFragment)
                                        .onNetworkException(0);*/
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return cmsPagesDataResponseMutableLiveData;
    }

    /*Terms And Condition*/
    public LiveData<CmsPagesDataResponse> getTermsConditionResponse(ProgressDialog progressDialog
            , Context context, TermsConditionFragment termsConditionFragment) {

        MutableLiveData<CmsPagesDataResponse> cmsPagesDataResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getTermsConditionResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CmsPagesDataResponse>() {
                    @Override
                    public void onSuccess(@NonNull CmsPagesDataResponse baseResponse) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                        if (baseResponse != null){
                            cmsPagesDataResponseMutableLiveData.setValue(baseResponse);
                        }else {
                            cmsPagesDataResponseMutableLiveData.setValue(null);
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

                            cmsPagesDataResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                               /* ((NetworkExceptionListener) aboutUsFragment)
                                        .onNetworkException(0);*/
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return cmsPagesDataResponseMutableLiveData;
    }

    public LiveData<CmsPagesDataResponse> getReturnRefundResponse(ProgressDialog progressDialog, Context context, ReturnRefundFragment returnRefundFragment) {

        MutableLiveData<CmsPagesDataResponse> cmsPagesDataResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getReturnRefundResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CmsPagesDataResponse>() {
                    @Override
                    public void onSuccess(@NonNull CmsPagesDataResponse baseResponse) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                        if (baseResponse != null){
                            cmsPagesDataResponseMutableLiveData.setValue(baseResponse);
                        }else {
                            cmsPagesDataResponseMutableLiveData.setValue(null);
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

                            cmsPagesDataResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                               /* ((NetworkExceptionListener) aboutUsFragment)
                                        .onNetworkException(0);*/
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return cmsPagesDataResponseMutableLiveData;
    }
}