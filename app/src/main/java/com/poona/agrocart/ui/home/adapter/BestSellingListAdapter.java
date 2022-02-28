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
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.data.network.responses.homeResponse.Product;
import com.poona.agrocart.databinding.RowBestSellingItemBinding;
import com.poona.agrocart.databinding.RowExclusiveItemBinding;
import com.poona.agrocart.ui.home.HomeFragment;

import java.util.ArrayList;

public class BestSellingListAdapter extends RecyclerView.Adapter<BestSellingListAdapter.BestSellingViewHolder> {
    private final Context bdContext;
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private RowBestSellingItemBinding sellingItemBinding;
    private  OnProductClickListener onProductClickListener;
    private  OnPlusClickListener onPlusClickListener;

    public BestSellingListAdapter(Context context, ArrayList<Product> productList, HomeFragment homeFragment) {
        this.bdContext=context;
        this.productArrayList=productList;
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public interface OnPlusClickListener {
        void OnPlusClick(RowBestSellingItemBinding rowBestSellingItemBinding, Product product, int position);
    }


 /*   public BestSellingListAdapter(ArrayList<Product> products,
                                  Context bdContext, OnProductClickListener onProductClickListener,
                                  OnPlusClickListener onPlusClickListener) {
        this.products = products;
        this.bdContext = bdContext;
        this.onProductClickListener = onProductClickListener;
        this.onPlusClickListener = onPlusClickListener;
    }*/

    @NonNull
    @Override
    public BestSellingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sellingItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_best_selling_item, parent, false);
        return new BestSellingViewHolder(sellingItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellingViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        sellingItemBinding.setBestsellingModule(product);
        holder.bindData(product, position);

        if (product.getSpecialOffer().isEmpty())
            sellingItemBinding.txtItemName.setVisibility(View.INVISIBLE);
        if (product.getSpecialOffer().isEmpty())
            sellingItemBinding.txtItemPrice.setVisibility(View.INVISIBLE);
        if (product.getIsO3().equalsIgnoreCase("yes"))
            sellingItemBinding.txtOrganic.setVisibility(View.VISIBLE);


        if (product.getInCart() == 1){
            sellingItemBinding.imgPlus.setImageResource(R.drawable.ic_added);
        } else if (product.getInCart() == 0){
            sellingItemBinding.imgPlus.setImageResource(R.drawable.ic_plus_white);
        }



    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class BestSellingViewHolder extends RecyclerView.ViewHolder {
        public BestSellingViewHolder(RowBestSellingItemBinding rowBestSellingItemBinding) {
            super(rowBestSellingItemBinding.getRoot());
        }

        public void bindData(Product product, int position) {
            sellingItemBinding.setVariable(BR.exclusiveOfferModule, product);
            sellingItemBinding.executePendingBindings();
//            rowExclusiveItemBinding.txtItemPrice.setText(product.getProductUnits().get(0).getOfferPrice());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onProductClickListener.onProductClick(product);
                }
            });


            sellingItemBinding.imgPlus.setOnClickListener(view1 -> {
                onPlusClickListener.OnPlusClick(sellingItemBinding, product, position);
            });
        }
    }
}
