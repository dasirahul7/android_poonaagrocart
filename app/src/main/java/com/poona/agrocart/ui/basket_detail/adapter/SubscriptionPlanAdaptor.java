package com.poona.agrocart.ui.basket_detail.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;

import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.SubscriptionPlanTypeRecyclerItemBinding;

import com.poona.agrocart.ui.basket_detail.model.SubscriptionPlan;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionPlanAdaptor extends RecyclerView.Adapter<SubscriptionPlanAdaptor.SubscriptionAdaptorViewHolder> {
    private List<SubscriptionPlan> subscriptionPlans = new ArrayList<>();
    private Context context;
    RadioButton radioButton = null;
    public int mSelectedItem = -1;



    public SubscriptionPlanAdaptor(Context context, List<SubscriptionPlan> subscriptionPlans){

        this.subscriptionPlans = subscriptionPlans;
        this.context = context;
    }

    @NonNull
    @Override
    public SubscriptionPlanAdaptor.SubscriptionAdaptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubscriptionPlanTypeRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.subscription_plan_type_recycler_item, parent, false);


        return new SubscriptionAdaptorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionPlanAdaptor.SubscriptionAdaptorViewHolder holder, int position) {
        SubscriptionPlan subscriptionPlan = subscriptionPlans.get(position);
        holder.subscriptionPlanTypeRecyclerItemBinding.setSubscriptionPlan(subscriptionPlan);
        holder.bind(subscriptionPlan);
        radioButton = holder.subscriptionPlanTypeRecyclerItemBinding.radio;

        /* radio Selected Item */
        radioButton.setChecked(mSelectedItem == position);
        if (mSelectedItem == position) {
            holder.subscriptionPlanTypeRecyclerItemBinding.cvSubscriptionPlan.setStrokeColor(ContextCompat.getColor(context, R.color.weight_border_color));
            holder.subscriptionPlanTypeRecyclerItemBinding.tvSubscriptionPlan.setBackgroundColor(ContextCompat.getColor(context, R.color.weight_fill_color));
            holder.subscriptionPlanTypeRecyclerItemBinding.tvSubscriptionPlan.setTextColor(ContextCompat.getColor(context, R.color.weight_border_color));
        } else {
            holder.subscriptionPlanTypeRecyclerItemBinding.cvSubscriptionPlan.setStrokeColor(ContextCompat.getColor(context, R.color.color_horizontal_line));
            holder.subscriptionPlanTypeRecyclerItemBinding.tvSubscriptionPlan.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.subscriptionPlanTypeRecyclerItemBinding.tvSubscriptionPlan.setTextColor(ContextCompat.getColor(context, R.color.color_grey_txt));
        }

        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean radioBoolean ) {
                if (radioButton != null) {
                    radioButton.setChecked(false);
                }
                //store the clicked radiobutton
                radioButton = holder.subscriptionPlanTypeRecyclerItemBinding.radio;
            }
        });


    }

    @Override
    public int getItemCount() {
        return subscriptionPlans.size();
    }

    public class SubscriptionAdaptorViewHolder extends RecyclerView.ViewHolder {
        SubscriptionPlanTypeRecyclerItemBinding subscriptionPlanTypeRecyclerItemBinding;

        @SuppressLint("NotifyDataSetChanged")
        public SubscriptionAdaptorViewHolder(SubscriptionPlanTypeRecyclerItemBinding itemView) {
            super(itemView.getRoot());
            this.subscriptionPlanTypeRecyclerItemBinding = itemView;

            itemView.cvSubscriptionPlan.setOnClickListener(view -> {
                mSelectedItem = getLayoutPosition();
                //strReasonType = "check";
               // onTypeClickListener.itemClick(strReasonType,cancelOrderReasonLists.get(getLayoutPosition()).getCancelId());
                notifyDataSetChanged();
            });

        }

        public void bind(SubscriptionPlan subscriptionPlan) {
            subscriptionPlanTypeRecyclerItemBinding.setVariable(BR.subscriptionPlan, subscriptionPlan);
            subscriptionPlanTypeRecyclerItemBinding.executePendingBindings();
        }
    }
}
