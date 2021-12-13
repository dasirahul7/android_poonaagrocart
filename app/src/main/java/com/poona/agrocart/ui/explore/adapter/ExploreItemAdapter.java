package com.poona.agrocart.ui.explore.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowExploreItemBinding;
import com.poona.agrocart.ui.explore.model.ExploreItems;

import java.util.ArrayList;

public class ExploreItemAdapter extends RecyclerView.Adapter<ExploreItemAdapter.ExploreItemHolder> {
    private final Context exContext;
    private ArrayList<ExploreItems> exploreItems = new ArrayList<>();
    private RowExploreItemBinding exploreItemBinding;
    private final View rootView;

    public ExploreItemAdapter(Context exContext, ArrayList<ExploreItems> exploreItems,View view) {
        this.exContext = exContext;
        this.exploreItems = exploreItems;
        this.rootView = view;
    }

    @NonNull
    @Override
    public ExploreItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        exploreItemBinding = DataBindingUtil.inflate(LayoutInflater.from(exContext),R.layout.row_explore_item,parent,false);
        return new ExploreItemHolder(exploreItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreItemHolder holder, int position) {
        ExploreItems items = exploreItems.get(position);
        exploreItemBinding.setExploreModules(items);
        holder.bind(items);
    }

    @Override
    public int getItemCount() {
        return exploreItems.size();
    }

    public class ExploreItemHolder extends RecyclerView.ViewHolder
    {
        RowExploreItemBinding rowExploreItemBinding;
        public ExploreItemHolder(RowExploreItemBinding binding)
        {
            super(binding.getRoot());

            this.rowExploreItemBinding=binding;
            rowExploreItemBinding.itemCard.setOnClickListener(v -> {
                redirectToRequiredProductsPage(rootView,getAdapterPosition());
            });
        }

        private void redirectToRequiredProductsPage(View view,int adapterPosition)
        {
        }

        public void bind(ExploreItems items)
        {
            exploreItemBinding.setVariable(BR.exploreModules,items);
            exploreItemBinding.executePendingBindings();
            exploreItemBinding.itemLayout.setOnClickListener(v -> {
                gotoExploreItems(v,items.getName(),getAdapterPosition());
            });
        }
    }

    private void gotoExploreItems(View v, String name,int adapterPosition)
    {
        Bundle bundle=new Bundle();
        switch (adapterPosition)
        {
            case 0:
                Navigation.findNavController(v).navigate(R.id.action_nav_explore_to_nav_explore_baskets);
                break;
            case 1:
                bundle.putString("ProductCategory","vegetable");
                Navigation.findNavController(v).navigate(R.id.action_nav_explore_to_nav_products_list,bundle);
                break;
            default:
                bundle.putString("ProductCategory","fruits");
                Navigation.findNavController(v).navigate(R.id.action_nav_explore_to_nav_products_list,bundle);
                break;
        }
        //Navigation.findNavController(v).navigate(R.id.action_nav_explore_to_nav_explore_baskets,bundle);
    }
}
