package com.poona.agrocart.ui.nav_orders.order_view;

import static com.poona.agrocart.app.AppConstants.ITEM_LIST;
import static com.poona.agrocart.app.AppConstants.LIMIT;
import static com.poona.agrocart.app.AppConstants.OFFSET;
import static com.poona.agrocart.app.AppConstants.ORDER_SUBSCRIPTION_ID;
import static com.poona.agrocart.app.AppConstants.REVIEW_LIST;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.poona.agrocart.R;


import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.ItemsDetail;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.SubscribeBasketItemListResponse;
import com.poona.agrocart.databinding.FragmentBasketItemBinding;

import com.poona.agrocart.ui.BaseFragment;


import java.util.ArrayList;
import java.util.HashMap;


public class BasketItemFragment extends BaseFragment {

    private FragmentBasketItemBinding fragmentBasketItemBinding;
    private BasketItemViewHolder basketItemViewHolder;
    private View view;
    private ArrayList<ItemsDetail> basketItemList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rvBasketItem;
    private BasketItemsAdapter basketItemsAdapter;
    private boolean isBasketVisible = true;

    private final int limit = 5;
    private int offset = 0;
    private int visibleItemCount = 0;
    private int totalCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
          //  mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentBasketItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket_item, container, false);
        fragmentBasketItemBinding.setLifecycleOwner(this);
        basketItemViewHolder = new ViewModelProvider(this).get(BasketItemViewHolder.class);
        fragmentBasketItemBinding.setBasketItemViewHolder(basketItemViewHolder);
        view = fragmentBasketItemBinding.getRoot();
        initTitleWithBackBtn("Subscription Basket Item");

        initView();

        Bundle bundle = this.getArguments();
        isBasketVisible = bundle.getBoolean("isBasketVisible");
        rvBasketItem = fragmentBasketItemBinding.rvBasketItem;
        if (isBasketVisible) {
            setRVAdapter();
        }

       /* Bundle bundle = this.getArguments();
        isBasketVisible = bundle.getBoolean("isBasketVisible");
        rvBasketItem = fragmentBasketItemBinding.rvBasketItem;
        if (isBasketVisible) {
            setRVAdapter();
        }

        if (bundle != null) {
            basketItemList = bundle.getParcelableArrayList(ITEM_LIST);
        }
        initView();

        if (basketItemList.size() > 0){
            setRVAdapter();
        }*/

        return view;
    }

    private void initView() {
        rvBasketItem = fragmentBasketItemBinding.rvBasketItem;
    }

    private void setScrollListener() {
        rvBasketItem.setNestedScrollingEnabled(true);
        NestedScrollView nestedScrollView = fragmentBasketItemBinding.nvBasketItem;

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    visibleItemCount = linearLayoutManager.getItemCount();

                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                            && visibleItemCount != totalCount) {
                        if (isConnectingToInternet(context)) {
                            /*Call Our Store List API here*/
                            callSubscriptionBasketItemList(showCircleProgressDialog(context, ""), "ScrollView");
                        } else
                            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);

                    } else if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                            && visibleItemCount == totalCount) {
                        infoToast(requireActivity(), getString(R.string.no_result_found));  //change
                    }
                }
            }
        });


    }

    private void setRVAdapter() {

        basketItemList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        callSubscriptionBasketItemList(showCircleProgressDialog(context, ""), "Recyclerview");

        rvBasketItem.setHasFixedSize(true);
        rvBasketItem.setLayoutManager(linearLayoutManager);

        basketItemsAdapter = new BasketItemsAdapter(basketItemList,isBasketVisible, getContext(),1);
        rvBasketItem.setAdapter(basketItemsAdapter);

        //Added the Pagination
        setScrollListener();
    }
    private void callSubscriptionBasketItemList(ProgressDialog progressDialog, String fromFunction){

        if (fromFunction.equals("onScrolled")) {
            offset = offset + 1;
        } else {
            offset = 0;
        }

        @SuppressLint("NotifyDataSetChanged") Observer<SubscribeBasketItemListResponse> subscribeBasketItemListResponseObserver = subscribeBasketItemListResponse -> {

            if (subscribeBasketItemListResponse != null) {
                Log.e("Subscription Basket Item List List Api ResponseData", new Gson().toJson(subscribeBasketItemListResponse));

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (subscribeBasketItemListResponse.getStatus()) {


                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (offset == 0)
                            basketItemList.clear();

                        totalCount = Integer.parseInt(subscribeBasketItemListResponse.getTotalCount());
                        if (subscribeBasketItemListResponse.getBasketSubscriptionDetails() != null
                                && subscribeBasketItemListResponse.getBasketSubscriptionDetails().getItemsDetails().size() > 0) {
                            basketItemList.addAll(subscribeBasketItemListResponse.getBasketSubscriptionDetails().getItemsDetails());
                            basketItemsAdapter.notifyDataSetChanged();
                        }


                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, subscribeBasketItemListResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(subscribeBasketItemListResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, subscribeBasketItemListResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };
        basketItemViewHolder.getSubscriptionBasketItemList(context, progressDialog, BasketItemInputParameter(), BasketItemFragment.this)
                .observe(getViewLifecycleOwner(), subscribeBasketItemListResponseObserver);
    }

    private HashMap<String,String> BasketItemInputParameter() {

        HashMap<String, String> map = new HashMap<>();

        map.put(ORDER_SUBSCRIPTION_ID, "1");
        map.put(OFFSET, String.valueOf(offset));
        map.put(LIMIT, String.valueOf(limit));

        return map;
    }
}