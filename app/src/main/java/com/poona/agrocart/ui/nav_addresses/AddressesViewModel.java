package com.poona.agrocart.ui.nav_addresses;

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
import com.poona.agrocart.data.network.reponses.AddressesResponse;
import com.poona.agrocart.data.network.reponses.ProfileResponse;
import com.poona.agrocart.ui.nav_profile.MyProfileFragment;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;

public class AddressesViewModel extends AndroidViewModel {

    private String TAG = AddressesViewModel.class.getSimpleName();

    public AddressesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<AddressesResponse> getAddressesResponse(ProgressDialog progressDialog,
                                                            AddressesFragment addressesFragment) {
        MutableLiveData<AddressesResponse> responseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(addressesFragment.getContext())
                .create(ApiInterface.class)
                .getAddressesListResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AddressesResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull AddressesResponse response) {
                        progressDialog.dismiss();
                        responseMutableLiveData.setValue(response);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        AddressesResponse response = new AddressesResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    AddressesResponse.class);

                            responseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) addressesFragment).onNetworkException(0, "");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return responseMutableLiveData;
    }
}