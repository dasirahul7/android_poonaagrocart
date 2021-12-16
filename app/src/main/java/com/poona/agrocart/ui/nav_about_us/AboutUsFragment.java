package com.poona.agrocart.ui.nav_about_us;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

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
        initTitleBar(getString(R.string.menu_about_us));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AboutUsViewModel.class);
    }

}