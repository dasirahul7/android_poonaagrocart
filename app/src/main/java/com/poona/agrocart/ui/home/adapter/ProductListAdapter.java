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
    private RowBestSellingItemBinding rowBestSellingItemBinding;
    private RowProductItemBinding rowProductItemBinding;
    private final String ListType;
    private final View view;

    public ProductListAdapter(ArrayList<Product> products, FragmentActivity context, String listType, View view) {
        this.products = products;
        this.bdContext = context;
        this.ListType = listType;
        this.view = view;
    }


    @Override
    public BestSellingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowBestSellingItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_best_selling_item, parent, false);
        rowProductItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_product_item, parent, false);
        if (ListType.equalsIgnoreCase(PORTRAIT))
            return new BestSellingHolder(rowBestSellingItemBinding);
        else return new BestSellingHolder(rowProductItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellingHolder holder, int position) {
        Product product = products.get(position);
        if (ListType.equalsIgnoreCase(PORTRAIT)) {
            rowBestSellingItemBinding.setProductModule(product);
            holder.bind(product);
        } else {
            rowProductItemBinding.setProductModule(product);
            holder.bindProduct(product);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class BestSellingHolder extends RecyclerView.ViewHolder {
        public BestSellingHolder(RowBestSellingItemBinding rowItemBinding) {
            super(rowItemBinding.getRoot());
            rowItemBinding.cardviewProduct.setOnClickListener(v -> {
                redirectToProductDetails(view);
            });

            rowItemBinding.imgPlus.setOnClickListener(v -> {
                gotoCart(view);
            });
        }

        private void redirectToProductDetails(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("name",products.get(getAdapterPosition()).getName());
            bundle.putString("image",products.get(getAdapterPosition()).getImg());
            bundle.putString("price",products.get(getAdapterPosition()).getPrice());
            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_product_details,bundle);
        }

        public BestSellingHolder(RowProductItemBinding rowProductItemBinding) {
            super(rowProductItemBinding.getRoot());
            rowProductItemBinding.ivCross.setVisibility(View.INVISIBLE);
            rowProductItemBinding.cardviewProduct.setOnClickListener(v -> {
                redirectToProductDetails(view);
            });
            rowProductItemBinding.imgPlus.setOnClickListener(v -> {

                increaseQuantity();
            });
            rowProductItemBinding.ivMinus.setOnClickListener(v -> {
                decreaseQuantity();
            });
        }

        private void increaseQuantity() {
            int quantity = Integer.parseInt(rowProductItemBinding.etQuantity.getText().toString());
            quantity++;
            rowProductItemBinding.etQuantity.setText(String.valueOf(quantity));
//            AppUtils.setMinusButton(quantity,rowProductItemBinding.ivMinus);
        }

        private void decreaseQuantity() {
            int quantity = Integer.parseInt(rowProductItemBinding.etQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                rowProductItemBinding.etQuantity.setText(String.valueOf(quantity));
//                AppUtils.setMinusButton(quantity,rowProductItemBinding.ivMinus);
            }
        }

        public void bind(Product product) {
            if (product.getOffer().isEmpty())
                rowBestSellingItemBinding.txtItemOffer.setVisibility(View.INVISIBLE);
            if (product.getOfferPrice().isEmpty())
                rowBestSellingItemBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            if (product.isOrganic())
                rowBestSellingItemBinding.txtOrganic.setVisibility(View.VISIBLE);
            if (product.getQty().equals("0")){
                rowBestSellingItemBinding.txtItemQty.setVisibility(View.INVISIBLE);
                rowBestSellingItemBinding.txtItemPrice.setVisibility(View.INVISIBLE);
                rowBestSellingItemBinding.txtItemOfferPrice.setVisibility(View.INVISIBLE);
                rowBestSellingItemBinding.txtItemOffer.setVisibility(View.INVISIBLE);
                rowBestSellingItemBinding.txtOutOfStock.setVisibility(View.VISIBLE);
            }
            rowBestSellingItemBinding.setVariable(BR.productModule, product);
            rowBestSellingItemBinding.executePendingBindings();
        }

        public void bindProduct(Product product) {
            if(product.getImg().endsWith(".jpeg")||product.getImg().endsWith("jpg"))
            rowProductItemBinding.itemImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (product.getOffer().isEmpty())
                rowProductItemBinding.txtItemOffer.setVisibility(View.INVISIBLE);
            if (product.getPrice().isEmpty())
                rowProductItemBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            if (product.isOrganic())
                rowProductItemBinding.txtOrganic.setVisibility(View.VISIBLE);
            rowProductItemBinding.setVariable(BR.productModule, product);
            rowProductItemBinding.executePendingBindings();
        }
    }

    private void gotoCart(View v) {
        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_cart);
    }
}
