package com.poona.agrocart.ui.nav_my_basket;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.poona.agrocart.app.AppConstants.IMAGE_DOC_BASE_URL;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.myOrderResponse.SubscribeBasketListCustomerResponse;
import com.poona.agrocart.databinding.FragmentMyBasketBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_my_basket.model.BasketOrder;
import com.poona.agrocart.ui.nav_orders.order_view.OrderViewDetailsViewModel;
import com.poona.agrocart.ui.nav_orders.order_view.OrderViewFragment;

import java.util.ArrayList;

public class MyBasketFragment extends BaseFragment implements NetworkExceptionListener, BasketOrdersAdapter.OnInvoiceClickListener {
    private FragmentMyBasketBinding fragmentMyBasketBinding;
    private MyBasketViewModel myBasketViewModel;
    private RecyclerView rvBasketItems;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private BasketOrdersAdapter basketOrdersAdapter;
    private ArrayList<SubscribeBasketListCustomerResponse.SubscribeBasketListCustomer> basketOrderArrayList;
    private View view;
    long downloadID;
    private DownloadManager downloadManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentMyBasketBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_basket, container, false);
        fragmentMyBasketBinding.setLifecycleOwner(this);
        myBasketViewModel = new ViewModelProvider(this).get(MyBasketViewModel.class);
        fragmentMyBasketBinding.setMyBasketViewModel(myBasketViewModel);
        view = fragmentMyBasketBinding.getRoot();

        initView();
        if(isConnectingToInternet(context)){
            setRvAdapter(view);
        }else{
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if(isConnectingToInternet(context)){
                    setRvAdapter(view);
                }else {
                    showNotifyAlert(requireActivity(),context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });

        initTitleBar(getString(R.string.menu_my_basket_subs));

        return view;
    }

    private void initView() {
        refreshLayout = fragmentMyBasketBinding.rlBasketMain;
        rvBasketItems = fragmentMyBasketBinding.rvBasketItems;
    }

    private void setRvAdapter(View view) {
        basketOrderArrayList = new ArrayList<>();
       // basketListingData();
        callSubscriptionBasketListApi(showCircleProgressDialog(context, ""));
        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvBasketItems.setHasFixedSize(true);
        rvBasketItems.setLayoutManager(linearLayoutManager);
        refreshLayout.setRefreshing(false);
        basketOrdersAdapter = new BasketOrdersAdapter(basketOrderArrayList, view, false, this);
        rvBasketItems.setAdapter(basketOrdersAdapter);
    }

    private void callSubscriptionBasketListApi(ProgressDialog progressDialog){
        @SuppressLint("NotifyDataSetChanged") Observer<SubscribeBasketListCustomerResponse> subscribeBasketListCustomerResponseObserver = subscribeBasketListCustomerResponse -> {
            if (subscribeBasketListCustomerResponse != null) {
                Log.e(" My Basket List Api ResponseData", new Gson().toJson(subscribeBasketListCustomerResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (subscribeBasketListCustomerResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create Update Successfully

                        if (subscribeBasketListCustomerResponse.getOrderDetials()!= null &&
                                subscribeBasketListCustomerResponse.getOrderDetials().size() > 0) {
                            basketOrderArrayList.addAll(subscribeBasketListCustomerResponse.getOrderDetials());
                            basketOrdersAdapter.notifyDataSetChanged();
                        }
                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, subscribeBasketListCustomerResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(subscribeBasketListCustomerResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, subscribeBasketListCustomerResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };
        myBasketViewModel.getSubScriptionBasketListApi(progressDialog, context, MyBasketFragment.this)
                .observe(getViewLifecycleOwner(), subscribeBasketListCustomerResponseObserver);
    }

    /*Download the document*/
    private void beginDownload(String docPdf) {
        DownloadManager.Request request = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            request = new DownloadManager.Request(Uri.parse(docPdf))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);
        } else {
            request = new DownloadManager.Request(Uri.parse(docPdf))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setAllowedOverRoaming(true);
        }
        downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);

        // using query method
        boolean finishDownload = true;
        int progress;
        while (!finishDownload) {
            Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadID));
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_FAILED: {
                        finishDownload = true;
                        break;
                    }
                    case DownloadManager.STATUS_PAUSED:
                        break;
                    case DownloadManager.STATUS_PENDING:
                        break;
                    case DownloadManager.STATUS_RUNNING: {
                        @SuppressLint("Range") final long total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        if (total >= 0) {
                            @SuppressLint("Range") final long downloaded = ((Cursor) cursor).getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            progress = (int) ((downloaded * 100L) / total);
                            // if you use downloadmanger in async task, here you can use like this to display progress.
                            // Don't forget to do the division in long to get more digits rather than double.
                            //  publishProgress((int) ((downloaded * 100L) / total));
                        }

                        break;
                    }
                    case DownloadManager.STATUS_SUCCESSFUL: {
                        progress = 100;
                        // if you use aysnc task
                        // publishProgress(100);
                        finishDownload = true;
                        Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onNetworkException(int from, String type) {

        showServerErrorDialog(getString(R.string.for_better_user_experience), MyBasketFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if (from == 0) {
                    callSubscriptionBasketListApi(showCircleProgressDialog(context, ""));
                }
            }else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }

    @Override
    public void ItemClick(int position) {
        String strInvoiceDownload = basketOrderArrayList.get(position).getInvoiceFile();

        if(strInvoiceDownload != null){
            beginDownload(IMAGE_DOC_BASE_URL+strInvoiceDownload);
        }else{
            infoToast(context, "please wait for while! file will download");
        }

    }
}