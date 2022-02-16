package com.poona.agrocart.ui.nav_favourites;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.favoutiteResponse.FavouriteLisResponse;
import com.poona.agrocart.databinding.FragmentFavouriteItemsBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.model.ProductOld;

import java.util.ArrayList;

public class FavouriteItemsFragment extends BaseFragment
{
    private FragmentFavouriteItemsBinding fragmentFavouriteItemsBinding;
    private FavouriteViewModel favouriteViewModel;
    private RecyclerView rvFavouriteItems;
    private LinearLayoutManager linearLayoutManager;
    private FavouriteItemAdapter favouriteItemAdapter;
    private ArrayList<ProductOld> favouriteItemsList = new ArrayList<>();

    View view;
    private String TAG= FavouriteItemsFragment.class.getSimpleName();

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
        callFavouriteAPi(showCircleProgressDialog(context,""));
//        favouriteViewModel.getFavouriteItemMutableLiveData().observe(getViewLifecycleOwner(),products -> {
//            favouriteItemsList = products;
//            for (ProductOld productOld :products)
//                System.out.println("ProductOld "+ productOld.getName());
//            favouriteItemAdapter = new FavouriteItemAdapter(favouriteItemsList,requireActivity(),view);
//            rvFavouriteItems.setAdapter(favouriteItemAdapter);
//            favouriteItemAdapter.setOnProductClick(product -> {
//                toDetails(product,view);
//            });
//        });

    }

    private void callFavouriteAPi(ProgressDialog showCircleProgressDialog) {
        Observer<FavouriteLisResponse> favouriteLisResponseObserver = favouriteLisResponse -> {
            if (favouriteLisResponse!=null){
                showCircleProgressDialog.dismiss();
                Log.e(TAG, "callFavouriteAPi: "+favouriteLisResponse.getMessage() );
            }
        };
        favouriteViewModel.favouriteLisResponseLiveData(showCircleProgressDialog,FavouriteItemsFragment.this)
                .observe(getViewLifecycleOwner(), favouriteLisResponseObserver);
    }

    private void makeFavouriteList() {
        ArrayList favouriteList = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            ProductOld favouriteItem = new ProductOld("FAV"+i,
                    "Bell Pepper Red","1kg","10","50",
                    getString(R.string.img_bell_pepper_red)
                    ,"Pune","Kashmir");
            favouriteList.add(favouriteItem);
        }
//        favouriteViewModel.favouriteItemMutableLiveData.setValue(favouriteList);
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