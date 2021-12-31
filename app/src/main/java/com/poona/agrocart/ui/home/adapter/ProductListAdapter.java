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
import com.poona.agrocart.databinding.RowBestSellingItemBinding;
import com.poona.agrocart.databinding.RowProductItemBinding;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductHolder> {
    private ArrayList<Product> products = new ArrayList<>();
    private final Context bdContext;
    private RowBestSellingItemBinding bestSellingBinding;
    private RowProductItemBinding productBinding;
    private final String ListType;
    private final View view;
    private OnProductClick onProductClick;

    public OnProductClick getOnAddClickListener() {
        return onProductClick;
    }

    public void setOnAddClickListener(OnProductClick onProductClick) {
        this.onProductClick = onProductClick;
    }

    public interface OnProductClick {
        void OnAddClick(Product product, int position);
    }


    public ProductListAdapter(ArrayList<Product> products, FragmentActivity context, String listType, View view) {
        this.products = products;
        this.bdContext = context;
        this.ListType = listType;
        this.view = view;
    }


    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_product_item, parent, false);
        return new ProductHolder(productBinding, onProductClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = products.get(position);
        if (ListType.equalsIgnoreCase(PORTRAIT)) {
            bestSellingBinding.setProductModule(product);
            if (product.isInBasket())
                    bestSellingBinding.imgPlus.setImageResource(R.drawable.ic_added);
            holder.bind(product,position);
        } else {
            productBinding.setProductModule(product);
            if (product.isInBasket())
                productBinding.ivPlus.setImageResource(R.drawable.ic_added);
            holder.bindProduct(product,position);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        //The Portrait list holder
        private OnProductClick onProductClick;


        // Redirect to Product details
        private void redirectToProductDetails(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("name", products.get(getAdapterPosition()).getName());
            bundle.putString("image", products.get(getAdapterPosition()).getImg());
            bundle.putString("price", products.get(getAdapterPosition()).getPrice());
            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_product_details, bundle);
        }

        private void addToCart(Product product, int position) {
            AppUtils.infoToast(bdContext, "Added in basket");
            products.get(getAdapterPosition()).setInBasket(true);
            notifyDataSetChanged();
            onProductClick.OnAddClick(product,position);
//        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_cart);
        }

        // The Landscape Item Holder
        public ProductHolder(RowProductItemBinding productBinding, OnProductClick onProductClick) {
            super(productBinding.getRoot());
            this.onProductClick = onProductClick;
            productBinding.ivCross.setVisibility(View.INVISIBLE);
            productBinding.ivMinus.setVisibility(View.INVISIBLE);
            productBinding.etQuantity.setVisibility(View.INVISIBLE);
            // Redirect Product detail from bottom cart items
            productBinding.cardviewProduct.setOnClickListener(v -> {
                redirectToProductDetails(view);
            });
        }

        //Best selling Item Bind
        public void bind(Product product, int i) {
            if (product.getOffer().isEmpty())
                bestSellingBinding.txtItemOffer.setVisibility(View.INVISIBLE);
            if (product.getOfferPrice().isEmpty())
                bestSellingBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            if (product.isOrganic())
                bestSellingBinding.txtOrganic.setVisibility(View.VISIBLE);
            if (product.getQty().equals("0")) {
                bestSellingBinding.txtItemQty.setVisibility(View.INVISIBLE);
                bestSellingBinding.txtItemPrice.setVisibility(View.INVISIBLE);
                bestSellingBinding.txtItemOfferPrice.setVisibility(View.INVISIBLE);
                bestSellingBinding.txtItemOffer.setVisibility(View.INVISIBLE);
                bestSellingBinding.txtOutOfStock.setVisibility(View.VISIBLE);
            }
            bestSellingBinding.setVariable(BR.productModule, product);
            bestSellingBinding.executePendingBindings();

            //Add to cart
            bestSellingBinding.imgPlus.setOnClickListener(v -> {
                addToCart(product, i);
            });
        }

        //Only Product Item bind
        public void bindProduct(Product product, int position) {
            if (product.getImg().endsWith(".jpeg") || product.getImg().endsWith("jpg"))
                productBinding.itemImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (product.getOffer().isEmpty())
                productBinding.txtItemOffer.setVisibility(View.INVISIBLE);
            if (product.getPrice().isEmpty())
                productBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            if (product.isOrganic())
                productBinding.txtOrganic.setVisibility(View.VISIBLE);
            if (product.isInBasket())
                productBinding.ivPlus.setImageResource(R.drawable.ic_added);
            productBinding.setVariable(BR.productModule, product);
            productBinding.executePendingBindings();

            //Add to Cart
            productBinding.ivPlus.setOnClickListener(v -> {
                addToCart(product,position);
            });
        }

    }


}
