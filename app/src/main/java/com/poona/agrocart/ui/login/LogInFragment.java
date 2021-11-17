package com.poona.agrocart.ui.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentLogInBinding;
import com.poona.agrocart.ui.BaseFragment;


/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class LogInFragment extends BaseFragment implements View.OnClickListener {

    private FragmentLogInBinding fragmentLogInBinding;
    private boolean showPassword=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLogInBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_log_in, container, false);
        fragmentLogInBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentLogInBinding).getRoot();

        initViews(view);

        return view;
    }

    private void initViews(View view)
    {
        fragmentLogInBinding.ivShowHidePassword.setOnClickListener(this);
        fragmentLogInBinding.ctvSignUp.setOnClickListener(this);

        fragmentLogInBinding.ivPoonaAgroMainLogo.bringToFront();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Typeface typeFace=Typeface.createFromAsset(getContext().getAssets(),"fonts/poppins/poppins_regular.ttf");
        fragmentLogInBinding.countryCodePicker.setTypeFace(typeFace);
        fragmentLogInBinding.countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                TextView title =(TextView)  dialog.findViewById(R.id.textView_title);
                title.setText("Select country");
            }
            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {
            }
            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) {
            }
        });

        fragmentLogInBinding.cetPhoneNo.addTextChangedListener(new TextWatcher() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_show_hide_password:
                if(!showPassword) {
                    fragmentLogInBinding.cetPassoword.setTransformationMethod(null);
                    fragmentLogInBinding.ivShowHidePassword.setImageResource(R.drawable.ic_password_show);
                }
                else {
                    fragmentLogInBinding.cetPassoword.setTransformationMethod(new PasswordTransformationMethod());
                    fragmentLogInBinding.ivShowHidePassword.setImageResource(R.drawable.ic_password_hide);
                }
                showPassword=!showPassword;
                fragmentLogInBinding.cetPassoword.setSelection(fragmentLogInBinding.cetPassoword.getText().length());
                break;
            case R.id.ctv_sign_up:
                Navigation.findNavController(v).navigate(R.id.action_LogInFragment_to_SignInFragment);
                break;
        }
    }
}