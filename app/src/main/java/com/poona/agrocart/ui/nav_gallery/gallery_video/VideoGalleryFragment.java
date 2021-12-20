package com.poona.agrocart.ui.nav_gallery.gallery_video;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.VideoGalleryFragmentBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_gallery.Videos;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoGalleryFragment extends BaseFragment {
    private static ArrayList<Videos> videosList = new ArrayList<>();
    private View videoView;
    private VideoGalleryFragmentBinding videoFragmentBinding;
    public static VideoGalleryFragment newInstance(MutableLiveData<ArrayList<Videos>> videoLiveData) {
        VideoGalleryFragment fragment = new VideoGalleryFragment();
        videosList = videoLiveData.getValue();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        videoFragmentBinding = VideoGalleryFragmentBinding.inflate(LayoutInflater.from(context));
        videoView = videoFragmentBinding.getRoot();
        return videoView;
    }
}