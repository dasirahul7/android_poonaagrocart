package com.poona.agrocart.ui.verify_otp;

import static com.poona.agrocart.app.AppConstants.OTP;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.VerifyOtpResponse;
import com.poona.agrocart.databinding.FragmentVerifyOtpBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VerifyOtpFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {

    private static final String TAG =VerifyOtpFragment.class.getSimpleName() ;
    private FragmentVerifyOtpBinding fragmentVerifyOtpBinding;
    private BasicDetails basicDetails;
//    private CommonViewModel commonViewModel;
    private VerifyOtpViewModel verifyOtpViewModel;
    private View verifyView;
    private Bundle bundle;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentVerifyOtpBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_verify_otp,container,false);
        fragmentVerifyOtpBinding.setLifecycleOwner(this);
        verifyView = fragmentVerifyOtpBinding.getRoot();

        initViews(verifyView);

        ivBack.setOnClickListener(v -> {
            hideKeyBoard(requireActivity());
            Navigation.findNavController(verifyView).popBackStack();
        });

        return verifyView;
    }

    private void initViews(View view)
    {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), getString(R.string.font_poppins_regular));
        fragmentVerifyOtpBinding.etOtp.setTypeface(font);

        fragmentVerifyOtpBinding.btnVerifyOtp.setOnClickListener(this);

//        commonViewModel=new ViewModelProvider(this).get(CommonViewModel.class);
        verifyOtpViewModel=new ViewModelProvider(this).get(VerifyOtpViewModel.class);
        try {
            // Mask Phone number
            bundle =this.getArguments();
            String phone = getArguments().getString(AppConstants.MOBILE_NO);
            String otp = getArguments().getString(AppConstants.USER_OTP);
            String strPattern = "\\d(?=\\d{3})";
            basicDetails=new BasicDetails();
            basicDetails.setOtp(otp);
            String phoneNumber = "number:"+phone.replaceAll(strPattern, "*" );
            basicDetails.setMobileNumber(phoneNumber);
            verifyOtpViewModel.userMobileMsg.setValue(context.getString(R.string.otp_sent)+" "+basicDetails.getMobileNumber());
//            commonViewModel.otpMobileMsg.setValue(getString(R.string.otp_sent)+phone.replaceAll(strPattern, "*"));
            fragmentVerifyOtpBinding.etOtp.requestFocus();
            fragmentVerifyOtpBinding.etOtp.setCursorVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentVerifyOtpBinding.setVerifyOtpViewModel(verifyOtpViewModel);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

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
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onClick(View v) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        hideKeyBoard(requireActivity());
        if(bundle!=null)
        {
            basicDetails.setOtp(Objects.requireNonNull(fragmentVerifyOtpBinding.etOtp.getText()).toString());
            int errorCodeForOtp=basicDetails.isValidOtp();
            if(errorCodeForOtp==0)
            {
                errorToast(requireActivity(),getString(R.string.otp_should_not_be_empty));
            }
            else if(errorCodeForOtp==1)
            {
                errorToast(requireActivity(),getString(R.string.please_enter_valid_otp));
            }
            else
            {
                if (isConnectingToInternet(context)) {
                    //add API call here
                    callVerifyOtpApi(showCircleProgressDialog(context,""));
                }
                else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        }
    }

    private void callVerifyOtpApi(ProgressDialog progressDialog) {
        /* print user input parameters */
        for (Map.Entry<String, String> entry : verifyOtpParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        Observer<VerifyOtpResponse> verifyOtpResponseObserver = verifyOtpResponse -> {
            if (verifyOtpResponse != null) {
                progressDialog.dismiss();
                Log.e("Sign In Api Response", new Gson().toJson(verifyOtpResponse));
                switch (verifyOtpResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if(verifyOtpResponse.getMessage() != null){
                            successToast(context, ""+verifyOtpResponse.getMessage());
                            Navigation.findNavController(verifyView).navigate(R.id.action_verifyOtpFragment_to_signUpFragment,bundle);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, verifyOtpResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(verifyOtpResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, verifyOtpResponse.getMessage());
                        break;
                }
            } else {
                progressDialog.dismiss();
            }
        };

        verifyOtpViewModel.submitVerifyOtp(progressDialog,verifyOtpParameters(),VerifyOtpFragment.this)
                .observe(getViewLifecycleOwner(),verifyOtpResponseObserver);
    }

    private HashMap<String, String> verifyOtpParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(OTP, fragmentVerifyOtpBinding.etOtp.getText().toString());
        return map;
    }

    @Override
    public void onNetworkException(int from) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), VerifyOtpFragment.this,() -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if(from == 0) {
                    callVerifyOtpApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}