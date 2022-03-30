package com.poona.agrocart.ui.nav_wallet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.orderResponse.DeliverySlot;
import com.poona.agrocart.data.network.responses.walletTransaction.TransactionType;

import java.util.ArrayList;


public class TypeAdaptor extends ArrayAdapter<TransactionType> {

    public TypeAdaptor(Context context, ArrayList<TransactionType> transactionTypes) {
        super(context, 0, transactionTypes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);

    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_spinner, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.text_view_name);


        TransactionType currentItem = getItem(position);
        if (currentItem != null) {
            if (position == 0) {
                // Set the hint text color gray
                textView.setText("Select Transaction mode");
            } else {
                textView.setText(currentItem.getWalletTransactionType());
            }


            textView.setGravity(Gravity.CENTER_VERTICAL);
        }
        return convertView;

    }
}
