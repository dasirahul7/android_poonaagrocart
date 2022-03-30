package com.poona.agrocart.ui.bottom_sheet;

import static com.poona.agrocart.app.AppConstants.BRAND_ID;
import static com.poona.agrocart.app.AppConstants.CATEGORY_ID_VALUE;
import static com.poona.agrocart.app.AppConstants.SORT_BY;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppUtils.hideKeyBoard;
import static com.poona.agrocart.app.AppUtils.infoToast;
import static com.poona.agrocart.app.AppUtils.isConnectingToInternet;
import static com.poona.agrocart.app.AppUtils.showCircleProgressDialog;
import static com.poona.agrocart.app.AppUtils.showNotifyAlert;
import static com.poona.agrocart.app.AppUtils.successToast;
import static com.poona.agrocart.app.AppUtils.warningToast;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.filterResponse.BrandFliterList;
import com.poona.agrocart.data.network.responses.filterResponse.CategoryFilterList;
import com.poona.agrocart.data.network.responses.filterResponse.FilterListResponse;
import com.poona.agrocart.data.network.responses.filterResponse.SortByFilterList;
import com.poona.agrocart.databinding.BottomSheetFilterDialogBinding;
import com.poona.agrocart.ui.nav_explore.adapter.FilterItemAdapter;
import com.poona.agrocart.ui.nav_explore.model.FilterItem;
import com.poona.agrocart.ui.products_list.ProductListFragment;
import com.poona.agrocart.widgets.ExpandIconView;

import java.util.ArrayList;
import java.util.Arrays;

public class BottomSheetFilterFragment extends BottomSheetDialogFragment implements View.OnClickListener, NetworkExceptionListener, ProductListFragment.OnFilterClickListener {
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
    private Bundle bundle ;
    public String sortById = "", brandId = "", strCategoryId = "";
    public ArrayList<String> sortByIds = new ArrayList<>();
    public ArrayList<String> brandIds = new ArrayList<>();
    public ArrayList<String> categoryIds = new ArrayList<>();
    private String getStrCategoryId = "", getStrSortId = "", getStrBrandId = "";
    public ArrayList<String> preCategoryIds = new ArrayList<>();
    public OnClickButtonListener onClickButtonListener;
    ProductListFragment productListFragment = new ProductListFragment();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public BottomSheetFilterFragment(boolean showCategory, Bundle bundle) {
        this.showCategory = showCategory;
        this.bundle = bundle;
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        bottomSheetFilterFragment = BottomSheetFilterDialogBinding.inflate(LayoutInflater.from(getActivity()));
        basketFilterViewModel = new ViewModelProvider(this).get(BasketFilterViewModel.class);
        view = bottomSheetFilterFragment.getRoot();

        assert getArguments() != null;
        sortById= getArguments().getString(SORT_BY);

        showFilterValue();
        if(isConnectingToInternet(requireContext())){
            callFilterListApi(showCircleProgressDialog(getContext(),""));
        }else {
            showNotifyAlert(requireActivity(), getString(R.string.info), getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        setClicks();

        //Hide all list by default
        setFilterHideOrShow();
        setBrandHideOrShow();

        if (showCategory) {
            bottomSheetFilterFragment.llMainCategoryFilter.setVisibility(View.VISIBLE);
        } else {
            bottomSheetFilterFragment.llMainCategoryFilter.setVisibility(View.GONE);
        }

       /* bundle = getArguments();
        if(bundle != null)
            getStrSortId = bundle.getString(SORT_BY);*/
           // brandId = bundle.getString(BRAND_ID);
           // sortById= bundle.getString(SORT_BY);

       // getOnClickButtonListener();

        return view;
    }

    private void showFilterValue() {

        /*try {
            bundle = new Bundle();
            bundle = this.getArguments();
            getStrCategoryId = bundle.getString(CATEGORY_ID_VALUE);
            Log.d("TAG", "showFilterValue: "+getStrCategoryId);
            //bottomSheetFragment.show( getChildFragmentManager(), bottomSheetFragment.getTag()); // Assuming you are using Activity
        }catch (NullPointerException e) {
            e.printStackTrace();
        }*/

       /* bundle = this.getArguments();
        if(bundle != null){
            strCategoryId = bundle.getString(CATEGORY_ID_VALUE);
            brandId = bundle.getString(BRAND_ID);
            sortById= bundle.getString(SORT_BY);
        }
*/


    }

    private void setClicks() {
        bottomSheetFilterFragment.ivCategory.setOnClickListener(this);
        bottomSheetFilterFragment.llCategoryFilter.setOnClickListener(this);
        bottomSheetFilterFragment.ivSortBy.setOnClickListener(this);
        bottomSheetFilterFragment.llSortByFilter.setOnClickListener(this);
        bottomSheetFilterFragment.ivBrand.setOnClickListener(this);
        bottomSheetFilterFragment.llBrandFilter.setOnClickListener(this);

        bottomSheetFilterFragment.closeBtn.setOnClickListener( view1 -> {
            dismiss();
        });

        bottomSheetFilterFragment.applyBtn.setOnClickListener(view1 -> {
            onClickButtonListener.itemClick(categoryIds, brandIds, sortByIds);
            dismiss();
           /* Bundle bundle = new Bundle();
            bundle.putStringArrayList(CATEGORY_ID_VALUE, categoryIds);
            bundle.putStringArrayList(BRAND_ID, brandIds);
            bundle.putStringArrayList(SORT_BY, sortByIds);
            Log.d("TAG", "setClicks:sortby "+sortById);
            Log.d("TAG", "setClicks:category "+strCategoryId);
            Log.d("TAG", "setClicks:brand "+brandId);
            NavHostFragment.findNavController(BottomSheetFilterFragment.this).navigate(R.id.action_bottom_sheet_fragment_to_nav_products_list
                    , bundle);*/

        });
    }

    @Override
    public void onNetworkException(int from, String type) {

            if (isConnectingToInternet(getContext())) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        callFilterListApi(showCircleProgressDialog(getContext(),""));
                        break;

                }
            }

    }

    @Override
    public void filterItemClick(String sort) {
        this.getStrSortId = sort;

        System.out.println("sortby"+getStrSortId);
    }


    public interface OnClickButtonListener{
        void itemClick(ArrayList<String> categoryId, ArrayList<String>  brandId, ArrayList<String>  sortId);
    }

    public OnClickButtonListener getOnClickButtonListener() {
        return onClickButtonListener;
    }

    public void setOnClickButtonListener(OnClickButtonListener onClickButtonListener) {
        this.onClickButtonListener = onClickButtonListener;
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
                if(item.getId().isEmpty()){
                    FilterItem filterItem = new FilterItem(item.getId(),item.getCategoryName(),false);
                    preCategoryIds = new ArrayList<String>(Arrays.asList(getStrCategoryId.split(",")));

                    if (preCategoryIds.contains(filterItem.getId()))

                        filterItem.isSelected();
                }else {
                    FilterItem filterItem = new FilterItem(item.getId(), item.getCategoryName(), false);
                    categoryItems.add(filterItem);
                }

                try {
                    System.out.println("CategoryFilterList "+item.getId());
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
