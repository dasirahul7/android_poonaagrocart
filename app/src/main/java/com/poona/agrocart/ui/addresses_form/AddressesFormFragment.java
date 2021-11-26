package com.poona.agrocart.ui.addresses_form;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentAddressesFormBinding;

public class AddressesFormFragment extends Fragment {

    private FragmentAddressesFormBinding fragmentAddressesFormBinding;
    private AddressFormViewModel addressFormViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentAddressesFormBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_addresses_form, container, false);
        fragmentAddressesFormBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentAddressesFormBinding).getRoot();

        initView();

        return view;
    }

    private void initView()
    {
        addressFormViewModel = new ViewModelProvider(this).get(AddressFormViewModel.class);
        fragmentAddressesFormBinding.setAddressFormViewModel(addressFormViewModel);
    }
}