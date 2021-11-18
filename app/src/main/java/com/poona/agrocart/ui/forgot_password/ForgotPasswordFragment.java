package com.poona.agrocart.ui.forgot_password;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentForgotPasswordBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.CommonViewModel;

public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener{

    private FragmentForgotPasswordBinding fragmentForgotPasswordBinding;
    private CommonViewModel commonViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentForgotPasswordBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);
        fragmentForgotPasswordBinding.setLifecycleOwner(this);
        final View view = fragmentForgotPasswordBinding.getRoot();

        initViews();

        return view;
    }

    private void initViews()
    {
        fragmentForgotPasswordBinding.imgMainBackground.bringToFront();
        commonViewModel=new ViewModelProvider(this).get(CommonViewModel.class);
        fragmentForgotPasswordBinding.setCommonViewModel(commonViewModel);

        fragmentForgotPasswordBinding.btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_continue:
                break;
        }
    }
}