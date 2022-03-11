package com.poona.agrocart.ui.verify_otp;

import static com.poona.agrocart.app.AppConstants.COUNTRY_CODE;
import static com.poona.agrocart.app.AppConstants.OTP;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.USER_MOBILE;
import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

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
import com.poona.agrocart.data.network.responses.SignInResponse;
import com.poona.agrocart.data.network.responses.VerifyOtpResponse;
import com.poona.agrocart.databinding.FragmentVerifyOtpBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.login.BasicDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VerifyOtpFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {

    private static final String TAG = VerifyOtpFragment.class.getSimpleName();
    private static final int VERIFY = 0;
    private static final int RESEND = 1;
    private FragmentVerifyOtpBinding fragmentVerifyOtpBinding;
    private BasicDetails basicDetails;
    //    private CommonViewModel commonViewModel;
    private VerifyOtpViewModel verifyOtpViewModel;
    private View verifyView;
    private Bundle bundle;

    private final String strPattern = "\\d(?=\\d{3})";
    private String phone;
    private String otp = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentVerifyOtpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_otp, container, false);
        fragmentVerifyOtpBinding.setLifecycleOwner(this);
        verifyView = fragmentVerifyOtpBinding.getRoot();

        verifyOtpViewModel = new ViewModelProvider(this).get(VerifyOtpViewModel.class);
        try {
            // Mask Phone number
            bundle = this.getArguments();
            phone = getArguments().getString(AppConstants.USER_MOBILE);
            otp = getArguments().getString(AppConstants.USER_OTP);
            basicDetails = new BasicDetails();
            basicDetails.setOtp(otp);
            String phoneNumber = "number " + phone.replaceAll(strPattern, "*");
            basicDetails.setMobileNumber(phoneNumber);
            verifyOtpViewModel.userMobileMsg.setValue(context.getString(R.string.otp_sent) + " " + basicDetails.getMobileNumber());
            fragmentVerifyOtpBinding.etOtp.requestFocus();
            fragmentVerifyOtpBinding.etOtp.setCursorVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initViews(verifyView);

        ivBack.setOnClickListener(v -> {
            hideKeyBoard(requireActivity());
            Navigation.findNavController(verifyView).popBackStack();
        });

        fragmentVerifyOtpBinding.etOtp.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                checkValidEnteredOtp();
                return true;
            }
            return false;
        });

        /*Otp keyboard action*/
//        fragmentVerifyOtpBinding.etOtp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i==EditorInfo.IME_ACTION_SEND){
//                    checkValidEnteredOtp();
//                    return true;
//                }
//                return false;
//            }
//        });
        return verifyView;
    }

    private void initViews(View view) {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), getString(R.string.font_poppins_regular));
        fragmentVerifyOtpBinding.etOtp.setTypeface(font);

        fragmentVerifyOtpBinding.btnVerifyOtp.setOnClickListener(this);
        fragmentVerifyOtpBinding.tvResendOtp.setOnClickListener(this);

        fragmentVerifyOtpBinding.setVerifyOtpViewModel(verifyOtpViewModel);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        enableSoftKeyboard();

        fragmentVerifyOtpBinding.etOtp.requestFocus();

        setUpTextWatcher();
//        startOtpTimer();
    }

    private void setUpTextWatcher() {
        fragmentVerifyOtpBinding.etOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    hideKeyBoard(requireActivity());
//                    if (s.toString().equals(basicDetails.getOtp())) {
                        checkValidEnteredOtp();
//                    }
                }
            }
        });
    }

    private void enableSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_verify_otp:
                checkValidEnteredOtp();
                break;
            case R.id.tv_resend_otp:
