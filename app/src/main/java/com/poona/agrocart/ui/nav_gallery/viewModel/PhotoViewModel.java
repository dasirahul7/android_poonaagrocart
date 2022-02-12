package com.poona.agrocart.ui.nav_gallery.viewModel;

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
import com.poona.agrocart.data.network.reponses.GalleryResponse;
import com.poona.agrocart.ui.nav_gallery.fragment.PhotoGalleryFragment;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class PhotoViewModel extends AndroidViewModel {
    private static final String TAG = PhotoViewModel.class.getSimpleName();


    public PhotoViewModel(@NonNull Application application) {
        super(application);
    }



    public LiveData<GalleryResponse> getGalleryImageResponse(ProgressDialog progressDialog, Context context
            , PhotoGalleryFragment photoGalleryFragment) {

        MutableLiveData<GalleryResponse> galleryResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getGalleryReponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GalleryResponse>() {
                    @Override
                    public void onSuccess(@NonNull GalleryResponse baseResponse) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                        if (baseResponse != null){
                            galleryResponseMutableLiveData.setValue(baseResponse);
                        }else {
                            galleryResponseMutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }

                        Gson gson = new GsonBuilder().create();
                        GalleryResponse baseResponse = new GalleryResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), GalleryResponse.class);

                            if (baseResponse != null){
                                galleryResponseMutableLiveData.setValue(baseResponse);
                            }else {
                                galleryResponseMutableLiveData.setValue(null);
                            }
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) photoGalleryFragment)
                                    .onNetworkException(0,"");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return galleryResponseMutableLiveData;
    }
}
