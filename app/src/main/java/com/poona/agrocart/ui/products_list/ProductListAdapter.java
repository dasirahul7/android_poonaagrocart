package com.poona.agrocart.ui.products_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowBestSellingItemBinding;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductsViewHolder>
{
    private ArrayList<Product> vegetableArrayList;

    public ProductListAdapter(ArrayList<Product> vegetableArrayList) {
        this.vegetableArrayList = vegetableArrayList;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowBestSellingItemBinding binding = DataBindingUtil.inflate
                (LayoutInflater.from(parent.getContext()),
                        R.layout.row_best_selling_item, parent, false);
        return new ProductsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        final Product vegetable = vegetableArrayList.get(position);
        holder.homeBestSellingItemBinding.setProductModule(vegetable);
        holder.bind(vegetable);
    }

    @Override
    public int getItemCount() {
        return vegetableArrayList.size();
    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder
    {
        RowBestSellingItemBinding homeBestSellingItemBinding;

        public ProductsViewHolder(RowBestSellingItemBinding homeBestSellingItemBinding) {
            super(homeBestSellingItemBinding.getRoot());
            this.homeBestSellingItemBinding=homeBestSellingItemBinding;
        }

        public void bind(Product product)
        {
            homeBestSellingItemBinding.setVariable(BR.productModule,product);
            homeBestSellingItemBinding.executePendingBindings();
        }
    }
}
