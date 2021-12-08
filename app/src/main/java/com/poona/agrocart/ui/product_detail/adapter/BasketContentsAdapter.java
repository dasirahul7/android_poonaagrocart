package com.poona.agrocart.ui.product_detail.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvBasketContentsListBinding;
import com.poona.agrocart.ui.product_detail.model.BasketContent;

import java.util.ArrayList;

public class BasketContentsAdapter extends RecyclerView.Adapter<BasketContentsAdapter.BasketContentsViewHolder>
{
    private ArrayList<BasketContent> basketContentArrayList;

    public BasketContentsAdapter(ArrayList<BasketContent> basketContentArrayList)
    {
        this.basketContentArrayList = basketContentArrayList;
    }

    @NonNull
    @Override
    public BasketContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RvBasketContentsListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_basket_contents_list, parent, false);
        return new BasketContentsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketContentsViewHolder holder, int position)
    {
        final BasketContent basketContent = basketContentArrayList.get(position);
        holder.rvBasketContentsListBinding.setBasketContent(basketContent);
        holder.bind(basketContent);
    }

    @Override
    public int getItemCount()
    {
        return basketContentArrayList.size();
    }

    public static class BasketContentsViewHolder extends RecyclerView.ViewHolder
    {
        RvBasketContentsListBinding rvBasketContentsListBinding;

        public BasketContentsViewHolder(RvBasketContentsListBinding rvBasketContentsListBinding)
        {
            super(rvBasketContentsListBinding.getRoot());
            this.rvBasketContentsListBinding = rvBasketContentsListBinding;
        }

        public void bind(BasketContent basketContent)
        {
            rvBasketContentsListBinding.setVariable(BR.basketContent, basketContent);
            rvBasketContentsListBinding.executePendingBindings();
        }
    }
}
