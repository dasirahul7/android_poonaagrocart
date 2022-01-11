package com.poona.agrocart.ui.nav_gallery.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.ui.nav_gallery.model.Photos;
import com.poona.agrocart.ui.nav_gallery.model.Videos;

import java.util.ArrayList;

public class GalleryViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<Photos>> photoLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Videos>> videoLiveData = new MutableLiveData<>();

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        this.photoLiveData.setValue(null);
        this.videoLiveData.setValue(null);
    }
}