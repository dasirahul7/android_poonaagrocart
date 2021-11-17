package com.poona.agrocart.ui.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentLogInBinding;
import com.poona.agrocart.ui.BaseFragment;

import java.util.Objects;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class LogInFragment extends BaseFragment implements View.OnClickListener {

    private FragmentLogInBinding fragmentLogInBinding;
    private String mobileNo="",countryCode="+91";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLogInBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false);
        fragmentLogInBinding.setLifecycleOwner(this);

        final View view = ((ViewDataBinding) fragmentLogInBinding).getRoot();

        initView(view);

        return view;
    }

    private void initView(View view)
    {
        fragmentLogInBinding.ivSignUp.setOnClickListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //setting custom font to country code picker
        Typeface typeFace=Typeface.createFromAsset(getContext().getAssets(),"fonts/poppins/poppins_regular.ttf");
        fragmentLogInBinding.countryCodePicker.setTypeFace(typeFace);
        fragmentLogInBinding.countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                //your code
                TextView title =(TextView)  dialog.findViewById(R.id.textView_title);
                title.setText("Select country");
            }
            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) { }
            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) { }
        });
        fragmentLogInBinding.countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode=fragmentLogInBinding.countryCodePicker.getSelectedCountryCodeWithPlus();
                Toast.makeText(getActivity(), countryCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_sign_up:
                mobileNo= Objects.requireNonNull(fragmentLogInBinding.cetMobileNo.getText()).toString();
                if(TextUtils.isEmpty(mobileNo) || mobileNo.length()<10) {
                    errorToast(getActivity(), "Invalid phone number");
                }
                else{
                    mobileNo=countryCode+mobileNo;
                    Navigation.findNavController(v).navigate(R.id.action_LoginFragment_to_verifyOtpFragment);
                }
                break;
            case R.id.cbtn_login:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}