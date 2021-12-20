package com.poona.agrocart.ui.nav_gallery;

import android.provider.ContactsContract;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class GalleryViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Photos>> photoLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Videos>> videoLiveData = new MutableLiveData<>();

    public GalleryViewModel(MutableLiveData<ArrayList<Photos>> photoLiveData,
                            MutableLiveData<ArrayList<Videos>> videoLiveData) {
        this.photoLiveData = photoLiveData;
        this.videoLiveData = videoLiveData;
    }
}