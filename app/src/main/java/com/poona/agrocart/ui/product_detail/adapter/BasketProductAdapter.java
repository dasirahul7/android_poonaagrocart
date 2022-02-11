package com.poona.agrocart.ui.product_detail.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.reponses.BasketResponse;
import com.poona.agrocart.databinding.RvBasketContentsListBinding;

import java.util.ArrayList;

public class BasketProductAdapter extends RecyclerView.Adapter<BasketProductAdapter.BasketContentsViewHolder>
{
    private final ArrayList<BasketResponse.BasketProduct> basketContentArrayList;

    public BasketProductAdapter(ArrayList<BasketResponse.BasketProduct> basketContentArrayList)
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
        final BasketResponse.BasketProduct basketProduct = basketContentArrayList.get(position);
        holder.rvBasketContentsListBinding.setBasketContent(basketProduct);
        holder.bind(basketProduct);
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

        public void bind(BasketResponse.BasketProduct basketContent)
        {
            rvBasketContentsListBinding.setVariable(BR.basketContent, basketContent);
            rvBasketContentsListBinding.executePendingBindings();
        }
    }
}
