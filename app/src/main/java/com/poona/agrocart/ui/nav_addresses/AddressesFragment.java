package com.poona.agrocart.ui.nav_addresses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentAddressesBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_addresses.model.Address;

import java.util.ArrayList;

public class AddressesFragment extends BaseFragment implements View.OnClickListener
{
    private FragmentAddressesBinding fragmentAddressesBinding;
    private RecyclerView rvAddress;
    private LinearLayoutManager linearLayoutManager;
    private AddressesAdapter addressesAdapter;
    private ArrayList<Address> addressArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentAddressesBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_addresses, container, false);
        fragmentAddressesBinding.setLifecycleOwner(this);
        final View view = fragmentAddressesBinding.getRoot();

        initView();
        setRvAdapter();

        return view;
    }

    private void setRvAdapter()
    {
        addressArrayList=new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvAddress.setHasFixedSize(true);
        rvAddress.setLayoutManager(linearLayoutManager);

        addressesAdapter = new AddressesAdapter(addressArrayList);
        rvAddress.setAdapter(addressesAdapter);
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 3; i++)
        {
            Address address = new Address();
            address.setName(getString(R.string.ayush));
            address.setMobileNumber(getString(R.string._91_986_095_3315));
            address.setAddress(getString(R.string.home));
            address.setAddress(getString(R.string.nand_nivas_building_floor_3_b_3_lane_no_13_bhatrau_nivas_vishrantwadi_pune_411015));
            addressArrayList.add(address);
        }
    }

    private void initView()
    {
        rvAddress=fragmentAddressesBinding.rvAddress;
        fragmentAddressesBinding.btnAddAddress.setOnClickListener(this);
        initTitleBar(getString(R.string.menu_addresses));
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_add_address:
                redirectToAddressForm(v);
                break;
        }
    }

    private void redirectToAddressForm(View v)
    {
        Navigation.findNavController(v).navigate(R.id.action_nav_address_to_addressesFormFragment2);
    }
}