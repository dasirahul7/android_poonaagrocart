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
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.ui.login.CommonViewModel;

public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener{

    private FragmentForgotPasswordBinding fragmentForgotPasswordBinding;
    private CommonViewModel commonViewModel;
    private BasicDetails basicDetails;

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
        fragmentForgotPasswordBinding.btnContinue.setOnClickListener(this);

        commonViewModel=new ViewModelProvider(this).get(CommonViewModel.class);
        fragmentForgotPasswordBinding.setCommonViewModel(commonViewModel);

        fragmentForgotPasswordBinding.ivPoonaAgroMainLogo.bringToFront();

        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        fragmentForgotPasswordBinding.setCommonViewModel(commonViewModel);
        basicDetails=new BasicDetails();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_continue:
                redirectToLoginScreen(v);
                break;
        }
    }

    private void redirectToLoginScreen(View v)
    {
        basicDetails.setEmailId(fragmentForgotPasswordBinding.etEmail.getText().toString());

        int errorCodeForEmail=basicDetails.isValidEmailId();

        if(errorCodeForEmail==0)
        {
            errorToast(requireActivity(),getString(R.string.email_id_should_not_be_empty));
        }
        else if(errorCodeForEmail==1)
        {
            infoToast(requireActivity(),getString(R.string.please_enter_valid_email_id));
        }
        else
        {
            hideKeyBoard(requireActivity());
            if (isConnectingToInternet(context)) {
                //add API call here
            }
            else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }
    }
}