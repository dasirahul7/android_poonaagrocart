package com.poona.agrocart.ui.sign_in;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.hbb20.CountryCodePicker;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.databinding.FragmentSignInBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.ui.sign_in.model.LoginResponse;
import com.poona.agrocart.ui.sign_in.model.User;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "SignInFragment";
    private FragmentSignInBinding fragmentSignInBinding;
    private BasicDetails basicDetails;
    private View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSignInBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);

        rootView = (fragmentSignInBinding).getRoot();

        initView(rootView);

        return rootView;
    }

    private void initView(View view) {
        fragmentSignInBinding.ivSignUp.setOnClickListener(this);
        fragmentSignInBinding.tvTermsOfService.setOnClickListener(this);
        fragmentSignInBinding.tvPrivacyPolicy.setOnClickListener(this);

        basicDetails = new BasicDetails();


        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();

        //set up country code spinner
        setUpCountryCodePicker();

        //hide keyboard after entering mobile number
        setUpTextWatcher();
    }

    public void setUpCountryCodePicker() {
        Typeface typeFace = Typeface.createFromAsset(requireContext().getAssets(), getString(R.string.font_poppins_regular));
        fragmentSignInBinding.countryCodePicker.setTypeFace(typeFace);

        basicDetails.setCountryCode(fragmentSignInBinding.countryCodePicker.getDefaultCountryCodeWithPlus());

        fragmentSignInBinding.countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                TextView title = dialog.findViewById(R.id.textView_title);
                title.setText(R.string.select_country);
            }

            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {
            }

            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) {
            }
        });

        fragmentSignInBinding.countryCodePicker.setOnCountryChangeListener(() -> {
            basicDetails.setCountryCode(fragmentSignInBinding.countryCodePicker.getSelectedCountryCodeWithPlus());
        });
    }

    private void setUpTextWatcher() {
        fragmentSignInBinding.etPhoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    hideKeyBoard(requireActivity());
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_sign_up:
                signInAndRedirectToVerifyOtp(v);
                break;
            case R.id.tv_terms_of_service:
                redirectToTermsAndCondtn(v);
                break;
            case R.id.tv_privacy_policy:
                redirectToPrivacyPolicy(v);
                break;
        }
    }

    private void redirectToPrivacyPolicy(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("from","SignIn");
        bundle.putString("title",getString(R.string.privacy_policy));
        Navigation.findNavController(v).navigate(R.id.action_signInFragment_to_signInPrivacyFragment,bundle);
    }

    private void redirectToTermsAndCondtn(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("from","SignIn");
        bundle.putString("title",getString(R.string.menu_terms_conditions));
        Navigation.findNavController(v).navigate(R.id.action_signInFragment_to_signInTermsFragment,bundle);
    }

    private void signInAndRedirectToVerifyOtp(View v) {
        //scrollview.fullScroll(View.FOCUS_UP);

        basicDetails.setMobileNumber(Objects.requireNonNull(fragmentSignInBinding.etPhoneNo.getText()).toString());

        int errorCodeForMobileNumber = basicDetails.isValidMobileNumber();
        int errorCoedForCountryCode = basicDetails.isValidCountryCode();
        if (errorCodeForMobileNumber == 0) {
            errorToast(requireActivity(), getString(R.string.mobile_number_should_not_be_empty));
        } else if (errorCodeForMobileNumber == 1) {
            errorToast(requireActivity(), getString(R.string.enter_valid_mobile_number));
        } else if (errorCoedForCountryCode == 0) {
            errorToast(requireActivity(), getString(R.string.please_select_country_code));
        } else {
            hideKeyBoard(requireActivity());
            if (isConnectingToInternet(context)) {
                //add API call here
                loginApi(showCircleProgressDialog(context,""));

            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }
    }

    private void loginApi(ProgressDialog progressDialog) {
        ApiInterface apiInterface = ApiClientAuth.getClient(context).create(ApiInterface.class);
        Call<LoginResponse> responseCall = apiInterface.getLoginResponse(basicDetails.getMobileNumber());
        responseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (progressDialog != null){
                    progressDialog.dismiss();
                }
                if (response.isSuccessful()){
//                    Log.d(TAG, "onResponse: "+response.body().getUser().getuOtp());
                    User user = response.body().getUser();
                    try {
                        System.out.println("User "+user.getuOtp());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    preferences.setAuthorizationToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiNDAiLCJ1c2VydHlwZSI6IjIiLCJ0aW1lU3RhbXAiOiIyMDIyLTAxLTE5IDA4OjA1OjM1In0.tAFCrOniv_PBQWkVb92fJ2eEfuum1GZi3ANTFA1Yg70");
                    Bundle bundle = new Bundle();
                    bundle.putString(AppConstants.MOBILE_NO, basicDetails.getMobileNumber());
                    bundle.putString(AppConstants.COUNTRY_CODE, basicDetails.getCountryCode());
                    Navigation.findNavController(rootView).navigate(R.id.action_signInFragment_to_verifyOtpFragment, bundle);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (progressDialog != null){
                    progressDialog.dismiss();
                }
            }
        });
    }
}