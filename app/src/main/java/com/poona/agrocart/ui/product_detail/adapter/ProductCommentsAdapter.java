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
    private boolean full;

    public ProductCommentsAdapter(ArrayList<ProductComment> commentArrayList, boolean full)
    {
        this.commentArrayList = commentArrayList;
        this.full = full;
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
        if (full)
            return commentArrayList.size();
        else return 3;
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
            rvProductCommentBinding.ratingBar.setRating(comment.getRating());
            rvProductCommentBinding.setVariable(BR.productComment,comment);
            rvProductCommentBinding.executePendingBindings();
        }
    }
}
