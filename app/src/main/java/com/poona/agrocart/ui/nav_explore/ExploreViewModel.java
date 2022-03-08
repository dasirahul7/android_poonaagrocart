package com.poona.agrocart.ui.nav_explore;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ProgressDialog;
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
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.CategoryResponse;
import com.poona.agrocart.ui.nav_explore.model.ExploreItems;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class ExploreViewModel extends AndroidViewModel {
    private static final String TAG = ExploreViewModel.class.getSimpleName();
    public MutableLiveData<ArrayList<ExploreItems>> arrayListMutableLiveData;

    public ExploreViewModel(@NonNull Application application) {
        super(application);
        arrayListMutableLiveData  = new MutableLiveData<>();
        arrayListMutableLiveData.setValue(null);
    }

    @SuppressLint("CheckResult")
    public LiveData<CategoryResponse> categoryResponseLiveData(ProgressDialog progressDialog,
                                                               HashMap<String, String> hashMap,
                                                               ExploreFragment exploreFragment) {

        MutableLiveData<CategoryResponse> categoryResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(exploreFragment.getContext())
                .create(ApiInterface.class)
                .homeCategoryResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CategoryResponse>() {

                    @Override
                    public void onSuccess(CategoryResponse response) {
                        if (response != null) {
                            progressDialog.dismiss();
                            categoryResponseMutableLiveData.setValue(response);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        CategoryResponse response = new CategoryResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    CategoryResponse.class);

                            categoryResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) exploreFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return categoryResponseMutableLiveData;
    }

}