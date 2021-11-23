package com.poona.agrocart.ui.favourites;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentFavouriteItemsBinding;
import com.poona.agrocart.databinding.FragmentOrderViewBinding;
import com.poona.agrocart.ui.order_view.BasketItem;
import com.poona.agrocart.ui.order_view.BasketItemsAdapter;

import java.util.ArrayList;

public class FavouriteItemsFragment extends Fragment {

    private FragmentFavouriteItemsBinding fragmentFavouriteItemsBinding;
    private RecyclerView rvFavouriteItems;
    private LinearLayoutManager linearLayoutManager;
    private FavouriteItemAdapter favouriteItemAdapter;
    private ArrayList<FavouriteItem> favouriteItemsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFavouriteItemsBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_favourite_items, container, false);
        fragmentFavouriteItemsBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentFavouriteItemsBinding).getRoot();

        initView();

        setRvAdapter();

        return view;
    }

    private void setRvAdapter()
    {
        favouriteItemsList=new ArrayList<>();
        prepareListingData();
        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvFavouriteItems.setHasFixedSize(true);
        rvFavouriteItems.setLayoutManager(linearLayoutManager);

        //initializing our adapter
        favouriteItemAdapter = new FavouriteItemAdapter(favouriteItemsList);

        //Adding adapter to recyclerview
        rvFavouriteItems.setAdapter(favouriteItemAdapter);
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 3; i++) {
            FavouriteItem favouriteItem = new FavouriteItem();
            favouriteItem.setName("Bell Pepper Red");
            favouriteItem.setWeight("1kg");
            favouriteItem.setPrice("RS.30");
            favouriteItemsList.add(favouriteItem);
        }
    }

    private void initView() {
        rvFavouriteItems=fragmentFavouriteItemsBinding.rvFavouriteItems;
    }
}