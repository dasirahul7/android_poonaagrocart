package com.poona.agrocart.ui.login;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CommonViewModel extends AndroidViewModel 
{
    public MutableLiveData<String> mobileNo;
    public MutableLiveData<String> countryCode;

    public MutableLiveData<String> otp;

    public MutableLiveData<String> userName;
    public MutableLiveData<String> emailId;

    public MutableLiveData<String> city;
    public MutableLiveData<String> area;
    
    public CommonViewModel(@NonNull Application application)
    {
        super(application);

        mobileNo=new MutableLiveData<>();
        countryCode=new MutableLiveData<>();

        otp=new MutableLiveData<>();

        userName=new MutableLiveData<>();
        emailId=new MutableLiveData<>();

        city=new MutableLiveData<>();
        area=new MutableLiveData<>();

        mobileNo.setValue("");
        countryCode.setValue("");

        userName.setValue("");
        emailId.setValue("");

        city.setValue("");
        area.setValue("");
    }
}
