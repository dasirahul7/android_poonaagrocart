package com.poona.agrocart.ui.nav_orders.order_view.adaptor;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderReviewCommentAdaptor extends RecyclerView.Adapter<OrderReviewCommentAdaptor.OrderReviewCommentViewHolder> {


    @NonNull
    @Override
    public OrderReviewCommentAdaptor.OrderReviewCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderReviewCommentAdaptor.OrderReviewCommentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrderReviewCommentViewHolder extends RecyclerView.ViewHolder {
        public OrderReviewCommentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
