package com.poona.agrocart.ui.terms_conditions;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentTermsConditionBinding;
import com.poona.agrocart.ui.BaseFragment;

public class TermsConditionFragment extends BaseFragment {

    private TermsConditionViewModel mViewModel;
    private FragmentTermsConditionBinding termsConditionBinding;

    public static TermsConditionFragment newInstance() {
        return new TermsConditionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        termsConditionBinding = FragmentTermsConditionBinding.inflate(getLayoutInflater());
        termsConditionBinding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(TermsConditionViewModel.class);
        View termsView = termsConditionBinding.getRoot();
        initTitleBar(getString(R.string.terms_conditions));
        return termsView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}