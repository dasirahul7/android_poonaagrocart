package com.poona.agrocart.ui.product_detail.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvProductCommentBinding;
import com.poona.agrocart.ui.product_detail.model.ProductComment;
import java.util.ArrayList;

public class ProductCommentsAdapter extends RecyclerView.Adapter<ProductCommentsAdapter.CommentViewHolder>
{
    private ArrayList<ProductComment> commentArrayList;

    public ProductCommentsAdapter(ArrayList<ProductComment> commentArrayList)
    {
        this.commentArrayList = commentArrayList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RvProductCommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_product_comment, parent, false);
        return new ProductCommentsAdapter.CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position)
    {
        final ProductComment comment = commentArrayList.get(position);
        holder.rvProductCommentBinding.setProductComment(comment);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder
    {
        RvProductCommentBinding rvProductCommentBinding;

        public CommentViewHolder(RvProductCommentBinding rvProductCommentBinding)
        {
            super(rvProductCommentBinding.getRoot());
            this.rvProductCommentBinding = rvProductCommentBinding;
        }

        public void bind(ProductComment comment)
        {
            rvProductCommentBinding.setVariable(BR.productComment,comment);
            rvProductCommentBinding.executePendingBindings();
        }
    }
}
