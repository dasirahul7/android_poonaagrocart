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

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.BestSellingHolder> {
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
        void OnAddClick(Product product);
    }


    public ProductListAdapter(ArrayList<Product> products, FragmentActivity context, String listType, View view) {
        this.products = products;
        this.bdContext = context;
        this.ListType = listType;
        this.view = view;
    }


    @Override
    public BestSellingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        bestSellingBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_best_selling_item, parent, false);
        productBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_product_item, parent, false);
        if (ListType.equalsIgnoreCase(PORTRAIT))
            return new BestSellingHolder(bestSellingBinding,onProductClick);
        else return new BestSellingHolder(productBinding,onProductClick);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellingHolder holder, int position) {
        Product product = products.get(position);
        if (ListType.equalsIgnoreCase(PORTRAIT)) {
            bestSellingBinding.setProductModule(product);
            holder.bind(product);
        } else {
            productBinding.setProductModule(product);
            holder.bindProduct(product);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class BestSellingHolder extends RecyclerView.ViewHolder {
        //The Portrait list holder
        private OnProductClick onProductClick;
        public BestSellingHolder(RowBestSellingItemBinding bestSellingBinding,OnProductClick onBestClick) {
            super(bestSellingBinding.getRoot());
            this.onProductClick = onBestClick;
            // Redirect to product details from best selling item
            bestSellingBinding.cardviewProduct.setOnClickListener(v -> {
                redirectToProductDetails(view);
            });

        }

        // Redirect to Product details
        private void redirectToProductDetails(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("name",products.get(getAdapterPosition()).getName());
            bundle.putString("image",products.get(getAdapterPosition()).getImg());
            bundle.putString("price",products.get(getAdapterPosition()).getPrice());
            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_product_details,bundle);
        }
        // The Landscape Item Holder
        public BestSellingHolder(RowProductItemBinding productBinding,OnProductClick onProductClick) {
            super(productBinding.getRoot());
            this.onProductClick = onProductClick;
            productBinding.ivCross.setVisibility(View.INVISIBLE);
            productBinding.ivMinus.setVisibility(View.INVISIBLE);
            productBinding.etQuantity.setVisibility(View.INVISIBLE);
            // Redirect Product detail from bottom cart items
            productBinding.cardviewProduct.setOnClickListener(v -> {
                redirectToProductDetails(view);
            });
            productBinding.ivPlus.setOnClickListener(v -> {

            });
            productBinding.ivMinus.setOnClickListener(v -> {
                int quantity = Integer.parseInt(productBinding.etQuantity.getText().toString());
                if (quantity > 1) {
                    quantity--;
                    productBinding.etQuantity.setText(String.valueOf(quantity));
                AppUtils.setMinusButton(quantity,productBinding.ivMinus);
                }
            });
        }
        //Best selling Item Bind
        public void bind(Product product) {
            if (product.getOffer().isEmpty())
                bestSellingBinding.txtItemOffer.setVisibility(View.INVISIBLE);
            if (product.getOfferPrice().isEmpty())
                bestSellingBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            if (product.isOrganic())
                bestSellingBinding.txtOrganic.setVisibility(View.VISIBLE);
            if (product.getQty().equals("0")){
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
                addToCart(product);
            });
        }

        //Only Product Item bind
        public void bindProduct(Product product) {
            if(product.getImg().endsWith(".jpeg")||product.getImg().endsWith("jpg"))
            productBinding.itemImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (product.getOffer().isEmpty())
                productBinding.txtItemOffer.setVisibility(View.INVISIBLE);
            if (product.getPrice().isEmpty())
                productBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            if (product.isOrganic())
                productBinding.txtOrganic.setVisibility(View.VISIBLE);
            productBinding.setVariable(BR.productModule, product);
            productBinding.executePendingBindings();

            //Add to Cart
            productBinding.ivPlus.setOnClickListener(v -> {
                addToCart(product);
            });
        }

    }

    private void addToCart(Product product) {
        AppUtils.infoToast(bdContext,product.getName());
        onProductClick.OnAddClick(product);
//        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_cart);
    }
}
