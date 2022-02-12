package com.poona.agrocart.ui.nav_stores;

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
import com.poona.agrocart.ui.nav_stores.model.OurStoreListResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class OurStoreViewModel extends AndroidViewModel {
    private static final String TAG = OurStoreViewModel.class.getSimpleName();

    public OurStoreViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<OurStoreListResponse> getOurStoreListResponse(ProgressDialog progressDialog
            , Context context, HashMap<String, String> ourStoreInputParameter, OurStoresFragment ourStoresFragment) {

        {
            MutableLiveData<OurStoreListResponse> ourStoreListResponseMutableLiveData = new MutableLiveData<>();

            ApiClientAuth.getClient(context)
                    .create(ApiInterface.class)
                    .getOurStoreList(ourStoreInputParameter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<OurStoreListResponse>() {
                        @Override
                        public void onSuccess(@NonNull OurStoreListResponse baseResponse) {
                            progressDialog.dismiss();

                            if (baseResponse != null){
                                ourStoreListResponseMutableLiveData.setValue(baseResponse);
                            }else {
                                ourStoreListResponseMutableLiveData.setValue(null);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressDialog.dismiss();
                            Gson gson = new GsonBuilder().create();
                            OurStoreListResponse baseResponse = new OurStoreListResponse();
                            try {
                                baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), OurStoreListResponse.class);

                                ourStoreListResponseMutableLiveData.setValue(baseResponse);
                            }  catch (Exception exception) {
                                Log.e(TAG, exception.getMessage());
                               /* ((NetworkExceptionListener) supportTicketFragment)
                                        .onNetworkException(0,"");*/
                            }
                            Log.e(TAG, e.getMessage());
                        }
                    });

            return ourStoreListResponseMutableLiveData;
        }
    }
}
