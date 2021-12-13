package com.poona.agrocart.ui.sign_up;

import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.text.Editable;
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
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.ui.login.CommonViewModel;
import java.util.Objects;

public class SignUpFragment extends BaseFragment implements View.OnClickListener
{
    private FragmentSignUpBinding fragmentSignUpBinding;
    private CommonViewModel commonViewModel;
    private BasicDetails basicDetails;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSignUpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);
        fragmentSignUpBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentSignUpBinding).getRoot();

        commonViewModel=new ViewModelProvider(this).get(CommonViewModel.class);
        fragmentSignUpBinding.setCommonViewModel(commonViewModel);

        initView(view);

        ivBack.setOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        return view;
    }

    private void initView(View view)
    {
        fragmentSignUpBinding.tvTermsOfService.setOnClickListener(this);
        fragmentSignUpBinding.tvPrivacyPolicy.setOnClickListener(this);
        fragmentSignUpBinding.btnSignUp.setOnClickListener(this);
        fragmentSignUpBinding.rbIndividual.setOnClickListener(this);
        fragmentSignUpBinding.rbBusiness.setOnClickListener(this);

        Typeface poppinsRegularFont = Typeface.createFromAsset(getContext().getAssets(), getString(R.string.font_poppins_regular));
        fragmentSignUpBinding.rbIndividual.setTypeface(poppinsRegularFont);
        fragmentSignUpBinding.rbBusiness.setTypeface(poppinsRegularFont);

        hideKeyBoard(requireActivity());
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
        basicDetails=new BasicDetails();

        Bundle bundle=this.getArguments();
        if(bundle!=null)
        {
            fragmentSignUpBinding.etPhoneNo.setText(bundle.getString(AppConstants.MOBILE_NO));
            String tempCountryCode=bundle.getString(AppConstants.COUNTRY_CODE).replace("+","");
            fragmentSignUpBinding.countryCodePicker.setCountryForPhoneCode(Integer.parseInt(tempCountryCode));

            basicDetails.setCountryCode(fragmentSignUpBinding.countryCodePicker.getSelectedCountryCodeWithPlus());
            basicDetails.setMobileNumber(fragmentSignUpBinding.etPhoneNo.getText().toString());

            commonViewModel.mobileNo.setValue(basicDetails.getMobileNumber());
            commonViewModel.countryCode.setValue(basicDetails.getCountryCode());
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

            case R.id.rb_individual:
                changeBgs(true);
                break;

            case R.id.rb_business:
                changeBgs(false);
                break;
        }

    }

    private void changeBgs(boolean isIndividualSelected) {
        if (isIndividualSelected) {
            fragmentSignUpBinding.llRbIndividual.setBackgroundResource(R.drawable.bg_signup_rb_selected);
            fragmentSignUpBinding.llRbBusiness.setBackgroundResource(R.drawable.bg_signup_rb_unselected);
            fragmentSignUpBinding.rbIndividual.setChecked(true);
            fragmentSignUpBinding.rbBusiness.setChecked(false);
        } else {
            fragmentSignUpBinding.llRbIndividual.setBackgroundResource(R.drawable.bg_signup_rb_unselected);
            fragmentSignUpBinding.llRbBusiness.setBackgroundResource(R.drawable.bg_signup_rb_selected);
            fragmentSignUpBinding.rbBusiness.setChecked(true);
            fragmentSignUpBinding.rbIndividual.setChecked(false);
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
                commonViewModel.mobileNo.setValue(basicDetails.getMobileNumber());
                commonViewModel.countryCode.setValue(basicDetails.getCountryCode());
                commonViewModel.userName.setValue(basicDetails.getUserName());
                commonViewModel.emailId.setValue(basicDetails.getEmailId());
                successToast(context,"Login Success");
                Navigation.findNavController(v).navigate(R.id.action_signUpFragment_to_selectLocationFragment);

            }
            else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }

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