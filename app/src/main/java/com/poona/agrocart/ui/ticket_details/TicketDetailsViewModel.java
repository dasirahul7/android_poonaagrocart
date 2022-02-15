package com.poona.agrocart.ui.ticket_details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class TicketDetailsViewModel extends AndroidViewModel {
    private static final String TAG = TicketDetailsViewModel.class.getSimpleName();

    public MutableLiveData<String> ticketId ;
    public MutableLiveData<String> ticketDate ;
    public MutableLiveData<String> status ;
    public MutableLiveData<String> subject ;
    public MutableLiveData<String> remark ;

    public TicketDetailsViewModel(@NonNull Application application) {
        super(application);

        ticketId = new MutableLiveData<>();
        ticketDate = new MutableLiveData<>();
        status = new MutableLiveData<>();
        subject = new MutableLiveData<>();
        remark = new MutableLiveData<>();


        ticketId.setValue("");
        ticketDate.setValue("");
        status.setValue("");
        subject.setValue("");
        remark.setValue("");

    }
}
