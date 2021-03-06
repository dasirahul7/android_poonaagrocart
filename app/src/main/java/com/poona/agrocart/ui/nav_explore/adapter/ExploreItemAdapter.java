package com.poona.agrocart.ui.nav_explore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowExploreItemBinding;
import com.poona.agrocart.ui.nav_explore.model.ExploreItems;

import java.util.ArrayList;

public class ExploreItemAdapter extends RecyclerView.Adapter<ExploreItemAdapter.ExploreItemHolder> {
    private final Context exContext;
    private final View rootView;
    private ArrayList<ExploreItems> exploreItems = new ArrayList<>();
    private RowExploreItemBinding exploreItemBinding;
    private final OnExploreClickListener onExploreClickListener;

    public ExploreItemAdapter(Context exContext, ArrayList<ExploreItems> exploreItems, View view, OnExploreClickListener onExploreClickListener) {
        this.exContext = exContext;
        this.exploreItems = exploreItems;
        this.rootView = view;
        this.onExploreClickListener = onExploreClickListener;
    }

    @NonNull
    @Override
    public ExploreItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        exploreItemBinding = DataBindingUtil.inflate(LayoutInflater.from(exContext), R.layout.row_explore_item, parent, false);
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

    public interface OnExploreClickListener {
        void onExploreItemClick(ExploreItems items);
    }

    public class ExploreItemHolder extends RecyclerView.ViewHolder {
        RowExploreItemBinding rowExploreItemBinding;

        public ExploreItemHolder(RowExploreItemBinding binding) {
            super(binding.getRoot());
            this.rowExploreItemBinding = binding;
        }


        public void bind(ExploreItems items) {
            exploreItemBinding.setVariable(BR.exploreModules, items);
            exploreItemBinding.executePendingBindings();
            exploreItemBinding.itemLayout.setOnClickListener(v -> {
                onExploreClickListener.onExploreItemClick(items);
            });
        }
    }

//    private void gotoExploreItems(View v, String name,int adapterPosition)
//    {
//        Bundle bundle=new Bundle();
//        bundle.putString(LIST_TITLE,name);
//        bundle.putString(CATEGORY_ID,);
//        Navigation.findNavController(v).navigate(R.id.action_nav_explore_to_nav_products_list,bundle);
//    }
}
