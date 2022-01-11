package com.poona.agrocart.ui.nav_profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MyProfileViewModel extends AndroidViewModel
{
    public MutableLiveData<String> name;
    public MutableLiveData<String> mobileNo;
    public MutableLiveData<String> alternateMobileNo;
    public MutableLiveData<String> emailId;
    public MutableLiveData<String> state;
    public MutableLiveData<String> city;
    public MutableLiveData<String> area;
    public MutableLiveData<String> gender;
    public MutableLiveData<String> dateOfBirth;

    public MyProfileViewModel(@NonNull Application application)
    {
        super(application);

        name=new MutableLiveData<>();
        mobileNo=new MutableLiveData<>();
        alternateMobileNo=new MutableLiveData<>();
        emailId=new MutableLiveData<>();
        state=new MutableLiveData<>();
        city=new MutableLiveData<>();
        area=new MutableLiveData<>();
        gender=new MutableLiveData<>();
        dateOfBirth=new MutableLiveData<>();

        name.setValue("");
        mobileNo.setValue("");
        alternateMobileNo.setValue("");
        emailId.setValue("");
        state.setValue("");
        city.setValue("");
        area.setValue("");
        gender.setValue("");
        dateOfBirth.setValue("");
    }
}
