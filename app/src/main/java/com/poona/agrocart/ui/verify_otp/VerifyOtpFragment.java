package com.poona.agrocart.ui.verify_otp;

import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.databinding.FragmentVerifyOtpBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.ui.login.CommonViewModel;

import java.util.Objects;

public class VerifyOtpFragment extends BaseFragment implements View.OnClickListener{

    private FragmentVerifyOtpBinding fragmentVerifyOtpBinding;
    private BasicDetails basicDetails;
    private CommonViewModel commonViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentVerifyOtpBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_verify_otp,container,false);
        fragmentVerifyOtpBinding.setLifecycleOwner(this);
        final View view = fragmentVerifyOtpBinding.getRoot();

        initViews(view);

        ivBack.setOnClickListener(v -> {
            hideKeyBoard(requireActivity());
            Navigation.findNavController(view).popBackStack();
        });

        return view;
    }

    private void initViews(View view)
    {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), getString(R.string.font_poppins_regular));
        fragmentVerifyOtpBinding.etOtp.setTypeface(font);

        fragmentVerifyOtpBinding.btnVerifyOtp.setOnClickListener(this);

        commonViewModel=new ViewModelProvider(this).get(CommonViewModel.class);
        try {
            // Mask Phone number
            String phone = getArguments().getString(AppConstants.MOBILE_NO);
            String strPattern = "\\d(?=\\d{3})";
//            System.out.println( "number:"+phone.replaceAll(strPattern, "*") );
            commonViewModel.otpMobileMsg.setValue(getString(R.string.otp_sent)+phone.replaceAll(strPattern, "*"));
            fragmentVerifyOtpBinding.etOtp.requestFocus();
            fragmentVerifyOtpBinding.etOtp.setCursorVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentVerifyOtpBinding.setCommonViewModel(commonViewModel);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        basicDetails=new BasicDetails();
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
        Bundle bundle=this.getArguments();
        if(bundle!=null)
        {
            basicDetails.setOtp(Objects.requireNonNull(fragmentVerifyOtpBinding.etOtp.getText()).toString());
            commonViewModel.otp.setValue(basicDetails.getOtp());
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
                    Navigation.findNavController(v).navigate(R.id.action_verifyOtpFragment_to_signUpFragment,bundle);
                }
                else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        }
    }
}