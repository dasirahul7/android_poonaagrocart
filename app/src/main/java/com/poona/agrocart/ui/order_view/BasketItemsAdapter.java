package com.poona.agrocart.ui.order_view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvBasketDetailBinding;

import java.util.ArrayList;

public class BasketItemsAdapter extends RecyclerView.Adapter<BasketItemsAdapter.BasketItemViewHolder>
{
    private ArrayList<BasketItem> basketItems;

    public BasketItemsAdapter(ArrayList<BasketItem> basketItems) {
        this.basketItems = basketItems;
    }

    @NonNull
    @Override
    public BasketItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvBasketDetailBinding binding = DataBindingUtil.inflate
                (LayoutInflater.from(parent.getContext()),
                        R.layout.rv_basket_detail, parent, false);
        return new BasketItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketItemViewHolder holder, int position) {
        final BasketItem basketItem = basketItems.get(position);
        holder.rvBasketDetailBinding.setBasketItemModel(basketItem);
        holder.bind(basketItem);
    }

    @Override
    public int getItemCount() {
        return basketItems.size();
    }

    public static class BasketItemViewHolder extends RecyclerView.ViewHolder {

        RvBasketDetailBinding rvBasketDetailBinding;

        public BasketItemViewHolder(RvBasketDetailBinding rvBasketDetailBinding) {
            super(rvBasketDetailBinding.getRoot());
            this.rvBasketDetailBinding=rvBasketDetailBinding;

        }

        public void bind(BasketItem basketItem)
        {
            rvBasketDetailBinding.setVariable(BR.basketItemModel,basketItem);
            rvBasketDetailBinding.executePendingBindings();
        }
    }
}
