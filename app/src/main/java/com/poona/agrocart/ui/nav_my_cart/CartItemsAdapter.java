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
import com.poona.agrocart.ui.home.model.ProductOld;

import java.util.ArrayList;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartItemsViewHolder>
{
    private final ArrayList<CartData> cartItemArrayList;
    private OnClickCart onCartItemClick;
    private RowProductItemBinding rvCartItemBinding;

    public interface OnClickCart {
        void onItemClick(CartData cartData);
        void onPlusCart(CartData cartData);
    }
    

    public CartItemsAdapter(ArrayList<CartData> cartItemArrayList,OnClickCart onCartItemClick) {
        this.cartItemArrayList = cartItemArrayList;
        this.onCartItemClick = onCartItemClick;
    }

    @NonNull
    @Override
    public CartItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rvCartItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_product_item, parent, false);
        return new CartItemsAdapter.CartItemsViewHolder(rvCartItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsViewHolder holder, int position) {
        final CartData cartItem = cartItemArrayList.get(position);
        holder.rvCartItemBinding.setProductOldModule(cartItem);
        holder.bind(cartItem,position);
        if (cartItem.getItemType().equalsIgnoreCase("basket")){
            rvCartItemBinding.tvName.setText(cartItem.getBasketName());
            rvCartItemBinding.tvOfferPrice.setText("RS."+cartItem.getBasketRate());
//            rvCartItemBinding.tv.setVisibility(View.INVISIBLE);
            rvCartItemBinding.tvLocation.setText(cartItem.getLocation());
//            rvCartItemBinding.tvOfferPrice.setVisibility(View.INVISIBLE);
            rvCartItemBinding.ivPlus.setImageResource(R.drawable.ic_added);
        }else {
            rvCartItemBinding.tvName.setText(cartItem.getProductName());
            rvCartItemBinding.tvOfferPrice.setText("RS."+cartItem.getPricePerQuantity());
//            rvCartItemBinding.tvSellingPrice.setText("RS."+favourite.getSelling_price());
            rvCartItemBinding.tvWeight.setText(cartItem.getWeight()+""+cartItem.getUnitName());

        }

    }

    @Override
    public int getItemCount() {
        return cartItemArrayList.size();
    }

    public class CartItemsViewHolder extends RecyclerView.ViewHolder {
        RowProductItemBinding rvCartItemBinding;
        OnClickCart onCartItemClick;

        public CartItemsViewHolder(RowProductItemBinding rvCartItemBinding) {
            super(rvCartItemBinding.getRoot());
            this.rvCartItemBinding = rvCartItemBinding;
            this.onCartItemClick = onCartItemClick;
            this.rvCartItemBinding.txtItemOffer.setVisibility(View.GONE);
            this.rvCartItemBinding.closeLayout.setVisibility(View.VISIBLE);
            rvCartItemBinding.ivPlus.setOnClickListener(v -> {
                increaseQuantity();
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

        public void bind(CartData cartItem, int position) {
            rvCartItemBinding.setVariable(BR.cartItem, cartItem);
            rvCartItemBinding.executePendingBindings();
        }
    }

}
