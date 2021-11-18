package com.poona.agrocart.ui.sign_up;

import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.databinding.FragmentSignUpBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.CommonViewModel;

public class SignUpFragment extends BaseFragment implements View.OnClickListener {

    private FragmentSignUpBinding fragmentSignUpBinding;
    private String mobileNo="",countryCode="+91",userName="",emailId="";
    private CommonViewModel commonViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSignUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        fragmentSignUpBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentSignUpBinding).getRoot();

        commonViewModel=new ViewModelProvider(this).get(CommonViewModel.class);
        fragmentSignUpBinding.setCommonViewModel(commonViewModel);

        initView(view);

        ivBack.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });

        return view;
    }

    private void initView(View view)
    {
        hideKeyBoard(getActivity());
        Bundle bundle=this.getArguments();
        if(bundle!=null)
        {
            fragmentSignUpBinding.etPhoneNo.setText(bundle.getString(AppConstants.MOBILE_NO));
            String tempCountryCode=bundle.getString(AppConstants.COUNTRY_CODE).replace("+","");
            fragmentSignUpBinding.countryCodePicker.setCountryForPhoneCode(Integer.parseInt(tempCountryCode));
        }
        fragmentSignUpBinding.tvTermsOfService.setOnClickListener(this);
        fragmentSignUpBinding.tvPrivacyPolicy.setOnClickListener(this);
        fragmentSignUpBinding.btnSignUp.setOnClickListener(this);
        fragmentSignUpBinding.ivPoonaAgroMainLogo.bringToFront();
        Typeface typeFace=Typeface.createFromAsset(getContext().getAssets(),"fonts/poppins/poppins_regular.ttf");
        fragmentSignUpBinding.countryCodePicker.setTypeFace(typeFace);
        fragmentSignUpBinding.countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                TextView title = dialog.findViewById(R.id.textView_title);
                title.setText("Select country");
            }
            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) { }
            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) { }
        });
        fragmentSignUpBinding.countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode=fragmentSignUpBinding.countryCodePicker.getDefaultCountryCodeWithPlus();
            }
        });

        fragmentSignUpBinding.etPhoneNo.addTextChangedListener(new TextWatcher() {
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

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.tv_terms_of_service:
                break;

            case R.id.tv_privacy_policy:
                break;

            case R.id.btn_sign_up:
                userName=fragmentSignUpBinding.etUsername.getText().toString();
                mobileNo=fragmentSignUpBinding.etPhoneNo.getText().toString();
                emailId=fragmentSignUpBinding.etEmailOptional.getText().toString();
                if(checkConditions()) {
                    mobileNo=countryCode+mobileNo;
                    commonViewModel.userName.setValue(userName);
                    commonViewModel.emailId.setValue(emailId);
                    Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_selectLocationFragment);
                }
                break;
        }

    }

    private boolean checkConditions()
    {
        if(TextUtils.isEmpty(userName))
        {
            errorToast(getActivity(),getString(R.string.empty_username));
            return false;
        }
        else if(TextUtils.isEmpty(mobileNo) ||  mobileNo.length()<10)
        {
            errorToast(getActivity(),getString(R.string.invalid_phone_number));
            return false;
        }
        return true;
    }
}