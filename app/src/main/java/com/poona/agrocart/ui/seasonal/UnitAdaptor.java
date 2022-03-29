package com.poona.agrocart.ui.seasonal;

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
import com.poona.agrocart.data.network.responses.GetUnitResponse;
import com.poona.agrocart.data.network.responses.orderResponse.DeliverySlot;

import java.util.ArrayList;


public class UnitAdaptor extends ArrayAdapter<GetUnitResponse.UnitData> {

    public UnitAdaptor(Context context, ArrayList<GetUnitResponse.UnitData> unitDataArrayList) {
        super(context, 0, unitDataArrayList);
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
        GetUnitResponse.UnitData currentItem = getItem(position);
        if (currentItem != null) {
            if(position == 0){
                textViewShiftName.setText("Select Unit");
            }else {
                textViewShiftName.setText(currentItem.getUnitName());
            }
            textViewShiftName.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        }
        return convertView;

    }
}
