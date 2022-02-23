package com.poona.agrocart.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.databinding.HomeProductItemBinding;
import com.poona.agrocart.databinding.RowExclusiveItemBinding;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductHolder> {
    private final Context bdContext;
    private ArrayList<ProductListResponse.Product> products = new ArrayList<>();
    private HomeProductItemBinding productBinding;
    private final OnProductClickListener onProductClickListener;
    private final OnPlusClickListener onPlusClickListener;

    public interface OnProductClickListener {
        void onProductClick(ProductListResponse.Product product);
    }

    public interface OnPlusClickListener {
        void OnPlusClick(HomeProductItemBinding homeProductItemBinding, ProductListResponse.Product product, int position);
    }

    public ProductListAdapter(ArrayList<ProductListResponse.Product> products,
                              FragmentActivity context,
                              OnProductClickListener onProductClickListener,
                              OnPlusClickListener onPlusClickListener) {
        this.products = products;
        this.bdContext = context;
        this.onProductClickListener = onProductClickListener;
        this.onPlusClickListener = onPlusClickListener;
    }

    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.home_product_item, parent, false);
        return new ProductHolder(productBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        ProductListResponse.Product product = products.get(position);
        productBinding.setHomeProductModel(product);
        holder.bindProduct(product, position);
        productBinding.imgPlus.setOnClickListener(view -> {
            if (product.getInCart()==0){
                productBinding.rlAddToCartLoader.setVisibility(View.VISIBLE);
                onPlusClickListener.OnPlusClick(productBinding, product, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        // The Landscape Item Holder
        public ProductHolder(HomeProductItemBinding productBinding) {
            super(productBinding.getRoot());
        }

        //Only ProductOld Item bind
        public void bindProduct(ProductListResponse.Product product, int position) {
            productBinding.setVariable(BR.productModule, product);
            productBinding.executePendingBindings();
            if (product.getFeatureImg().endsWith(".jpeg") || product.getFeatureImg().endsWith("jpg"))
                productBinding.itemImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (product.getSpecialOffer().isEmpty())
                productBinding.txtItemOffer.setVisibility(View.INVISIBLE);
            if (product.getIsO3().equalsIgnoreCase("yes"))
                productBinding.txtOrganic.setVisibility(View.VISIBLE);
            else productBinding.txtOrganic.setVisibility(View.GONE);
            if (product.getInCart() == 1)
                productBinding.imgPlus.setImageResource(R.drawable.ic_added);
            else productBinding.imgPlus.setImageResource(R.drawable.ic_plus_white);

            itemView.setOnClickListener(view -> {
                onProductClickListener.onProductClick(product);
            });
            if (product.getInCart() == 1)
                productBinding.ivPlus.setEnabled(false);
        }

    }


}
