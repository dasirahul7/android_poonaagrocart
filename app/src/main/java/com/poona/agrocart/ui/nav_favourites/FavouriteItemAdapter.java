package com.poona.agrocart.ui.nav_favourites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.reponses.favoutiteResponse.FavouriteListResponse;
import com.poona.agrocart.databinding.RowFavouriteListItemBinding;
import com.poona.agrocart.databinding.RowProductItemBinding;
import com.poona.agrocart.ui.home.OnPlusClick;
import com.poona.agrocart.ui.home.OnProductClick;
import com.poona.agrocart.ui.home.model.ProductOld;

import java.util.ArrayList;

public class FavouriteItemAdapter extends RecyclerView.Adapter<FavouriteItemAdapter.FavouriteHolder> {
    private ArrayList<FavouriteListResponse.Favourite> favouriteArrayList = new ArrayList<>();
    private final Context bdContext;
    private  RowFavouriteListItemBinding favouriteListItemBinding;
    private OnPlusClick onPlusClick;
    private OnProductClick onProductClick;

    public FavouriteItemAdapter(Context context, ArrayList<FavouriteListResponse.Favourite> favouriteItemsList,
                                FavouriteItemsFragment favouriteItemsFragment) {
        this.favouriteArrayList = favouriteItemsList;
        this.bdContext = context;
    }

    public void setOnPlusClick(OnPlusClick onPlusClick) {
        this.onPlusClick = onPlusClick;
    }

    public void setOnProductClick(OnProductClick onProductClick) {
        this.onProductClick = onProductClick;
    }

    @NonNull
    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        favouriteListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_favourite_list_item, parent, false);
        return new FavouriteHolder(favouriteListItemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavouriteHolder holder, int position) {
        FavouriteListResponse.Favourite favourite = favouriteArrayList.get(position);
        favouriteListItemBinding.setFavouriteModelList(favourite);
        holder.bindProduct(favourite, position);

        if (favourite.getItemType().equalsIgnoreCase("basket")){
            favouriteListItemBinding.tvName.setText(favourite.getBasketName());
            favouriteListItemBinding.tvOfferActualPrice.setText("RS."+favourite.getOffer_price());
            favouriteListItemBinding.tvSellingPrice.setVisibility(View.INVISIBLE);
            favouriteListItemBinding.tvLocation.setVisibility(View.INVISIBLE);
        }else {
            favouriteListItemBinding.tvName.setText(favourite.getProductName());
            favouriteListItemBinding.tvOfferActualPrice.setText("RS."+favourite.getOffer_price());
            favouriteListItemBinding.tvSellingPrice.setText("RS."+favourite.getSelling_price());
            favouriteListItemBinding.tvProductWeight.setText(favourite.getWeight()+""+favourite.getUnitName());
        }

    }

    @Override
    public int getItemCount() {
        return favouriteArrayList.size();
    }

    public class FavouriteHolder extends RecyclerView.ViewHolder {

        // The Landscape Item Holder
        public FavouriteHolder(RowFavouriteListItemBinding productBinding) {
            super(productBinding.getRoot());
        }

        //Only ProductOld Item bind
        public void bindProduct( FavouriteListResponse.Favourite productOld, int position) {
            favouriteListItemBinding.setVariable(BR.productOldModule, productOld);
            favouriteListItemBinding.executePendingBindings();
            //favouriteListItemBinding.txtItemOffer.setVisibility(View.GONE);
            //favouriteListItemBinding.txtItemPrice.setVisibility(View.GONE);
            //favouriteListItemBinding.txtOrganic.setVisibility(View.GONE);
            favouriteListItemBinding.ivPlus.setImageResource(R.drawable.ic_added);
            //Remove the left margin from Rs text
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,0);
            favouriteListItemBinding.tvOfferActualPrice.setLayoutParams(params);

            favouriteListItemBinding.ivFavourite.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(v -> {
              //  onProductClick.toProductDetail(productOld);
            });
        }
    }

}
