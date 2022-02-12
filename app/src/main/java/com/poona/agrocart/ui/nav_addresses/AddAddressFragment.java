package com.poona.agrocart.ui.nav_addresses;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentAddressesFormBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.sign_in.SignInViewModel;

public class AddAddressFragment extends BaseFragment implements View.OnClickListener {
    private FragmentAddressesFormBinding fragmentAddressesFormBinding;
    private AddressesViewModel addressesViewModel;
    private final String[] cities={"Pune"};
    private final String[] areas={"Vishrantwadi", "Khadki"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentAddressesFormBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_addresses_form, container, false);

        addressesViewModel = new ViewModelProvider(this).get(AddressesViewModel.class);
        fragmentAddressesFormBinding.setAddressesViewModel(addressesViewModel);

        fragmentAddressesFormBinding.setLifecycleOwner(this);
        final View view = fragmentAddressesFormBinding.getRoot();

        initView();
        setupSpinners();

        return view;
    }

    private void setupSpinners() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions,cities);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        fragmentAddressesFormBinding.spinnerCity.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions,areas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        fragmentAddressesFormBinding.spinnerArea.setAdapter(arrayAdapter);
    }

    private void initView() {
        fragmentAddressesFormBinding.tvUseCurrentLocation.setOnClickListener(this);

        Typeface poppinsRegularFont = Typeface.createFromAsset(getContext().getAssets(), getString(R.string.font_poppins_medium));
        fragmentAddressesFormBinding.rbHome.setTypeface(poppinsRegularFont);
        fragmentAddressesFormBinding.rbOffice.setTypeface(poppinsRegularFont);
        fragmentAddressesFormBinding.rbOther.setTypeface(poppinsRegularFont);

        initTitleWithBackBtn(getString(R.string.addresses_form));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_use_current_location:
                break;
        }
    }
}