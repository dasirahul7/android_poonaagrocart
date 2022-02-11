package com.poona.agrocart.ui.sign_in;

import static com.poona.agrocart.app.AppConstants.CMS_NAME;
import static com.poona.agrocart.app.AppConstants.CMS_TYPE;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.MOBILE_NUMBER;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_402;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.databinding.FragmentSignInBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.data.network.reponses.SignInResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignInFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {
    private static final String TAG = SignInFragment.class.getSimpleName();
    private SignInViewModel signInViewModel;
    private FragmentSignInBinding fragmentSignInBinding;
    private BasicDetails basicDetails;
    private View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSignInBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        fragmentSignInBinding.setSignInViewModel(signInViewModel);

        fragmentSignInBinding.setLifecycleOwner(this);

        rootView = fragmentSignInBinding.getRoot();
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
                redirectToCmsFragment(1); //Terms & Condition
                break;
            case R.id.tv_privacy_policy:
                redirectToCmsFragment(2); //Privacy Policy
                break;
        }
    }

    private void redirectToCmsFragment(int from) {
        Bundle bundle = new Bundle();
        bundle.putString(FROM_SCREEN, TAG);
        bundle.putInt(CMS_TYPE, from);
        Navigation.findNavController(rootView).navigate(R.id.action_signInFragment_to_cmsFragment, bundle);
    }

    private void signInAndRedirectToVerifyOtp(View v) {
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
                callSignInApi(showCircleProgressDialog(context,""));
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }
    }

    private void callSignInApi(ProgressDialog progressDialog) {
        /* print user input parameters */
        for (Map.Entry<String, String> entry : signInParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        Observer<SignInResponse> signInResponseObserver = signInResponse -> {
            if (signInResponse != null) {
                progressDialog.dismiss();
                Log.e("Sign In Api Response", new Gson().toJson(signInResponse));
                switch (signInResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if(signInResponse.getUser() != null){
                            successToast(context, ""+signInResponse.getUser().getOtp());
                            basicDetails.setOtp(signInResponse.getUser().getOtp());
                            preferences.setAuthorizationToken(signInResponse.getToken());
                            basicDetails.setUserId(signInResponse.getUser().getId());
                            Bundle bundle = new Bundle();
                            bundle.putString(AppConstants.USER_MOBILE, basicDetails.getMobileNumber());
                            bundle.putString(AppConstants.COUNTRY_CODE, basicDetails.getCountryCode());
                            bundle.putString(AppConstants.USER_OTP, basicDetails.getOtp());
                            bundle.putString(AppConstants.USER_ID, basicDetails.getUserId());
                            Navigation.findNavController(rootView).navigate(R.id.action_signInFragment_to_verifyOtpFragment, bundle);
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_402://Validation Errors
                        goToAskAndDismiss(signInResponse.getMessage(), context);
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, signInResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(signInResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, signInResponse.getMessage());
                        break;
                }
            } else {
                progressDialog.dismiss();
            }
        };

        signInViewModel
                .submitSignInDetails(progressDialog, signInParameters(), SignInFragment.this)
                .observe(getViewLifecycleOwner(), signInResponseObserver);
    }

    private HashMap<String, String> signInParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(MOBILE_NUMBER, basicDetails.getMobileNumber());
        return map;
    }

    @Override
    public void onNetworkException(int from) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), SignInFragment.this,() -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if(from == 0) {
                    callSignInApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}