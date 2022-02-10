package com.poona.agrocart.ui.nav_stores;

import static com.poona.agrocart.app.AppConstants.LIMIT;
import static com.poona.agrocart.app.AppConstants.OFFSET;
import static com.poona.agrocart.app.AppConstants.ORDER_ID;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.TICKET_ID;

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

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentOurStoresBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_stores.model.OurStoreListData;
import com.poona.agrocart.ui.nav_stores.model.OurStoreListResponse;


import java.util.ArrayList;
import java.util.HashMap;

public class OurStoresFragment extends BaseFragment implements OurStoreAdapter.OnStoreClickListener
{
    private FragmentOurStoresBinding fragmentOurStoresBinding;
    private OurStoreViewModel ourStoreViewModel;
    private RecyclerView rvOurStores;
    private LinearLayoutManager linearLayoutManager;
    private OurStoreAdapter ourStoreAdapter;
    private ArrayList<OurStoreListData> storeArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentOurStoresBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_our_stores, container, false);
        fragmentOurStoresBinding.setLifecycleOwner(this);
        ourStoreViewModel=new ViewModelProvider(this).get(OurStoreViewModel.class);
        fragmentOurStoresBinding.setOurStoreViewModel(ourStoreViewModel);
        final View view = fragmentOurStoresBinding.getRoot();

        initView();
        setRvAdapter();

        return view;
    }

    private void initView()
    {
        rvOurStores=fragmentOurStoresBinding.rvOurStores;
        initTitleBar(getString(R.string.our_stores));

    }

    private void setRvAdapter()
    {
        storeArrayList=new ArrayList<>();
        callOurStoreListApi(showCircleProgressDialog(context, ""), "RecyclerView");

        // storeListData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvOurStores.setHasFixedSize(true);
        rvOurStores.setLayoutManager(linearLayoutManager);

        ourStoreAdapter = new OurStoreAdapter(storeArrayList, context ,this);
        rvOurStores.setAdapter(ourStoreAdapter);

        //pagination for list
        setScrollListener();
    }

    private int offset = 0;
    private final int limit = 10;
    private int visibleItemCount = 0;
    private int totalCount = 0;

    private void setScrollListener() {
        rvOurStores.setNestedScrollingEnabled(true);
        NestedScrollView nestedScrollView= fragmentOurStoresBinding.nvOurStore;

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(v.getChildAt(v.getChildCount() - 1) != null) {
                visibleItemCount = linearLayoutManager.getItemCount();

                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount != totalCount) {
                    callOurStoreListApi(showCircleProgressDialog(context, ""), "onScrolled");
                }
                else if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount == totalCount) {
                    infoToast(requireActivity(), getString(R.string.no_result_found));  //change
                }
            }
        });

    }

    private void callOurStoreListApi(ProgressDialog progressDialog, String fromFunction){

        if (fromFunction.equals("onScrolled")) {
            offset = offset + 2;
        } else {
            offset = 0;
        }
        @SuppressLint("NotifyDataSetChanged") Observer<OurStoreListResponse> ourStoreListResponseObserver = ourStoreListResponse -> {
            if (ourStoreListResponse != null){
                Log.e("Our Store List Api Response", new Gson().toJson(ourStoreListResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (ourStoreListResponse.getStatus()) {


                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (offset == 0)
                            storeArrayList.clear();

                        if (ourStoreListResponse.getData() != null
                                && ourStoreListResponse.getData().size() > 0){
                            storeArrayList.addAll(ourStoreListResponse.getData());
                            ourStoreAdapter.notifyDataSetChanged();

                            /*if(ourStoreListResponse.getData() != null){
                                llEmptyLayout.setVisibility(View.INVISIBLE);
                                llMainLayout.setVisibility(View.VISIBLE);
                            }*/
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
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }
        };

        ourStoreViewModel.getOurStoreListResponse(progressDialog, context,ourStoreInputParameter(),
                OurStoresFragment.this)
                .observe(getViewLifecycleOwner(), ourStoreListResponseObserver);
    }


    private HashMap<String, String> ourStoreInputParameter () {
        HashMap<String, String> map = new HashMap<>();

        map.put(LIMIT, String.valueOf(limit));
        map.put(OFFSET, String.valueOf(offset));
        return map;
}


    @Override
    public void itemViewClick(int position) {

        String orderId =storeArrayList.get(position).getId();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID, orderId);
        NavHostFragment.findNavController(OurStoresFragment.this).navigate(R.id.action_nav_store_to_storeLocationFragment, bundle);

    }
}