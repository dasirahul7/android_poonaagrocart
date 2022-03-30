package com.poona.agrocart.ui.nav_favourites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.favoutiteResponse.FavouriteListResponse;
import com.poona.agrocart.databinding.RowFavouriteListItemBinding;

import java.util.ArrayList;

public class FavouriteItemAdapter extends RecyclerView.Adapter<FavouriteItemAdapter.FavouriteHolder> {
    private final Context bdContext;
    private ArrayList<FavouriteListResponse.Favourite> favouriteArrayList = new ArrayList<>();
    private RowFavouriteListItemBinding favouriteListItemBinding;
    private final OnPlusClick onPlusClick;
    private final OnProductClick onProductClick;
    private final OnFavouriteClick onFavouriteClick;

    public FavouriteItemAdapter(Context context, ArrayList<FavouriteListResponse.Favourite> favouriteItemsList,
                                FavouriteItemsFragment favouriteItemsFragment) {
        this.favouriteArrayList = favouriteItemsList;
        this.bdContext = context;
        this.onPlusClick = favouriteItemsFragment;
        this.onProductClick = favouriteItemsFragment;
        this.onFavouriteClick = favouriteItemsFragment;
    }

    @NonNull
    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favouriteListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_favourite_list_item, parent, false);
        return new FavouriteHolder(favouriteListItemBinding);
    }

    public interface OnPlusClick {
        void addToCartClickItem(int position);
    }
    public interface OnProductClick {
        void toProductDetailClickItem(int position);
    }
    public interface OnFavouriteClick {
        void removeFavouriteClickItem(int position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavouriteHolder holder, int position) {
        FavouriteListResponse.Favourite favourite = favouriteArrayList.get(position);
        favouriteListItemBinding.setFavouriteModelList(favourite);
        holder.bindProduct(favourite);

        if (favourite.getItemType().equalsIgnoreCase("basket")) {
            favouriteListItemBinding.tvName.setText(favourite.getBasketName());
            favouriteListItemBinding.tvOfferActualPrice.setText("RS. " + favourite.getBasketRate());
            favouriteListItemBinding.tvSellingPrice.setVisibility(View.GONE);
            favouriteListItemBinding.tvLocation.setVisibility(View.GONE);
            favouriteListItemBinding.tvProductWeight.setVisibility(View.GONE);
            favouriteListItemBinding.tvOrganic.setVisibility(View.INVISIBLE);
            if (favourite.getInCart() == 1){
                favouriteListItemBinding.ivPlus.setImageResource(R.drawable.ic_added);

            } else if (favourite.getInCart() == 0){
                favouriteListItemBinding.ivPlus.setImageResource(R.drawable.ic_plus_white);
            }

        } else {
            favouriteListItemBinding.tvName.setText(favourite.getProductName());
            favouriteListItemBinding.tvOfferActualPrice.setText("RS. " + favourite.getOffer_price());
            favouriteListItemBinding.tvSellingPrice.setText("RS. " + favourite.getSelling_price());
            favouriteListItemBinding.tvProductWeight.setText(favourite.getWeight() + "" + favourite.getUnitName());
            if (favourite.getIs_o3().equalsIgnoreCase("yes")){
                favouriteListItemBinding.tvOrganic.setVisibility(View.VISIBLE);
            } else{
                favouriteListItemBinding.tvOrganic.setVisibility(View.INVISIBLE);
            }

            if (favourite.getInCart() == 1){
              favouriteListItemBinding.ivPlus.setImageResource(R.drawable.ic_added);

            } else if (favourite.getInCart() == 0){
                favouriteListItemBinding.ivPlus.setImageResource(R.drawable.ic_plus_white);
            }
        }

    }

    @Override
    public int getItemCount() {
        return favouriteArrayList.size();
    }

    public class FavouriteHolder extends RecyclerView.ViewHolder {

       private RowFavouriteListItemBinding productBinding;

        // The Landscape Item Holder
        public FavouriteHolder(RowFavouriteListItemBinding productBinding) {
            super(productBinding.getRoot());
            this.productBinding = productBinding;
        }

        //Only ProductOld Item bind
        public void bindProduct(FavouriteListResponse.Favourite favourite) {

            favouriteListItemBinding.setVariable(BR.favouriteModelList, favourite);
            favouriteListItemBinding.executePendingBindings();

            productBinding.ivPlus.setOnClickListener(view -> {
                if (onPlusClick != null) {
                    int postion = getLayoutPosition();
                    if (postion != RecyclerView.NO_POSITION) {
                        onPlusClick.addToCartClickItem(postion);
                    }
                }
            });
            productBinding.ivFavourite.setOnClickListener(view -> {
                if (onFavouriteClick != null) {
                    int postion = getLayoutPosition();
                    if (postion != RecyclerView.NO_POSITION) {
                        onFavouriteClick.removeFavouriteClickItem(postion);
                    }
                }
            });
            productBinding.ll2.setOnClickListener(view -> {
                if (onProductClick != null) {
                    int postion = getLayoutPosition();
                    if (postion != RecyclerView.NO_POSITION) {
                        onProductClick.toProductDetailClickItem(postion);
                    }
                }
            });
        }
    }

}
