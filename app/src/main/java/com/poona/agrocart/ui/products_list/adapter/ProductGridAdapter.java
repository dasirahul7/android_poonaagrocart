package com.poona.agrocart.ui.products_list.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.reponses.ProductListResponse;
import com.poona.agrocart.databinding.RowProductListItemBinding;

import java.util.ArrayList;

public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ProductsViewHolder>
{
    private final ArrayList<ProductListResponse.Product> vegetableArrayList;
    private OnProductClickListener onProductClickListener;

    public ProductGridAdapter(ArrayList<ProductListResponse.Product> vegetableArrayList, OnProductClickListener onProductClickListener) {
        this.vegetableArrayList = vegetableArrayList;
        this.onProductClickListener = onProductClickListener;
    }

    public interface OnProductClickListener{
        void onProductClick(ProductListResponse.Product product);
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RowProductListItemBinding binding = DataBindingUtil.inflate
                (LayoutInflater.from(parent.getContext()),
                        R.layout.row_product_list_item, parent, false);
        return new ProductsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position)
    {
        final ProductListResponse.Product vegetable = vegetableArrayList.get(position);
        holder.productListItemBinding.setProductModule(vegetable);
        holder.bind(vegetable);
    }

    @Override
    public int getItemCount() {
        return vegetableArrayList.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder
    {
        RowProductListItemBinding productListItemBinding;

        public ProductsViewHolder(RowProductListItemBinding productListItemBinding) {
            super(productListItemBinding.getRoot());
            this.productListItemBinding=productListItemBinding;

        }

        private void redirectToCartScreen(View view)
        {
            Navigation.findNavController(view).navigate(R.id.action_nav_products_list_to_nav_cart);
        }


        public void bind(ProductListResponse.Product product)
        {
            productListItemBinding.setVariable(BR.productModule, product);
            productListItemBinding.executePendingBindings();
//            if (Product.isInBasket())
//                productListItemBinding.imgPlus.setImageResource(R.drawable.ic_added);
//            if (Product.isOrganic())
//                productListItemBinding.txtOrganic.setVisibility(View.VISIBLE);
//            if (Product.getWeight().equals("0")) {
//                productListItemBinding.tvWeight.setVisibility(View.INVISIBLE);
//                productListItemBinding.llPrice.setVisibility(View.GONE);
//                productListItemBinding.txtOutOfStock.setVisibility(View.VISIBLE);
//                productListItemBinding.imgPlus.setVisibility(View.GONE);
//            }
            if (Build.VERSION.SDK_INT<=Build.VERSION_CODES.M){
                productListItemBinding.txtItemOfferPrice.setTextSize(14);
            }
            itemView.setOnClickListener(view -> {
                onProductClickListener.onProductClick(product);
            });
        }
    }
}
