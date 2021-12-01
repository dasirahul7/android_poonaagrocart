package com.poona.agrocart.ui.my_cart;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentFavouriteItemsBinding;
import com.poona.agrocart.databinding.FragmentMyCartBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.favourites.FavouriteItem;
import com.poona.agrocart.ui.favourites.FavouriteItemAdapter;

import java.util.ArrayList;

public class MyCartFragment extends BaseFragment implements View.OnClickListener
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

        initTitleBar(getString(R.string.my_basket));
        initView();
        setRvAdapter();

        return view;
    }

    private void initView()
    {
        fragmentMyCartBinding.btnPlaceOrder.setOnClickListener(this);

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

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_place_order:
                redirectToOrderSummary(v);
                break;
        }
    }

    private void redirectToOrderSummary(View v)
    {
        Navigation.findNavController(v).navigate(R.id.action_nav_cart_to_nav_order_summary);
    }
}