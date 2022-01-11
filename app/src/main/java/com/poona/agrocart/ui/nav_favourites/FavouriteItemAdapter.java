package com.poona.agrocart.ui.nav_favourites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowProductItemBinding;
import com.poona.agrocart.ui.home.OnPlusClick;
import com.poona.agrocart.ui.home.OnProductClick;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class FavouriteItemAdapter extends RecyclerView.Adapter<FavouriteItemAdapter.FavouriteHolder> {
    private ArrayList<Product> products = new ArrayList<>();
    private final Context bdContext;
    private RowProductItemBinding productBinding;
    private final View view;
    private OnPlusClick onPlusClick;
    private OnProductClick onProductClick;

    public void setOnPlusClick(OnPlusClick onPlusClick) {
        this.onPlusClick = onPlusClick;
    }

    public void setOnProductClick(OnProductClick onProductClick) {
        this.onProductClick = onProductClick;
    }

    public FavouriteItemAdapter(ArrayList<Product> products, FragmentActivity context, View view) {
        this.products = products;
        this.bdContext = context;
        this.view = view;
    }


    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_product_item, parent, false);
        return new FavouriteHolder(productBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteHolder holder, int position) {
        Product product = products.get(position);
        productBinding.setProductModule(product);
        holder.bindProduct(product, position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class FavouriteHolder extends RecyclerView.ViewHolder {

        // The Landscape Item Holder
        public FavouriteHolder(RowProductItemBinding productBinding) {
            super(productBinding.getRoot());
        }

        //Only Product Item bind
        public void bindProduct(Product product, int position) {
            productBinding.setVariable(BR.productModule, product);
            productBinding.executePendingBindings();
            productBinding.txtItemOffer.setVisibility(View.GONE);
            productBinding.txtItemPrice.setVisibility(View.GONE);
            productBinding.txtOrganic.setVisibility(View.GONE);
            productBinding.ivPlus.setImageResource(R.drawable.ic_added);
            //Remove the left margin from Rs text
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,0);
            productBinding.tvOfferPrice.setLayoutParams(params);

            productBinding.ivFavourite.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(v -> {
                onProductClick.toProductDetail(product);
            });

        }

    }


}
