package com.poona.agrocart.ui.nav_orders.order_view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentOrderViewBinding;
import com.poona.agrocart.ui.BaseFragment;

import java.util.ArrayList;

public class OrderViewFragment extends BaseFragment implements View.OnClickListener{

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
        final View view = fragmentOrderViewBinding.getRoot();

        initView();
        setRVAdapter();

        initTitleWithBackBtn(getString(R.string.order_view));

        return view;
    }

    private void initView()
    {
        fragmentOrderViewBinding.btnTrackOrder.setOnClickListener(this);

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
        fragmentOrderViewBinding.llSubTotal.setVisibility(View.GONE);
        fragmentOrderViewBinding.line2.setVisibility(View.GONE);
    }

    private void setBasketContentsVisible()
    {
        fragmentOrderViewBinding.tvBasketDetails.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.tvProductDetails.setVisibility(View.GONE);
        fragmentOrderViewBinding.btnTrackOrder.setVisibility(View.GONE);
        fragmentOrderViewBinding.llSubTotal.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.line2.setVisibility(View.VISIBLE);
    }

    private void setRVAdapter()
    {
        basketItemList=new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvBasketListItems.setHasFixedSize(true);
        rvBasketListItems.setLayoutManager(linearLayoutManager);

        basketItemsAdapter = new BasketItemsAdapter(basketItemList,isBasketVisible,getContext());
        rvBasketListItems.setAdapter(basketItemsAdapter);
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 4; i++)
        {
            BasketItem basketItem = new BasketItem();
            basketItem.setNameOfProduct("ABC");
            basketItem.setWeight("250gms");
            basketItem.setQuantity(getString(R.string.sample_unit));
            basketItem.setDate("22nd Sept 2021");
            basketItem.setTime("9.00 am to 9.00 pm");
            if(i==0)
                basketItem.setDeliveryStatus("Delivered");
            else if(i==2 || i==3)
                basketItem.setDeliveryStatus("Confirmed");
            else
                basketItem.setDeliveryStatus("In transist");
            basketItem.setPrice("Rs.200");
            basketItemList.add(basketItem);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_track_order:
                redirectToTrackOrderFragment(v);
                break;
        }
    }

    private void redirectToTrackOrderFragment(View view)
    {
        if (isConnectingToInternet(context))
        Navigation.findNavController(view).navigate(R.id.action_orderViewFragment_to_nav_order_track);
        else showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
    }
}