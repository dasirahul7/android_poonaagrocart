package com.poona.agrocart.ui.bottom_sheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.DialogBasketFilterListBinding;
import com.poona.agrocart.ui.nav_explore.adapter.FilterItemAdapter;
import com.poona.agrocart.ui.nav_explore.model.FilterItem;
import com.poona.agrocart.widgets.ExpandIconView;

import java.util.ArrayList;

public class BasketFilterFragment extends BottomSheetDialogFragment {
    private DialogBasketFilterListBinding dialogBasketFilterListBinding;
    private FilterItemAdapter categoryAdapter, sortByAdapter, brandAdapter;
    private BasketFilterViewModel basketFilterViewModel;
    private ArrayList<FilterItem> categoryItems;
    private ArrayList<FilterItem> filterItems;
    private ArrayList<FilterItem> brandItems;
    private BottomSheetListener mListener;
    private boolean isCategoryVisible = false;
    private boolean isFilterVisible = true;
    private boolean isBrandVisible = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogBasketFilterListBinding = DialogBasketFilterListBinding.inflate(LayoutInflater.from(getActivity()));
        basketFilterViewModel = new ViewModelProvider(this).get(BasketFilterViewModel.class);
        setRvAdapter();
//        this.getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bottom_shhet_background);

        setStyle(DialogFragment.STYLE_NO_FRAME,R.style.BottomSheetDialogTheme);
        dialogBasketFilterListBinding.ivCategory.setOnClickListener(v -> {
            setCategoryHideOrShow();
        });
        return dialogBasketFilterListBinding.getRoot();
    }

    private void setCategoryHideOrShow() {
        if (isCategoryVisible) {
            dialogBasketFilterListBinding.ivCategory.setState(ExpandIconView.MORE, true);
            collapseView(dialogBasketFilterListBinding.rvCategoryList);
        }else {
            dialogBasketFilterListBinding.ivCategory.setState(ExpandIconView.LESS, true);
            expandView(dialogBasketFilterListBinding.rvCategoryList);
        }
        isCategoryVisible = !isCategoryVisible;
    }
    private void setFilterOrShow() {
        if (isFilterVisible) {
            dialogBasketFilterListBinding.ivSortBy.setState(ExpandIconView.MORE, true);
            collapseView(dialogBasketFilterListBinding.rvSortList);
        }else {
            isFilterVisible = !isFilterVisible;
        }
    }

    private void setRvAdapter() {
        //setCategory
        basketFilterViewModel.getCategoryLiveData().observe(getViewLifecycleOwner(),categoryFilterItems -> {
            categoryItems = categoryFilterItems;
            categoryAdapter = new FilterItemAdapter(categoryItems, getActivity());
            dialogBasketFilterListBinding.rvCategoryList.setLayoutManager(new LinearLayoutManager(getActivity(),
                    RecyclerView.VERTICAL, false));
            dialogBasketFilterListBinding.rvCategoryList.setHasFixedSize(true);
            dialogBasketFilterListBinding.rvCategoryList.setAdapter(categoryAdapter);
        });
       //set SotBy and Filter
        basketFilterViewModel.getFilterItemLiveData().observe(getViewLifecycleOwner(),sortByFilterItems -> {
            filterItems = sortByFilterItems;
            sortByAdapter = new FilterItemAdapter(filterItems, getActivity());
            dialogBasketFilterListBinding.rvSortList.setLayoutManager(new LinearLayoutManager(getActivity(),
                    RecyclerView.VERTICAL, false));
            dialogBasketFilterListBinding.rvSortList.setHasFixedSize(true);
            dialogBasketFilterListBinding.rvSortList.setAdapter(sortByAdapter);
        });
        // set Brand Filter
        basketFilterViewModel.getBrandItemLiveData().observe(getViewLifecycleOwner(),brandFilterItems -> {
            brandItems = brandFilterItems;
            brandAdapter = new FilterItemAdapter(brandItems, getActivity());
            dialogBasketFilterListBinding.rvBrandList.setHasFixedSize(true);
            dialogBasketFilterListBinding.rvBrandList.setLayoutManager(new LinearLayoutManager(getActivity(),
                    RecyclerView.VERTICAL, false));
            dialogBasketFilterListBinding.rvBrandList.setAdapter(brandAdapter);
        });

    }

    public interface BottomSheetListener {
        void onBottomSheetClick();
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


}
