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
import com.poona.agrocart.data.network.reponses.CmsResponse;

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
}