package com.poona.agrocart.ui.my_cart;

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
import com.poona.agrocart.databinding.FragmentMyCartBinding;
import com.poona.agrocart.ui.favourites.FavouriteItem;
import com.poona.agrocart.ui.favourites.FavouriteItemAdapter;

import java.util.ArrayList;

public class MyCartFragment extends Fragment
{
    private FragmentMyCartBinding fragmentMyCartBinding;
    private RecyclerView rvCart;
    private LinearLayoutManager linearLayoutManager;
    private CartItemsAdapter cartItemsAdapter;
    private ArrayList<CartItem> cartItemArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentMyCartBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_my_cart, container, false);
        fragmentMyCartBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentMyCartBinding).getRoot();

        initView();
        setRvAdapter();

        return view;
    }

    private void initView() {
        rvCart=fragmentMyCartBinding.rvCart;
    }

    private void setRvAdapter()
    {
        cartItemArrayList=new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvCart.setHasFixedSize(true);
        rvCart.setLayoutManager(linearLayoutManager);

        cartItemsAdapter = new CartItemsAdapter(cartItemArrayList);
        rvCart.setAdapter(cartItemsAdapter);
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 4; i++)
        {
            CartItem cartItem = new CartItem();
            cartItem.setName("Bell Pepper Red");
            cartItem.setWeight("1kg");
            cartItem.setPrice("Rs.30");
            cartItem.setFinalPrice("RS.25");
            cartItemArrayList.add(cartItem);
        }
    }
}