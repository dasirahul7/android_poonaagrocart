package com.poona.agrocart.ui.nav_profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.poona.agrocart.R;
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.widgets.CustomCheckedTextView;
import com.poona.agrocart.widgets.CustomTextView;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<BasicDetails> {
    private List<BasicDetails> items;
    private Context context;

    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull List<BasicDetails> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.custom_spinner_list_item_checked, null);
        }
        CustomCheckedTextView tvTitle = v.findViewById(R.id.cct_select);
        tvTitle.setText(items.get(position).getName());

        return v;
    }

    @Override
    public BasicDetails getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.custom_spinner_list_item, null);
        }
        CustomTextView tvTitle = v.findViewById(R.id.tv_spinner_title);
        tvTitle.setText(items.get(position).getName());

        return v;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public List<BasicDetails> getItems() {
        return items;
    }
}