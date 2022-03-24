package com.poona.agrocart.ui.nav_wallet.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.walletTransaction.WalletTransaction;
import com.poona.agrocart.databinding.RvTransactionBinding;
import com.poona.agrocart.ui.nav_wallet.WalletTransactionFragment;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private final ArrayList<WalletTransaction> transactionArrayList;
    OnInvoiceClickListener onInvoiceClickListener;
    private WalletTransactionFragment walletTransactionFragment;

    public TransactionAdapter(ArrayList<WalletTransaction> transactionArrayList) {
        this.transactionArrayList = transactionArrayList;
        this.onInvoiceClickListener = walletTransactionFragment;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvTransactionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_transaction, parent, false);
        return new TransactionViewHolder(binding);
    }

    public interface OnInvoiceClickListener {
        void onInvoiceClick(WalletTransaction transaction);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final WalletTransaction transaction = transactionArrayList.get(position);
        holder.rvTransactionBinding.setTransaction(transaction);
        holder.bind(transaction);
        holder.checkEmpty(transaction);

        if (transaction.getTransactionId() != null) {
            holder.rvTransactionBinding.tvTransactionId.setText(transaction.getTransactionId());
        } else {
            holder.rvTransactionBinding.tvTransactionId.setText("N/A");
        }
        if (transaction.getOrderId()!=null && !transaction.getOrderId().isEmpty())
            holder.rvTransactionBinding.llInvoice.setVisibility(View.VISIBLE);
        else holder.rvTransactionBinding.llInvoice.setVisibility(View.INVISIBLE);
        holder.rvTransactionBinding.tvPaymentMode.setText("Online");

        holder.rvTransactionBinding.btnDownloadInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInvoiceClickListener.onInvoiceClick(transaction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionArrayList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        RvTransactionBinding rvTransactionBinding;

        public TransactionViewHolder(RvTransactionBinding rvTransactionBinding) {
            super(rvTransactionBinding.getRoot());
            this.rvTransactionBinding = rvTransactionBinding;

        }

        public void bind(WalletTransaction transaction) {
            rvTransactionBinding.setVariable(BR.transaction, transaction);
            rvTransactionBinding.executePendingBindings();
        }

        public void checkEmpty(WalletTransaction transaction) {
            if (transaction.getTransactionId()==null)
                rvTransactionBinding.llTransactionId.setVisibility(View.INVISIBLE);
            if (transaction.getCreatedAt()==null)
                rvTransactionBinding.llDate.setVisibility(View.INVISIBLE);
        }
    }

}
