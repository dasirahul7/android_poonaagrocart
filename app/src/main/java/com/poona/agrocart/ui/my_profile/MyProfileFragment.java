package com.poona.agrocart.ui.my_profile;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentMyProfileBinding;

public class MyProfileFragment extends Fragment {

    private FragmentMyProfileBinding fragmentMyProfileBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        fragmentMyProfileBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_my_profile, container, false);
        fragmentMyProfileBinding.setLifecycleOwner(this);

        final View view=fragmentMyProfileBinding.getRoot();

        return view;
    }
}