package com.poona.agrocart.ui.forgot_password;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentForgotPasswordBinding;
import com.poona.agrocart.ui.BaseFragment;

public class ForgotPasswordFragment extends BaseFragment {

    private FragmentForgotPasswordBinding fragmentForgotPasswordBinding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentForgotPasswordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);
        fragmentForgotPasswordBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentForgotPasswordBinding).getRoot();

        initViews();

        return view;
    }

    private void initViews()
    {
        fragmentForgotPasswordBinding.imgMainBackground.bringToFront();
    }
}