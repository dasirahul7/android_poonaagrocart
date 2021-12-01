package com.poona.agrocart.ui.basket_page;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.DialogBasketFilterListBinding;
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
        final View view = ((ViewDataBinding) fragmentBasketPageBinding).getRoot();
        if ((getArguments() != null ? getArguments().getString("Title") : null) != null)
            BasketType = getArguments().getString("Title");
        initTitleBar(BasketType);

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

    private void makeSelected(int position) {
        for (int i = 0; i < categoryItems.size(); i++) {
            if (position == i) {
                categoryItems.get(i).setSelected(true);
            } else {
                categoryItems.get(i).setSelected(false);
            }
            categoryAdapter.notifyDataSetChanged();
        }
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
            fragmentBasketPageBinding.rlContainer.setVisibility(View.GONE);
            fragmentBasketPageBinding.bottomView.setVisibility(View.GONE);
            bottomSheetBehavior.setState(STATE_COLLAPSED);
            fragmentBasketPageBinding.etSearch.setEnabled(false);
        });
    }

    private void prepareListingData() {
        for (int i = 0; i < 6; i++) {
            BasketCard basketCard = new BasketCard();
            basketCard.setProductName(getString(R.string.sprouts_basket));
            basketCard.setDiscountedPrice(getString(R.string.rs_800));
            basketCard.setImgUrl(String.valueOf(R.drawable.img_diet_basket));
            basketCardsList.add(basketCard);
        }
    }
}