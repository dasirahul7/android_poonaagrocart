package com.poona.agrocart.ui.my_orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvOrderBinding;
import com.poona.agrocart.ui.my_orders.model.Order;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>
{
    private final ArrayList<Order> orderArrayList;
    private final Context context;
    private final View view;

    public OrdersAdapter(ArrayList<Order> orderArrayList, Context context,View view)
    {
        this.orderArrayList = orderArrayList;
        this.context=context;
        this.view=view;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RvOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_order, parent, false);
        return new OrdersAdapter.OrdersViewHolder(binding,view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position)
    {
        final Order order = orderArrayList.get(position);
        holder.rvOrderBinding.setOrder(order);
        holder.bind(order,context);
    }

    @Override
    public int getItemCount()
    {
        return orderArrayList.size();
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder
    {
        RvOrderBinding rvOrderBinding;

        public OrdersViewHolder(RvOrderBinding rvOrderBinding, View view)
        {
            super(rvOrderBinding.getRoot());
            this.rvOrderBinding=rvOrderBinding;
            rvOrderBinding.cardviewOrder.setOnClickListener(v -> {
                redirectToBasketOrderView(view);
            });
        }

        private void redirectToBasketOrderView(View view)
        {
            Bundle bundle=new Bundle();
            bundle.putBoolean("isBasketVisible",false);
            Navigation.findNavController(view).navigate(R.id.action_nav_orders_to_orderViewFragment2,bundle);
        }

        @SuppressLint("ResourceType")
        public void bind(Order order, Context context)
        {
            rvOrderBinding.setVariable(BR.order,order);
            if(order.getStatus().equals(context.getString(R.string.in_process)))
            {
                rvOrderBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_in_process)));
            }
            else if(order.getStatus().equals(context.getString(R.string.delivered)))
            {
                rvOrderBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_delivered)));
            }
            else if(order.getStatus().equals(context.getString(R.string.confirmed)))
            {
                rvOrderBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_confirmed)));
            }
            else
            {
                rvOrderBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_cancelled)));
            }
            rvOrderBinding.executePendingBindings();
        }
    }
}
