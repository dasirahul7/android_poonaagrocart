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
import com.poona.agrocart.data.network.responses.homeResponse.HomeResponse;
import com.poona.agrocart.data.network.responses.homeResponse.Product;
import com.poona.agrocart.databinding.RowExclusiveItemBinding;

import java.util.ArrayList;

public class ExclusiveOfferListAdapter extends RecyclerView.Adapter<ExclusiveOfferListAdapter.ExclusiveItemHolder> {
    private final Context bdContext;
    private ArrayList<Product> products = new ArrayList<>();
    private RowExclusiveItemBinding rowExclusiveItemBinding;
    private final OnProductClickListener onProductClickListener;
    private final OnPlusClickListener onPlusClickListener;
    public boolean added = false;


    public ExclusiveOfferListAdapter(ArrayList<Product> products,
                                     Context bdContext, OnProductClickListener onProductClickListener,
                                     OnPlusClickListener onPlusClickListener) {
        this.products = products;
        this.bdContext = bdContext;
        this.onProductClickListener = onProductClickListener;
        this.onPlusClickListener = onPlusClickListener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ExclusiveItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowExclusiveItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_exclusive_item, parent, false);
        return new ExclusiveItemHolder(rowExclusiveItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExclusiveItemHolder holder, int position) {
        Product product = products.get(position);
        holder.bindData(product, position);
        System.out.println("name : "+product.getProductName());
        holder.itemBinding.imgPlus.setOnClickListener(view1 -> {
            onPlusClickListener.OnPlusClick(rowExclusiveItemBinding, product, position);
        });
        holder.setIsRecyclable(false);// prevent repeating items
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public interface OnPlusClickListener {
        void OnPlusClick(RowExclusiveItemBinding rowExclusiveItemBinding, Product product, int position);
    }

    public class ExclusiveItemHolder extends RecyclerView.ViewHolder {
        private RowExclusiveItemBinding  itemBinding;
        public ExclusiveItemHolder(RowExclusiveItemBinding rowExclusiveItemBinding) {
            super(rowExclusiveItemBinding.getRoot());
            this.itemBinding = rowExclusiveItemBinding;
        }

        public void bindData(Product product, int position) {
            this.itemBinding.setExclusiveOfferModule(product);
            this.itemBinding.setVariable(BR.exclusiveOfferModule, product);
            this.itemBinding.executePendingBindings();
//            rowExclusiveItemBinding.txtItemPrice.setText(product.getProductUnits().get(0).getOfferPrice());
            if (product.getSpecialOffer().isEmpty())
                this.itemBinding.txtItemOffer.setVisibility(View.INVISIBLE);
            if (product.getSpecialOffer().isEmpty())
                this.itemBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            if (product.getIsO3().equalsIgnoreCase("yes"))
                this.itemBinding.txtOrganic.setVisibility(View.VISIBLE);
            if (product.getInCart() == 1)
                this.itemBinding.imgPlus.setImageResource(R.drawable.ic_added);
            else this.itemBinding.imgPlus.setImageResource(R.drawable.ic_plus_white);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onProductClickListener.onProductClick(product);
                }
            });
        }
    }
}
