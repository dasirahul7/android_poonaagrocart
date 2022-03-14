package com.poona.agrocart.ui.order_summary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.databinding.RowDeliveryOptionRadioBinding;
import com.poona.agrocart.data.network.responses.orderResponse.DeliverySlot;

import java.util.ArrayList;

public class DeliveryDialogAdapter extends RecyclerView.Adapter<DeliveryDialogAdapter.DeliveryItemHolder> {
    private final ArrayList<DeliverySlot> deliverySlots;
    private final Context dlContext;
    private RowDeliveryOptionRadioBinding deliveryOptionsBinding;
    private int mSelectedItem = -1;
    private CompoundButton lastCheckedRB;
    private OnSlotClickListener onSlotClickListener;

    public DeliveryDialogAdapter(ArrayList<DeliverySlot> deliverySlots, Context dlContext,OnSlotClickListener onSlotClickListener) {
        this.deliverySlots = deliverySlots;
        this.onSlotClickListener = onSlotClickListener;
        this.dlContext = dlContext;
    }

    public interface OnSlotClickListener{
        void OnSlotClick(DeliverySlot deliverySlot);
    }

    public OnSlotClickListener getOnSlotClickListener() {
        return onSlotClickListener;
    }

    public void setOnSlotClickListener(OnSlotClickListener onSlotClickListener) {
        this.onSlotClickListener = onSlotClickListener;
    }

    @NonNull
    @Override
    public DeliveryItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        deliveryOptionsBinding = RowDeliveryOptionRadioBinding.inflate(LayoutInflater.from(dlContext), parent, false);
        return new DeliveryItemHolder(deliveryOptionsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryItemHolder holder, int position) {
        DeliverySlot slot = deliverySlots.get(position);
        deliveryOptionsBinding.setModelDeliverySlot(slot);
        holder.optionsBinding.rdSlot.setChecked(mSelectedItem == position);
        holder.optionsBinding.rdSlot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean radioBoolean) {
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB = holder.optionsBinding.rdSlot;
            }
        });

        holder.sotBind(slot);
    }

    @Override
    public int getItemCount() {
        return deliverySlots.size();
    }

    public class DeliveryItemHolder extends RecyclerView.ViewHolder {
        private final RowDeliveryOptionRadioBinding optionsBinding;

        public DeliveryItemHolder(@NonNull RowDeliveryOptionRadioBinding binding) {
            super(binding.getRoot());
            this.optionsBinding = binding;
        }

        public void sotBind(DeliverySlot slot) {
            optionsBinding.setVariable(BR.modelDeliverySlot, slot);
            optionsBinding.executePendingBindings();
            itemView.setOnClickListener(v -> {
                mSelectedItem = getBindingAdapterPosition();
            });
            optionsBinding.rdSlot.setOnCheckedChangeListener((compoundButton, b) -> {
                onSlotClickListener.OnSlotClick(slot);
            });
        }
    }
}
