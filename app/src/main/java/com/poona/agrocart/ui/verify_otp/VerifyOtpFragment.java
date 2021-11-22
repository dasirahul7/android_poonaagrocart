package com.poona.agrocart.ui.verify_otp;

import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.databinding.FragmentVerifyOtpBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;

public class VerifyOtpFragment extends BaseFragment implements View.OnClickListener{

    private FragmentVerifyOtpBinding fragmentVerifyOtpBinding;
    private BasicDetails basicDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentVerifyOtpBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_verify_otp,container,false);
        fragmentVerifyOtpBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentVerifyOtpBinding).getRoot();

        initViews(view);

        ivBack.setOnClickListener(v -> {
            hideKeyBoard(requireActivity());
            Navigation.findNavController(view).popBackStack();
        });

        return view;
    }

    private void initViews(View view)
    {
        fragmentVerifyOtpBinding.btnVerifyOtp.setOnClickListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        basicDetails=new BasicDetails();
        enableSoftKeyboard();

        fragmentVerifyOtpBinding.etOtp.requestFocus();

        setUpTextWatcher();
    }

    private void setUpTextWatcher() {
        fragmentVerifyOtpBinding.etOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==4)
                {
                    hideKeyBoard(requireActivity());
                }
            }
        });
    }

    private void enableSoftKeyboard()
    {
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onClick(View v) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        hideKeyBoard(requireActivity());
        Bundle bundle=this.getArguments();
        if(bundle!=null)
        {
            basicDetails.setOtp(fragmentVerifyOtpBinding.etOtp.getText().toString());
            int errorCodeForOtp=basicDetails.isValidOtp();
            if(errorCodeForOtp==0)
            {
                errorToast(requireActivity(),getString(R.string.otp_should_not_be_empty));
            }
            else if(errorCodeForOtp==1)
            {
                infoToast(requireActivity(),getString(R.string.please_enter_valid_otp));
            }
            else
            {
                if (isConnectingToInternet(context)) {
                    //add API call here
                    Navigation.findNavController(v).navigate(R.id.action_verifyOtpFragment_to_signUpFragment,bundle);
                }
                else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        }
    }
}