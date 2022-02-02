package com.poona.agrocart.ui.home.adapter;

import static com.poona.agrocart.app.AppConstants.PORTRAIT;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.databinding.HomeProductItemBinding;
import com.poona.agrocart.databinding.RowBestSellingItemBinding;
import com.poona.agrocart.databinding.HomeProductItemBinding;
import com.poona.agrocart.ui.home.OnPlusClick;
import com.poona.agrocart.ui.home.OnProductClick;
import com.poona.agrocart.ui.home.model.Exclusive;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductHolder> {
    private ArrayList<Exclusive> products = new ArrayList<>();
    private final Context bdContext;
    private HomeProductItemBinding productBinding;
    private final View view;
    private OnPlusClick onPlusClick;
    private OnProductClick onProductClick;

    public void setOnPlusClick(OnPlusClick onPlusClick) {
        this.onPlusClick = onPlusClick;
    }

    public void setOnProductClick(OnProductClick onProductClick) {
        this.onProductClick = onProductClick;
    }

    public ProductListAdapter(ArrayList<Exclusive> products, FragmentActivity context, View view) {
        this.products = products;
        this.bdContext = context;
        this.view = view;
    }


    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.home_product_item, parent, false);
        return new ProductHolder(productBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Exclusive product = products.get(position);
            productBinding.setHomeProductModel(product);
            holder.bindProduct(product,position);
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
        //Only Product Item bind
        public void bindProduct(Exclusive product, int position) {
            productBinding.setVariable(BR.productModule, product);
            productBinding.executePendingBindings();
            if (product.getFeatureImg().endsWith(".jpeg") || product.getFeatureImg().endsWith("jpg"))
                productBinding.itemImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (product.getSpecialOffer().isEmpty())
                productBinding.txtItemOffer.setVisibility(View.INVISIBLE);
            if (product.getIsO3().equalsIgnoreCase("yes"))
                productBinding.txtOrganic.setVisibility(View.VISIBLE);
            else productBinding.txtOrganic.setVisibility(View.GONE);
//            if (product.isInBasket())
//                productBinding.ivPlus.setImageResource(R.drawable.ic_added);
//            productBinding.ivPlus.setOnClickListener(v -> {
//                onPlusClick.addToCart(product,position);
//            });
//            itemView.setOnClickListener(v -> {
//                onProductClick.toProductDetail(product);
//            });

        }

    }


}
