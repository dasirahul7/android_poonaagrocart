package com.poona.agrocart.ui.bottom_sheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.BottomSheetFilterDialogBinding;
import com.poona.agrocart.ui.nav_explore.adapter.FilterItemAdapter;
import com.poona.agrocart.ui.nav_explore.model.FilterItem;
import com.poona.agrocart.widgets.ExpandIconView;

import java.util.ArrayList;

public class BottomSheetFilterFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetFilterDialogBinding bottomSheetFilterFragment;
    private FilterItemAdapter categoryAdapter, sortByAdapter, brandAdapter;
    private BasketFilterViewModel basketFilterViewModel;
    private ArrayList<FilterItem> categoryItems;
    private ArrayList<FilterItem> filterItems;
    private ArrayList<FilterItem> brandItems;
    private BottomSheetListener mListener;
    private boolean isCategoryVisible = true;
    private boolean isFilterVisible = true;
    private boolean isBrandVisible = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomSheetFilterFragment = BottomSheetFilterDialogBinding.inflate(LayoutInflater.from(getActivity()));
        basketFilterViewModel = new ViewModelProvider(this).get(BasketFilterViewModel.class);
        setRvAdapter();
        setClicks();
        //Hide all list by default
        setFilterHideOrShow();
        setBrandHideOrShow();
        return bottomSheetFilterFragment.getRoot();
    }

    private void setClicks() {
        bottomSheetFilterFragment.ivCategory.setOnClickListener(this);
        bottomSheetFilterFragment.llCategoryFilter.setOnClickListener(this);
        bottomSheetFilterFragment.ivSortBy.setOnClickListener(this);
        bottomSheetFilterFragment.llSortByFilter.setOnClickListener(this);
        bottomSheetFilterFragment.ivBrand.setOnClickListener(this);
        bottomSheetFilterFragment.llBrandFilter.setOnClickListener(this);
    }

    private void setCategoryHideOrShow() {
        if (isCategoryVisible) {
            bottomSheetFilterFragment.ivCategory.setState(ExpandIconView.MORE, true);
            collapseView(bottomSheetFilterFragment.rvCategoryList);
        } else {
            bottomSheetFilterFragment.ivCategory.setState(ExpandIconView.LESS, true);
            expandView(bottomSheetFilterFragment.rvCategoryList);
        }
        isCategoryVisible = !isCategoryVisible;
    }

    private void setFilterHideOrShow() {
        if (isFilterVisible) {
            bottomSheetFilterFragment.ivSortBy.setState(ExpandIconView.MORE, true);
            collapseView(bottomSheetFilterFragment.rvSortList);
        } else {
            bottomSheetFilterFragment.ivSortBy.setState(ExpandIconView.LESS, true);
            expandView(bottomSheetFilterFragment.rvSortList);
        }
        isFilterVisible = !isFilterVisible;
    }

    private void setBrandHideOrShow() {
        if (isBrandVisible) {
            bottomSheetFilterFragment.ivBrand.setState(ExpandIconView.MORE, true);
            collapseView(bottomSheetFilterFragment.rvBrandList);
        } else {
            bottomSheetFilterFragment.ivBrand.setState(ExpandIconView.LESS, true);
            expandView(bottomSheetFilterFragment.rvBrandList);
        }
        isBrandVisible = !isBrandVisible;
    }

    private void setRvAdapter() {
        //setCategory
        basketFilterViewModel.getCategoryLiveData().observe(getViewLifecycleOwner(), categoryFilterItems -> {
            categoryItems = categoryFilterItems;
            categoryAdapter = new FilterItemAdapter(categoryItems, getActivity());
            bottomSheetFilterFragment.rvCategoryList.setLayoutManager(new LinearLayoutManager(getActivity(),
                    RecyclerView.VERTICAL, false));
            bottomSheetFilterFragment.rvCategoryList.setHasFixedSize(true);
            bottomSheetFilterFragment.rvCategoryList.setAdapter(categoryAdapter);
        });
        //set SotBy and Filter
        basketFilterViewModel.getFilterItemLiveData().observe(getViewLifecycleOwner(), sortByFilterItems -> {
            filterItems = sortByFilterItems;
            sortByAdapter = new FilterItemAdapter(filterItems, getActivity());
            bottomSheetFilterFragment.rvSortList.setLayoutManager(new LinearLayoutManager(getActivity(),
                    RecyclerView.VERTICAL, false));
            bottomSheetFilterFragment.rvSortList.setHasFixedSize(true);
            bottomSheetFilterFragment.rvSortList.setAdapter(sortByAdapter);
        });
        // set Brand Filter
        basketFilterViewModel.getBrandItemLiveData().observe(getViewLifecycleOwner(), brandFilterItems -> {
            brandItems = brandFilterItems;
            brandAdapter = new FilterItemAdapter(brandItems, getActivity());
            bottomSheetFilterFragment.rvBrandList.setHasFixedSize(true);
            bottomSheetFilterFragment.rvBrandList.setLayoutManager(new LinearLayoutManager(getActivity(),
                    RecyclerView.VERTICAL, false));
            bottomSheetFilterFragment.rvBrandList.setAdapter(brandAdapter);
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_category:
            case R.id.ll_category_filter:
                setCategoryHideOrShow();
                break;
            case R.id.iv_sort_by:
            case R.id.ll_sort_by_filter:
                setFilterHideOrShow();
                break;
            case R.id.iv_brand:
            case R.id.ll_brand_filter:
                setBrandHideOrShow();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void collapseView(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                super.applyTransformation(interpolatedTime, t);

                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density + 300));
        v.startAnimation(a);
    }

    private void expandView(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                super.applyTransformation(interpolatedTime, t);

                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density + 300));
        v.startAnimation(a);
    }

    public interface BottomSheetListener {
        void onBottomSheetClick();
    }

//    @Override
//    public int getTheme() {
//        return R.style.CustomBottomSheetDialog;
//    }
}
