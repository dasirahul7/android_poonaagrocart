package com.poona.agrocart.ui.verify_otp;

import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentVerifyOtpBinding;
import com.poona.agrocart.ui.BaseFragment;

public class VerifyOtpFragment extends BaseFragment implements View.OnClickListener{

    private FragmentVerifyOtpBinding fragmentVerifyOtpBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentVerifyOtpBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_verify_otp,container,false);
        fragmentVerifyOtpBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentVerifyOtpBinding).getRoot();

        initViews(view);

        ivBack.setOnClickListener(v -> {
            hideKeyBoard(requireActivity());
            Navigation.findNavController(view).popBackStack();
        });

        return view;
    }

    private void initViews(View view)
    {
        fragmentVerifyOtpBinding.cbtnVerifyOtp.setOnClickListener(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        enableSoftKeyboard();
        fragmentVerifyOtpBinding.cetOtp.requestFocus();
    }


    private void enableSoftKeyboard()
    {
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imgr.showSoftInput(fragmentVerifyOtpBinding.cetOtp, 0);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onClick(View v) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        hideKeyBoard(requireActivity());
        Navigation.findNavController(v).navigate(R.id.action_verifyOtpFragment_to_signUpFragment);
    }
}