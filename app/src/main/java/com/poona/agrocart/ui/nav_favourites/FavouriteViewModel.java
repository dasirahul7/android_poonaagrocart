package com.poona.agrocart.ui.nav_favourites;

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
import com.poona.agrocart.data.network.responses.favoutiteResponse.FavouriteListResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class FavouriteViewModel extends AndroidViewModel {
    private String TAG = FavouriteViewModel.class.getSimpleName();

    public FavouriteViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<FavouriteListResponse> favouriteLisResponseLiveData(ProgressDialog progressDialog,
                                                                        FavouriteItemsFragment favouriteItemsFragment){
        MutableLiveData<FavouriteListResponse> favouriteLisResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(favouriteItemsFragment.getContext())
                .create(ApiInterface.class)
                .getFavouriteList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FavouriteListResponse>() {
                    @Override
                    public void onSuccess(FavouriteListResponse favouriteLisResponse) {
                        if (favouriteLisResponse!=null){
                            progressDialog.dismiss();
                            favouriteLisResponseMutableLiveData.setValue(favouriteLisResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        FavouriteListResponse response = new FavouriteListResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    FavouriteListResponse.class);

                            favouriteLisResponseMutableLiveData.setValue(response);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
//                            ((NetworkExceptionListener) homeFragment).onNetworkException(1,"");
                        }

                        Log.e(TAG, e.getMessage());
                    }
                });
        return  favouriteLisResponseMutableLiveData;

    }
}
