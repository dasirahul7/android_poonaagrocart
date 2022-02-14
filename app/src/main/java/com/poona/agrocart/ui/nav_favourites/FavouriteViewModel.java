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
import com.poona.agrocart.data.network.reponses.favoutiteResponse.FavouriteLisResponse;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class FavouriteViewModel extends AndroidViewModel {
    private String TAG = FavouriteViewModel.class.getSimpleName();

    public FavouriteViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<FavouriteLisResponse> favouriteLisResponseLiveData(ProgressDialog progressDialog,
                                                                       FavouriteItemsFragment favouriteItemsFragment){
        MutableLiveData<FavouriteLisResponse> favouriteLisResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(favouriteItemsFragment.getContext())
                .create(ApiInterface.class)
                .getFavouriteList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FavouriteLisResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull FavouriteLisResponse favouriteLisResponse) {
                        if (favouriteLisResponse!=null){
                            progressDialog.dismiss();
                            Log.e(TAG, "onSuccess: "+new Gson().toJson(favouriteLisResponse));
                            favouriteLisResponseMutableLiveData.setValue(favouriteLisResponse);
                            if (favouriteLisResponse.getFavouriteList()!=null){
                                Log.e(TAG, "onSuccess: "+favouriteLisResponse.getFavouriteList().size() );
                            }
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        FavouriteLisResponse response = new FavouriteLisResponse();
                        try {
                            response = gson.fromJson(((HttpException) e).response().errorBody().string(),
                                    FavouriteLisResponse.class);

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
