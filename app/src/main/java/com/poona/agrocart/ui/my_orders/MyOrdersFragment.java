package com.poona.agrocart.ui.my_orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentMyOrdersBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.my_orders.model.Order;
import java.util.ArrayList;

public class MyOrdersFragment extends BaseFragment {

    private FragmentMyOrdersBinding fragmentMyOrdersBinding;
    private RecyclerView rvOrders;
    private ArrayList<Order> orderArrayList;
    private LinearLayoutManager linearLayoutManager;
    private OrdersAdapter ordersAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentMyOrdersBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_my_orders, container, false);
        fragmentMyOrdersBinding.setLifecycleOwner(this);

        View root = fragmentMyOrdersBinding.getRoot();

        initView();
        setRvAdapter(root);

        initTitleBar(getString(R.string.menu_my_orders));
        return root;
    }

    private void setRvAdapter(View view)
    {
        orderArrayList=new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvOrders.setHasFixedSize(true);
        rvOrders.setLayoutManager(linearLayoutManager);

        ordersAdapter = new OrdersAdapter(orderArrayList,context,view);
        rvOrders.setAdapter(ordersAdapter);
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 4; i++)
        {
            Order order = new Order();
            order.setOrderId(getString(R.string._1200283e));
            order.setDateAndTime(getString(R.string.sep_30_2021_20_15_am));
            order.setAmount(getString(R.string.rs_800));
            if(i==0) {
                order.setStatus(getString(R.string.in_process));
            }
            else if(i==1) {
                order.setStatus(getString(R.string.delivered));
            }
            else if(i==2) {
                order.setStatus(getString(R.string.confirmed));
            }
            else {
                order.setStatus(getString(R.string.cancelled));
            }
            order.setQuantity(getString(R.string._25));
            orderArrayList.add(order);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        fragmentMyOrdersBinding = null;
    }

    private void initView()
    {
        rvOrders=fragmentMyOrdersBinding.rvOrders;
    }
}