package com.poona.agrocart.ui.my_profile;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentMyProfileBinding;
import com.poona.agrocart.ui.BaseFragment;

public class MyProfileFragment extends BaseFragment
{
    private FragmentMyProfileBinding fragmentMyProfileBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentMyProfileBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_my_profile, container, false);
        fragmentMyProfileBinding.setLifecycleOwner(this);

        final View view=fragmentMyProfileBinding.getRoot();
        initTitleBar(getString(R.string.my_profile));
        initView(view);

        return view;
    }

    private void initView(View view)
    {

    }
}