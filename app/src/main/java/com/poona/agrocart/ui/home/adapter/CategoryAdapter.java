package com.poona.agrocart.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowCategoryItemBinding;
import com.poona.agrocart.ui.home.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private ArrayList<Category> categories = new ArrayList<>();
    private Context context;
    private RowCategoryItemBinding categoryBinding;
    private View view;

    public CategoryAdapter(ArrayList<Category> categories, Context context,View view) {
        this.categories = categories;
        this.context = context;
        this.view=view;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        categoryBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.row_category_item,parent,false);
        return new CategoryHolder(categoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category category = categories.get(position);
        categoryBinding.setCategoryModule(category);
        holder.bind(category,view);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        public CategoryHolder(RowCategoryItemBinding binding) {
            super(binding.getRoot());
        }

        public void bind(Category category,View view) {
            categoryBinding.setVariable(BR.categoryModule,category);
            categoryBinding.executePendingBindings();

            categoryBinding.cardviewCategory.setOnClickListener(v -> {
                redirectToProductsList(view);
            });
        }
    }

    private void redirectToProductsList(View v)
    {
        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_products_list);
    }
}