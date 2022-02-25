package com.poona.agrocart.ui.nav_notification;

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
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.help_center_response.TicketTypeResponse;
import com.poona.agrocart.data.network.responses.notification.NotificationListResponse;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class NotificationViewModel extends ViewModel {

    private static final String TAG = NotificationViewModel.class.getSimpleName();
    MutableLiveData<ArrayList<NotificationListResponse.NotificationList>> arrayListMutableLiveData = new MutableLiveData<>();

    public LiveData<NotificationListResponse> getNotification(ProgressDialog progressDialog, Context context, HashMap<String, String> notificationInputParameter
            , NotificationFragment notificationFragment) {

        MutableLiveData<NotificationListResponse> notificationListResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getNotification(notificationInputParameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NotificationListResponse>() {
                    @Override
                    public void onSuccess(@NonNull NotificationListResponse baseResponse) {
                        progressDialog.dismiss();
                        notificationListResponseMutableLiveData.setValue(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();

                        Gson gson = new GsonBuilder().create();
                        NotificationListResponse baseResponse = new NotificationListResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), NotificationListResponse.class);

                            notificationListResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) notificationFragment).onNetworkException(0, "");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return notificationListResponseMutableLiveData;
    }

    /*public NotificationViewModel() {
        ArrayList<Notification> notificationList = new ArrayList<>();
        Notification notification = new Notification("1", sample_notification, sample_date);
        for (int i = 0; i < 8; i++)
            notificationList.add(notification);
        arrayListMutableLiveData.setValue(notificationList);
    }*/
}