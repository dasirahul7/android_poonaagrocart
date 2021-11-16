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

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class LogInFragment extends BaseFragment implements View.OnClickListener {

    private FragmentLogInBinding fragmentLogInBinding;

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
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {
                //your code
            }
            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) {
                //your code
            }
        });
    }

    //this method will call whenever we change country code in spinner.
    public void onCountryPickerClick() {
        fragmentLogInBinding.countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

                Toast.makeText(getContext(), String.valueOf(fragmentLogInBinding.countryCodePicker.getSelectedCountryCodeAsInt()), Toast.LENGTH_SHORT).show();
                fragmentLogInBinding.countryCodePicker.getDefaultCountryCodeWithPlus();
                //ccp.getSelectedCountryCodeWithPlus();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_sign_up:
                Navigation.findNavController(v).navigate(R.id.action_LoginFragment_to_verifyOtpFragment);
                break;
        }
    }
}