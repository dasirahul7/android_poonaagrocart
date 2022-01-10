package com.poona.agrocart.ui.nav_favourites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.databinding.FragmentFavouriteItemsBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.adapter.ProductListAdapter;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class FavouriteItemsFragment extends BaseFragment
{
    private FragmentFavouriteItemsBinding fragmentFavouriteItemsBinding;
    private FavouriteViewModel favouriteViewModel;
    private RecyclerView rvFavouriteItems;
    private LinearLayoutManager linearLayoutManager;
    private FavouriteItemAdapter favouriteItemAdapter;
    private ArrayList<Product> favouriteItemsList = new ArrayList<>();

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentFavouriteItemsBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_favourite_items, container, false);
//        fragmentFavouriteItemsBinding.setLifecycleOwner(this);
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
         view = fragmentFavouriteItemsBinding.getRoot();
         initTitleBar(getString(R.string.menu_favourite));
        initView();

        return view;
    }



    private void initView()
    {
        makeFavouriteList();
        rvFavouriteItems=fragmentFavouriteItemsBinding.rvFavouriteItems;
        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvFavouriteItems.setHasFixedSize(true);
        rvFavouriteItems.setLayoutManager(linearLayoutManager);
        favouriteViewModel.getFavouriteItemMutableLiveData().observe(getViewLifecycleOwner(),products -> {
            favouriteItemsList = products;
            for (Product product :products)
                System.out.println("Product "+product.getName());
            favouriteItemAdapter = new FavouriteItemAdapter(favouriteItemsList,requireActivity(),view);
            rvFavouriteItems.setAdapter(favouriteItemAdapter);
            favouriteItemAdapter.setOnProductClick(product -> {
                toDetails(product,view);
            });
        });

    }

    private void makeFavouriteList() {
        ArrayList favouriteList = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            Product favouriteItem = new Product("FAV"+i,
                    "Bell Pepper Red","1kg","10","50",
                    getString(R.string.img_bell_pepper_red)
                    ,"Pune","Kashmir");
            favouriteList.add(favouriteItem);
        }
        favouriteViewModel.favouriteItemMutableLiveData.setValue(favouriteList);
    }

    private void checkFavouriteEmpty() {
        if (favouriteItemsList.size()>0)
            return;
        else {
            fragmentFavouriteItemsBinding.emptyLayout.setVisibility(View.VISIBLE);
            fragmentFavouriteItemsBinding.continueBtn.setVisibility(View.VISIBLE);
        }
    }
}