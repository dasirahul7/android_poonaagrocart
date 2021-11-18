package com.poona.agrocart.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CommonViewModel extends AndroidViewModel 
{
    public MutableLiveData<String> mobileNo;
    public MutableLiveData<String> countryCode;
    
    public CommonViewModel(@NonNull Application application)
    {
        super(application);
        mobileNo=new MutableLiveData<>();
        countryCode=new MutableLiveData<>();

        mobileNo.setValue("");
        countryCode.setValue("");
    }

}
