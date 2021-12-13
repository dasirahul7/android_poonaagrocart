package com.poona.agrocart.ui.basket_page;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentBasketPageBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.explore.adapter.FilterItemAdapter;
import com.poona.agrocart.ui.explore.model.FilterItem;
import com.poona.agrocart.ui.home.HomeActivity;

import java.util.ArrayList;

public class BasketPageFragment extends BaseFragment {
    private FragmentBasketPageBinding fragmentBasketPageBinding;
    private RecyclerView rvBasketCards;
    private ArrayList<BasketCard> basketCardsList;
    private BasketCardAdapter basketCardAdapter;
    private String BasketType;
    private BottomSheetBehavior bottomSheetBehavior;
    FilterItemAdapter categoryAdapter, sortByAdapter, brandAdapter;
    private ArrayList<FilterItem> categoryItems;
    private ArrayList<FilterItem> filterItems;
    private ArrayList<FilterItem> brandItems;


    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentBasketPageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket_page, container, false);
        fragmentBasketPageBinding.setLifecycleOwner(this);
        final View view = fragmentBasketPageBinding.getRoot();
        if ((getArguments() != null ? getArguments().getString("Title") : null) != null)
            BasketType = getArguments().getString("Title");
        initTitleWithBackBtn(getString(R.string.basket));

        ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setVisibility(View.VISIBLE);
        ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setOnClickListener(v -> {
            // Show the Filter Dialog
//            Transition transition = new Fade();
//            transition.setDuration(400);
//            transition.addTarget(R.id.image);
            try {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentBasketPageBinding.bottomView.setVisibility(View.VISIBLE);
            fragmentBasketPageBinding.rlContainer.setVisibility(View.VISIBLE);
            fragmentBasketPageBinding.rlContainer.setClickable(false);
            fragmentBasketPageBinding.etSearch.setEnabled(false);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            fragmentBasketPageBinding.bottomView.setAlpha(5.0f);
//
//        // Start the animation
//            fragmentBasketPageBinding.bottomView.animate()
//                    .translationY(view.getHeight())
//                    .alpha(1.0f)
//                    .setListener(null);
        });
        setFilterAdapter();
        initView();
        setRVAdapter();
        return view;
    }

    private void setFilterAdapter() {
        categoryItems = new ArrayList<>();
        filterItems = new ArrayList<>();
        brandItems = new ArrayList<>();

        //Sample categories
        FilterItem category1 = new FilterItem("1", "Vegetable", false);
        FilterItem category2 = new FilterItem("2", "Leafy Vegetables", false);
        FilterItem category3 = new FilterItem("3", "Herbs", false);
        FilterItem category4 = new FilterItem("4", "Fruits", false);
        FilterItem category5 = new FilterItem("5", "Dry Fruits", false);
        FilterItem category6 = new FilterItem("6", "Sprouts", false);

        categoryItems.add(category1);
        categoryItems.add(category2);
        categoryItems.add(category3);
        categoryItems.add(category4);
        categoryItems.add(category5);

        //Sample Sort By Filter
        FilterItem filterItem1 = new FilterItem("1", "Low to High", false);
        FilterItem filterItem2 = new FilterItem("2", "High to Low", false);
        FilterItem filterItem3 = new FilterItem("3", "Newest Arrived", false);
        filterItems.add(filterItem1);
        filterItems.add(filterItem2);
        filterItems.add(filterItem3);
        filterItems.add(filterItem3);
        filterItems.add(filterItem3);

        // sample Brand Items

        FilterItem brand1 = new FilterItem("1", "B&G Green", false);
        FilterItem brand2 = new FilterItem("2", "Del Monte", false);
        FilterItem brand3 = new FilterItem("3", "Fruttella", false);
        brandItems.add(brand1);
        brandItems.add(brand2);
        brandItems.add(brand3);
        brandItems.add(brand3);


        categoryAdapter = new FilterItemAdapter(categoryItems, getActivity());
        sortByAdapter = new FilterItemAdapter(filterItems, getActivity());
        brandAdapter = new FilterItemAdapter(brandItems, getActivity());

        fragmentBasketPageBinding.filterView.rvCategoryList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        fragmentBasketPageBinding.filterView.rvCategoryList.setHasFixedSize(true);
        fragmentBasketPageBinding.filterView.rvSortList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        fragmentBasketPageBinding.filterView.rvSortList.setHasFixedSize(true);
        fragmentBasketPageBinding.filterView.rvBrandList.setHasFixedSize(true);
        fragmentBasketPageBinding.filterView.rvBrandList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        fragmentBasketPageBinding.filterView.rvCategoryList.setAdapter(categoryAdapter);
        fragmentBasketPageBinding.filterView.rvSortList.setAdapter(sortByAdapter);
        fragmentBasketPageBinding.filterView.rvBrandList.setAdapter(brandAdapter);

    }


    private void setRVAdapter() {
        basketCardsList = new ArrayList<>();
        prepareListingData();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        rvBasketCards.setHasFixedSize(true);
        rvBasketCards.setLayoutManager(gridLayoutManager);

        basketCardAdapter = new BasketCardAdapter(basketCardsList);

        rvBasketCards.setAdapter(basketCardAdapter);
    }

    private void initView() {
        this.rvBasketCards = fragmentBasketPageBinding.rvBasketCards;

        bottomSheetBehavior = BottomSheetBehavior.from(fragmentBasketPageBinding.bottomView);
        fragmentBasketPageBinding.filterView.closeBtn.setOnClickListener(v -> {
            closeFilter();
        });
        fragmentBasketPageBinding.filterView.applyBtn.setOnClickListener(v -> {
            closeFilter();
        });
    }

    private void closeFilter() {
        fragmentBasketPageBinding.rlContainer.setVisibility(View.GONE);
        fragmentBasketPageBinding.bottomView.setVisibility(View.GONE);
        bottomSheetBehavior.setState(STATE_COLLAPSED);
        fragmentBasketPageBinding.etSearch.setEnabled(false);
    }

    private void prepareListingData() {
        for (int i = 0; i < 6; i++) {
            BasketCard basketCard = new BasketCard();
            basketCard.setImgUrl("data:image/png;base64,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");
            basketCard.setProductName(getString(R.string.sprouts_basket));
            basketCard.setDiscountedPrice(getString(R.string.rs_800));
            basketCardsList.add(basketCard);
        }
    }
}