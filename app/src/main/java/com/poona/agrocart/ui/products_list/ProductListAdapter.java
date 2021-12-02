package com.poona.agrocart.ui.products_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowBestSellingItemBinding;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductsViewHolder>
{
    private ArrayList<Product> vegetableArrayList;
    private View view;

    public ProductListAdapter(ArrayList<Product> vegetableArrayList, View view)
    {
        this.vegetableArrayList = vegetableArrayList;
        this.view=view;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RowBestSellingItemBinding binding = DataBindingUtil.inflate
                (LayoutInflater.from(parent.getContext()),
                        R.layout.row_best_selling_item, parent, false);
        return new ProductsViewHolder(binding,view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position)
    {
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

        public ProductsViewHolder(RowBestSellingItemBinding homeBestSellingItemBinding,View view) {
            super(homeBestSellingItemBinding.getRoot());
            this.homeBestSellingItemBinding=homeBestSellingItemBinding;

            homeBestSellingItemBinding.cardviewProduct.setOnClickListener(v -> {
                redirectToProductsDetail(view);
            });

            homeBestSellingItemBinding.imgPlus.setOnClickListener(v -> {
                redirectToCartScreen(view);
            });
        }

        private void redirectToCartScreen(View view)
        {
            Navigation.findNavController(view).navigate(R.id.action_nav_products_list_to_nav_cart);
        }

        private void redirectToProductsDetail(View v)
        {
            Navigation.findNavController(v).navigate(R.id.action_nav_products_list_to_productDetailFragment2);
        }

        public void bind(Product product)
        {
            homeBestSellingItemBinding.setVariable(BR.productModule,product);
            homeBestSellingItemBinding.executePendingBindings();
        }
    }
}
