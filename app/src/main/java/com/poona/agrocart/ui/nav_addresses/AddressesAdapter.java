package com.poona.agrocart.ui.nav_addresses;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvAddressBinding;
import com.poona.agrocart.ui.nav_addresses.model.Address;

import java.util.ArrayList;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>
{
    private final ArrayList<Address> addressArrayList;

    public AddressesAdapter(ArrayList<Address> addressArrayList) {
        this.addressArrayList = addressArrayList;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvAddressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_address, parent, false);
        return new AddressesAdapter.AddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        final Address address = addressArrayList.get(position);
        holder.rvAddressBinding.setAddress(address);
        holder.bind(address);
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder
    {
        RvAddressBinding rvAddressBinding;

        public AddressViewHolder(RvAddressBinding rvAddressBinding)
        {
            super(rvAddressBinding.getRoot());
            this.rvAddressBinding=rvAddressBinding;
        }

        public void bind(Address address)
        {
            rvAddressBinding.setVariable(BR.address,address);
            rvAddressBinding.executePendingBindings();
        }
    }
}
