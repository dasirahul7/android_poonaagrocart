package com.poona.agrocart.ui.my_basket;

import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentMyBasketBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.my_basket.model.BasketOrder;
import java.util.ArrayList;

public class MyBasketFragment extends BaseFragment
{
    private FragmentMyBasketBinding fragmentMyBasketBinding;
    private RecyclerView rvBasketItems;
    private LinearLayoutManager linearLayoutManager;
    private BasketOrdersAdapter basketOrdersAdapter;
    private ArrayList<BasketOrder> basketOrderArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentMyBasketBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_my_basket, container, false);
        fragmentMyBasketBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentMyBasketBinding).getRoot();

        initView();
        setRvAdapter(view);

        initTitleBar(getString(R.string.menu_my_basket));

        return view;
    }

    private void initView()
    {
        rvBasketItems=fragmentMyBasketBinding.rvBasketItems;
    }

    private void setRvAdapter(View view)
    {
        basketOrderArrayList=new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvBasketItems.setHasFixedSize(true);
        rvBasketItems.setLayoutManager(linearLayoutManager);

        basketOrdersAdapter = new BasketOrdersAdapter(basketOrderArrayList,view,false);
        rvBasketItems.setAdapter(basketOrdersAdapter);
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 2; i++)
        {
            BasketOrder basketOrder = new BasketOrder();
            basketOrder.setOrderId(getString(R.string._paac002));
            basketOrder.setName(getString(R.string.diet_basket));
            basketOrder.setPrice(getString(R.string.rs_200_x_4));
            basketOrder.setPaymentMode(getString(R.string.qr));
            basketOrder.setTransactionId(getString(R.string._1200283e));
            basketOrder.setDate(getString(R.string._12_september_2021));
            basketOrderArrayList.add(basketOrder);
        }
    }
}