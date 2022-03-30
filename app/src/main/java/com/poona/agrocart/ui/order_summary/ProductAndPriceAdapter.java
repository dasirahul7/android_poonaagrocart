package com.poona.agrocart.ui.order_summary;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.orderResponse.ItemsDetail;
import com.poona.agrocart.databinding.RvProductAndPriceDetailsBinding;
import com.poona.agrocart.ui.order_summary.model.ProductAndPrice;

import java.util.ArrayList;

public class ProductAndPriceAdapter extends RecyclerView.Adapter<ProductAndPriceAdapter.ProductAndPriceViewHolder> {
    private final ArrayList<ItemsDetail> productAndPriceArrayList;

    public ProductAndPriceAdapter(ArrayList<ItemsDetail> productAndPriceArrayList) {
        this.productAndPriceArrayList = productAndPriceArrayList;
    }

    @NonNull
    @Override
    public ProductAndPriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvProductAndPriceDetailsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_product_and_price_details, parent, false);
        return new ProductAndPriceAdapter.ProductAndPriceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAndPriceViewHolder holder, int position) {
        final ItemsDetail itemsDetail = productAndPriceArrayList.get(position);
        holder.rvProductAndPriceDetailsBinding.setProductAndPrice(itemsDetail);
        holder.bind(itemsDetail);
    }

    @Override
    public int getItemCount() {
        return productAndPriceArrayList.size();
    }

    public static class ProductAndPriceViewHolder extends RecyclerView.ViewHolder {
        RvProductAndPriceDetailsBinding rvProductAndPriceDetailsBinding;

        public ProductAndPriceViewHolder(RvProductAndPriceDetailsBinding rvProductAndPriceDetailsBinding) {
            super(rvProductAndPriceDetailsBinding.getRoot());
            this.rvProductAndPriceDetailsBinding = rvProductAndPriceDetailsBinding;
        }

        public void bind(ItemsDetail itemsDetail) {
            rvProductAndPriceDetailsBinding.setVariable(BR.productAndPrice, itemsDetail);
            rvProductAndPriceDetailsBinding.executePendingBindings();
            if (itemsDetail.subscribeNowId!=null)
                rvProductAndPriceDetailsBinding.tvQuantity.setText(itemsDetail.pricePerQuantity+" X "+itemsDetail.noOfSubscription);
        }
    }
}
