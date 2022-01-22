package com.poona.agrocart.ui.sign_up;

import static com.poona.agrocart.app.AppConstants.EMAIL;
import static com.poona.agrocart.app.AppConstants.MOBILE_NUMBER;
import static com.poona.agrocart.app.AppConstants.OTP;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.USERNAME;
import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.reponses.BaseResponse;
import com.poona.agrocart.databinding.FragmentSignUpBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.ui.login.CommonViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Observable;

public class SignUpFragment extends BaseFragment implements View.OnClickListener
{
    private static final String TAG = SignUpFragment.class.getSimpleName();
    private FragmentSignUpBinding fragmentSignUpBinding;
    private SignUpViewModel signUpViewModel;
    private BasicDetails basicDetails;
    private View signUpView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSignUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        fragmentSignUpBinding.setLifecycleOwner(this);
        signUpView = ((ViewDataBinding) fragmentSignUpBinding).getRoot();

        signUpViewModel=new ViewModelProvider(this).get(SignUpViewModel.class);
        fragmentSignUpBinding.setSignUpViewModel(signUpViewModel);

        initView(signUpView);

        ivBack.setOnClickListener(v -> Navigation.findNavController(signUpView).popBackStack());

        return signUpView;
    }

    private void initView(View view)
    {
        fragmentSignUpBinding.tvTermsOfService.setOnClickListener(this);
        fragmentSignUpBinding.tvPrivacyPolicy.setOnClickListener(this);
        fragmentSignUpBinding.btnSignUp.setOnClickListener(this);
//        fragmentSignUpBinding.rbIndividual.setOnClickListener(this);
//        fragmentSignUpBinding.rbBusiness.setOnClickListener(this);

        Typeface poppinsRegularFont = Typeface.createFromAsset(getContext().getAssets(), getString(R.string.font_poppins_regular));
//        fragmentSignUpBinding.rbIndividual.setTypeface(poppinsRegularFont);
//        fragmentSignUpBinding.rbBusiness.setTypeface(poppinsRegularFont);

        hideKeyBoard(requireActivity());
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        basicDetails=new BasicDetails();

        Bundle bundle=this.getArguments();
        if(bundle!=null)
        {
            fragmentSignUpBinding.etPhoneNo.setText(bundle.getString(AppConstants.USER_MOBILE));
            String tempCountryCode=bundle.getString(AppConstants.COUNTRY_CODE).replace("+","");
            fragmentSignUpBinding.countryCodePicker.setCountryForPhoneCode(Integer.parseInt(tempCountryCode));

            basicDetails.setCountryCode(fragmentSignUpBinding.countryCodePicker.getSelectedCountryCodeWithPlus());
            basicDetails.setMobileNumber(fragmentSignUpBinding.etPhoneNo.getText().toString());

            signUpViewModel.mobileNo.setValue(basicDetails.getMobileNumber());
            signUpViewModel.countryCode.setValue(basicDetails.getCountryCode());
        }

        fragmentSignUpBinding.ivPoonaAgroMainLogo.bringToFront();

        setUpCountryCodePicker();

        setUpTextWatcher();
    }

    public void setUpCountryCodePicker()
    {
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

        /*fragmentSignUpBinding.countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                commonViewModel.countryCode.setValue(fragmentSignUpBinding.countryCodePicker.getSelectedCountryCodeWithPlus());
            }
        });*/
    }

    private void setUpTextWatcher()
    {
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
        switch (v.getId())
        {
            case R.id.tv_terms_of_service:
                redirectToTermsAndCondtn(v);
                break;

            case R.id.tv_privacy_policy:
                redirectToPrivacyPolicy(v);
                break;

            case R.id.btn_sign_up:
                RedirectToSelectLocationFragment(v);
                break;

        }

    }


    private void RedirectToSelectLocationFragment(View v)
    {
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
        /* print user input parameters */
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
        Navigation.findNavController(signUpView).navigate(R.id.action_signUpFragment_to_selectLocationFragment);
    }

    private HashMap<String, String> signUpParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(USERNAME, fragmentSignUpBinding.etUsername.getText().toString());
        map.put(MOBILE_NUMBER, fragmentSignUpBinding.etPhoneNo.getText().toString());
        map.put(EMAIL, fragmentSignUpBinding.etEmailId.getText().toString());
        return map;
    }

    private void redirectToPrivacyPolicy(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("from", "SignUp");
        bundle.putString("title", getString(R.string.privacy_policy));
        Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_signInPrivacyFragment, bundle);
    }

    private void redirectToTermsAndCondtn(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("from", "SignUp");
        bundle.putString("title", getString(R.string.menu_terms_conditions));
        Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_signInTermsFragment, bundle);
    }
}