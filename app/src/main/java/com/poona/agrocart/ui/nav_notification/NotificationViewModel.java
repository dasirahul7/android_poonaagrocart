package com.poona.agrocart.ui.nav_notification;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class NotificationViewModel extends ViewModel {
    private final String sample_notification = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Facilisi semper egestas at cursus maecenas duis enim sit ipsum. Ri amet tristique comm.";
    private final String sample_date = "12 September 2021";
    MutableLiveData<ArrayList<Notification>> arrayListMutableLiveData = new MutableLiveData<>();

    public NotificationViewModel() {
        ArrayList<Notification> notificationList = new ArrayList<>();
        Notification notification = new Notification("1", sample_notification, sample_date);
        for (int i = 0; i < 8; i++)
            notificationList.add(notification);
        arrayListMutableLiveData.setValue(notificationList);
    }
}