package com.poona.agrocart.ui.about_us;

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
import com.poona.agrocart.databinding.FragmentAboutUsBinding;
import com.poona.agrocart.ui.BaseFragment;

public class AboutUsFragment extends BaseFragment {

    private AboutUsViewModel mViewModel;
    private FragmentAboutUsBinding aboutUsBinding;

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        aboutUsBinding = FragmentAboutUsBinding.inflate(getLayoutInflater());
        aboutUsBinding.setLifecycleOwner(this);
        View view = aboutUsBinding.getRoot();
        initTitleBar(getString(R.string.about_us));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AboutUsViewModel.class);
        // TODO: Use the ViewModel
    }

}