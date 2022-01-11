package com.poona.agrocart.ui.nav_gallery.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.R;
import com.poona.agrocart.ui.nav_gallery.model.Photos;
import com.poona.agrocart.ui.nav_gallery.model.Videos;

import java.util.ArrayList;

public class VideoViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<Videos>> videoLiveData = new MutableLiveData<>();
    private ArrayList<Videos> videosList;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        setVideoList();
        videoLiveData.setValue(videosList);
    }

    private void setVideoList() {
        videosList = new ArrayList<Videos>();
        Videos video = new Videos(getApplication().getString(R.string.sample_photo));
        videosList.add(video);
        videosList.add(video);
        videosList.add(video);
        videosList.add(video);
        videosList.add(video);
        videosList.add(video);
        videosList.add(video);
        videosList.add(video);
        videosList.add(video);
        videosList.add(video);
        videosList.add(video);
        videosList.add(video);
    }
}
