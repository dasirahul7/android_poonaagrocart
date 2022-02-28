package com.poona.agrocart.ui.nav_orders.order_view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;

import com.poona.agrocart.databinding.CancelOrderReasonRecyclerViewBinding;
import com.poona.agrocart.ui.nav_orders.model.CancelOrderReasonList;

import java.util.ArrayList;
import java.util.List;

public class OrderCancelReasonAdaptor extends RecyclerView.Adapter<OrderCancelReasonAdaptor.OrderCancelReasonViewHolder> {

    private List<CancelOrderReasonList> cancelOrderReasonLists = new ArrayList<>();
    private Context context;

    public OrderCancelReasonAdaptor(Context context, List<CancelOrderReasonList> cancelOrderReasonLists) {
        this.context=context;
        this.cancelOrderReasonLists=cancelOrderReasonLists;
    }

    @NonNull
    @Override
    public OrderCancelReasonAdaptor.OrderCancelReasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       CancelOrderReasonRecyclerViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.cancel_order_reason_recycler_view,parent,false);

        return new OrderCancelReasonAdaptor.OrderCancelReasonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCancelReasonAdaptor.OrderCancelReasonViewHolder viewHolder, int position) {
        CancelOrderReasonList cancelOrderReasonList = cancelOrderReasonLists.get(position);
        viewHolder.binding.setCancelOrderReasonList(cancelOrderReasonList);
        viewHolder.bind(cancelOrderReasonList);
    }

    @Override
    public int getItemCount() {
        return cancelOrderReasonLists.size();
    }

    public class OrderCancelReasonViewHolder extends RecyclerView.ViewHolder {

        public CancelOrderReasonRecyclerViewBinding binding;

        public OrderCancelReasonViewHolder(CancelOrderReasonRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding= binding;
        }

        public void bind(CancelOrderReasonList cancelOrderReasonList) {
            binding.setVariable(BR.cancelOrderReasonList, cancelOrderReasonList);
            binding.executePendingBindings();
        }
    }
}
