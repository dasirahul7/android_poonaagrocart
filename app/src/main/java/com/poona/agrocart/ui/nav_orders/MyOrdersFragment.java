package com.poona.agrocart.ui.nav_orders;

import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.myOrderResponse.OrderListResponse;
import com.poona.agrocart.databinding.FragmentMyOrdersBinding;
import com.poona.agrocart.ui.BaseFragment;

import java.util.ArrayList;

public class MyOrdersFragment extends BaseFragment {

    private FragmentMyOrdersBinding fragmentMyOrdersBinding;
    private MyOrdersViewModel myOrdersViewModel;
    private RecyclerView rvOrders;
    private ArrayList<OrderListResponse.Order> orderArrayList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private OrdersAdapter ordersAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentMyOrdersBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders, container, false);
        fragmentMyOrdersBinding.setLifecycleOwner(this);
        myOrdersViewModel = new ViewModelProvider(this).get(MyOrdersViewModel.class);
        View root = fragmentMyOrdersBinding.getRoot();

        initView();
        setRvAdapter(root);

        initTitleBar(getString(R.string.menu_my_orders));
        return root;
    }

    private void setRvAdapter(View view) {
        orderArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        callOrderListApi(showCircleProgressDialog(context, ""));
        rvOrders.setHasFixedSize(true);
        rvOrders.setLayoutManager(linearLayoutManager);

        ordersAdapter = new OrdersAdapter(orderArrayList, context, view, this);
        rvOrders.setAdapter(ordersAdapter);
    }

    private void callOrderListApi(ProgressDialog progressDialog){
        @SuppressLint("NotifyDataSetChanged") Observer<OrderListResponse> orderListResponseObserver = orderListResponse -> {
            if (orderListResponse != null) {
                Log.e("Order List Api ResponseData", new Gson().toJson(orderListResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (orderListResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        orderArrayList.clear();
                        if (orderListResponse.getOrderList()!= null &&
                                orderListResponse.getOrderList().size() > 0) {
                            orderArrayList.addAll(orderListResponse.getOrderList());
                            ordersAdapter.notifyDataSetChanged();

                            fragmentMyOrdersBinding.llMainLayout.setVisibility(View.VISIBLE);
                            fragmentMyOrdersBinding.llEmptyScreen.setVisibility(View.GONE);

                        }else {
                            warningToast(context, orderListResponse.getMessage());
                            fragmentMyOrdersBinding.llEmptyScreen.setVisibility(View.VISIBLE);
                            fragmentMyOrdersBinding.llMainLayout.setVisibility(View.GONE);
                        }
                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, orderListResponse.getMessage());
                        fragmentMyOrdersBinding.llEmptyScreen.setVisibility(View.VISIBLE);
                        fragmentMyOrdersBinding.llMainLayout.setVisibility(View.GONE);
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(orderListResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, orderListResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };
        myOrdersViewModel.getOrderListResponse(progressDialog,context,MyOrdersFragment.this)
                .observe(getViewLifecycleOwner(),orderListResponseObserver);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentMyOrdersBinding = null;
    }

    private void initView() {
        rvOrders = fragmentMyOrdersBinding.rvOrders;
    }
}