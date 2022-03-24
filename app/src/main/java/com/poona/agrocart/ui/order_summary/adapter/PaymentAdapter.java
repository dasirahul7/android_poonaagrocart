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
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.databinding.RowPaymentRadioBinding;
import com.poona.agrocart.ui.order_summary.OrderSummaryFragment;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private ArrayList<Payments> paymentsArrayList;
    private RowPaymentRadioBinding rowPaymentRadioBinding;
    private int mSelectedItem = -1;
    private CompoundButton lastCheckedRB;
    private Context pContext;
    private OnPaymentClickListener onPaymentClickListener;
    private AppSharedPreferences preferences;
    private OrderSummaryFragment summaryFragment;

    /*interface for payment clicks*/
    public interface OnPaymentClickListener{
        void OnPaymentClick(Payments payments);
    }
    public PaymentAdapter(ArrayList<Payments> paymentsArrayList,Context context,
                          OnPaymentClickListener onPaymentClickListener,
                          OrderSummaryFragment summaryFragment) {
        this.paymentsArrayList = paymentsArrayList;
        this.pContext=context;
        this.onPaymentClickListener=onPaymentClickListener;
        this.summaryFragment=summaryFragment;
    }
    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowPaymentRadioBinding = RowPaymentRadioBinding.inflate(LayoutInflater.from(pContext), parent, false);
        return new PaymentViewHolder(rowPaymentRadioBinding,summaryFragment);
    }



    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payments payments = paymentsArrayList.get(position);
        rowPaymentRadioBinding.setPaymentModule(payments);
        preferences = new AppSharedPreferences(pContext);

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
        private OrderSummaryFragment summaryFragment;
        public PaymentViewHolder(@NonNull RowPaymentRadioBinding rowPaymentRadioBinding, OrderSummaryFragment summaryFragment) {
            super(rowPaymentRadioBinding.getRoot());
            this.rowPaymentRadioBinding = rowPaymentRadioBinding;
            this.summaryFragment = summaryFragment;
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
                if (payments.paymentType.equalsIgnoreCase("Wallet balance") && Integer.parseInt(payments.balance)<preferences.getPaymentAmount()){
                    summaryFragment.walletAlertAndDismiss("insufficient balance in wallet ",pContext);
                    rowPaymentRadioBinding.rbPayment.setChecked(false);
                }
                else onPaymentClickListener.OnPaymentClick(payments);
            });
        }
    }
}
