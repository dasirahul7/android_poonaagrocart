package com.poona.agrocart.ui.nav_addresses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.AddressesResponse;
import com.poona.agrocart.databinding.RvAddressBinding;
import com.poona.agrocart.ui.nav_my_cart.CartItemsAdapter;

import java.util.ArrayList;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressViewHolder> {
    private final ArrayList<AddressesResponse.Address> addressArrayList;

    private OnEditButtonClickListener onEditButtonClickListener;
    public interface OnEditButtonClickListener {
        void onItemClick(int position);
    }
    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        onEditButtonClickListener = listener;
    }

    private OnDeleteButtonClickListener onDeleteButtonClickListener;
    public interface OnDeleteButtonClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        onDeleteButtonClickListener = listener;
    }

    public AddressesAdapter(ArrayList<AddressesResponse.Address> addressArrayList) {
        this.addressArrayList = addressArrayList;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvAddressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_address, parent, false);
        return new AddressesAdapter.AddressViewHolder(binding, onEditButtonClickListener, onDeleteButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        final AddressesResponse.Address address = addressArrayList.get(position);
        holder.rvAddressBinding.setAddress(address);
        holder.bind(address);
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        RvAddressBinding rvAddressBinding;
        int selectedEditItem = 0;
        int selectedDeleteItem = 0;
        public AddressViewHolder(RvAddressBinding rvAddressBinding, OnEditButtonClickListener onEditButtonClickListener, OnDeleteButtonClickListener onDeleteButtonClickListener) {
            super(rvAddressBinding.getRoot());
            this.rvAddressBinding = rvAddressBinding;

            rvAddressBinding.ivEditAddress.setOnClickListener(view -> {
                selectedEditItem = getAdapterPosition();
                notifyDataSetChanged();
                if (onEditButtonClickListener != null) {
                    if (selectedEditItem != RecyclerView.NO_POSITION) {
                        onEditButtonClickListener.onItemClick(selectedEditItem);
                    }
                }
            });

            rvAddressBinding.ivDeleteAddress.setOnClickListener(view -> {
                selectedDeleteItem = getAdapterPosition();
                notifyDataSetChanged();
                if (onDeleteButtonClickListener != null) {
                    if (selectedDeleteItem != RecyclerView.NO_POSITION) {
                        onDeleteButtonClickListener.onItemClick(rvAddressBinding.getRoot(), selectedDeleteItem);
                    }
                }
            });
        }

        public void bind(AddressesResponse.Address address) {
            rvAddressBinding.setVariable(BR.address, address);
            rvAddressBinding.executePendingBindings();
        }
    }
}