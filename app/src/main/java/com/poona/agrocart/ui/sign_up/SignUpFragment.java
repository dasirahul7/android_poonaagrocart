package com.poona.agrocart.ui.sign_up;

import static com.poona.agrocart.app.AppConstants.CMS_TYPE;
import static com.poona.agrocart.app.AppConstants.EMAIL;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.MOBILE_NUMBER;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.USERNAME;
import static com.poona.agrocart.app.AppConstants.USER_ID;
import static com.poona.agrocart.app.AppConstants.USER_MOBILE;
import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.BaseResponse;
import com.poona.agrocart.databinding.FragmentSignUpBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {
    private static final String TAG = SignUpFragment.class.getSimpleName();
    private FragmentSignUpBinding fragmentSignUpBinding;
    private SignUpViewModel signUpViewModel;
    private BasicDetails basicDetails;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSignUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        fragmentSignUpBinding.setLifecycleOwner(this);
        view = ((ViewDataBinding) fragmentSignUpBinding).getRoot();

        signUpViewModel=new ViewModelProvider(this).get(SignUpViewModel.class);
        fragmentSignUpBinding.setSignUpViewModel(signUpViewModel);

        initView();

        ivBack.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        return view;
    }

    private void initView() {
        fragmentSignUpBinding.btnSignUp.setOnClickListener(this);

        hideKeyBoard(requireActivity());
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        basicDetails=new BasicDetails();

        Bundle bundle=getArguments();
        if(bundle!=null) {
            fragmentSignUpBinding.etPhoneNo.setText(bundle.getString(AppConstants.USER_MOBILE));
            Log.d(TAG, "initView: "+bundle.getString(AppConstants.USER_MOBILE));

            basicDetails.setMobileNumber(fragmentSignUpBinding.etPhoneNo.getText().toString());

            signUpViewModel.mobileNo.setValue(basicDetails.getMobileNumber());
            signUpViewModel.countryCode.setValue(basicDetails.getCountryCode());
        }

        fragmentSignUpBinding.ivPoonaAgroMainLogo.bringToFront();

        setUpCountryCodePicker();

        setUpTextWatcher();

        SpannableString ssTermsPolicy = new SpannableString(getResources().getString(R.string.by_continuing_you_agree));
        ClickableSpan clickableSpanTerms = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                redirectToCmsFragment(1); //Terms & Condition
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ssTermsPolicy.setSpan(clickableSpanTerms, 31, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpanPolicy = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                redirectToCmsFragment(2); //Privacy Policy
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ssTermsPolicy.setSpan(clickableSpanPolicy, 52, 64, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        fragmentSignUpBinding.tvTermsPolicy.setText(ssTermsPolicy);
        fragmentSignUpBinding.tvTermsPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        fragmentSignUpBinding.tvTermsPolicy.setHighlightColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public void setUpCountryCodePicker() {
        Typeface typeFace=Typeface.createFromAsset(requireContext().getAssets(),getString(R.string.font_poppins_regular));
        fragmentSignUpBinding.countryCodePicker.setTypeFace(typeFace);
        fragmentSignUpBinding.countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                TextView title=dialog.findViewById(R.id.textView_title);
                title.setText(getString(R.string.select_country));
            }
            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) { }
            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) { }
        });
    }

    private void setUpTextWatcher() {
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                RedirectToSelectLocationFragment(v);
                break;

        }

    }

    private void RedirectToSelectLocationFragment(View v) {
        basicDetails.setUserName(fragmentSignUpBinding.etUsername.getText().toString());
        basicDetails.setCountryCode(fragmentSignUpBinding.countryCodePicker.getSelectedCountryCodeWithPlus());
        basicDetails.setMobileNumber(fragmentSignUpBinding.etPhoneNo.getText().toString());
        basicDetails.setEmailId(fragmentSignUpBinding.etEmailId.getText().toString());

        int errorCodeUserName=basicDetails.isValidUserName();
        int errorCodeEmailId=basicDetails.isValidEmailId();

        if(errorCodeUserName==0){
            errorToast(requireActivity(),getString(R.string.username_should_not_be_empty));
        }
        else if(errorCodeEmailId==1){
            errorToast(requireActivity(),getString(R.string.please_enter_valid_email_id));
        }
        else {
            if (isConnectingToInternet(context)) {
                //add API call here
                callRegisterApi(showCircleProgressDialog(context,""));
            }
            else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }

    }

    private void callRegisterApi(ProgressDialog progressDialog) {
        /*print user input parameters*/
        for (Map.Entry<String, String> entry : signUpParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }
        Observer<BaseResponse> registerResponseObserver = registerResponse -> {
            if (registerResponse != null) {
                progressDialog.dismiss();
                Log.e("Register Api Response", new Gson().toJson(registerResponse));
                switch (registerResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if(registerResponse.getStatus() == 200){
                            successToast(context, ""+registerResponse.getMessage());
                            Bundle bundle = getArguments();
                            if (bundle.getString(USER_ID)!=null){
                                preferences.setUid(bundle.getString(USER_ID));
                                preferences.setUserMobile(bundle.getString(USER_MOBILE));
                            }
                            redirectToHomeScreen();
//                            Navigation.findNavController(verifyView).navigate(R.id.action_verifyOtpFragment_to_signUpFragment,bundle);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, registerResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(registerResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, registerResponse.getMessage());
                        break;
                }
            } else {
                progressDialog.dismiss();
            }
        };
        signUpViewModel.submitRegistrationApi(progressDialog,signUpParameters(),SignUpFragment.this)
                .observe(getViewLifecycleOwner(),registerResponseObserver);

    }

    private void redirectToHomeScreen() {
        signUpViewModel.mobileNo.setValue(basicDetails.getMobileNumber());
        signUpViewModel.countryCode.setValue(basicDetails.getCountryCode());
        signUpViewModel.userName.setValue(basicDetails.getUserName());
        signUpViewModel.emailId.setValue(basicDetails.getEmailId());
        Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_selectLocationFragment);
    }

    private HashMap<String, String> signUpParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(USERNAME, fragmentSignUpBinding.etUsername.getText().toString());
        map.put(MOBILE_NUMBER, fragmentSignUpBinding.etPhoneNo.getText().toString());
        map.put(EMAIL, fragmentSignUpBinding.etEmailId.getText().toString());
        return map;
    }

    private void redirectToCmsFragment(int from) {
        Bundle bundle = new Bundle();
        bundle.putString(FROM_SCREEN, TAG);
        bundle.putInt(CMS_TYPE, from);
        Navigation.findNavController(view).navigate(R.id.action_to_cmsFragment, bundle);
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), SignUpFragment.this,() -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if(from == 0) {
                    callRegisterApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}