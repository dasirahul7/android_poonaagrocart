package com.poona.agrocart.ui.order_summary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.data.network.responses.AddressesResponse;
import com.poona.agrocart.databinding.RowAddressRadioBinding;
import com.poona.agrocart.ui.order_summary.model.Address;

import java.util.ArrayList;

public class AddressDialogAdapter extends RecyclerView.Adapter<AddressDialogAdapter.AddressHolder> {
    private ArrayList<AddressesResponse.Address> addressList = new ArrayList<>();
    private final Context adContext;
    private RowAddressRadioBinding addressItemBinding;
    private int mSelectedItem = -1;
    private CompoundButton lastCheckedRB;
    private OnAddressClickListener onAddressClickListener;

    public interface OnAddressClickListener{
        void OnAddressClick(AddressesResponse.Address address);
    }

    public AddressDialogAdapter(ArrayList<AddressesResponse.Address> addressList,
                                Context context, OnAddressClickListener onAddressClickListener) {
        this.addressList = addressList;
        this.adContext = context;
        this.onAddressClickListener = onAddressClickListener;
    }

    @NonNull
    @Override
    public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        addressItemBinding = RowAddressRadioBinding.inflate(LayoutInflater.from(adContext), parent, false);
        return new AddressHolder(addressItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressHolder holder, int position) {
        AddressesResponse.Address address = addressList.get(position);
        addressItemBinding.setModelAddress(address);
        // radio Selected Item
        holder.addressBinding.addRadio.setChecked(mSelectedItem == position);
        holder.addressBinding.addRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean radioBoolean) {
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB = holder.addressBinding.addRadio;
            }
        });
        holder.addressBind(address);
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class AddressHolder extends RecyclerView.ViewHolder {
        private final RowAddressRadioBinding addressBinding;

        public AddressHolder(RowAddressRadioBinding rowAddressItemBinding) {
            super(rowAddressItemBinding.getRoot());
            this.addressBinding = rowAddressItemBinding;
            itemView.setOnClickListener(v -> {
                mSelectedItem = getBindingAdapterPosition();
            });
        }

        public void addressBind(AddressesResponse.Address address) {
            addressBinding.setVariable(BR.modelAddress, address);
            addressBinding.executePendingBindings();
            addressBinding.addRadio.setOnClickListener(view -> {
                onAddressClickListener.OnAddressClick(address);
            });
        }

    }
}
