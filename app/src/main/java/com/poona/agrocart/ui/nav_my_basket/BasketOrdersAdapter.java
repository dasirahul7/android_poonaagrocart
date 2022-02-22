package com.poona.agrocart.ui.nav_my_basket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvOrdersBasketBinding;
import com.poona.agrocart.ui.nav_my_basket.model.BasketOrder;

import java.util.ArrayList;

public class BasketOrdersAdapter extends RecyclerView.Adapter<BasketOrdersAdapter.BasketOrdersViewHolder> {
    private final ArrayList<BasketOrder> basketOrderArrayList;
    private final View view;
    private final boolean isWallet;

    public BasketOrdersAdapter(ArrayList<BasketOrder> basketOrderArrayList, View view, boolean isWallet) {
        this.basketOrderArrayList = basketOrderArrayList;
        this.view = view;
        this.isWallet = isWallet;
    }

    @NonNull
    @Override
    public BasketOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvOrdersBasketBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_orders_basket, parent, false);
        return new BasketOrdersAdapter.BasketOrdersViewHolder(binding, view, isWallet);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketOrdersViewHolder holder, int position) {
        final BasketOrder basketOrder = basketOrderArrayList.get(position);
        holder.rvOrdersBasketBinding.setBasketOrder(basketOrder);
        holder.bind(basketOrder);
    }

    @Override
    public int getItemCount() {
        return basketOrderArrayList.size();
    }

    public static class BasketOrdersViewHolder extends RecyclerView.ViewHolder {
        RvOrdersBasketBinding rvOrdersBasketBinding;

        public BasketOrdersViewHolder(RvOrdersBasketBinding rvOrdersBasketBinding, View view, boolean isWallet) {
            super(rvOrdersBasketBinding.getRoot());
            this.rvOrdersBasketBinding = rvOrdersBasketBinding;
            if (!isWallet)
                rvOrdersBasketBinding.cardviewOrder.setOnClickListener(v -> {
                    redirectToBasketOrderView(view);
                });

            if (isWallet)
                rvOrdersBasketBinding.tvBasketName.setVisibility(View.INVISIBLE);
            else
                rvOrdersBasketBinding.tvBasketName.setVisibility(View.VISIBLE);

        }

        private void redirectToBasketOrderView(View v) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isBasketVisible", true);
            Navigation.findNavController(v).navigate(R.id.action_nav_basket_to_orderViewFragment2, bundle);
        }

        public void bind(BasketOrder basketOrder) {
            rvOrdersBasketBinding.setVariable(BR.basketOrder, basketOrder);
            rvOrdersBasketBinding.executePendingBindings();
        }

    }

}
