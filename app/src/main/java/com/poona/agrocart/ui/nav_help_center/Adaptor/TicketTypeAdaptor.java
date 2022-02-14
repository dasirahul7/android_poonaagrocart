package com.poona.agrocart.ui.nav_help_center.Adaptor;

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
import com.poona.agrocart.data.network.reponses.help_center_response.TicketTypeResponse;

import java.util.List;


public class TicketTypeAdaptor extends ArrayAdapter<TicketTypeResponse.TicketType> {

    public TicketTypeAdaptor(Context context, List<TicketTypeResponse.TicketType> ticketTypes) {
        super(context, 0, ticketTypes);
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

        TextView textViewShiftName= convertView.findViewById(R.id.text_view_name);
        if (position == 0) {
            // Set the hint text color gray
            textViewShiftName.setTextColor(Color.parseColor("#86C11F"));
        } else {
            textViewShiftName.setTextColor(Color.BLACK);
        }
        TicketTypeResponse.TicketType currentItem=getItem(position);
        if(currentItem!=null) {
            textViewShiftName.setText(currentItem.getTicketType());
            textViewShiftName.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        }
        return convertView;

    }
}