//                fragmentVerifyOtpBinding.tvTimer.setVisibility(View.VISIBLE);
                if (isConnectingToInternet(context)) {
                    fragmentVerifyOtpBinding.etOtp.setText("");
                    callResendOtpAPI(showCircleProgressDialog(context, ""));
                } else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
                break;
        }

    }

    private void checkValidEnteredOtp() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        hideKeyBoard(requireActivity());
        if (bundle != null) {
            basicDetails.setOtp(Objects.requireNonNull(fragmentVerifyOtpBinding.etOtp.getText()).toString());
            int errorCodeForOtp = basicDetails.isValidOtp();
            if (errorCodeForOtp == 0) {
                errorToast(requireActivity(), getString(R.string.otp_should_not_be_empty));
            } else if (errorCodeForOtp == 1) {
                errorToast(requireActivity(), getString(R.string.please_enter_valid_otp));
            } else {
                if (isConnectingToInternet(context)) {
                    //add API call here
                    callVerifyOtpApi(showCircleProgressDialog(context, ""));
                } else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        }
    }

    //Verify OTP API here
    private void callVerifyOtpApi(ProgressDialog progressDialog) {
        /*print user input parameters*/
        for (Map.Entry<String, String> entry : verifyOtpParameters(0).entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        Observer<VerifyOtpResponse> verifyOtpResponseObserver = verifyOtpResponse -> {
            if (verifyOtpResponse != null) {
                progressDialog.dismiss();
                Log.e("Verify Otp Api ResponseData", new Gson().toJson(verifyOtpResponse));
                switch (verifyOtpResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (verifyOtpResponse.getMessage() != null) {
                            successToast(context, "" + verifyOtpResponse.getMessage());
                            preferences.seIstVerified(true);
                            if (verifyOtpResponse.getUser().getVerified() == 1 &&
                                    (verifyOtpResponse.getUser().getStateName()!=null &&
                                    !verifyOtpResponse.getUser().getStateName().equalsIgnoreCase("")
                                    &&verifyOtpResponse.getUser().getStateId()!=null &&
                                            !verifyOtpResponse.getUser().getStateId().equalsIgnoreCase("")
                                            && verifyOtpResponse.getUser().getCityName()!=null &&
                                            !verifyOtpResponse.getUser().getCityName().equalsIgnoreCase("")
                                            && verifyOtpResponse.getUser().getCityId()!=null &&
                                            !verifyOtpResponse.getUser().getCityId().equalsIgnoreCase("")
                                            && verifyOtpResponse.getUser().getAreaId()!=null &&
                                            !verifyOtpResponse.getUser().getAreaId().equalsIgnoreCase("")
                                            && verifyOtpResponse.getUser().getAreaName()!=null &&
                                            !verifyOtpResponse.getUser().getAreaName().equalsIgnoreCase("")

                                    )){
                                preferences.setIsLoggedIn(true);
                                Intent intent = new Intent(context, HomeActivity.class);
                                preferences.setUid(verifyOtpResponse.getUser().getUserId());
                                if (verifyOtpResponse.getUser().getUserName()!=null)
                                preferences.setUserName(verifyOtpResponse.getUser().getUserName());
                                if (verifyOtpResponse.getUser().getImage()!=null)
                                preferences.setUserProfile(verifyOtpResponse.getUser().getImage());
                                preferences.setUserMobile(verifyOtpResponse.getUser().getUserMobile());
                                preferences.setUserAddress(verifyOtpResponse.getUser().getCityName() + ", " + verifyOtpResponse.getUser().getStateName());
                                preferences.setUserCountry(bundle.getString(COUNTRY_CODE));
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                requireActivity().finish();
                                startActivity(intent);
                            } else {
                                Bundle bundle = new Bundle();
//                                bundle.putString(USER_ID, verifyOtpResponse.getUser().getUserId());
                                bundle.putString(USER_MOBILE, verifyOtpResponse.getUser().getUserMobile());
                                bundle.putString(COUNTRY_CODE, bundle.getString(COUNTRY_CODE));
                                preferences.setUserMobile(verifyOtpResponse.getUser().getUserMobile());
                                Navigation.findNavController(verifyView).navigate(R.id.action_verifyOtpFragment_to_signUpFragment, bundle);
                            }
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

        verifyOtpViewModel.submitVerifyOtp(progressDialog, verifyOtpParameters(0), VerifyOtpFragment.this)
                .observe(getViewLifecycleOwner(), verifyOtpResponseObserver);
    }

    // Resend OTP API here
    private void callResendOtpAPI(ProgressDialog progressDialog) {
        Observer<SignInResponse> signInResponseObserver = resendOtpResponse -> {
            if (resendOtpResponse != null) {
                progressDialog.dismiss();
                Log.e("Verify Otp Api ResponseData", new Gson().toJson(resendOtpResponse));
                switch (resendOtpResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (resendOtpResponse.getMessage() != null) {
                            successToast(context, "" + resendOtpResponse.getMessage());
                            verifyOtpViewModel.otp.setValue("");
                            basicDetails.setOtp(resendOtpResponse.getUser().getOtp());
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, resendOtpResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(resendOtpResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, resendOtpResponse.getMessage());
                        break;
                }
            }
        };
        verifyOtpViewModel.resendOtpApi(progressDialog, verifyOtpParameters(1), VerifyOtpFragment.this)
                .observe(getViewLifecycleOwner(), signInResponseObserver);
    }

    private void startOtpTimer() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                fragmentVerifyOtpBinding.tvResendOtp.setVisibility(View.GONE);
                fragmentVerifyOtpBinding.tvTimer.setText(String.format("Otp will expire in %d Sec", millisUntilFinished / 1000));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                fragmentVerifyOtpBinding.tvResendOtp.setEnabled(true);
                fragmentVerifyOtpBinding.tvTimer.setVisibility(View.GONE);
                fragmentVerifyOtpBinding.tvResendOtp.setVisibility(View.VISIBLE);
            }

        }.start();
    }


    private HashMap<String, String> verifyOtpParameters(int paramType) {
        HashMap<String, String> map = new HashMap<>();
        if (paramType == VERIFY) {
            map.put(OTP, Objects.requireNonNull(fragmentVerifyOtpBinding.etOtp.getText()).toString());
        }
//        else {
//            assert getArguments() != null;
//            map.put(USER_ID, getArguments().getString(USER_ID));
//        }
        return map;

    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), VerifyOtpFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        //Verify OTp API here
                        callVerifyOtpApi(showCircleProgressDialog(context, ""));
                        break;
                    case 1:
                        //Resend OTP API here
                        callResendOtpAPI(showCircleProgressDialog(context, ""));
                        break;
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}