package com.poona.agrocart.ui.products_list;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentProductListBinding;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class ProductListFragment extends Fragment
{
    private FragmentProductListBinding fragmentProductListBinding;
    private RecyclerView rvVegetables;
    private ArrayList<Product> vegetableArrayList;
    private ArrayList<Product> fruitsArrayList;
    private ProductListAdapter productListAdapter;
    private String isVegetablesOrFruits="fruits";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentProductListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_product_list, container, false);
        fragmentProductListBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentProductListBinding).getRoot();

        initView();
        setRVAdapter();

        return view;
    }

    private void setRVAdapter()
    {
        vegetableArrayList =new ArrayList<>();
        fruitsArrayList=new ArrayList<>();

        if(isVegetablesOrFruits.equals("vegetable"))
            prepareListingDataForVegetables();
        else
            prepareListingDataForFruits();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(),2);
        rvVegetables.setHasFixedSize(true);
        rvVegetables.setLayoutManager(gridLayoutManager);

        productListAdapter = new ProductListAdapter(vegetableArrayList);

        rvVegetables.setAdapter(productListAdapter);
    }

    private void prepareListingDataForFruits()
    {
        for(int i = 0; i < 6; i++) {
            Product vegetable = new Product("123","Apple","1kg",
                    "10% Off","Rs.40","Rs.36","https://www.linkpicture.com/q/Potato-Free-Download-PNG-1.png");
            vegetableArrayList.add(vegetable);
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
    }
}