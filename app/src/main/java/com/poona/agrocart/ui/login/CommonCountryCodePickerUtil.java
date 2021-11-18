package com.poona.agrocart.ui.login;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentSignInBinding;

public class CommonCountryCodePickerUtil
{
    public static void setUpCountryCodePciker(FragmentSignInBinding fragmentSignInBinding, Context context,CommonViewModel commonViewModel)
    {
        Typeface typeFace=Typeface.createFromAsset(context.getAssets(),"fonts/poppins/poppins_regular.ttf");
        fragmentSignInBinding.countryCodePicker.setTypeFace(typeFace);
        commonViewModel.countryCode.setValue(fragmentSignInBinding.countryCodePicker.getDefaultCountryCodeWithPlus());
        fragmentSignInBinding.countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                TextView title=dialog.findViewById(R.id.textView_title);
                title.setText("Select country");
            }
            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) { }
            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) { }
        });
        fragmentSignInBinding.countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                commonViewModel.countryCode.setValue(fragmentSignInBinding.countryCodePicker.getSelectedCountryCodeWithPlus());
            }
        });
    }
}
