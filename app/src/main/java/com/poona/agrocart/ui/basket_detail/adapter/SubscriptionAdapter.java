package com.poona.agrocart.ui.basket_detail.adapter;

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
import com.poona.agrocart.data.network.responses.BasketDetailsResponse;
import com.poona.agrocart.widgets.CustomTextView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.WeightHolder> {

    private final Context context;
    private ArrayList<BasketDetailsResponse.SubscriptionType> subscriptionTypes = new ArrayList<>();
    public int mSelectedItem = 0;
    private final onSubTypeClickListener onSubTypeClicklistener;

    public SubscriptionAdapter(ArrayList<BasketDetailsResponse.SubscriptionType> subscriptionTypes, Context context,
                               onSubTypeClickListener onSubTypeClicklistener) {
        this.subscriptionTypes = subscriptionTypes;
        this.context = context;
        this.onSubTypeClicklistener = onSubTypeClicklistener;
    }

    @NonNull
    @Override
    public WeightHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeightHolder(LayoutInflater.from(context).inflate(R.layout.row_weight, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeightHolder holder, @SuppressLint("RecyclerView") int position) {
        BasketDetailsResponse.SubscriptionType unit = subscriptionTypes.get(position);
        holder.weightTxt.setText(subscriptionTypes.get(position).getSubscriptionTypeName());
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
        return subscriptionTypes.size();
    }

    public interface onSubTypeClickListener {
        void OnSubTypeClick(BasketDetailsResponse.SubscriptionType type);
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
                mSelectedItem = getBindingAdapterPosition();
                onSubTypeClicklistener.OnSubTypeClick(subscriptionTypes.get(getBindingAdapterPosition()));
                notifyDataSetChanged();
            });
        }
    }
}
