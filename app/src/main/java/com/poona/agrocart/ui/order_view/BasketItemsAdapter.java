package com.poona.agrocart.ui.order_view;

import android.view.LayoutInflater;
import android.view.View;
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
    private boolean isBasketVisible;

    public BasketItemsAdapter(ArrayList<BasketItem> basketItems,boolean isBasketVisible)
    {
        this.basketItems = basketItems;
        this.isBasketVisible=isBasketVisible;
    }

    @NonNull
    @Override
    public BasketItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RvBasketDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.rv_basket_detail, parent, false);
        return new BasketItemViewHolder(binding,isBasketVisible);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketItemViewHolder holder, int position)
    {
        final BasketItem basketItem = basketItems.get(position);
        holder.rvBasketDetailBinding.setBasketItemModel(basketItem);
        holder.bind(basketItem);
    }

    @Override
    public int getItemCount()
    {
        return basketItems.size();
    }

    public static class BasketItemViewHolder extends RecyclerView.ViewHolder
    {

        RvBasketDetailBinding rvBasketDetailBinding;
        boolean isBasketVisible;

        public BasketItemViewHolder(RvBasketDetailBinding rvBasketDetailBinding,boolean isBasketVisible)
        {
            super(rvBasketDetailBinding.getRoot());
            this.rvBasketDetailBinding=rvBasketDetailBinding;
            this.isBasketVisible=isBasketVisible;
            if(this.isBasketVisible)
            {
                rvBasketDetailBinding.tvDateAndTime.setVisibility(View.VISIBLE);
                rvBasketDetailBinding.tvQuantity.setVisibility(View.GONE);
                rvBasketDetailBinding.tvOrderStatus.setVisibility(View.VISIBLE);
                rvBasketDetailBinding.tvOrderPrice.setVisibility(View.VISIBLE);
                rvBasketDetailBinding.tvProductOrderPrice.setVisibility(View.GONE);
            }
            else
            {
                rvBasketDetailBinding.tvDateAndTime.setVisibility(View.INVISIBLE);
                rvBasketDetailBinding.tvQuantity.setVisibility(View.VISIBLE);
                rvBasketDetailBinding.tvOrderStatus.setVisibility(View.GONE);
                rvBasketDetailBinding.tvOrderPrice.setVisibility(View.INVISIBLE);
                rvBasketDetailBinding.tvProductOrderPrice.setVisibility(View.VISIBLE);
            }
        }

        public void bind(BasketItem basketItem)
        {
            rvBasketDetailBinding.setVariable(BR.basketItemModel,basketItem);
            rvBasketDetailBinding.executePendingBindings();
        }
    }
}
