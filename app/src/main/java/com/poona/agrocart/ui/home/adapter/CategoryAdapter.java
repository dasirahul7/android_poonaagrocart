package com.poona.agrocart.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.homeResponse.Category;
import com.poona.agrocart.data.network.responses.homeResponse.HomeResponse;
import com.poona.agrocart.databinding.RowCategoryItemBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private final Context context;
    private ArrayList<Category> categories = new ArrayList<>();
    private RowCategoryItemBinding categoryBinding;
    private final OnCategoryClickListener onCategoryClickListener;

    public CategoryAdapter(ArrayList<Category> categories,
                           Context context, OnCategoryClickListener onCategoryClickListener) {
        this.categories = categories;
        this.context = context;
        this.onCategoryClickListener = onCategoryClickListener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        categoryBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_category_item, parent, false);
        return new CategoryHolder(categoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category category = categories.get(position);
        categoryBinding.setCategoryModule(category);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface OnCategoryClickListener {
        void categoryClick(Category category);
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        public CategoryHolder(RowCategoryItemBinding binding) {
            super(binding.getRoot());
        }

        public void bind(Category category) {
            categoryBinding.setVariable(BR.categoryModule, category);
            categoryBinding.executePendingBindings();

            categoryBinding.cardviewCategory.setOnClickListener(v -> {
                onCategoryClickListener.categoryClick(category);
            });
        }
    }
}
