package com.poona.agrocart.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.HomeCatItemBinding;
import com.poona.agrocart.ui.home.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private ArrayList<Category> categories = new ArrayList<>();
    private Context context;
    private HomeCatItemBinding categoryBinding;

    public CategoryAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        categoryBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.home_cat_item,parent,false);
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

    public class CategoryHolder extends RecyclerView.ViewHolder {
        public CategoryHolder(HomeCatItemBinding binding) {
            super(binding.getRoot());
        }

        public void bind(Category category) {
            categoryBinding.setVariable(BR.categoryModule,category);
            categoryBinding.executePendingBindings();
        }
    }
}
