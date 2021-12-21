package com.poona.agrocart.ui.nav_gallery.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.R;
import com.poona.agrocart.ui.nav_gallery.model.Photos;

import java.util.ArrayList;

public class PhotoViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<Photos>> photoLiveData = new MutableLiveData<>();
    private ArrayList<Photos> photoList;

    public PhotoViewModel(@NonNull Application application) {
        super(application);
        setPhotoList();
        photoLiveData.setValue(photoList);
    }

    private void setPhotoList() {
        photoList = new ArrayList<>();
        Photos photo = new Photos(getApplication().getString(R.string.sample_photo));
//        Photos photo = new Photos(getString(R.string.sample_about_store));
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
        photoList.add(photo);
    }
}
