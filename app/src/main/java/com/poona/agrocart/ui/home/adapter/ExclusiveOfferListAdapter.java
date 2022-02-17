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
import com.poona.agrocart.databinding.RowExclusiveItemBinding;

import java.util.ArrayList;

public class ExclusiveOfferListAdapter extends RecyclerView.Adapter<ExclusiveOfferListAdapter.ExclusiveItemHolder> {
    private ArrayList<ProductListResponse.Product> products = new ArrayList<>();
    private final Context bdContext;
    private RowExclusiveItemBinding rowExclusiveItemBinding;
    private OnProductClickListener onProductClickListener;
    private OnPlusClickListener onPlusClickListener;


    public ExclusiveOfferListAdapter(ArrayList<ProductListResponse.Product> products,
                                     Context bdContext,OnProductClickListener onProductClickListener,
                                     OnPlusClickListener onPlusClickListener) {
        this.products = products;
        this.bdContext = bdContext;this.onProductClickListener = onProductClickListener;
        this.onPlusClickListener = onPlusClickListener;
    }
    public interface OnProductClickListener{
        void onProductClick(ProductListResponse.Product product);
    }
    public interface OnPlusClickListener {
        void OnPlusClick(ProductListResponse.Product product,int position);
    }


    @NonNull
    @Override
    public ExclusiveItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowExclusiveItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_exclusive_item, parent, false);
        return new ExclusiveItemHolder(rowExclusiveItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExclusiveItemHolder holder, int position) {
        ProductListResponse.Product product = products.get(position);
        rowExclusiveItemBinding.setExclusiveOfferModule(product);
        holder.bindData(product,position);
        rowExclusiveItemBinding.imgPlus.setOnClickListener(view1 -> {
            onPlusClickListener.OnPlusClick(product,position);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ExclusiveItemHolder extends RecyclerView.ViewHolder {
        public ExclusiveItemHolder(RowExclusiveItemBinding rowExclusiveItemBinding) {
            super(rowExclusiveItemBinding.getRoot());
        }

        public void bindData(ProductListResponse.Product product, int position) {
            rowExclusiveItemBinding.setVariable(BR.exclusiveOfferModule, product);
            rowExclusiveItemBinding.executePendingBindings();
//            rowExclusiveItemBinding.txtItemPrice.setText(product.getProductUnits().get(0).getOfferPrice());
            if (product.getSpecialOffer().isEmpty())
                rowExclusiveItemBinding.txtItemName.setVisibility(View.INVISIBLE);
            if (product.getSpecialOffer().isEmpty())
                rowExclusiveItemBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            if (product.getIsO3().equalsIgnoreCase("yes"))
                rowExclusiveItemBinding.txtOrganic.setVisibility(View.VISIBLE);
            if (product.getInCart()==1)
                rowExclusiveItemBinding.imgPlus.setImageResource(R.drawable.ic_added);
            else rowExclusiveItemBinding.imgPlus.setImageResource(R.drawable.ic_plus_white);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onProductClickListener.onProductClick(product);
                }
            });
        }
    }
}
