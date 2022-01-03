package com.poona.agrocart.ui.nav_faq;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentFaqBinding;
import com.poona.agrocart.ui.BaseFragment;

public class FaQFragment extends BaseFragment {

    private FaQViewModel mViewModel;
    private FragmentFaqBinding fragmentFaqBinding;
    private View faQView;

    public static FaQFragment newInstance() {
        return new FaQFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentFaqBinding = FragmentFaqBinding.inflate(LayoutInflater.from(context));
        faQView = fragmentFaqBinding.getRoot();
        initViews();
        return faQView;
    }

    private void initViews() {
        initTitleBar(getString(R.string.menu_faq));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FaQViewModel.class);
        // TODO: Use the ViewModel
    }

}