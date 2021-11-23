package com.poona.agrocart.ui.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentLogInBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class LogInFragment extends BaseFragment implements View.OnClickListener {

    private FragmentLogInBinding fragmentLogInBinding;
    private boolean showPassword=false;
    private CommonViewModel commonViewModel;
    private BasicDetails basicDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLogInBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_log_in, container, false);
        fragmentLogInBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentLogInBinding).getRoot();

        initViews(view);

        return view;
    }

    private void initViews(View view)
    {
        fragmentLogInBinding.tvForgotPassword.setOnClickListener(this);
        fragmentLogInBinding.ivShowHidePassword.setOnClickListener(this);
        fragmentLogInBinding.tvSignUp.setOnClickListener(this);

        basicDetails=new BasicDetails();
        fragmentLogInBinding.ivPoonaAgroMainLogo.bringToFront();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        fragmentLogInBinding.setCommonViewModel(commonViewModel);

        setUpCountryCodePicker();

        setUpTextWatcher();
    }

    private void setUpTextWatcher()
    {
        fragmentLogInBinding.etMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10)
                {
                    hideKeyBoard(requireActivity());
                }
            }
        });
    }

    private void setUpCountryCodePicker()
    {
        Typeface typeFace=Typeface.createFromAsset(getContext().getAssets(),"fonts/poppins/poppins_regular.ttf");
        fragmentLogInBinding.countryCodePicker.setTypeFace(typeFace);
        fragmentLogInBinding.countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                TextView title =(TextView)  dialog.findViewById(R.id.textView_title);
                title.setText("Select country");
            }
            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {
            }
            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_show_hide_password:
                toogleShowAndHidePassword();
                break;
            case R.id.tv_sign_up:
                Navigation.findNavController(v).navigate(R.id.action_LogInFragment_to_SignInFragment);
                break;
            case R.id.tv_forgot_password:
                Navigation.findNavController(v).navigate(R.id.action_LoginFragment_to_forgotPasswordFragment);
                break;
            case R.id.btn_login:
                redirectToDashboard(v);
                break;
        }
    }

    private void redirectToDashboard(View v)
    {
        commonViewModel.mobileNo.setValue(fragmentLogInBinding.etMobileNo.getText().toString());

        basicDetails.setMobileNumber(commonViewModel.mobileNo.getValue());
        basicDetails.setPassword(fragmentLogInBinding.etPassoword.getText().toString());

        int errorCodeForMobileNumber=basicDetails.isValidMobileNumber();
        int errorCodeForPassword=basicDetails.isValidPassword();
        if(errorCodeForMobileNumber==0){
            errorToast(requireActivity(),getString(R.string.mobile_number_should_not_be_empty));
        }
        else if(errorCodeForMobileNumber==1){
            infoToast(requireActivity(),getString(R.string.enter_valid_mobile_number));
        }
        else if(errorCodeForPassword==0){
            errorToast(requireActivity(),getString(R.string.password_should_not_be_empty));
        }
        else{
            hideKeyBoard(requireActivity());
            if (isConnectingToInternet(context)) {
                //add API call here
                successToast(context,"DONE");
                Intent intent = new Intent(context, HomeActivity.class);
                startActivity(intent);
            }
            else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }
    }

    private void toogleShowAndHidePassword()
    {
        if(!showPassword) {
            fragmentLogInBinding.etPassoword.setTransformationMethod(null);
            fragmentLogInBinding.ivShowHidePassword.setImageResource(R.drawable.ic_password_show);
        }
        else {
            fragmentLogInBinding.etPassoword.setTransformationMethod(new PasswordTransformationMethod());
            fragmentLogInBinding.ivShowHidePassword.setImageResource(R.drawable.ic_password_hide);
        }
        showPassword=!showPassword;
        fragmentLogInBinding.etPassoword.setSelection(fragmentLogInBinding.etPassoword.getText().length());
    }
}