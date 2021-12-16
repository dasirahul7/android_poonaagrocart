package com.poona.agrocart.ui.nav_explore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowCheckBoxItemBinding;
import com.poona.agrocart.ui.nav_explore.model.FilterItem;

import java.util.ArrayList;

public class FilterItemAdapter extends RecyclerView.Adapter<FilterItemAdapter.FilterViewHolder> {
    private ArrayList<FilterItem> filterItems = new ArrayList<>();
    private final Context context;
    private RowCheckBoxItemBinding itemBinding;
    private OnFilterClickListener onFilterClickListener;

    public interface OnFilterClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnFilterClickListener onFilterClickListener) {
        this.onFilterClickListener = onFilterClickListener;
    }

    public FilterItemAdapter(ArrayList<FilterItem> filterItems, Context context) {
        this.filterItems = filterItems;
        this.context = context;
//        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public FilterItemAdapter.FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_check_box_item, parent, false);
        View view = itemBinding.getRoot();
        return new FilterViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterItemAdapter.FilterViewHolder holder, int position) {
        FilterItem item = filterItems.get(position);
        holder.itemBinding.setModuleFilter(item);
        holder.bindItem(item, onFilterClickListener);
    }


    @Override
    public int getItemCount() {
        return filterItems.size();
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder {
        private final RowCheckBoxItemBinding itemBinding;

        public FilterViewHolder(RowCheckBoxItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding=itemBinding;
        }

        public void bindItem(FilterItem item, OnFilterClickListener onFilterClickListener) {
            itemBinding.setModuleFilter(item);
            itemBinding.executePendingBindings();
            itemBinding.checkView.setOnClickListener(v -> {
                if (itemBinding.checkbox.isChecked()){
                    itemBinding.checkbox.setChecked(false);
                    setCheckedView();
                }
                else {
                    itemBinding.checkbox.setChecked(true);
                    setCheckedView();
                }


            });

        }

        private void setCheckedView() {
            if (itemBinding.checkbox.isChecked())
                itemBinding.tvFilter.setTextColor(context.getColor(R.color.colorPrimary));
            else itemBinding.tvFilter.setTextColor(context.getColor(R.color.black));
        }
    }
}
