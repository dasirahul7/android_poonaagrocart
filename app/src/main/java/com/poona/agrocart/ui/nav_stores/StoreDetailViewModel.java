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
import com.poona.agrocart.ui.nav_stores.model.OurStoreListData;
import com.poona.agrocart.ui.nav_stores.model.OurStoreListResponse;
import com.poona.agrocart.ui.nav_stores.model.Store;
import com.poona.agrocart.ui.nav_stores.model.store_details.OurStoreViewDataResponse;
import com.poona.agrocart.ui.nav_stores.model.store_details.StoreDetail;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class StoreDetailViewModel extends AndroidViewModel {
    private static final String TAG = OurStoreViewModel.class.getSimpleName();
   // public MutableLiveData<StoreDetail> storeMutableLiveData;
    public MutableLiveData<String> storeName;
    public MutableLiveData<String> aboutStore;
    public MutableLiveData<String> contactPersonalNumber;
    public MutableLiveData<String> mobileNumber;
    public MutableLiveData<String> personalAddress;
    public MutableLiveData<String> storeArea;
    public MutableLiveData<String> storeCity;
    public MutableLiveData<String> storeImage;

    public StoreDetailViewModel(Application application) {
        super(application);
        storeName = new MutableLiveData<>();
        aboutStore = new MutableLiveData<>();
        contactPersonalNumber = new MutableLiveData<>();
        mobileNumber = new MutableLiveData<>();
        personalAddress = new MutableLiveData<>();
        storeImage = new MutableLiveData<>();
        storeArea = new MutableLiveData<>();
        storeCity = new MutableLiveData<>();

        storeName.setValue("");
        aboutStore.setValue("");
        contactPersonalNumber.setValue("");
        mobileNumber.setValue("");
        personalAddress.setValue("");
        storeArea.setValue("");
        storeCity.setValue("");
        storeImage.setValue("");

    }

    public LiveData<OurStoreViewDataResponse> getOurStoreDetailsResponse(ProgressDialog progressDialog
            , Context context, HashMap<String, String> ourStoreDetailsInputParameter) {
        {
            MutableLiveData<OurStoreViewDataResponse> ourStoreViewDataResponseMutableLiveData = new MutableLiveData<>();

            ApiClientAuth.getClient(context)
                    .create(ApiInterface.class)
                    .getOurStoreDetails(ourStoreDetailsInputParameter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<OurStoreViewDataResponse>() {
                        @Override
                        public void onSuccess(@NonNull OurStoreViewDataResponse baseResponse) {
                            progressDialog.dismiss();

                            if (baseResponse != null){
                                ourStoreViewDataResponseMutableLiveData.setValue(baseResponse);
                            }else {
                                ourStoreViewDataResponseMutableLiveData.setValue(null);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressDialog.dismiss();
                            Gson gson = new GsonBuilder().create();
                            OurStoreViewDataResponse baseResponse = new OurStoreViewDataResponse();
                            try {
                                baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), OurStoreViewDataResponse.class);

                                ourStoreViewDataResponseMutableLiveData.setValue(baseResponse);
                            }  catch (Exception exception) {
                                Log.e(TAG, exception.getMessage());
                               /* ((NetworkExceptionListener) supportTicketFragment)
                                        .onNetworkException(0);*/
                            }
                            Log.e(TAG, e.getMessage());
                        }
                    });

            return ourStoreViewDataResponseMutableLiveData;
        }
    }
}