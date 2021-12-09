package com.poona.agrocart.ui.favourites;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvFavouritesBinding;

import java.util.ArrayList;

public class FavouriteItemAdapter extends RecyclerView.Adapter<FavouriteItemAdapter.FavouriteItemViewHolder>
{
    private ArrayList<FavouriteItem> favouriteItemsList;

    public FavouriteItemAdapter(ArrayList<FavouriteItem> favouriteItemsList)
    {
        this.favouriteItemsList = favouriteItemsList;
    }

    @NonNull
    @Override
    public FavouriteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RvFavouritesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.rv_favourites, parent, false);
        return new FavouriteItemAdapter.FavouriteItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteItemViewHolder holder, int position)
    {
        final FavouriteItem favouriteItem = favouriteItemsList.get(position);
        holder.rvFavouritesBinding.setFavouriteItem(favouriteItem);
        holder.bind(favouriteItem);
    }

    @Override
    public int getItemCount()
    {
        return favouriteItemsList.size();
    }

    public class FavouriteItemViewHolder extends RecyclerView.ViewHolder
    {
        RvFavouritesBinding rvFavouritesBinding;

        public FavouriteItemViewHolder(RvFavouritesBinding rvFavouritesBinding)
        {
            super(rvFavouritesBinding.getRoot());
            this.rvFavouritesBinding=rvFavouritesBinding;

            rvFavouritesBinding.cardviewFavourite.setOnClickListener(v -> {
                goToProductDetails(v);
            });

            rvFavouritesBinding.imgPlus.setOnClickListener(v -> {
                goToCart(v);
            });
        }

        private void goToCart(View v)
        {
            Navigation.findNavController(v).navigate(R.id.action_nav_favourite_to_nav_cart);
        }

        private void goToProductDetails(View v)
        {
            Bundle bundle = new Bundle();
            bundle.putString("name",favouriteItemsList.get(getAdapterPosition()).getName());
            bundle.putString("image",favouriteItemsList.get(getAdapterPosition()).getImgUrl());
            bundle.putString("price",favouriteItemsList.get(getAdapterPosition()).getPrice());
            Navigation.findNavController(v).navigate(R.id.action_nav_favourite_to_nav_product_details,bundle);
        }

        public void bind(FavouriteItem favouriteItem)
        {
            rvFavouritesBinding.setVariable(BR.favouriteItem,favouriteItem);
            rvFavouritesBinding.executePendingBindings();
        }
    }
}
