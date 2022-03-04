package com.poona.agrocart.ui.nav_orders.order_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.ItemsDetail;
import com.poona.agrocart.databinding.RvBasketDetailBinding;

import java.util.ArrayList;

public class BasketItemsAdapter extends RecyclerView.Adapter<BasketItemsAdapter.BasketItemViewHolder> {
    private final ArrayList<ItemsDetail> basketItems;
    private final boolean isBasketVisible;
    private final Context context;

    public BasketItemsAdapter(ArrayList<ItemsDetail> basketItems, boolean isBasketVisible, Context context) {
        this.basketItems = basketItems;
        this.isBasketVisible = isBasketVisible;
        this.context = context;
    }

    @NonNull
    @Override
    public BasketItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvBasketDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_basket_detail, parent, false);
        return new BasketItemViewHolder(binding, isBasketVisible);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketItemViewHolder holder, int position) {
        final ItemsDetail basketItem = basketItems.get(position);
        holder.rvBasketDetailBinding.setBasketItemModel(basketItem);
        holder.bind(basketItem, context);
    }

    @Override
    public int getItemCount() {
        return basketItems.size();
    }

    public class BasketItemViewHolder extends RecyclerView.ViewHolder {

        RvBasketDetailBinding rvBasketDetailBinding;
        boolean isBasketVisible;

        public BasketItemViewHolder(RvBasketDetailBinding rvBasketDetailBinding, boolean isBasketVisible) {
            super(rvBasketDetailBinding.getRoot());
            this.rvBasketDetailBinding = rvBasketDetailBinding;
            this.isBasketVisible = isBasketVisible;
            if (this.isBasketVisible) {
                rvBasketDetailBinding.tvDateAndTime.setVisibility(View.VISIBLE);
                rvBasketDetailBinding.tvQuantity.setVisibility(View.GONE);
                rvBasketDetailBinding.tvOrderPrice.setTextColor(context.getColor(R.color.color_rv_basket_price));
            } else {
                rvBasketDetailBinding.tvDateAndTime.setVisibility(View.INVISIBLE);
                rvBasketDetailBinding.tvQuantity.setVisibility(View.VISIBLE);
                rvBasketDetailBinding.tvOrderStatus.setVisibility(View.GONE);
            }
        }

        @SuppressLint("ResourceType")
        public void bind(ItemsDetail basketItem, Context context) {
            rvBasketDetailBinding.setVariable(BR.basketItemModel, basketItem);

           /* if (this.isBasketVisible) {
                if (basketItem.getDeliveryStatus().equals("Delivered")) {
                    rvBasketDetailBinding.tvOrderStatus.setText(context.getString(R.string.delivered));
                    rvBasketDetailBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_in_process)));
                } else if (basketItem.getDeliveryStatus().equals("In transist")) {
                    rvBasketDetailBinding.btnTrackOrder.setVisibility(View.VISIBLE);
                    rvBasketDetailBinding.tvOrderStatus.setVisibility(View.GONE);
                } else {
                    rvBasketDetailBinding.tvOrderStatus.setText(context.getString(R.string.confirmed));
                    rvBasketDetailBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_invoice_btn)));
                }
            }*/

            rvBasketDetailBinding.executePendingBindings();
        }
    }
}
