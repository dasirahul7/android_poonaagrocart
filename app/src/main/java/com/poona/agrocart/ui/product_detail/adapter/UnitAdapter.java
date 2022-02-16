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
import com.poona.agrocart.data.network.reponses.ProductListResponse;
import com.poona.agrocart.widgets.CustomTextView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.WeightHolder> {

    private List<ProductListResponse.ProductUnit> weight = new ArrayList<>();
    private final Context context;
    private int mSelectedItem = 0;
    private OnUnitClickListener onUnitClickListener;
    private int isInCart;

    public UnitAdapter(List<ProductListResponse.ProductUnit> weight,int isInCart, Context context,
                       OnUnitClickListener onUnitClickListener) {
        this.weight = weight;
        this.isInCart = isInCart;
        this.context = context;
        this.onUnitClickListener = onUnitClickListener;
    }
    public interface OnUnitClickListener{
        void OnUniClick(ProductListResponse.ProductUnit unit);
    }

    @NonNull
    @Override
    public WeightHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeightHolder(LayoutInflater.from(context).inflate(R.layout.row_weight, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeightHolder holder, int position) {
        holder.weightTxt.setText(MessageFormat.format("{0}{1}", weight.get(position).getWeight(), weight.get(position).getUnitName()));
        holder.radioButton.setChecked(mSelectedItem == position);
            if (mSelectedItem == position) {
                holder.weightCard.setStrokeColor(ContextCompat.getColor(context, R.color.weight_border_color));
                holder.weightTxt.setBackgroundColor(ContextCompat.getColor(context, R.color.weight_fill_color));
                holder.weightTxt.setTextColor(ContextCompat.getColor(context, R.color.weight_border_color));
            } else {
                holder.weightCard.setStrokeColor(ContextCompat.getColor(context, R.color.color_horizontal_line));
                holder.weightTxt.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                holder.weightTxt.setTextColor(ContextCompat.getColor(context, R.color.color_grey_txt));
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
                if (isInCart==0) {
                    mSelectedItem = getAdapterPosition();
                    onUnitClickListener.OnUniClick(weight.get(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });
        }
    }
}
