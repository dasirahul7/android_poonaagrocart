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
import com.poona.agrocart.data.network.reponses.BestSellingResponse;
import com.poona.agrocart.data.network.reponses.ProductListResponse;
import com.poona.agrocart.databinding.RowExclusiveItemBinding;
import com.poona.agrocart.ui.home.OnPlusClick;
import com.poona.agrocart.ui.home.OnProductClick;

import java.util.ArrayList;

public class ExclusiveOfferListAdapter extends RecyclerView.Adapter<ExclusiveOfferListAdapter.ExclusiveItemHolder> {
    private ArrayList<ProductListResponse.Product> products = new ArrayList<>();
    private final Context bdContext;
    private RowExclusiveItemBinding rowExclusiveItemBinding;
    private final View view;
    private OnPlusClick onPlusClick;
    private OnProductClick onProductClick;

    public void setOnPlusClick(OnPlusClick onPlusClick) {
        this.onPlusClick = onPlusClick;
    }

    public void setOnProductClick(OnProductClick onProductClick) {
        this.onProductClick = onProductClick;
    }

    public ExclusiveOfferListAdapter(ArrayList<ProductListResponse.Product> products, Context bdContext, View view) {
        this.products = products;
        this.bdContext = bdContext;
        this.view = view;
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

//            if (product.isInBasket())
//                rowExclusiveItemBinding.imgPlus.setImageResource(R.drawable.ic_added);
//            if (Objects.equals(product.getProductUnits().get(0).getWeight(), "0")) {
//                rowExclusiveItemBinding.txtItemQty.setVisibility(View.INVISIBLE);
//                rowExclusiveItemBinding.txtItemPrice.setVisibility(View.GONE);
//                rowExclusiveItemBinding.txtItemOfferPrice.setVisibility(View.GONE);
//                rowExclusiveItemBinding.txtItemOffer.setVisibility(View.GONE);
//                rowExclusiveItemBinding.imgPlus.setVisibility(View.GONE);
//                rowExclusiveItemBinding.txtOutOfStock.setVisibility(View.VISIBLE);
//            }
//            rowExclusiveItemBinding.imgPlus.setOnClickListener(v -> {
//                onPlusClick.addToCart(product, position);
//            });
//            itemView.setOnClickListener(v -> {
//                onProductClick.toProductDetail(product);
//            });

        }
    }
}
