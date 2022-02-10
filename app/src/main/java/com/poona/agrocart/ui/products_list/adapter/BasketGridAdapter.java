package com.poona.agrocart.ui.products_list.adapter;

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
import com.poona.agrocart.data.network.reponses.BasketResponse;
import com.poona.agrocart.databinding.RowBasketListItemBinding;

import java.util.ArrayList;

public class BasketGridAdapter extends RecyclerView.Adapter<BasketGridAdapter.BasketGridHolder>
{
    private final ArrayList<BasketResponse.Basket> basketArrayList;
    private RowBasketListItemBinding rowBasketBinding;

    public BasketGridAdapter(ArrayList<BasketResponse.Basket> basketArrayList)
    {
        this.basketArrayList = basketArrayList;
    }

    @NonNull
    @Override
    public BasketGridHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RowBasketListItemBinding binding = DataBindingUtil.inflate
                (LayoutInflater.from(parent.getContext()),
                        R.layout.row_basket_list_item, parent, false);
        return new BasketGridHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketGridHolder holder, int position)
    {
        final BasketResponse.Basket vegetable = basketArrayList.get(position);
        holder.basketListItemBinding.setBasket(vegetable);
        holder.bind(vegetable);
    }

    @Override
    public int getItemCount() {
        return basketArrayList.size();
    }

    public class BasketGridHolder extends RecyclerView.ViewHolder
    {
        RowBasketListItemBinding basketListItemBinding;

        public BasketGridHolder(RowBasketListItemBinding productListItemBinding) {
            super(productListItemBinding.getRoot());
            this.basketListItemBinding =productListItemBinding;

//            productListItemBinding.cardviewProduct.setOnClickListener(v -> {
//                redirectToProductsDetail(view);
//            });

        }

        private void redirectToProductsDetail(View v)
        {
            Bundle bundle = new Bundle();
            bundle.putString("name",basketArrayList.get(getAdapterPosition()).getBasketName());
            bundle.putString("image",basketArrayList.get(getAdapterPosition()).getFeatureImg());
            bundle.putString("price",basketArrayList.get(getAdapterPosition()).getBasketRate());
            Navigation.findNavController(v).navigate(R.id.action_nav_products_list_to_productDetailFragment2,bundle);
        }

        public void bind(BasketResponse.Basket basket)
        {
            basketListItemBinding.setVariable(BR.basket,basket);
            basketListItemBinding.executePendingBindings();
        }
    }
}
