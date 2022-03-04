package com.poona.agrocart.ui.nav_stores;

import static com.poona.agrocart.app.AppConstants.LIMIT;
import static com.poona.agrocart.app.AppConstants.OFFSET;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.STORE_ID;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.databinding.FragmentOurStoresBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_notification.NotificationFragment;
import com.poona.agrocart.ui.nav_stores.model.OurStoreListData;
import com.poona.agrocart.ui.nav_stores.model.OurStoreListResponse;

import java.util.ArrayList;
import java.util.HashMap;

public class OurStoresFragment extends BaseFragment implements OurStoreAdapter.OnStoreClickListener, NetworkExceptionListener {
    private final int limit = 10;
    private FragmentOurStoresBinding fragmentOurStoresBinding;
    private OurStoreViewModel ourStoreViewModel;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rvOurStores;
    private LinearLayoutManager linearLayoutManager;
    private OurStoreAdapter ourStoreAdapter;
    private ArrayList<OurStoreListData> storeArrayList;
    private int offset = 0;
    private int visibleItemCount = 0;
    private final int totalCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentOurStoresBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_our_stores, container, false);
        fragmentOurStoresBinding.setLifecycleOwner(this);
        ourStoreViewModel = new ViewModelProvider(this).get(OurStoreViewModel.class);
        fragmentOurStoresBinding.setOurStoreViewModel(ourStoreViewModel);
        final View view = fragmentOurStoresBinding.getRoot();

        initView();
        if(isConnectingToInternet(context)){
            setRvAdapter();
        }else{
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if(isConnectingToInternet(context)){
                    setRvAdapter();
                }else{
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });

        return view;
    }

    private void initView() {
        refreshLayout = fragmentOurStoresBinding.rlOurStore;
        rvOurStores = fragmentOurStoresBinding.rvOurStores;
        initTitleBar(getString(R.string.our_stores));

    }

    private void setRvAdapter() {
        storeArrayList = new ArrayList<>();
        if (isConnectingToInternet(context)) {
            /*Call Our Store List API here*/
            callOurStoreListApi(showCircleProgressDialog(context, ""), "RecyclerView");
        } else
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);

        // storeListData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvOurStores.setHasFixedSize(true);
        rvOurStores.setLayoutManager(linearLayoutManager);
        refreshLayout.setRefreshing(false);

        ourStoreAdapter = new OurStoreAdapter(storeArrayList, context, this);
        rvOurStores.setAdapter(ourStoreAdapter);

        //pagination for list
        setScrollListener();
    }

    private void setScrollListener() {
        rvOurStores.setNestedScrollingEnabled(true);
        NestedScrollView nestedScrollView = fragmentOurStoresBinding.nvOurStore;

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                visibleItemCount = linearLayoutManager.getItemCount();

                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount != totalCount) {
                    if (isConnectingToInternet(context)) {
                        /*Call Our Store List API here*/
                        callOurStoreListApi(showCircleProgressDialog(context, ""), "onScrolled");
                    } else
                        showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);

                } else if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount == totalCount) {
                    infoToast(requireActivity(), getString(R.string.no_result_found));  //change
                }
            }
        });

    }

    private void callOurStoreListApi(ProgressDialog progressDialog, String fromFunction) {

        if (fromFunction.equals("onScrolled")) {
            offset = offset + 2;
        } else {
            offset = 0;
        }
        @SuppressLint("NotifyDataSetChanged") Observer<OurStoreListResponse> ourStoreListResponseObserver = ourStoreListResponse -> {
            if (ourStoreListResponse != null) {
                Log.e("Our Store List Api ResponseData", new Gson().toJson(ourStoreListResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (ourStoreListResponse.getStatus()) {


                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (offset == 0)
                            storeArrayList.clear();

                        if (ourStoreListResponse.getData() != null
                                && ourStoreListResponse.getData().size() > 0) {
                            storeArrayList.addAll(ourStoreListResponse.getData());
                            ourStoreAdapter.notifyDataSetChanged();

                            fragmentOurStoresBinding.llMainLayout.setVisibility(View.VISIBLE);
                            fragmentOurStoresBinding.llEmptyScreen.setVisibility(View.GONE);
                        }else {
                            fragmentOurStoresBinding.llEmptyScreen.setVisibility(View.VISIBLE);
                            fragmentOurStoresBinding.llMainLayout.setVisibility(View.GONE);
                    }

                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, ourStoreListResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(ourStoreListResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, ourStoreListResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };

        ourStoreViewModel.getOurStoreListResponse(progressDialog, context, ourStoreInputParameter(),
                OurStoresFragment.this)
                .observe(getViewLifecycleOwner(), ourStoreListResponseObserver);
    }


    private HashMap<String, String> ourStoreInputParameter() {
        HashMap<String, String> map = new HashMap<>();

        map.put(LIMIT, String.valueOf(limit));
        map.put(OFFSET, String.valueOf(offset));
        return map;
    }


    @Override
    public void itemViewClick(int position) {

        String orderId = storeArrayList.get(position).getId();
        Bundle bundle = new Bundle();
        bundle.putString(STORE_ID, orderId);
        NavHostFragment.findNavController(OurStoresFragment.this).navigate(R.id.action_nav_store_to_storeLocationFragment, bundle);

    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), OurStoresFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if (from == 0) {
                    callOurStoreListApi(showCircleProgressDialog(context,""),"RecyclerView");
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);


    }
}