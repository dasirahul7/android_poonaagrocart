package com.poona.agrocart.ui.nav_addresses;

import android.text.TextUtils;
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

import java.util.ArrayList;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressViewHolder> {
    private final ArrayList<AddressesResponse.Address> addressArrayList;

    private OnEditButtonClickListener onEditButtonClickListener;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;
    private OnDefaultAddressClickListener onDefaultAddressClickListener;

    public AddressesAdapter(ArrayList<AddressesResponse.Address> addressArrayList) {
        this.addressArrayList = addressArrayList;
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        onEditButtonClickListener = listener;
    }

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        onDeleteButtonClickListener = listener;
    }

    public void setOnDefaultAddressClickListener(OnDefaultAddressClickListener listener) {
        onDefaultAddressClickListener = listener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvAddressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_address, parent, false);
        return new AddressesAdapter.AddressViewHolder(binding, onEditButtonClickListener, onDeleteButtonClickListener, onDefaultAddressClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        final AddressesResponse.Address address = addressArrayList.get(position);
        holder.rvAddressBinding.setAddress(address);

        if (address.getIsDefault() != null && !TextUtils.isEmpty(address.getIsDefault())
                && address.getIsDefault().equals("yes") || addressArrayList.size() == 1) {
            holder.rvAddressBinding.cbDefault.setClickable(false);
            holder.rvAddressBinding.cbDefault.setChecked(true);
        } else {
            holder.rvAddressBinding.cbDefault.setClickable(true);
            holder.rvAddressBinding.cbDefault.setChecked(false);
        }

        holder.bind(address);
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }

    public interface OnEditButtonClickListener {
        void onItemClick(int position);
    }

    public interface OnDeleteButtonClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface OnDefaultAddressClickListener {
        void onItemClick(View itemView, int position);
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        RvAddressBinding rvAddressBinding;
        int selectedEditItem = 0;
        int selectedDeleteItem = 0;
        int selectedDefaultAddressItem = 0;

        public AddressViewHolder(RvAddressBinding rvAddressBinding, OnEditButtonClickListener onEditButtonClickListener, OnDeleteButtonClickListener onDeleteButtonClickListener, OnDefaultAddressClickListener onDefaultAddressClickListener) {
            super(rvAddressBinding.getRoot());
            this.rvAddressBinding = rvAddressBinding;

            rvAddressBinding.ivEditAddress.setOnClickListener(view -> {
                selectedEditItem = getBindingAdapterPosition();
                notifyDataSetChanged();
                if (onEditButtonClickListener != null) {
                    if (selectedEditItem != RecyclerView.NO_POSITION) {
                        onEditButtonClickListener.onItemClick(selectedEditItem);
                    }
                }
            });

            rvAddressBinding.ivDeleteAddress.setOnClickListener(view -> {
                selectedDeleteItem = getBindingAdapterPosition();
                notifyDataSetChanged();
                if (onDeleteButtonClickListener != null) {
                    if (selectedDeleteItem != RecyclerView.NO_POSITION) {
                        onDeleteButtonClickListener.onItemClick(rvAddressBinding.getRoot(), selectedDeleteItem);
                    }
                }
            });

            rvAddressBinding.cbDefault.setOnClickListener(view -> {
                selectedDefaultAddressItem = getBindingAdapterPosition();
                notifyDataSetChanged();
                if (onDefaultAddressClickListener != null) {
                    if (selectedDefaultAddressItem != RecyclerView.NO_POSITION) {
                        onDefaultAddressClickListener.onItemClick(rvAddressBinding.getRoot(), selectedDefaultAddressItem);
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