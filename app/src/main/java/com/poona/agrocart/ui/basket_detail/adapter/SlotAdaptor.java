package com.poona.agrocart.ui.basket_detail.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.BasketDetailsResponse;
import com.poona.agrocart.data.network.responses.help_center_response.TicketTypeResponse;
import com.poona.agrocart.data.network.responses.orderResponse.DeliverySlot;

import java.util.ArrayList;
import java.util.List;


public class SlotAdaptor extends ArrayAdapter<DeliverySlot> {

    public SlotAdaptor(Context context, ArrayList<DeliverySlot> subscriptionTypes) {
        super(context, 0, subscriptionTypes);
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

        TextView textViewShiftName = convertView.findViewById(R.id.text_view_name);
        if (position == 0) {
            // Set the hint text color gray
            textViewShiftName.setTextColor(Color.BLACK);
        } else {
            textViewShiftName.setTextColor(Color.BLACK);
        }
        DeliverySlot currentItem = getItem(position);
        if (currentItem != null) {
            textViewShiftName.setText(currentItem.slotTime);
            textViewShiftName.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        }
        return convertView;

    }
}
