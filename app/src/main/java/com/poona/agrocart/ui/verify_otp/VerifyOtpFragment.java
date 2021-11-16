package com.poona.agrocart.ui.verify_otp;

import static android.content.Context.INPUT_METHOD_SERVICE;

import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.poona.agrocart.R;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.custom_otp_edit_text.CustomOtpEditText;

public class VerifyOtpFragment extends BaseFragment {


    //layout components
    private CustomButton btnVerifyOtp;
    private CustomOtpEditText cetOtp;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void findLayoutComponents(View view)
    {
        btnVerifyOtp=view.findViewById(R.id.cbtn_verify_otp);
        cetOtp=view.findViewById(R.id.cet_otp);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_otp, container, false);

        findLayoutComponents(view);
        initializeVars();

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        btnVerifyOtp.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_verifyOtpFragment_to_signUpFragment);
        });


        ivBack.setOnClickListener(v -> {
            hideKeyBoard(requireActivity());
            Navigation.findNavController(view).popBackStack();
        });


        return view;
    }

    private void initializeVars()
    {
        //to show otp soft keyboard
        enableSoftKeyboard();
        cetOtp.requestFocus();
    }

    private void enableSoftKeyboard()
    {
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.showSoftInput(cetOtp, 0);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}