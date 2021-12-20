package com.poona.agrocart.ui.nav_gallery.gallery_photo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.PhotoGalleryFragmentBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_gallery.Photos;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoGalleryFragment extends BaseFragment {
    private static ArrayList<Photos> photoList = new ArrayList<>();
    private PhotoGalleryFragmentBinding photoFragmentBinding;
    private View photoView;
    public static PhotoGalleryFragment newInstance(MutableLiveData<ArrayList<Photos>> photos) {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        photoList = photos.getValue();
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
        photoFragmentBinding = PhotoGalleryFragmentBinding.inflate(LayoutInflater.from(context));
        photoView = photoFragmentBinding.getRoot();
        return photoView;
    }
}