package com.poona.agrocart.ui.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.databinding.RowSearchItemBinding;
import com.poona.agrocart.ui.search.model.CommonSearchItem;

import java.util.ArrayList;

public class CommonSearchAdapter extends RecyclerView.Adapter<CommonSearchAdapter.SearchViewHolder> {
    private final ArrayList<CommonSearchItem> commonSearchItems;
    private RowSearchItemBinding rowSearchItemBinding;
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowSearchItemBinding = RowSearchItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new SearchViewHolder(rowSearchItemBinding);
    }
    public interface onSearchAddClickListener{
        void onSearchAddClick();
    }

    public CommonSearchAdapter(ArrayList<CommonSearchItem> commonSearchItems) {
        this.commonSearchItems = commonSearchItems;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        CommonSearchItem commonSearchItem = commonSearchItems.get(position);
        rowSearchItemBinding.setCommonSearchData(commonSearchItem);
        holder.bind(commonSearchItem);
    }

    @Override
    public int getItemCount() {
        return commonSearchItems.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        public SearchViewHolder(@NonNull RowSearchItemBinding itemView) {
            super(itemView.getRoot());
        }

        public void bind(CommonSearchItem commonSearchItem) {
            if (commonSearchItem.getWeight()==null)
                rowSearchItemBinding.tvWeight.setVisibility(View.GONE);
            if (commonSearchItem.getItemType().equalsIgnoreCase("Product") && commonSearchItem.getOfferPrice()==null)
            {
                rowSearchItemBinding.tvOfferPrice.setVisibility(View.GONE);
                rowSearchItemBinding.tvRs.setVisibility(View.GONE);
            }
            else if (commonSearchItem.getItemType().equalsIgnoreCase("Basket") && commonSearchItem.getBasketRate()==null)
            {
                rowSearchItemBinding.txtItemPrice.setVisibility(View.GONE);
                rowSearchItemBinding.tvRs.setVisibility(View.GONE);
            }
            if (commonSearchItem.getLocation()==null || commonSearchItem.getLocation().equalsIgnoreCase(""))
                rowSearchItemBinding.tvLocation.setVisibility(View.GONE);

            rowSearchItemBinding.setVariable(BR.commonSearchData,commonSearchItem);
            rowSearchItemBinding.executePendingBindings();
        }
    }
}
