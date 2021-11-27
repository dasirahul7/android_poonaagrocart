package com.poona.agrocart.ui.order_view;

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
import com.poona.agrocart.databinding.FragmentOrderViewBinding;
import java.util.ArrayList;

public class OrderViewFragment extends Fragment {

    private FragmentOrderViewBinding fragmentOrderViewBinding;
    private RecyclerView rvBasketListItems;
    private LinearLayoutManager linearLayoutManager;
    private BasketItemsAdapter basketItemsAdapter;
    private ArrayList<BasketItem> basketItemList;
    private boolean isBasketVisible=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentOrderViewBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_order_view, container, false);
        fragmentOrderViewBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentOrderViewBinding).getRoot();

        initView();
        setRVAdapter();

        return view;
    }

    private void initView()
    {
        Bundle bundle=this.getArguments();
        isBasketVisible=bundle.getBoolean("isBasketVisible");
        rvBasketListItems=fragmentOrderViewBinding.rvBasketItems;
        if(isBasketVisible)
        {
            setBasketContentsVisible();
        }
        else
        {
            showProductDetails();
        }
    }

    private void showProductDetails()
    {
        fragmentOrderViewBinding.tvBasketDetails.setVisibility(View.GONE);
        fragmentOrderViewBinding.tvProductDetails.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.btnTrackOrder.setVisibility(View.VISIBLE);
    }

    private void setBasketContentsVisible()
    {

        fragmentOrderViewBinding.tvBasketDetails.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.tvProductDetails.setVisibility(View.GONE);
        fragmentOrderViewBinding.btnTrackOrder.setVisibility(View.GONE);
    }

    private void setRVAdapter()
    {
        basketItemList=new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvBasketListItems.setHasFixedSize(true);
        rvBasketListItems.setLayoutManager(linearLayoutManager);

        basketItemsAdapter = new BasketItemsAdapter(basketItemList,isBasketVisible);
        rvBasketListItems.setAdapter(basketItemsAdapter);
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 4; i++)
        {
            BasketItem basketItem = new BasketItem();
            basketItem.setNameOfProduct("ABC");
            basketItem.setWeight("250gms");
            basketItem.setDate("22nd Sept 2021");
            basketItem.setTime("9.00 am to 9.00 pm");
            basketItem.setDeliveryStatus("Delivered");
            basketItem.setPrice("Rs.200");
            basketItemList.add(basketItem);
        }
    }
}