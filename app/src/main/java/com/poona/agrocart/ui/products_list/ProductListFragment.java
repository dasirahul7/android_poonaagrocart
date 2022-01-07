package com.poona.agrocart.ui.products_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentProductListBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.bottom_sheet.BottomSheetFilterFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class ProductListFragment extends BaseFragment
{
    private FragmentProductListBinding fragmentProductListBinding;
    private ProductListViewModel productListViewModel;
    private RecyclerView rvVegetables;
    private ArrayList<Product> vegetableArrayList;
    private ArrayList<Product> fruitsArrayList;
    private ProductGridAdapter productGridAdapter;
    private String isVegetablesOrFruits="vegetable";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentProductListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_product_list, container, false);
        fragmentProductListBinding.setLifecycleOwner(this);
        final View view = fragmentProductListBinding.getRoot();
        initView();
        setTitleBar();
        ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setVisibility(View.VISIBLE);
        ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setOnClickListener(v -> {
            // Show the Filter Dialog
            BottomSheetFilterFragment filterFragment = new BottomSheetFilterFragment();
            filterFragment.show(getChildFragmentManager(),"FilterFragment");
        });
        setRVAdapter(view);

        return view;
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
            //Listing Vegetables
            productListViewModel.getVegesMutableLiveData().observe(getViewLifecycleOwner(),products -> {
                vegetableArrayList = products;
                productGridAdapter = new ProductGridAdapter(vegetableArrayList,view);
                rvVegetables.setAdapter(productGridAdapter);
            });

        }
        else {
            //Listing Fruits
            productListViewModel.getFruitMutableLiveData().observe(getViewLifecycleOwner(),products -> {
                fruitsArrayList = products;
                productGridAdapter = new ProductGridAdapter(fruitsArrayList,view);
                rvVegetables.setAdapter(productGridAdapter);
            });

        }

    }

    private void initView()
    {
        productListViewModel = new ViewModelProvider(this).get(ProductListViewModel.class);

        Bundle bundle=this.getArguments();
        if(bundle!=null)
            isVegetablesOrFruits=bundle.getString("ProductCategory");

        this.rvVegetables= fragmentProductListBinding.rvProducts;
    }
}