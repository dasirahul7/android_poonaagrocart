package com.poona.agrocart.ui.bottom_sheet;

import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppUtils.infoToast;
import static com.poona.agrocart.app.AppUtils.showCircleProgressDialog;
import static com.poona.agrocart.app.AppUtils.successToast;
import static com.poona.agrocart.app.AppUtils.warningToast;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.filterResponse.BrandFliterList;
import com.poona.agrocart.data.network.responses.filterResponse.CategoryFilterList;
import com.poona.agrocart.data.network.responses.filterResponse.FilterListResponse;
import com.poona.agrocart.data.network.responses.filterResponse.SortByFilterList;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.ItemsDetail;
import com.poona.agrocart.databinding.BottomSheetFilterDialogBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_explore.adapter.FilterItemAdapter;
import com.poona.agrocart.ui.nav_explore.model.FilterItem;
import com.poona.agrocart.widgets.ExpandIconView;

import java.util.ArrayList;

public class BottomSheetFilterFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private BottomSheetFilterDialogBinding bottomSheetFilterFragment;
    private FilterItemAdapter categoryAdapter, sortByAdapter, brandAdapter;
    private BasketFilterViewModel basketFilterViewModel;
    private LinearLayout linearLayout;
    private ArrayList<FilterItem> categoryItems = new ArrayList<>();
    private ArrayList<FilterItem> filterItems = new ArrayList<>();
    private ArrayList<FilterItem> brandItems = new ArrayList<>();
    private BottomSheetListener mListener;
    private boolean isCategoryVisible = true;
    private boolean isFilterVisible = true;
    private boolean isBrandVisible = true;
    private View view;
    private boolean showCategory;
    private String sortById = "", brandId = "", strCategoryId = "";
    public ArrayList<String> sortByIds = new ArrayList<>();
    public ArrayList<String> brandIds = new ArrayList<>();
    public ArrayList<String> categoryIds = new ArrayList<>();
    public BottomSheetFilterFragment(boolean showCategory) {
        this.showCategory = showCategory;
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        bottomSheetFilterFragment = BottomSheetFilterDialogBinding.inflate(LayoutInflater.from(getActivity()));
        basketFilterViewModel = new ViewModelProvider(this).get(BasketFilterViewModel.class);
         view = bottomSheetFilterFragment.getRoot();

        callFilterListApi(showCircleProgressDialog(getContext(),""));
        setClicks();

        //Hide all list by default
        setFilterHideOrShow();
        setBrandHideOrShow();

        if (showCategory) {
            bottomSheetFilterFragment.llMainCategoryFilter.setVisibility(View.VISIBLE);
        } else {
            bottomSheetFilterFragment.llMainCategoryFilter.setVisibility(View.GONE);
        }

        return view;
    }

    private void setClicks() {
        bottomSheetFilterFragment.ivCategory.setOnClickListener(this);
        bottomSheetFilterFragment.llCategoryFilter.setOnClickListener(this);
        bottomSheetFilterFragment.ivSortBy.setOnClickListener(this);
        bottomSheetFilterFragment.llSortByFilter.setOnClickListener(this);
        bottomSheetFilterFragment.ivBrand.setOnClickListener(this);
        bottomSheetFilterFragment.llBrandFilter.setOnClickListener(this);

        bottomSheetFilterFragment.applyBtn.setOnClickListener(view1 -> {
            Log.d("TAG", "setClicks:sortby "+sortById);
            Log.d("TAG", "setClicks:category "+brandId);
            Log.d("TAG", "setClicks:brand "+strCategoryId);
        });
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
            for (CategoryFilterList item: categoryFilterItems){
                FilterItem filterItem = new FilterItem(item.getId(),item.getCategoryName(),false);
                categoryItems.add(filterItem);
                try {
                    System.out.println("CategoryFilterList "+item.getCategoryName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            categoryAdapter = new FilterItemAdapter(categoryItems, getActivity());
            bottomSheetFilterFragment.rvCategoryList.setLayoutManager(new LinearLayoutManager(getActivity(),
                    RecyclerView.VERTICAL, false));
            bottomSheetFilterFragment.rvCategoryList.setHasFixedSize(true);
            bottomSheetFilterFragment.rvCategoryList.setAdapter(categoryAdapter);
            categoryAdapter.setOnFilterClickListener(new FilterItemAdapter.OnFilterClickListener() {
                @Override
                public void onItemCheck(FilterItem item) {
                    categoryIds.add(item.getId());

                    strCategoryId = String.valueOf(categoryIds);
                }

                @Override
                public void onItemUncheck(FilterItem item) {
                    categoryIds.remove(item.getId());

                    strCategoryId = String.valueOf(categoryIds);

                }
            });
        });
        //set SotBy and Filter
        basketFilterViewModel.getFilterItemLiveData().observe(getViewLifecycleOwner(), sortByFilterItems -> {
            for (SortByFilterList item: sortByFilterItems){
                FilterItem filterItem = new FilterItem(item.getPriceFilterId(),item.getPriceFilter(),false);
                filterItems.add(filterItem);
                try {
                    System.out.println("SortByFilterList "+item.getPriceFilter());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
          sortByAdapter = new FilterItemAdapter(filterItems, getActivity());
          bottomSheetFilterFragment.rvSortList.setLayoutManager(new LinearLayoutManager(getActivity(),
                   RecyclerView.VERTICAL, false));
          bottomSheetFilterFragment.rvSortList.setHasFixedSize(true);
            bottomSheetFilterFragment.rvSortList.setAdapter(sortByAdapter);
            sortByAdapter.setOnFilterClickListener(new FilterItemAdapter.OnFilterClickListener() {
                @Override
                public void onItemCheck(FilterItem item) {
                    sortByIds.add(item.getId());
                    sortById = String.valueOf(sortByIds);
                }

                @Override
                public void onItemUncheck(FilterItem item) {
                    sortByIds.remove(item.getId());
                    sortById = String.valueOf(sortByIds);
                }
            });
        });
       //set Brand Filter
        basketFilterViewModel.getBrandItemLiveData().observe(getViewLifecycleOwner(), brandFilterItems -> {
            for (BrandFliterList item: brandFilterItems){
                FilterItem filterItem = new FilterItem(item.getBrandId(),item.getBrandName(),false);
                brandItems.add(filterItem);
                try {
                    System.out.println("BrandFliterList "+item.getBrandName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            brandAdapter = new FilterItemAdapter(brandItems, getActivity());
            bottomSheetFilterFragment.rvBrandList.setHasFixedSize(true);
            bottomSheetFilterFragment.rvBrandList.setLayoutManager(new LinearLayoutManager(getActivity(),
                    RecyclerView.VERTICAL, false));
            bottomSheetFilterFragment.rvBrandList.setAdapter(brandAdapter);
            brandAdapter.setOnFilterClickListener(new FilterItemAdapter.OnFilterClickListener() {
                @Override
                public void onItemCheck(FilterItem item) {
                    brandIds.add(item.getId());
                    brandId = String.valueOf(brandIds);
                }

                @Override
                public void onItemUncheck(FilterItem item) {
                    brandIds.remove(item.getId());
                    brandId = String.valueOf(brandIds);
                }
            });

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

    private void callFilterListApi(ProgressDialog progressDialog){
        Observer<FilterListResponse> filterListResponseObserver = filterListResponse -> {
            if (filterListResponse != null) {
                Log.e("Filter List Api ResponseData", new Gson().toJson(filterListResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (filterListResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(getContext(), filterListResponse.getMessage());
                        try {
                            Log.d("TAG", "callFilterListApi: "+filterListResponse.getData().getCategoryList().size());
                            Log.d("TAG", "callFilterListApi: "+filterListResponse.getData().getPriceFilterList().size());
                            Log.d("TAG", "callFilterListApi: "+filterListResponse.getData().getBrandList().size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        basketFilterViewModel.iniFiltersList(filterListResponse);
                        setRvAdapter();

                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(getContext(), filterListResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        //goToAskSignInSignUpScreen(getContext(), filterListResponse.getMessage());
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(getContext(), filterListResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

        };
        basketFilterViewModel.getFilterListPesponse(progressDialog,BottomSheetFilterFragment.this)
                .observe(getViewLifecycleOwner(),filterListResponseObserver );
    }
}
