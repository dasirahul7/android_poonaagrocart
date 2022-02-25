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
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.data.network.responses.homeResponse.Product;
import com.poona.agrocart.databinding.RowBestSellingItemBinding;
import com.poona.agrocart.databinding.RowExclusiveItemBinding;

import java.util.ArrayList;

public class BestSellingListAdapter extends RecyclerView.Adapter<BestSellingListAdapter.BestSellingViewHolder> {
    private final Context bdContext;
    private ArrayList<Product> products = new ArrayList<>();
    private RowBestSellingItemBinding sellingItemBinding;
    private final OnProductClickListener onProductClickListener;
    private final OnPlusClickListener onPlusClickListener;


    public BestSellingListAdapter(ArrayList<Product> products,
                                  Context bdContext, OnProductClickListener onProductClickListener,
                                  OnPlusClickListener onPlusClickListener) {
        this.products = products;
        this.bdContext = bdContext;
        this.onProductClickListener = onProductClickListener;
        this.onPlusClickListener = onPlusClickListener;
    }

    @NonNull
    @Override
    public BestSellingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sellingItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_best_selling_item, parent, false);
        return new BestSellingViewHolder(sellingItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellingViewHolder holder, int position) {
        Product product = products.get(position);
        sellingItemBinding.setBestsellingModule(product);
        holder.bindData(product, position);
        sellingItemBinding.imgPlus.setOnClickListener(view1 -> {
            onPlusClickListener.OnPlusClick(sellingItemBinding, product, position);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public interface OnPlusClickListener {
        void OnPlusClick(RowBestSellingItemBinding rowBestSellingItemBinding, Product product, int position);
    }

    public class BestSellingViewHolder extends RecyclerView.ViewHolder {
        public BestSellingViewHolder(RowBestSellingItemBinding rowBestSellingItemBinding) {
            super(rowBestSellingItemBinding.getRoot());
        }

        public void bindData(Product product, int position) {
            sellingItemBinding.setVariable(BR.exclusiveOfferModule, product);
            sellingItemBinding.executePendingBindings();
//            rowExclusiveItemBinding.txtItemPrice.setText(product.getProductUnits().get(0).getOfferPrice());
            if (product.getSpecialOffer().isEmpty())
                sellingItemBinding.txtItemName.setVisibility(View.INVISIBLE);
            if (product.getSpecialOffer().isEmpty())
                sellingItemBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            if (product.getIsO3().equalsIgnoreCase("yes"))
                sellingItemBinding.txtOrganic.setVisibility(View.VISIBLE);
            if (product.getInCart() == 1)
                sellingItemBinding.imgPlus.setImageResource(R.drawable.ic_added);
            else sellingItemBinding.imgPlus.setImageResource(R.drawable.ic_plus_white);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onProductClickListener.onProductClick(product);
                }
            });
        }
    }
}
