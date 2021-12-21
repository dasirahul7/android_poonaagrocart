package com.poona.agrocart.ui.nav_gallery.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.databinding.VideoGalleryFragmentBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_gallery.adapter.PhotoAdapter;
import com.poona.agrocart.ui.nav_gallery.adapter.VideoAdapter;
import com.poona.agrocart.ui.nav_gallery.model.Videos;
import com.poona.agrocart.ui.nav_gallery.viewModel.VideoViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoGalleryFragment extends BaseFragment {
    private static ArrayList<Videos> videosList = new ArrayList<>();
    private VideoGalleryFragmentBinding videoFragmentBinding;
    private VideoViewModel videoViewModel;
    private View videoView;
    private RecyclerView rvVideo;
    private VideoAdapter videoAdapter;

    public static VideoGalleryFragment newInstance() {
        VideoGalleryFragment fragment = new VideoGalleryFragment();
//        videosList = videoLiveData.getValue();
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
        initViews();
        return videoView;
    }

    private void initViews() {
        videoFragmentBinding = VideoGalleryFragmentBinding.inflate(LayoutInflater.from(context));
        videoView = videoFragmentBinding.getRoot();
        // Initialize ViewModel
        videoViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        //Initialize recyclerView
        rvVideo = videoFragmentBinding.rvVideo;
        //initialize Adapter
        videoAdapter = new VideoAdapter(context,videoViewModel.videoLiveData.getValue());
        // set Recycler with adapter
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        rvVideo.setLayoutManager(gridLayoutManager);
        rvVideo.setAdapter(videoAdapter);
    }

}