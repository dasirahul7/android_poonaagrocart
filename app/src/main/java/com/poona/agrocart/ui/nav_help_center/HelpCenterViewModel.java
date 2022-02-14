package com.poona.agrocart.ui.nav_help_center;

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
import com.poona.agrocart.data.network.reponses.help_center_response.CreateTicketResponse;
import com.poona.agrocart.data.network.reponses.help_center_response.TicketListResponse;
import com.poona.agrocart.data.network.reponses.help_center_response.TicketTypeResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class HelpCenterViewModel extends AndroidViewModel {
    private static final String TAG = HelpCenterViewModel.class.getSimpleName();

    public HelpCenterViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<TicketTypeResponse> getTicketTypeResponse(ProgressDialog progressDialog, Context context
            , HelpCenterFragment helpCenterFragment) {

        MutableLiveData<TicketTypeResponse> ticketTypeResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getTicketType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<TicketTypeResponse>() {
                    @Override
                    public void onSuccess(@NonNull TicketTypeResponse baseResponse) {
                        progressDialog.dismiss();
                        if (baseResponse != null){
                            ticketTypeResponseMutableLiveData.setValue(baseResponse);
                        }else {
                            ticketTypeResponseMutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        TicketTypeResponse baseResponse = new TicketTypeResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), TicketTypeResponse.class);

                            ticketTypeResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                           /* Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) faQFragment).onNetworkException(0,"");*/
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return ticketTypeResponseMutableLiveData;
    }

    public LiveData<TicketListResponse> getTicketListResponse(ProgressDialog progressDialog, Context context,
                                                              HashMap<String, String> ticketListInputParameter, HelpCenterFragment helpCenterFragment) {

        MutableLiveData<TicketListResponse> ticketListResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getTicketList(ticketListInputParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<TicketListResponse>() {
                    @Override
                    public void onSuccess(@NonNull TicketListResponse baseResponse) {
                        progressDialog.dismiss();
                        if (baseResponse != null){
                            ticketListResponseMutableLiveData.setValue(baseResponse);
                        }else {
                            ticketListResponseMutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        TicketListResponse baseResponse = new TicketListResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), TicketListResponse.class);

                            ticketListResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                           /* Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) faQFragment).onNetworkException(0,"");*/
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return ticketListResponseMutableLiveData;
    }

    public LiveData<CreateTicketResponse> getCreateTicketResponse(ProgressDialog progressDialog, Context context
            , HashMap<String, String> createTicketInputParameter, HelpCenterFragment helpCenterFragment) {
        MutableLiveData<CreateTicketResponse> createTicketResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getCreateTicket(createTicketInputParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CreateTicketResponse>() {
                    @Override
                    public void onSuccess(@NonNull CreateTicketResponse baseResponse) {
                        progressDialog.dismiss();
                        if (baseResponse != null){
                            createTicketResponseMutableLiveData.setValue(baseResponse);
                        }else {
                            createTicketResponseMutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        CreateTicketResponse baseResponse = new CreateTicketResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), CreateTicketResponse.class);

                            createTicketResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                           /* Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) faQFragment).onNetworkException(0,"");*/
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return createTicketResponseMutableLiveData;
    }
}
