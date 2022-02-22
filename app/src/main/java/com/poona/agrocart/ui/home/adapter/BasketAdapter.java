package com.poona.agrocart.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.BasketResponse;
import com.poona.agrocart.databinding.RowBasketItemBinding;

import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketHolder> {
    private final Context bContext;
    private ArrayList<BasketResponse.Basket> baskets = new ArrayList<>();
    private RowBasketItemBinding basketItemBinding;
    private final OnBasketClickListener onBasketClickListener;

    public BasketAdapter(ArrayList<BasketResponse.Basket> baskets, Context context, OnBasketClickListener onBasketClickListener) {
        this.baskets = baskets;
        this.bContext = context;
        this.onBasketClickListener = onBasketClickListener;
    }

    @Override
    public BasketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        basketItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bContext), R.layout.row_basket_item, parent, false);
        return new BasketHolder(basketItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketHolder holder, int position) {
        BasketResponse.Basket basket = baskets.get(position);
        basketItemBinding.setBasketModule(basket);
        int mPosition = position;
        if (mPosition % 2 == 1)
            basketItemBinding.basketView.setBackgroundColor(bContext.getColor(R.color.basket_color_honey_dew));
        else
            basketItemBinding.basketView.setBackgroundColor(bContext.getColor(R.color.basket_color_old_lace));
        holder.bind(basket);
    }

    @Override
    public int getItemCount() {
        return baskets.size();
    }

    public interface OnBasketClickListener {
        void OnBasketClick(BasketResponse.Basket basket);
    }

    public class BasketHolder extends RecyclerView.ViewHolder {
        public BasketHolder(RowBasketItemBinding basketItemBinding) {
            super(basketItemBinding.getRoot());
        }


        public void bind(BasketResponse.Basket basket) {
            basketItemBinding.setVariable(BR.basketModule, basket);
            basketItemBinding.executePendingBindings();
            basketItemBinding.cardviewBasketItem.setOnClickListener(v -> {
                onBasketClickListener.OnBasketClick(basket);
            });

        }
    }
}
