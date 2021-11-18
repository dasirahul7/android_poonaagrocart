package com.poona.agrocart.ui.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import com.poona.agrocart.ui.BaseFragment;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class LogInFragment extends BaseFragment {

    //private Spinner spinnerCountryCodes;
    private CountryCodePicker countryCodePicker;
    private ImageView ivSignUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_log_in, container, false);

        //finding/initializing fragment components
        findLayoutComponents(view);

        //setting custom font to country code picker
        Typeface typeFace=Typeface.createFromAsset(getContext().getAssets(),"fonts/poppins/poppins_regular.ttf");
        countryCodePicker.setTypeFace(typeFace);

        //hiding action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
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

        ivSignUp.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_LoginFragment_to_verifyOtpFragment);
        });

        return view;
    }

    //this method will call whenever we change country code in spinner.
    public void onCountryPickerClick() {
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(getContext(), String.valueOf(countryCodePicker.getSelectedCountryCodeAsInt()), Toast.LENGTH_SHORT).show();
                countryCodePicker.getDefaultCountryCodeWithPlus();
                //ccp.getSelectedCountryCodeWithPlus();
            }
        });
    }


    //finding/initializing all the layout components
    private void findLayoutComponents(View view) {
        //spinnerCountryCodes = view.findViewById(R.id.spinner_country_code);
        countryCodePicker=view.findViewById(R.id.country_code_picker);
        ivSignUp=view.findViewById(R.id.iv_sign_up);
    }
}