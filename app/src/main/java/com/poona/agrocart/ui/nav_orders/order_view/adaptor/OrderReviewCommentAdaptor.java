package com.poona.agrocart.ui.nav_orders.order_view.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.OrderReviewCommentRecyclerViewItemBinding;

public class OrderReviewCommentAdaptor extends RecyclerView.Adapter<OrderReviewCommentAdaptor.OrderReviewCommentViewHolder> {


    @NonNull
    @Override
    public OrderReviewCommentAdaptor.OrderReviewCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*OrderReviewCommentRecyclerViewItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.order_review_comment_recycler_view_item, parent, false);
        return new OrderReviewCommentAdaptor.OrderReviewCommentViewHolder(binding);*/
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
