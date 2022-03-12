package com.poona.agrocart.ui.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowSearchItemBinding;
import com.poona.agrocart.ui.search.model.CommonSearchItem;

import java.util.ArrayList;

public class CommonSearchAdapter extends RecyclerView.Adapter<CommonSearchAdapter.SearchViewHolder> {
    private final ArrayList<CommonSearchItem> commonSearchItems;
    private RowSearchItemBinding rowSearchItemBinding;
    private OnSearchItemClickListener onSearchItemClickListener;
    private String itemId,pUid;

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowSearchItemBinding = RowSearchItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new SearchViewHolder(rowSearchItemBinding);
    }
    public interface OnSearchItemClickListener{
        void onSearchItemClick(CommonSearchItem commonSearchItem);
        void onSearchAddClick(String itemId, String pUid, int position);
    }

    public CommonSearchAdapter(ArrayList<CommonSearchItem> commonSearchItems,
                               OnSearchItemClickListener onSearchItemClickListener) {
        this.commonSearchItems = commonSearchItems;
        this.onSearchItemClickListener = onSearchItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        CommonSearchItem commonSearchItem = commonSearchItems.get(position);
        rowSearchItemBinding.setCommonSearchData(commonSearchItem);
        if (commonSearchItem.getItemType().equalsIgnoreCase("Product")) {
            itemId = commonSearchItem.getProductId();
            pUid = commonSearchItem.getPuId();
        }
        else itemId = commonSearchItem.getBasketId();
        if (commonSearchItem.getInCart()==1)
            rowSearchItemBinding.imgPlus.setImageResource(R.drawable.ic_added);
        else rowSearchItemBinding.imgPlus.setImageResource(R.drawable.ic_plus_white);

        //Add to cart initialize here
        rowSearchItemBinding.imgPlus.setOnClickListener(view -> {
           onSearchItemClickListener.onSearchAddClick(itemId,pUid,position);
        });
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
