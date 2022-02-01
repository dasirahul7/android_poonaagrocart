package com.poona.agrocart.ui.home.adapter;

import android.content.Context;
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
import com.poona.agrocart.databinding.RowBasketItemBinding;
import com.poona.agrocart.ui.home.model.Basket;

import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketHolder> {
    private ArrayList<Basket> baskets = new ArrayList<>();
    private final Context bContext;
    private RowBasketItemBinding basketItemBinding;
    private final View view;

    public BasketAdapter(ArrayList<Basket> baskets, Context context, View view) {
        this.baskets = baskets;
        this.bContext = context;
        this.view=view;
    }

    @Override
    public BasketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        basketItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bContext), R.layout.row_basket_item, parent, false);
        return new BasketHolder(basketItemBinding,view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketHolder holder, int position) {
        Basket basket = baskets.get(position);
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

    public class BasketHolder extends RecyclerView.ViewHolder {
        public BasketHolder(RowBasketItemBinding basketItemBinding,View view) {
            super(basketItemBinding.getRoot());

            basketItemBinding.cardviewBasketItem.setOnClickListener(v->{
                redirectToBasketDetailFragment(view);
            });
        }

        private void redirectToBasketDetailFragment(View v)
        {
            Bundle bundle = new Bundle();
            bundle.putString("name",baskets.get(getAdapterPosition()).getBasketName());
            bundle.putString("image",baskets.get(getAdapterPosition()).getFeatureImg());
            bundle.putString("price",baskets.get(getAdapterPosition()).getBasketRate());
            bundle.putString("Product","BasketDetail");
            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_product_details,bundle);
        }

        public void bind(Basket basket) {
            basketItemBinding.setVariable(BR.basketModule,basket);
            basketItemBinding.executePendingBindings();
        }
    }
}
