package com.poona.agrocart.ui.nav_my_cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowProductItemBinding;
import com.poona.agrocart.ui.home.model.ProductOld;

import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder>
{
    private final ArrayList<ProductOld> cartItemArrayList;
    private OnClickCart onCartItemClick;

    public interface OnClickCart {
        void onItemClick(int position);
    }

    public void setOnCartItemClick(OnClickCart onCartItemClick) {
        this.onCartItemClick = onCartItemClick;
    }

    public CartItemsAdapter(ArrayList<ProductOld> cartItemArrayList) {
        this.cartItemArrayList = cartItemArrayList;
    }

    @NonNull
    @Override
    public CartItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowProductItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_product_item, parent, false);
        return new CartItemsAdapter.CartItemsViewHolder(binding,onCartItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsViewHolder holder, int position) {
        final ProductOld cartItem = cartItemArrayList.get(position);
        holder.rvCartItemBinding.setProductOldModule(cartItem);
        holder.bind(cartItem,position);
    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    public class CartItemsViewHolder extends RecyclerView.ViewHolder {
        RowProductItemBinding rvCartItemBinding;
        OnClickCart onCartItemClick;

        public CartItemsViewHolder(RowProductItemBinding rvCartItemBinding,
                                   OnClickCart onCartItemClick) {
            super(rvCartItemBinding.getRoot());
            this.rvCartItemBinding = rvCartItemBinding;
            this.onCartItemClick = onCartItemClick;
            this.rvCartItemBinding.txtItemOffer.setVisibility(View.GONE);
            this.rvCartItemBinding.imgPlus.setVisibility(View.GONE);
            this.rvCartItemBinding.closeLayout.setVisibility(View.VISIBLE);
            rvCartItemBinding.ivPlus.setOnClickListener(v -> {
                increaseQuantity();
            });

            rvCartItemBinding.ivMinus.setOnClickListener(v -> {
                decreaseQuantity();
            });
            rvCartItemBinding.ivCross.setOnClickListener(v -> {
                    this.onCartItemClick.onItemClick(getAdapterPosition());
            });
        }


        private void decreaseQuantity() {
            int quantity = Integer.parseInt(rvCartItemBinding.etQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                rvCartItemBinding.etQuantity.setText(String.valueOf(quantity));
                setMinus(quantity);
            }
        }

        private void increaseQuantity() {
            int quantity = Integer.parseInt(rvCartItemBinding.etQuantity.getText().toString());
            quantity++;
            rvCartItemBinding.etQuantity.setText(String.valueOf(quantity));
            setMinus(quantity);
        }

        private void setMinus(int quantity) {
            if (quantity>1){
                rvCartItemBinding.ivMinus.setBackgroundResource(R.drawable.bg_green_square);
            }else {
                rvCartItemBinding.ivMinus.setBackgroundResource(R.drawable.bg_grey_square);
            }
        }

        public void bind(ProductOld cartItem, int position) {
            if (cartItem.getLocation().isEmpty())
                rvCartItemBinding.tvLocation.setVisibility(View.INVISIBLE);
            rvCartItemBinding.setVariable(BR.cartItem, cartItem);
            rvCartItemBinding.executePendingBindings();
        }
    }

}
