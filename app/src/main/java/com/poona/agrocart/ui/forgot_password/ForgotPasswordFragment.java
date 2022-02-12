package com.poona.agrocart.ui.forgot_password;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentForgotPasswordBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.ui.login.CommonViewModel;

public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener {

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

    private void initViews() {
        fragmentForgotPasswordBinding.btnContinue.setOnClickListener(this);
        fragmentForgotPasswordBinding.tvLogIn.setOnClickListener(this);

        setUpTextWatcher();

        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        fragmentForgotPasswordBinding.setCommonViewModel(commonViewModel);

        fragmentForgotPasswordBinding.ivPoonaAgroMainLogo.bringToFront();

        basicDetails = new BasicDetails();
    }

    private void setUpTextWatcher() {
        fragmentForgotPasswordBinding.etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    hideKeyBoard(requireActivity());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                redirectToLoginScreen(v);
                break;
            case R.id.tv_log_in:
                NavHostFragment.findNavController(ForgotPasswordFragment.this).navigate(R.id.action_forgotPasswordFragment_to_LoginFragment);
                break;
        }
    }

    private void redirectToLoginScreen(View v) {
        basicDetails.setMobileNumber(fragmentForgotPasswordBinding.etMobileNumber.getText().toString());
        commonViewModel.mobileNo.setValue(basicDetails.getMobileNumber());

        int errorCodeForPhoneNumber = basicDetails.isValidMobileNumber();

        if (errorCodeForPhoneNumber == 0) {
            errorToast(requireActivity(), getString(R.string.mobile_number_should_not_be_empty));
        } else if (errorCodeForPhoneNumber == 1) {
            infoToast(requireActivity(), getString(R.string.enter_valid_mobile_number));
        } else {
            hideKeyBoard(requireActivity());
            if (isConnectingToInternet(context)) {
                //add API call here
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }
    }
}