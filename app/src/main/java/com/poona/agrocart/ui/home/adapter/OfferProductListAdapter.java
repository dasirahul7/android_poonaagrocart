package com.poona.agrocart.ui.home.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowBestSellingItemBinding;
import com.poona.agrocart.ui.home.OnPlusClick;
import com.poona.agrocart.ui.home.OnProductClick;
import com.poona.agrocart.ui.home.model.ProductOld;

import java.util.ArrayList;

public class OfferProductListAdapter extends RecyclerView.Adapter<OfferProductListAdapter.BestSellingHolder> {
    private ArrayList<ProductOld> productOlds = new ArrayList<>();
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

    public OfferProductListAdapter(ArrayList<ProductOld> productOlds, Context bdContext, View view) {
        this.productOlds = productOlds;
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
        ProductOld productOld = productOlds.get(position);
        bestSellingBinding.setProductOldModule(productOld);
        holder.bindData(productOld,position);
    }

    @Override
    public int getItemCount() {
        return productOlds.size();
    }


    public class BestSellingHolder extends RecyclerView.ViewHolder {
        public BestSellingHolder(RowBestSellingItemBinding sellingItemBinding) {
            super(sellingItemBinding.getRoot());
        }

        public void bindData(ProductOld productOld, int position) {
            bestSellingBinding.setVariable(BR.productOldModule, productOld);
            bestSellingBinding.executePendingBindings();
            if (productOld.getOffer().isEmpty())
                bestSellingBinding.txtItemOffer.setVisibility(View.INVISIBLE);
            if (productOld.getOfferPrice().isEmpty())
                bestSellingBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            if (productOld.isOrganic())
                bestSellingBinding.txtOrganic.setVisibility(View.VISIBLE);
            if (productOld.isInBasket())
                bestSellingBinding.imgPlus.setImageResource(R.drawable.ic_added);
            if (productOld.getWeight().equals("0")) {
                bestSellingBinding.txtItemQty.setVisibility(View.INVISIBLE);
                bestSellingBinding.txtItemPrice.setVisibility(View.GONE);
                bestSellingBinding.txtItemOfferPrice.setVisibility(View.GONE);
                bestSellingBinding.txtItemOffer.setVisibility(View.GONE);
                bestSellingBinding.imgPlus.setVisibility(View.GONE);
                bestSellingBinding.txtOutOfStock.setVisibility(View.VISIBLE);
            }
            bestSellingBinding.imgPlus.setOnClickListener(v -> {
                onPlusClick.addToCart(productOld, position);
            });
            itemView.setOnClickListener(v -> {
                onProductClick.toProductDetail(productOld);
            });

        }
    }
}
