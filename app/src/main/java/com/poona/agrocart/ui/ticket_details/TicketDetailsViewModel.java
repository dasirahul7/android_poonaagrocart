package com.poona.agrocart.ui.ticket_details;

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
import com.poona.agrocart.data.network.responses.help_center_response.SendMessageResponse;
import com.poona.agrocart.data.network.responses.help_center_response.SendMessageResponse;
import com.poona.agrocart.data.network.responses.help_center_response.recieveMessage.RecieveMessageResponse;
import com.poona.agrocart.ui.nav_faq.model.FaqListResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class TicketDetailsViewModel extends AndroidViewModel {
    private static final String TAG = TicketDetailsViewModel.class.getSimpleName();

    public MutableLiveData<String> ticketId ;
    public MutableLiveData<String> ticketDate ;
    public MutableLiveData<String> status ;
    public MutableLiveData<String> subject ;
    public MutableLiveData<String> remark ;
    public MutableLiveData<String> etMessage;

    public TicketDetailsViewModel(@NonNull Application application) {
        super(application);

        ticketId = new MutableLiveData<>();
        ticketDate = new MutableLiveData<>();
        status = new MutableLiveData<>();
        subject = new MutableLiveData<>();
        remark = new MutableLiveData<>();
        etMessage = new MutableLiveData<>();


        ticketId.setValue("");
        ticketDate.setValue("");
        status.setValue("");
        subject.setValue("");
        remark.setValue("");
        etMessage.setValue("");

    }


    public LiveData<RecieveMessageResponse> getRecieveMessage(ProgressDialog progressDialog, Context context
            , HashMap<String, String> recieveMessageInputParameter, TicketDetailFragment ticketDetailFragment) {

        MutableLiveData<RecieveMessageResponse> recieveMessageResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getReceiveMessage(recieveMessageInputParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RecieveMessageResponse>() {
                    @Override
                    public void onSuccess(@NonNull RecieveMessageResponse baseResponse) {
                        progressDialog.dismiss();
                        if (baseResponse != null){
                            recieveMessageResponseMutableLiveData.setValue(baseResponse);
                        }else {
                            recieveMessageResponseMutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        RecieveMessageResponse baseResponse = new RecieveMessageResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), RecieveMessageResponse.class);

                            recieveMessageResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) ticketDetailFragment).onNetworkException(0,"");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return recieveMessageResponseMutableLiveData;

    }

    public LiveData<SendMessageResponse> sendMessageResponse(ProgressDialog progressDialog, Context context
            , HashMap<String, String> SendMessageParameters, TicketDetailFragment ticketDetailFragment) {

        MutableLiveData<SendMessageResponse> sendMessageResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getSenderMessage(SendMessageParameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SendMessageResponse>() {
                    @Override
                    public void onSuccess(@NonNull SendMessageResponse baseResponse) {
                        progressDialog.dismiss();
                        if (baseResponse != null){
                            sendMessageResponseMutableLiveData.setValue(baseResponse);
                        }else {
                            sendMessageResponseMutableLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        SendMessageResponse baseResponse = new SendMessageResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), SendMessageResponse.class);

                            sendMessageResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) ticketDetailFragment).onNetworkException(0,"");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return sendMessageResponseMutableLiveData;
    }
}
