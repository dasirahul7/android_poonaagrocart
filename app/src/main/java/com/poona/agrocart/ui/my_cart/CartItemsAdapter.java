package com.poona.agrocart.ui.my_cart;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvCartItemBinding;

import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder>
{
    private ArrayList<CartItem> cartItemArrayList;

    public CartItemsAdapter(ArrayList<CartItem> cartItemArrayList) {
        this.cartItemArrayList = cartItemArrayList;
    }

    @NonNull
    @Override
    public CartItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvCartItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_cart_item, parent, false);
        return new CartItemsAdapter.CartItemsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsViewHolder holder, int position) {
        final CartItem cartItem = cartItemArrayList.get(position);
        holder.rvCartItemBinding.setCartItem(cartItem);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    public static class CartItemsViewHolder extends RecyclerView.ViewHolder {
        RvCartItemBinding rvCartItemBinding;

        public CartItemsViewHolder(RvCartItemBinding rvCartItemBinding) {
            super(rvCartItemBinding.getRoot());
            this.rvCartItemBinding = rvCartItemBinding;
            rvCartItemBinding.ivPlus.setOnClickListener(v -> {
                increaseQuantity();
            });

            rvCartItemBinding.ivMinus.setOnClickListener(v -> {
                decreaseQuantity();
            });
        }

        private void decreaseQuantity() {
            int quantity = Integer.parseInt(rvCartItemBinding.etQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                rvCartItemBinding.etQuantity.setText(String.valueOf(quantity));
            }
        }

        private void increaseQuantity() {
            int quantity = Integer.parseInt(rvCartItemBinding.etQuantity.getText().toString());
            quantity++;
            rvCartItemBinding.etQuantity.setText(String.valueOf(quantity));
        }

        public void bind(CartItem cartItem) {
            rvCartItemBinding.setVariable(BR.cartItem, cartItem);
            rvCartItemBinding.executePendingBindings();
        }
    }
}
