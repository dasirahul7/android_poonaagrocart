package com.poona.agrocart.ui.order_summary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.data.network.responses.orderResponse.Payments;
import com.poona.agrocart.databinding.RowAddressRadioBinding;
import com.poona.agrocart.databinding.RowPaymentRadioBinding;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private ArrayList<Payments> paymentsArrayList;
    private RowPaymentRadioBinding rowPaymentRadioBinding;
    private int mSelectedItem = -1;
    private CompoundButton lastCheckedRB;
    private Context pContext;
    private OnPaymentClickListener onPaymentClickListener;

    /*interface for payment clicks*/
    public interface OnPaymentClickListener{
        void OnPaymentClick(Payments payments);
    }
    public PaymentAdapter(ArrayList<Payments> paymentsArrayList,Context context,OnPaymentClickListener onPaymentClickListener) {
        this.paymentsArrayList = paymentsArrayList;
        this.pContext=context;
        this.onPaymentClickListener=onPaymentClickListener;
    }
    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowPaymentRadioBinding = RowPaymentRadioBinding.inflate(LayoutInflater.from(pContext), parent, false);
        return new PaymentViewHolder(rowPaymentRadioBinding);
    }



    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payments payments = paymentsArrayList.get(position);
        rowPaymentRadioBinding.setPaymentModule(payments);
        // radio Selected Item
        rowPaymentRadioBinding.rbPayment.setChecked(mSelectedItem == position);
        rowPaymentRadioBinding.rbPayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean radioBoolean) {
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB = holder.rowPaymentRadioBinding.rbPayment;
            }
        });
        holder.bind(payments);
    }

    @Override
    public int getItemCount() {
        return paymentsArrayList.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        private final RowPaymentRadioBinding rowPaymentRadioBinding;
        public PaymentViewHolder(@NonNull RowPaymentRadioBinding rowPaymentRadioBinding) {
            super(rowPaymentRadioBinding.getRoot());
            this.rowPaymentRadioBinding = rowPaymentRadioBinding;
            itemView.setOnClickListener(v -> {
                mSelectedItem = getBindingAdapterPosition();
            });
        }

        public void bind(Payments payments) {
            rowPaymentRadioBinding.setVariable(BR.paymentModule,payments);
            rowPaymentRadioBinding.executePendingBindings();
            if (payments.paymentType.equalsIgnoreCase("Wallet balance"))
                rowPaymentRadioBinding.tvWalletBalance.setVisibility(View.VISIBLE);
            else rowPaymentRadioBinding.tvWalletBalance.setVisibility(View.GONE);
            rowPaymentRadioBinding.rbPayment.setOnClickListener(view -> {
                onPaymentClickListener.OnPaymentClick(payments);
            });
        }
    }
}
