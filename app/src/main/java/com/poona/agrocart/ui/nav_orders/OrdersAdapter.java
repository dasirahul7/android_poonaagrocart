package com.poona.agrocart.ui.nav_orders;

import static com.poona.agrocart.app.AppConstants.ORDER_ID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.myOrderResponse.OrderListResponse;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.MyOrderDetailsResponse;
import com.poona.agrocart.databinding.RvOrderBinding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {
    private final  ArrayList<OrderListResponse.Order> orderArrayList;
    private final Context context;
    private final View view;
    MyOrdersFragment myOrdersFragment;


    public OrdersAdapter(ArrayList<OrderListResponse.Order> orderArrayList, Context context, View view, MyOrdersFragment myOrdersFragment) {
        this.orderArrayList = orderArrayList;
        this.context = context;
        this.view = view;
        this.myOrdersFragment = myOrdersFragment;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_order, parent, false);
        return new OrdersAdapter.OrdersViewHolder(binding, view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        final OrderListResponse.Order order = orderArrayList.get(position);
        holder.rvOrderBinding.setOrder(order);
        holder.bind(order, context);


        String strTotalAmount = order.getPaidAmount();
        String strTotalQuantity = order.getTotalQuantity();

        if(strTotalAmount == null){
            holder.rvOrderBinding.tvTotalAmount.setText(context.getString(R.string.total_amount_null));
        }else {
            holder.rvOrderBinding.tvTotalAmount.setText(context.getString(R.string.text_rs) +" "+ order.getPaidAmount());
        }

        if(strTotalQuantity == null){
            holder.rvOrderBinding.tvTotalQuantity.setText(context.getString(R.string.total_quantity_null));
        }else {
            holder.rvOrderBinding.tvTotalQuantity.setText(order.getTotalQuantity());
        }

        String selectedDate = order.getCreatedAt();

        String txtDisplayDate = "";
        try {
            txtDisplayDate = myOrdersFragment.formatDate(selectedDate, "yyyy-mm-dd hh:mm:ss", "MMM dd, yyyy hh:mm aa");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.rvOrderBinding.tvOrderDate.setText(txtDisplayDate);

    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {
        RvOrderBinding rvOrderBinding;



        public OrdersViewHolder(RvOrderBinding rvOrderBinding, View view) {
            super(rvOrderBinding.getRoot());
            this.rvOrderBinding = rvOrderBinding;


            rvOrderBinding.cardviewOrder.setOnClickListener(v -> {
                redirectToBasketOrderView(view, orderArrayList.get(getLayoutPosition()).getOrderId());
            });

        }

        private void redirectToBasketOrderView(View view, String orderId) {
            Bundle bundle = new Bundle();
            bundle.putString(ORDER_ID, orderId);
            bundle.putBoolean("isBasketVisible", false);
            Navigation.findNavController(view).navigate(R.id.action_nav_orders_to_orderViewFragment2, bundle);
        }

        @SuppressLint("ResourceType")
        public void bind(OrderListResponse.Order order, Context context) {
            rvOrderBinding.setVariable(BR.order, order);
            switch (order.getOrderStatus()) {
                case "3":
                    rvOrderBinding.tvOrderStatus.setText(context.getString(R.string.in_process));
                    rvOrderBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_in_process)));
                    break;
                case "4":
                    rvOrderBinding.tvOrderStatus.setText(context.getString(R.string.delivered));
                    rvOrderBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_delivered)));
                    break;
                case "2":
                    rvOrderBinding.tvOrderStatus.setText(context.getString(R.string.confirmed));
                    rvOrderBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_confirmed)));
                    break;
                case "5":
                    rvOrderBinding.tvOrderStatus.setText(context.getString(R.string.cancelled));
                    rvOrderBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_cancelled)));
                    break;
                default:
                    rvOrderBinding.tvOrderStatus.setText(context.getString(R.string.pending));
                    rvOrderBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_order_pending)));
                    break;
            }
            rvOrderBinding.executePendingBindings();
        }
    }
}
