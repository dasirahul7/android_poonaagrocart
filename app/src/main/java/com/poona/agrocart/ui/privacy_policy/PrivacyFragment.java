package com.poona.agrocart.ui.privacy_policy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentTermsConditionBinding;
import com.poona.agrocart.ui.BaseFragment;

public class PrivacyFragment extends BaseFragment {

    private PrivacyViewModel mViewModel;
    private FragmentTermsConditionBinding privacyPolicyBinding;

    public static PrivacyFragment newInstance() {
        return new PrivacyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        privacyPolicyBinding = FragmentTermsConditionBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(PrivacyViewModel.class);
        privacyPolicyBinding.setLifecycleOwner(this);
        View view = privacyPolicyBinding.getRoot();
        initTitleBar(getString(R.string.privacy_policy));
        return view;
    }
}