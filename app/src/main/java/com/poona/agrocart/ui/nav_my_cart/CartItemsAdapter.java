package com.poona.agrocart.ui.nav_my_cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.cartResponse.CartData;
import com.poona.agrocart.databinding.RowProductItemBinding;
import com.poona.agrocart.ui.nav_addresses.AddressesAdapter;

import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder> {
    private final ArrayList<CartData> cartItemArrayList;
    private RowProductItemBinding rowProductItemBinding;

    private OnCartItemClickListener onCartItemClickListener;
    private OnCartAddCountClickListener onCartAddCountClickListener;
    private OnCartMinusCountClickListener onCartMinusCountClickListener;
    private OnCartItemDeleteClickListener onCartItemDeleteClickListener;

    public interface OnCartItemClickListener {
        void onCartItemClick(int position);
    }

    public interface OnCartAddCountClickListener {
        void onCartAddCountClick(int position);
    }

    public interface OnCartMinusCountClickListener {
        void onCartMinusCountClick(int position);
    }

    public interface OnCartItemDeleteClickListener {
        void onCartItemDeleteClick(int position);
    }

    public void setOnCartItemClick(OnCartItemClickListener listener) {
        onCartItemClickListener = listener;
    }

    public void setOnCartAddMinusCountClick(OnCartAddCountClickListener listener) {
        onCartAddCountClickListener = listener;
    }

    public void setOnCartMinusCountClick(OnCartMinusCountClickListener listener) {
        onCartMinusCountClickListener = listener;
    }

    public void setOnDeleteCartItemClick(OnCartMinusCountClickListener listener) {
        onCartMinusCountClickListener = listener;
    }

    public CartItemsAdapter(ArrayList<CartData> cartItemArrayList) {
        this.cartItemArrayList = cartItemArrayList;
    }

    @NonNull
    @Override
    public CartItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowProductItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_product_item, parent, false);
        return new CartItemsAdapter.CartItemsViewHolder(rowProductItemBinding,
                onCartItemClickListener,
                onCartAddCountClickListener,
                onCartMinusCountClickListener,
                onCartItemDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsViewHolder holder, int position) {
        final CartData cartItem = cartItemArrayList.get(position);
        holder.rowProductItemBinding.setProductOldModule(cartItem);
        holder.bind(cartItem, position);
        if (cartItem.getItemType().equalsIgnoreCase("basket")) {
            rowProductItemBinding.tvName.setText(cartItem.getBasketName());
            rowProductItemBinding.tvOfferPrice.setText("RS." + cartItem.getBasketRate());
            rowProductItemBinding.tvLocation.setText(cartItem.getLocation());
            rowProductItemBinding.ivPlus.setImageResource(R.drawable.ic_added);
        } else {
            rowProductItemBinding.tvName.setText(cartItem.getProductName());
            rowProductItemBinding.tvOfferPrice.setText("RS." + cartItem.getPricePerQuantity());
            rowProductItemBinding.tvWeight.setText(cartItem.getWeight() + "" + cartItem.getUnitName());
        }
    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    public class CartItemsViewHolder extends RecyclerView.ViewHolder {
        RowProductItemBinding rowProductItemBinding;
        private int selectedCartItem = 0;
        private int selectedAddCartItem = 0;
        private int selectedMinusCartItem = 0;
        private int selectedDeleteItem = 0;
        public CartItemsViewHolder(RowProductItemBinding rowProductItemBinding,
                                   OnCartItemClickListener onCartItemClickListener,
                                   OnCartAddCountClickListener onCartAddCountClickListener,
                                   OnCartMinusCountClickListener onCartMinusCountClickListener,
                                   OnCartItemDeleteClickListener onCartItemDeleteClickListener) {
            super(rowProductItemBinding.getRoot());
            this.rowProductItemBinding = rowProductItemBinding;

            this.rowProductItemBinding.txtItemOffer.setVisibility(View.GONE);
            this.rowProductItemBinding.icAddToCart.setVisibility(View.GONE);
            this.rowProductItemBinding.closeLayout.setVisibility(View.VISIBLE);

            rowProductItemBinding.getRoot().setOnClickListener(view -> {
                selectedCartItem = getAdapterPosition();
                notifyDataSetChanged();
                if (onCartItemClickListener != null) {
                    if (selectedCartItem != RecyclerView.NO_POSITION) {
                        onCartItemClickListener.onCartItemClick(selectedCartItem);
                    }
                }
            });

            rowProductItemBinding.ivPlus.setOnClickListener(view -> {
                selectedAddCartItem = getAdapterPosition();
                notifyDataSetChanged();
                if (onCartAddCountClickListener != null) {
                    if (selectedAddCartItem != RecyclerView.NO_POSITION) {
                        onCartAddCountClickListener.onCartAddCountClick(selectedAddCartItem);
                    }
                }
            });

            rowProductItemBinding.ivMinus.setOnClickListener(view -> {
                selectedMinusCartItem = getAdapterPosition();
                notifyDataSetChanged();
                if (onCartMinusCountClickListener != null) {
                    if (selectedMinusCartItem != RecyclerView.NO_POSITION) {
                        onCartMinusCountClickListener.onCartMinusCountClick(selectedMinusCartItem);
                    }
                }
            });

            rowProductItemBinding.ivCross.setOnClickListener(view -> {
                selectedDeleteItem = getAdapterPosition();
                notifyDataSetChanged();
                if (onCartItemDeleteClickListener != null) {
                    if (selectedDeleteItem != RecyclerView.NO_POSITION) {
                        onCartItemDeleteClickListener.onCartItemDeleteClick(selectedDeleteItem);
                    }
                }
            });

            /*rowProductItemBinding.ivPlus.setOnClickListener(v -> {
                increaseQuantity();
            });*/
        }


        private void decreaseQuantity() {
            int quantity = Integer.parseInt(rowProductItemBinding.etQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                rowProductItemBinding.etQuantity.setText(String.valueOf(quantity));
                setMinus(quantity);
            }
        }

        private void increaseQuantity() {
            int quantity = Integer.parseInt(rowProductItemBinding.etQuantity.getText().toString());
            quantity++;
            rowProductItemBinding.etQuantity.setText(String.valueOf(quantity));
            setMinus(quantity);
        }

        private void setMinus(int quantity) {
            if (quantity > 1) {
                rowProductItemBinding.ivMinus.setBackgroundResource(R.drawable.bg_green_square);
            } else {
                rowProductItemBinding.ivMinus.setBackgroundResource(R.drawable.bg_grey_square);
            }
        }

        public void bind(CartData cartItem, int position) {
            //rowProductItemBinding.setVariable(BR.cartItem, cartItem);
            rowProductItemBinding.executePendingBindings();
        }
    }
}