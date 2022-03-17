package com.poona.agrocart.ui.nav_orders.track_order;

import static com.poona.agrocart.app.AppConstants.ORDER_ID;
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

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.myOrderResponse.orderTrack.OrderTrack;
import com.poona.agrocart.data.network.responses.myOrderResponse.orderTrack.ProductOrderTrackResponse;
import com.poona.agrocart.databinding.FragmentOrderTrackBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_orders.order_view.OrderViewFragment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderTrackFragment extends BaseFragment implements NetworkExceptionListener {
    private FragmentOrderTrackBinding fragmentOrderTrackBinding;
    private OrderTrackViewModel orderTrackViewModel;
    private View view;
    private SwipeRefreshLayout refreshLayout;
    private List<OrderTrack> orderTracks = new ArrayList<>();
    private String order_id = "";
    private String txtDisplayDate="", selectedDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order_id = getArguments().getString(ORDER_ID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentOrderTrackBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_track, container, false);
        fragmentOrderTrackBinding.setLifecycleOwner(this);
        orderTrackViewModel = new ViewModelProvider(this).get(OrderTrackViewModel.class);
        fragmentOrderTrackBinding.setOrderTrackViewModel(orderTrackViewModel);
        view = fragmentOrderTrackBinding.getRoot();

        initView();

        fragmentOrderTrackBinding.clMainLayout.setVisibility(View.GONE);

        if(isConnectingToInternet(context)){
            callOrderTrackApi(showCircleProgressDialog(context, ""));
        }else {
            showNotifyAlert(requireActivity(), getString(R.string.info), getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if(isConnectingToInternet(context)){
                    callOrderTrackApi(showCircleProgressDialog(context, ""));
                }else {
                    showNotifyAlert(requireActivity(), getString(R.string.info), getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });

        return view;
    }


    private void initView() {
        initTitleWithBackBtn(getString(R.string.order_track));
        refreshLayout = fragmentOrderTrackBinding.rlOrderTrack;
    }

    private void callOrderTrackApi(ProgressDialog progressDialog){
        Observer<ProductOrderTrackResponse> productOrderTrackResponseObserver = productOrderTrackResponse -> {
            if (productOrderTrackResponse != null) {
                Log.e("Order Track Api ResponseData", new Gson().toJson(productOrderTrackResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                fragmentOrderTrackBinding.clMainLayout.setVisibility(View.VISIBLE);
                refreshLayout.setRefreshing(false);
                switch (productOrderTrackResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        orderTracks.clear();
                        if (productOrderTrackResponse.getOrderDetials()!= null &&
                                productOrderTrackResponse.getOrderDetials().size() > 0) {
                            orderTracks.addAll(productOrderTrackResponse.getOrderDetials());
                            setValues(orderTracks);
                        }
                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, productOrderTrackResponse.getMessage());

                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(productOrderTrackResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, productOrderTrackResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };
        orderTrackViewModel.getOrderTrackApi(progressDialog, context, orderTrackInputParameter(), OrderTrackFragment.this)
                .observe(getViewLifecycleOwner(), productOrderTrackResponseObserver);
    }

    private HashMap<String, String> orderTrackInputParameter(){
        HashMap<String, String> map = new HashMap<>();
        map.put(ORDER_ID, order_id);

        return map;
    }

    private String DisplayData(String selectedDate) {
        try {
            txtDisplayDate = formatDate(selectedDate, "yyyy-mm-dd hh:mm:ss", "MMM dd yyyy  hh:mm aa");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return txtDisplayDate ;
    }


    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void setValues(List<OrderTrack> orderTracks) {
        orderTrackViewModel.orderId.setValue(orderTracks.get(0).getOrderCode());
        orderTrackViewModel.cus_name.setValue(orderTracks.get(0).getName());
        orderTrackViewModel.cus_phone.setValue(orderTracks.get(0).getMobile());
        orderTrackViewModel.cus_address.setValue(orderTracks.get(0).getOrderAddressText() + ", " + orderTracks.get(0).getOrderAreaName()+", "+ orderTracks.get(0).getOrderCityName());

        selectedDate = orderTracks.get(0).getShouldDeliverOnDate();
        txtDisplayDate = "";
        try {
            txtDisplayDate = formatDate(selectedDate, "dd/mm/yyyy", "MMM dd, yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fragmentOrderTrackBinding.tvExpectedDate.setText(getString(R.string.expected_delivery_date)+" "+txtDisplayDate);

        switch (orderTracks.get(0).getOrderStatus()){
            case "1":
                fragmentOrderTrackBinding.imgOrderPlace.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderPlaceDate.setText(DisplayData(orderTracks.get(0).getPendingDate()));
                fragmentOrderTrackBinding.tvOrderPlaceDate.setVisibility(View.VISIBLE);
                break;
            case "2":
                fragmentOrderTrackBinding.imgOrderConfirmed.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderConfirmedDate.setText(DisplayData(orderTracks.get(0).getConfirmDate()));
                fragmentOrderTrackBinding.tvOrderConfirmedDate.setVisibility(View.VISIBLE);

                /*condition*/
                fragmentOrderTrackBinding.imgOrderPlace.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderPlaceDate.setVisibility(View.VISIBLE);
                break;
            case "3":
                fragmentOrderTrackBinding.imgOrderPacking.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderPackDate.setText(DisplayData(orderTracks.get(0).getInProcessDate()));
                fragmentOrderTrackBinding.tvOrderPackDate.setVisibility(View.VISIBLE);

                /*condition*/
                fragmentOrderTrackBinding.imgOrderPlace.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderPlaceDate.setVisibility(View.VISIBLE);
                fragmentOrderTrackBinding.imgOrderConfirmed.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderConfirmedDate.setVisibility(View.VISIBLE);
                break;
            case "4":
                fragmentOrderTrackBinding.imgOrderDelivered.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderDeliveredDate.setText(DisplayData(orderTracks.get(0).getConfirmDate()));
                fragmentOrderTrackBinding.tvOrderDeliveredDate.setVisibility(View.VISIBLE);

                /*condition*/
                fragmentOrderTrackBinding.imgOrderPlace.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderPlaceDate.setVisibility(View.VISIBLE);
                fragmentOrderTrackBinding.imgOrderConfirmed.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderConfirmedDate.setVisibility(View.VISIBLE);
                fragmentOrderTrackBinding.imgOrderPacking.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderPackDate.setVisibility(View.VISIBLE);
                break;
        }

    }


    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), OrderTrackFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if (from == 0) {
                    callOrderTrackApi(showCircleProgressDialog(context, ""));
                }
            }else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}