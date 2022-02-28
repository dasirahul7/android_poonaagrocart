package com.poona.agrocart.ui.nav_orders.order_view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.CancelOrderCategoryRecyclerViewBinding;
import com.poona.agrocart.ui.nav_orders.model.CancelOrderCategoryList;

import java.util.List;

public class OrderCancelCategoryAdaptor extends RecyclerView.Adapter<OrderCancelCategoryAdaptor.OrderCancelCategoryViewHolder> {
    private List<CancelOrderCategoryList> cancelOrderCategoryLists;
    private Context context;

    public OrderCancelCategoryAdaptor(Context context, List<CancelOrderCategoryList> cancelOrderCategoryLists) {
        this.context=context;
        this.cancelOrderCategoryLists=cancelOrderCategoryLists;
    }

    @NonNull
    @Override
    public OrderCancelCategoryAdaptor.OrderCancelCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CancelOrderCategoryRecyclerViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.cancel_order_category_recycler_view,parent,false);

        return new OrderCancelCategoryAdaptor.OrderCancelCategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCancelCategoryAdaptor.OrderCancelCategoryViewHolder viewHolder, int position) {
        CancelOrderCategoryList cancelOrderCategoryList = cancelOrderCategoryLists.get(position);
        viewHolder.binding.setCancelOrderCategoryList(cancelOrderCategoryList);
        viewHolder.bind(cancelOrderCategoryList);

    }

    @Override
    public int getItemCount() {
        return cancelOrderCategoryLists.size();
    }

    public class OrderCancelCategoryViewHolder extends RecyclerView.ViewHolder {
        public CancelOrderCategoryRecyclerViewBinding binding;

        public OrderCancelCategoryViewHolder(CancelOrderCategoryRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding= binding;
        }

        public void bind(CancelOrderCategoryList cancelOrderCategoryList) {
            binding.setVariable(BR.cancelOrderCategoryList, cancelOrderCategoryList);
            binding.executePendingBindings();
        }
    }
}
