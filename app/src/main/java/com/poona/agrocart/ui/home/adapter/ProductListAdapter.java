package com.poona.agrocart.ui.home.adapter;

import static com.poona.agrocart.app.AppConstants.PORTRAIT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowBestSellingItemBinding;
import com.poona.agrocart.databinding.RowProductItemBinding;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.BestSellingHolder> {
    private ArrayList<Product> products = new ArrayList<>();
    private Context bdContext;
    private RowBestSellingItemBinding rowItemBinding;
    private RowProductItemBinding rowProductItemBinding;
    private String ListType;

    public ProductListAdapter(ArrayList<Product> products, FragmentActivity context, String listType) {
        this.products = products;
        this.bdContext = context;
        this.ListType = listType;
    }


    @Override
    public BestSellingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext), R.layout.row_best_selling_item, parent, false);
        rowProductItemBinding = DataBindingUtil.inflate(LayoutInflater.from(bdContext),R.layout.row_product_item,parent,false);
        if (ListType.equalsIgnoreCase(PORTRAIT))
        return new BestSellingHolder(rowItemBinding);
        else return new BestSellingHolder(rowProductItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellingHolder holder, int position) {
        Product product = products.get(position);
        if (ListType.equalsIgnoreCase(PORTRAIT)){
            rowItemBinding.setProductModule(product);
            holder.bind(product);
        }else {
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
        }
        public BestSellingHolder(RowProductItemBinding rowProductItemBinding) {
            super(rowProductItemBinding.getRoot());
        }

        public void bind(Product product) {
            if (product.getOffer().isEmpty())
            {
                rowItemBinding.txtItemOffer.setVisibility(View.INVISIBLE);
                rowItemBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            }
            rowItemBinding.setVariable(BR.productModule,product);
            rowItemBinding.executePendingBindings();
        }
        public void bindProduct(Product product) {
            if (product.getOffer().isEmpty()){
                rowProductItemBinding.txtItemOffer.setVisibility(View.INVISIBLE);
                rowItemBinding.txtItemPrice.setVisibility(View.INVISIBLE);
            }
            rowProductItemBinding.setVariable(BR.productModule,product);
            rowProductItemBinding.executePendingBindings();
        }
    }
}
