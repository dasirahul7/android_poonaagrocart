package com.poona.agrocart.ui.nav_gallery.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.databinding.FragmentPhotoBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_gallery.model.Photos;
import com.poona.agrocart.ui.nav_gallery.adapter.PhotoAdapter;
import com.poona.agrocart.ui.nav_gallery.viewModel.PhotoViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoGalleryFragment extends BaseFragment {
    private static ArrayList<Photos> photoList = new ArrayList<>();
    private FragmentPhotoBinding fragmentPhotoBinding;
    private PhotoViewModel photoViewModel;
    private View photoView;
    private RecyclerView rvPhoto;
    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
//        photoList = photos.getValue();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initViews();
        setGallery();
        return photoView;
    }

    private void initViews() {
        fragmentPhotoBinding = FragmentPhotoBinding.inflate(LayoutInflater.from(context));
        photoView = fragmentPhotoBinding.getRoot();
        rvPhoto = fragmentPhotoBinding.rvPhoto;
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
    }

    private void setGallery() {
        PhotoAdapter photoAdapter = new PhotoAdapter(getActivity(),photoViewModel.photoLiveData.getValue());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        rvPhoto.setLayoutManager(gridLayoutManager);
        rvPhoto.setAdapter(photoAdapter);
    }

}