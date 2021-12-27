package com.poona.agrocart.ui.product_detail.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.poona.agrocart.R;
import com.poona.agrocart.widgets.CustomTextView;

import java.util.ArrayList;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.WeightHolder> {

    private ArrayList<String> weight = new ArrayList<>();
    private final Context context;
    private int mSelectedItem=-1;

    public WeightAdapter(ArrayList<String> weight, Context context) {
        this.weight = weight;
        this.context = context;
    }

    @NonNull
    @Override
    public WeightHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeightHolder(LayoutInflater.from(context).inflate(R.layout.row_weight,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeightHolder holder, int position) {
        holder.weightTxt.setText(weight.get(position));
            holder.radioButton.setChecked(mSelectedItem==position);
            if (mSelectedItem == position) {
            holder.weightCard.setStrokeColor(ContextCompat.getColor(context,R.color.weight_border_color));
            holder.weightTxt.setBackgroundColor(ContextCompat.getColor(context,R.color.weight_fill_color));
            holder.weightTxt.setTextColor(ContextCompat.getColor(context,R.color.weight_border_color));
            } else {
            holder.weightCard.setStrokeColor(ContextCompat.getColor(context,R.color.color_horizontal_line));
            holder.weightTxt.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return weight.size();
    }

    public class WeightHolder extends RecyclerView.ViewHolder {
        CustomTextView weightTxt;
        MaterialCardView weightCard;
        RadioButton radioButton;
        @SuppressLint("UseCompatLoadingForDrawables")
        public WeightHolder(@NonNull View itemView) {
            super(itemView);
            weightCard = itemView.findViewById(R.id.cv_weight);
            weightTxt = itemView.findViewById(R.id.tv_weight);
            radioButton = itemView.findViewById(R.id.radio);
            itemView.setOnClickListener(v -> {
                mSelectedItem = getAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }
}
