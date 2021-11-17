package com.poona.agrocart.ui.sign_in;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.databinding.FragmentSignInBinding;
import com.poona.agrocart.ui.BaseFragment;

import java.util.Objects;

public class SignInFragment extends BaseFragment implements View.OnClickListener{

    private FragmentSignInBinding fragmentSignInBinding;
    private String mobileNo="",countryCode="+91";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSignInBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        fragmentSignInBinding.setLifecycleOwner(this);

        final View view = ((ViewDataBinding) fragmentSignInBinding).getRoot();

        initView(view);

        return view;
    }

    private void initView(View view)
    {
        fragmentSignInBinding.ivSignUp.setOnClickListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Typeface typeFace=Typeface.createFromAsset(getContext().getAssets(),"fonts/poppins/poppins_regular.ttf");
        fragmentSignInBinding.countryCodePicker.setTypeFace(typeFace);
        fragmentSignInBinding.countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                TextView title =(TextView)  dialog.findViewById(R.id.textView_title);
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
                countryCode=fragmentSignInBinding.countryCodePicker.getSelectedCountryCodeWithPlus();
            }
        });

        fragmentSignInBinding.etMobileNo.addTextChangedListener(new TextWatcher() {
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
            case R.id.iv_sign_up:
                mobileNo= Objects.requireNonNull(fragmentSignInBinding.etMobileNo.getText()).toString();
                if(TextUtils.isEmpty(mobileNo) || mobileNo.length()<10) {
                    errorToast(getActivity(), getString(R.string.invalid_phone_number));
                }
                else{
                    Bundle bundle=new Bundle();
                    bundle.putString(AppConstants.MOBILE_NO,mobileNo);
                    bundle.putString(AppConstants.COUNTRY_CODE,countryCode);
                    mobileNo=countryCode+mobileNo;
                    Navigation.findNavController(v).navigate(R.id.action_signInFragment_to_verifyOtpFragment,bundle);
                }
                break;
            case R.id.cbtn_login:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}