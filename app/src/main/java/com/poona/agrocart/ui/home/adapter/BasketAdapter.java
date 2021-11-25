package com.poona.agrocart.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowBasketItemBinding;
import com.poona.agrocart.databinding.RowBestSellingItemBinding;
import com.poona.agrocart.ui.home.model.Basket;

import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketHolder> {
    private ArrayList<Basket> baskets = new ArrayList<>();
    private Context bContext;
    private RowBasketItemBinding basketItemBinding;

    public BasketAdapter(ArrayList<Basket> baskets, Context context) {
        this.baskets = baskets;
        this.bContext = context;
    }

    @Override
    public BasketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        basketItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bContext), R.layout.row_basket_item, parent, false);
        return new BasketHolder(basketItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketHolder holder, int position) {
        Basket basket = baskets.get(position);
        basketItemBinding.setBasketModule(basket);
        holder.bind(basket);
    }

    @Override
    public int getItemCount() {
        return baskets.size();
    }

    public class BasketHolder extends RecyclerView.ViewHolder {
        public BasketHolder(RowBasketItemBinding basketItemBinding) {
            super(basketItemBinding.getRoot());
        }

        public void bind(Basket basket) {
            basketItemBinding.setVariable(BR.basketModule,basket);
            basketItemBinding.executePendingBindings();
        }
    }
}
