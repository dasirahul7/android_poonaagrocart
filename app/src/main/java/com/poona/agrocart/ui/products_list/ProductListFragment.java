package com.poona.agrocart.ui.products_list;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentProductListBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.explore.adapter.FilterItemAdapter;
import com.poona.agrocart.ui.explore.model.FilterItem;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.model.Product;
import java.util.ArrayList;

public class ProductListFragment extends BaseFragment
{
    private FragmentProductListBinding fragmentProductListBinding;
    private RecyclerView rvVegetables;
    private ArrayList<Product> vegetableArrayList;
    private ArrayList<Product> fruitsArrayList;
    private ProductListAdapter productListAdapter;
    private String isVegetablesOrFruits="vegetable";

    private BottomSheetBehavior bottomSheetBehavior;
    FilterItemAdapter categoryAdapter, sortByAdapter, brandAdapter;
    private ArrayList<FilterItem> categoryItems;
    private ArrayList<FilterItem> filterItems;
    private ArrayList<FilterItem> brandItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentProductListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_product_list, container, false);
        fragmentProductListBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentProductListBinding).getRoot();

        Bundle bundle=this.getArguments();

        if(bundle!=null)
        isVegetablesOrFruits=bundle.getString("ProductCategory");

        setTitleBar();

        ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setVisibility(View.VISIBLE);
        ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setOnClickListener(v -> {
            try {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentProductListBinding.bottomView.setVisibility(View.VISIBLE);
            fragmentProductListBinding.rlContainer.setVisibility(View.VISIBLE);
            fragmentProductListBinding.rlContainer.setClickable(false);
            fragmentProductListBinding.etSearch.setEnabled(false);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        setFilterAdapter();

        initView();
        setRVAdapter(view);

        return view;
    }

    private void setFilterAdapter()
    {
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

        fragmentProductListBinding.filterView.rvCategoryList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        fragmentProductListBinding.filterView.rvCategoryList.setHasFixedSize(true);
        fragmentProductListBinding.filterView.rvSortList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        fragmentProductListBinding.filterView.rvSortList.setHasFixedSize(true);
        fragmentProductListBinding.filterView.rvBrandList.setHasFixedSize(true);
        fragmentProductListBinding.filterView.rvBrandList.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        fragmentProductListBinding.filterView.rvCategoryList.setAdapter(categoryAdapter);
        fragmentProductListBinding.filterView.rvSortList.setAdapter(sortByAdapter);
        fragmentProductListBinding.filterView.rvBrandList.setAdapter(brandAdapter);
    }

    private void setTitleBar()
    {
        if(isVegetablesOrFruits.equals("vegetable"))
            initTitleWithBackBtn(getString(R.string.vegetables));
        else
            initTitleWithBackBtn(getString(R.string.fruits));
    }

    private void setRVAdapter(View view)
    {
        vegetableArrayList =new ArrayList<>();
        fruitsArrayList=new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(),2);
        rvVegetables.setHasFixedSize(true);
        rvVegetables.setLayoutManager(gridLayoutManager);

        if(isVegetablesOrFruits.equals("vegetable"))
        {
            prepareListingDataForVegetables();
            productListAdapter = new ProductListAdapter(vegetableArrayList,view);
        }
        else {
            prepareListingDataForFruits();
            productListAdapter = new ProductListAdapter(fruitsArrayList,view);
        }
        rvVegetables.setAdapter(productListAdapter);
    }

    private void prepareListingDataForFruits()
    {
        for(int i = 0; i < 6; i++) {
            Product fruit = new Product("123","Apple","1kg",
                    "10% Off","Rs.40","Rs.36","https://www.linkpicture.com/q/Potato-Free-Download-PNG-1.png");
            fruitsArrayList.add(fruit);
        }
    }

    private void prepareListingDataForVegetables()
    {
        for(int i = 0; i < 6; i++) {
            Product vegetable = new Product("123","Ginger","1kg",
                    "10% Off","Rs.40","Rs.36","https://www.linkpicture.com/q/Potato-Free-Download-PNG-1.png");
            vegetableArrayList.add(vegetable);
        }
    }

    private void initView()
    {
        this.rvVegetables= fragmentProductListBinding.rvProducts;

        bottomSheetBehavior = BottomSheetBehavior.from(fragmentProductListBinding.bottomView);
        fragmentProductListBinding.filterView.closeBtn.setOnClickListener(v -> {
            closeFilter();
        });
        fragmentProductListBinding.filterView.applyBtn.setOnClickListener(v -> {
            closeFilter();
        });
    }

    private void closeFilter() {
        fragmentProductListBinding.rlContainer.setVisibility(View.GONE);
        fragmentProductListBinding.bottomView.setVisibility(View.GONE);
        bottomSheetBehavior.setState(STATE_COLLAPSED);
        fragmentProductListBinding.etSearch.setEnabled(false);
    }
}