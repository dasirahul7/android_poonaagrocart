package com.poona.agrocart.ui.my_cart;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;
import java.util.Objects;

public class MyCartFragment extends BaseFragment implements View.OnClickListener
{
    private FragmentMyCartBinding fragmentMyCartBinding;
    private RecyclerView rvCart;
    private LinearLayoutManager linearLayoutManager;
    private CartItemsAdapter cartItemsAdapter;
    private ArrayList<Product> cartItemArrayList;
    private View navHostFragment;
    private ViewGroup.MarginLayoutParams navHostMargins;
    private float scale;

    @Override
    public void onPause() {
        super.onPause();
        ((AppCompatActivity) requireActivity()).findViewById(R.id.bottom_menu_card).setVisibility(View.VISIBLE);
        setBottomMarginInDps(50);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) requireActivity()).findViewById(R.id.bottom_menu_card).setVisibility(View.VISIBLE);
        setBottomMarginInDps(50);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) requireActivity()).findViewById(R.id.bottom_menu_card).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) requireActivity()).findViewById(R.id.bottom_menu_card).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

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

    private void initView()
    {
        fragmentMyCartBinding.btnPlaceOrder.setOnClickListener(this);
        initTitleBar(getString(R.string.my_cart));
        rvCart=fragmentMyCartBinding.rvCart;
        scale = getResources().getDisplayMetrics().density;

        ((AppCompatActivity) requireActivity()).findViewById(R.id.bottom_menu_card).setVisibility(View.GONE);

        navHostFragment=((AppCompatActivity) requireActivity()).findViewById(R.id.nav_host_fragment_content_home);
        navHostMargins = (ViewGroup.MarginLayoutParams) navHostFragment.getLayoutParams();
        navHostMargins.bottomMargin = 0;
    }

    private void setBottomMarginInDps(int i)
    {
        int dpAsPixels = (int) (i*scale + 0.5f);
        navHostMargins.bottomMargin = dpAsPixels;
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
        for(int i = 0; i < 10; i++)
        {
            Product cartItem = new Product();
            cartItem.setName("Bell Pepper Red");
            cartItem.setQty("1kg");
            cartItem.setPrice("Rs.30");
            cartItem.setOfferPrice("RS.25");
            cartItem.setImg("https://www.linkpicture.com/q/capsicon.png");
            cartItem.setLocation("Vishrantwadi");
            if (i==3){
                cartItem.setName("Potato");
                cartItem.setQty("2kg");
                cartItem.setPrice("Rs.50");
                cartItem.setOfferPrice("RS.40");
            } if (i==5){
                cartItem.setName("Potato");
                cartItem.setQty("2kg");
                cartItem.setPrice("Rs.50");
                cartItem.setOfferPrice("RS.40");
            }
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