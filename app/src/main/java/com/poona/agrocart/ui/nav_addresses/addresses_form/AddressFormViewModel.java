package com.poona.agrocart.ui.nav_addresses.addresses_form;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class AddressFormViewModel extends AndroidViewModel
{
    public MutableLiveData<String> name;
    public MutableLiveData<String> mobile;
    public MutableLiveData<String> city;
    public MutableLiveData<String> area;
    public MutableLiveData<String> pincode;
    public MutableLiveData<String> houseNumber;
    public MutableLiveData<String> apartmentNumber;
    public MutableLiveData<String> street;
    public MutableLiveData<String> landmark;

    public AddressFormViewModel(@NonNull Application application)
    {
        super(application);

        name=new MutableLiveData<>();
        mobile=new MutableLiveData<>();
        city=new MutableLiveData<>();
        area=new MutableLiveData<>();
        pincode=new MutableLiveData<>();
        houseNumber=new MutableLiveData<>();
        apartmentNumber=new MutableLiveData<>();
        street=new MutableLiveData<>();
        landmark=new MutableLiveData<>();

        name.setValue("");
        mobile.setValue("");
        city.setValue("");
        area.setValue("");
        pincode.setValue("");
        houseNumber.setValue("");
        apartmentNumber.setValue("");
        street.setValue("");
        landmark.setValue("");
    }
}
