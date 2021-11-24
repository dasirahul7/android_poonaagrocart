package com.poona.agrocart.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.HomeBestSellingItemBinding;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.BestSellingHolder> {
    private ArrayList<Product> products = new ArrayList<>();
    private Context bdContext;
    private HomeBestSellingItemBinding rowItemBinding;

    public ProductListAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.bdContext = context;
    }

    @Override
    public BestSellingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.home_best_selling_item, parent, false);
        return new BestSellingHolder(rowItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellingHolder holder, int position) {
        Product product = products.get(position);
        rowItemBinding.setProductModule(product);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class BestSellingHolder extends RecyclerView.ViewHolder {
        public BestSellingHolder(HomeBestSellingItemBinding rowItemBinding) {
            super(rowItemBinding.getRoot());
        }

        public void bind(Product product) {
//            rowItemBinding.txtItemPrice.
            rowItemBinding.setVariable(BR.productModule,product);
            rowItemBinding.executePendingBindings();
        }
    }
}
