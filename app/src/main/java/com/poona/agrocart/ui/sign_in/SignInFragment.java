package com.poona.agrocart.ui.sign_in;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.Navigation;

import com.hbb20.CountryCodePicker;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentSelectLocationBinding;
import com.poona.agrocart.databinding.FragmentSignInBinding;
import com.poona.agrocart.ui.BaseFragment;

public class SignInFragment extends BaseFragment implements View.OnClickListener{

    private FragmentSignInBinding fragmentSignInBinding;
    private boolean showPassword=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSignInBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_sign_in, container, false);
        fragmentSignInBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentSignInBinding).getRoot();

        initViews(view);

        return view;
    }

    private void initViews(View view)
    {
        fragmentSignInBinding.ivShowHidePassword.setOnClickListener(this);
        fragmentSignInBinding.ctvSignUp.setOnClickListener(this);

        fragmentSignInBinding.ivPoonaAgroMainLogo.bringToFront();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Typeface typeFace=Typeface.createFromAsset(getContext().getAssets(),"fonts/poppins/poppins_regular.ttf");
        fragmentSignInBinding.countryCodePicker.setTypeFace(typeFace);
        fragmentSignInBinding.countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                //your code
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_show_hide_password:
                if(!showPassword) {
                    fragmentSignInBinding.cetPassoword.setTransformationMethod(null);
                    fragmentSignInBinding.ivShowHidePassword.setImageResource(R.drawable.ic_password_show);
                }
                else {
                    fragmentSignInBinding.cetPassoword.setTransformationMethod(new PasswordTransformationMethod());
                    fragmentSignInBinding.ivShowHidePassword.setImageResource(R.drawable.ic_password_hide);
                }
                showPassword=!showPassword;
                fragmentSignInBinding.cetPassoword.setSelection(fragmentSignInBinding.cetPassoword.getText().length());
                break;
            case R.id.ctv_sign_up:
                Navigation.findNavController(v).navigate(R.id.action_SignInFragment_to_LogInFragment);
                break;
        }
    }
}