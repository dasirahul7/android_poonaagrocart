package com.poona.agrocart.ui.home.adapter;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.databinding.RowBestSellingItemBinding;
import com.poona.agrocart.databinding.RowProductItemBinding;
import com.poona.agrocart.ui.home.OnPlusClick;
import com.poona.agrocart.ui.home.OnProductClick;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class BestSellingOfferAdapter extends RecyclerView.Adapter<BestSellingOfferAdapter.BestSellingHolder> {
    private ArrayList<Product> products = new ArrayList<>();
    private final Context bdContext;
    private RowBestSellingItemBinding bestSellingBinding;
    private final View view;
    private OnPlusClick onPlusClick;
    private OnProductClick onProductClick;

    public void setOnPlusClick(OnPlusClick onPlusClick) {
        this.onPlusClick = onPlusClick;
    }

    public void setOnProductClick(OnProductClick onProductClick) {
        this.onProductClick = onProductClick;
    }

    public BestSellingOfferAdapter(ArrayList<Product> products, Context bdContext, View view) {
        this.products = products;
        this.bdContext = bdContext;
        this.view = view;
    }

    @NonNull
    @Override
    public BestSellingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        bestSellingBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_best_selling_item, parent, false);
        return new BestSellingHolder(bestSellingBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellingHolder holder, int position) {
        Product product = products.get(position);
        bestSellingBinding.setProductModule(product);
        holder.bindData(product,position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class BestSellingHolder extends RecyclerView.ViewHolder {
        public BestSellingHolder(RowBestSellingItemBinding sellingItemBinding) {
            super(sellingItemBinding.getRoot());
        }

        public void bindData(Product product, int position) {
            bestSellingBinding.setVariable(BR.productModule, product);
            bestSellingBinding.executePendingBindings();
            if (product.getOffer().isEmpty())
                bestSellingBinding.txtItemOffer.setVisibility(View.INVISIBLE);
            if (product.getOfferPrice().isEmpty())
                bestSellingBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            if (product.isOrganic())
                bestSellingBinding.txtOrganic.setVisibility(View.VISIBLE);
            if (product.isInBasket())
                bestSellingBinding.imgPlus.setImageResource(R.drawable.ic_added);
            if (product.getQty().equals("0")) {
                bestSellingBinding.txtItemQty.setVisibility(View.INVISIBLE);
                bestSellingBinding.txtItemPrice.setVisibility(View.INVISIBLE);
                bestSellingBinding.txtItemOfferPrice.setVisibility(View.INVISIBLE);
                bestSellingBinding.txtItemOffer.setVisibility(View.INVISIBLE);
                bestSellingBinding.txtOutOfStock.setVisibility(View.VISIBLE);
            }
            bestSellingBinding.imgPlus.setOnClickListener(v -> {
                onPlusClick.addToCart(product, position);
            });
            itemView.setOnClickListener(v -> {
                onProductClick.toProductDetail(product);
            });

        }
    }
}
