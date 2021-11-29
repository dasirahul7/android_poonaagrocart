package com.poona.agrocart.ui.privacy_policy;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentPrivacyPolicyBinding;
import com.poona.agrocart.ui.BaseFragment;

public class PrivacyFragment extends BaseFragment {

    private PrivacyViewModel mViewModel;
    private FragmentPrivacyPolicyBinding privacyPolicyBinding;

    public static PrivacyFragment newInstance() {
        return new PrivacyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        privacyPolicyBinding = FragmentPrivacyPolicyBinding.inflate(getLayoutInflater());
        privacyPolicyBinding.setLifecycleOwner(this);
        View view = privacyPolicyBinding.getRoot();
        initTitleBar(getString(R.string.privacy_policy));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PrivacyViewModel.class);
        // TODO: Use the ViewModel
    }

}