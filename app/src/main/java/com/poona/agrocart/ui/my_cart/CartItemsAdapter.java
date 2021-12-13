package com.poona.agrocart.ui.my_cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowProductItemBinding;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder>
{
    private final ArrayList<Product> cartItemArrayList;

    public CartItemsAdapter(ArrayList<Product> cartItemArrayList) {
        this.cartItemArrayList = cartItemArrayList;
    }

    @NonNull
    @Override
    public CartItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowProductItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_product_item, parent, false);
        return new CartItemsAdapter.CartItemsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsViewHolder holder, int position) {
        final Product cartItem = cartItemArrayList.get(position);
        holder.rvCartItemBinding.setProductModule(cartItem);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    public class CartItemsViewHolder extends RecyclerView.ViewHolder {
        RowProductItemBinding rvCartItemBinding;

        public CartItemsViewHolder(RowProductItemBinding rvCartItemBinding) {
            super(rvCartItemBinding.getRoot());
            this.rvCartItemBinding = rvCartItemBinding;
            this.rvCartItemBinding.txtItemOffer.setVisibility(View.GONE);
            this.rvCartItemBinding.imgPlus.setVisibility(View.GONE);
            this.rvCartItemBinding.closeLayout.setVisibility(View.VISIBLE);
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

        public void bind(Product cartItem) {
            if (cartItem.getLocation().isEmpty())
                rvCartItemBinding.tvLocation.setVisibility(View.INVISIBLE);
            rvCartItemBinding.setVariable(BR.cartItem, cartItem);
            rvCartItemBinding.executePendingBindings();
            rvCartItemBinding.ivCross.setOnClickListener(v -> {
                synchronized (cartItem){
                    cartItemArrayList.remove(cartItem);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
