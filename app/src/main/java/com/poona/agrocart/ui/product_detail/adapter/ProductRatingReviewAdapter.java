package com.poona.agrocart.ui.product_detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.ProductDetailsResponse;
import com.poona.agrocart.data.network.responses.Review;
import com.poona.agrocart.databinding.RvProductCommentBinding;
import com.poona.agrocart.ui.product_detail.ProductDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class ProductRatingReviewAdapter extends RecyclerView.Adapter<ProductRatingReviewAdapter.CommentViewHolder> {
    private List<Review> reviewsList;
    private Context context;
    private int from=0;
    private static int DETAIL =0;
    private static int REVIEW =1;

    public ProductRatingReviewAdapter(Context context, List<Review> reviewsList, int from) {
        this.context=context;
        this.reviewsList=reviewsList;
        this.from=from;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvProductCommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_product_comment, parent, false);
        return new ProductRatingReviewAdapter.CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Review review = reviewsList.get(position);
        holder.rvProductCommentBinding.setProductViewModel(review);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {

        if(from==DETAIL){
            if(reviewsList.size() > 2){
                return 3;
            }else {
                return reviewsList.size();
            }
        }else {
            return reviewsList.size();
        }
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        RvProductCommentBinding rvProductCommentBinding;

        public CommentViewHolder(RvProductCommentBinding rvProductCommentBinding) {
            super(rvProductCommentBinding.getRoot());
            this.rvProductCommentBinding = rvProductCommentBinding;
        }

        public void bind(Review review) {
            rvProductCommentBinding.ratingBar.setRating(Float.parseFloat(review.getRating()));
            rvProductCommentBinding.setVariable(BR.productViewModel, review);
            rvProductCommentBinding.executePendingBindings();
        }
    }
}
