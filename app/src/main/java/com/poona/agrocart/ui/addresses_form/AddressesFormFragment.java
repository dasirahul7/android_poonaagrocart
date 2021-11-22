package com.poona.agrocart.ui.addresses_form;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentAddressesFormBinding;

public class AddressesFormFragment extends Fragment {

    private FragmentAddressesFormBinding fragmentAddressesFormBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAddressesFormBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_addresses_form, container, false);
        fragmentAddressesFormBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentAddressesFormBinding).getRoot();

        return view;
    }
}