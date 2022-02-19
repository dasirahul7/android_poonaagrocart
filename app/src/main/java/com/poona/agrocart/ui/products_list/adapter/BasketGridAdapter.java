package com.poona.agrocart.ui.products_list.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.BasketResponse;
import com.poona.agrocart.databinding.RowBasketListItemBinding;
import com.poona.agrocart.ui.home.adapter.BasketAdapter;

import java.util.ArrayList;

public class BasketGridAdapter extends RecyclerView.Adapter<BasketGridAdapter.BasketGridHolder> {
    private final ArrayList<BasketResponse.Basket> basketArrayList;
    private RowBasketListItemBinding rowBasketBinding;
    private final BasketAdapter.OnBasketClickListener onBasketClickListener;

    public BasketGridAdapter(ArrayList<BasketResponse.Basket> basketArrayList, BasketAdapter.OnBasketClickListener onBasketClickListener) {
        this.basketArrayList = basketArrayList;
        this.onBasketClickListener = onBasketClickListener;
    }

    @NonNull
    @Override
    public BasketGridHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowBasketListItemBinding binding = DataBindingUtil.inflate
                (LayoutInflater.from(parent.getContext()),
                        R.layout.row_basket_list_item, parent, false);
        return new BasketGridHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketGridHolder holder, int position) {
        final BasketResponse.Basket vegetable = basketArrayList.get(position);
        holder.basketListItemBinding.setBasket(vegetable);
        holder.bind(vegetable);
    }

    @Override
    public int getItemCount() {
        return basketArrayList.size();
    }

    public class BasketGridHolder extends RecyclerView.ViewHolder {
        RowBasketListItemBinding basketListItemBinding;

        public BasketGridHolder(RowBasketListItemBinding productListItemBinding) {
            super(productListItemBinding.getRoot());
            this.basketListItemBinding = productListItemBinding;
        }

        public void bind(BasketResponse.Basket basket) {
            basketListItemBinding.setVariable(BR.basket, basket);
            basketListItemBinding.executePendingBindings();
            itemView.setOnClickListener(view -> {
                onBasketClickListener.OnBasketClick(basket);
            });
        }
    }
}
