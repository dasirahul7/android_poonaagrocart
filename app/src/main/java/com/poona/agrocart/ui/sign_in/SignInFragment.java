package com.poona.agrocart.ui.sign_in;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.hbb20.CountryCodePicker;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.databinding.FragmentSignInBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.ui.login.CommonViewModel;

public class SignInFragment extends BaseFragment implements View.OnClickListener{

    private FragmentSignInBinding fragmentSignInBinding;
    private CommonViewModel commonViewModel;
    private BasicDetails basicDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSignInBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        fragmentSignInBinding.setLifecycleOwner(this);

        final View view = (fragmentSignInBinding).getRoot();

        initView(view);
        return view;
    }

    private void initView(View view)
    {
        fragmentSignInBinding.ivSignUp.setOnClickListener(this);

        basicDetails=new BasicDetails();

        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        fragmentSignInBinding.setCommonViewModel(commonViewModel);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //set up country code spinner
        setUpCountryCodePicker();

        //hide keyboard after entering mobile number
        setUpTextWatcher();
    }

    public void setUpCountryCodePicker()
    {
        Typeface typeFace=Typeface.createFromAsset(requireContext().getAssets(),getString(R.string.font_poppins_regular));
        fragmentSignInBinding.countryCodePicker.setTypeFace(typeFace);
        basicDetails.setCountryCode(fragmentSignInBinding.countryCodePicker.getDefaultCountryCodeWithPlus());
        commonViewModel.countryCode.setValue(basicDetails.getCountryCode());
        fragmentSignInBinding.countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                TextView title=dialog.findViewById(R.id.textView_title);
                title.setText(R.string.select_country);
            }
            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) { }
            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) { }
        });

        fragmentSignInBinding.countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                basicDetails.setCountryCode(fragmentSignInBinding.countryCodePicker.getSelectedCountryCodeWithPlus());
                commonViewModel.countryCode.setValue(basicDetails.getCountryCode());
            }
        });
    }

    private void setUpTextWatcher()
    {
        fragmentSignInBinding.etPhoneNo.addTextChangedListener(new TextWatcher() {
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_sign_up:
                signInAndRedirectToVerifyOtp(v);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void signInAndRedirectToVerifyOtp(View v)
    {
        basicDetails.setMobileNumber(fragmentSignInBinding.etPhoneNo.getText().toString());
        commonViewModel.mobileNo.setValue(basicDetails.getMobileNumber());

        int errorCodeForMobileNumber=basicDetails.isValidMobileNumber();
        int errorCoedForCountryCode=basicDetails.isValidCountryCode();
        if(errorCodeForMobileNumber==0){
            errorToast(requireActivity(),getString(R.string.mobile_number_should_not_be_empty));
        }
        else if(errorCodeForMobileNumber==1){
            infoToast(requireActivity(),getString(R.string.enter_valid_mobile_number));
        }
        else if(errorCoedForCountryCode==0){
            errorToast(requireActivity(),getString(R.string.please_select_country_code));
        }
        else{
            hideKeyBoard(requireActivity());
            if (isConnectingToInternet(context)) {
                //add API call here
                Bundle bundle=new Bundle();
                bundle.putString(AppConstants.MOBILE_NO,basicDetails.getMobileNumber());
                bundle.putString(AppConstants.COUNTRY_CODE,basicDetails.getCountryCode());
                Navigation.findNavController(v).navigate(R.id.action_signInFragment_to_verifyOtpFragment,bundle);
            }
            else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }
    }
}