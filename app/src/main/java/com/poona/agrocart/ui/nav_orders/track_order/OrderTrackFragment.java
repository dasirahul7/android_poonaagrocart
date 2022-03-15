package com.poona.agrocart.ui.nav_orders.track_order;

import static com.poona.agrocart.app.AppConstants.ORDER_ID;
import static com.poona.agrocart.app.AppConstants.ORDER_SUBSCRIPTION_ID;
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
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.myOrderResponse.orderTrack.OrderTrack;
import com.poona.agrocart.data.network.responses.myOrderResponse.orderTrack.ProductOrderTrackResponse;
import com.poona.agrocart.databinding.FragmentOrderTrackBinding;
import com.poona.agrocart.ui.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderTrackFragment extends BaseFragment {
    private FragmentOrderTrackBinding fragmentOrderTrackBinding;
    private OrderTrackViewModel orderTrackViewModel;
    private View view;
    private List<OrderTrack> orderTracks = new ArrayList<>();
    private String order_id = "";

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

        callOrderTrackApi(showCircleProgressDialog(context, ""));
        return view;
    }


    private void initView() {
        initTitleWithBackBtn(getString(R.string.order_track));
    }

    private void callOrderTrackApi(ProgressDialog progressDialog){
        Observer<ProductOrderTrackResponse> productOrderTrackResponseObserver = productOrderTrackResponse -> {

            if (productOrderTrackResponse != null) {
                Log.e("Order Track Api ResponseData", new Gson().toJson(productOrderTrackResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (productOrderTrackResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        orderTracks.clear();
                        if (productOrderTrackResponse.getOrderDetials()!= null &&
                                productOrderTrackResponse.getOrderDetials().size() > 0) {
                            successToast(context, productOrderTrackResponse.getMessage());
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

    @SuppressLint("ResourceAsColor")
    private void setValues(List<OrderTrack> orderTracks) {
        orderTrackViewModel.orderId.setValue(orderTracks.get(0).getOrderCode());
        orderTrackViewModel.expectedDate.setValue(orderTracks.get(0).getShouldDeliverOnDate());
        orderTrackViewModel.cus_name.setValue(orderTracks.get(0).getName());
        orderTrackViewModel.cus_phone.setValue(orderTracks.get(0).getMobile());
        orderTrackViewModel.cus_address.setValue(orderTracks.get(0).getOrderAddressText() + ", " + orderTracks.get(0).getOrderAreaName()+", "+ orderTracks.get(0).getOrderCityName());



        switch (orderTracks.get(0).getOrderStatus()){
            case "1":
                fragmentOrderTrackBinding.imgOrderPlace.setImageResource(R.drawable.ic_tick_green_bg);
                orderTrackViewModel.place_date.setValue(orderTracks.get(0).getPendingDate());
                fragmentOrderTrackBinding.tvOrderPlaceDate.setVisibility(View.VISIBLE);
                break;
            case "2":
                fragmentOrderTrackBinding.imgOrderConfirmed.setImageResource(R.drawable.ic_tick_green_bg);
                orderTrackViewModel.confirmed_date.setValue(orderTracks.get(0).getConfirmDate());
                fragmentOrderTrackBinding.tvOrderConfirmedDate.setVisibility(View.VISIBLE);

                /*condition*/
                fragmentOrderTrackBinding.imgOrderPlace.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderPlaceDate.setVisibility(View.VISIBLE);
                break;
            case "3":
                fragmentOrderTrackBinding.imgOrderPacking.setImageResource(R.drawable.ic_tick_green_bg);
                orderTrackViewModel.packing_date.setValue(orderTracks.get(0).getInProcessDate());
                fragmentOrderTrackBinding.tvOrderPackDate.setVisibility(View.VISIBLE);

                /*condition*/
                fragmentOrderTrackBinding.imgOrderPlace.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderPlaceDate.setVisibility(View.VISIBLE);
                fragmentOrderTrackBinding.imgOrderConfirmed.setImageResource(R.drawable.ic_tick_green_bg);
                fragmentOrderTrackBinding.tvOrderConfirmedDate.setVisibility(View.VISIBLE);
                break;
            case "4":
                fragmentOrderTrackBinding.imgOrderDelivered.setImageResource(R.drawable.ic_tick_green_bg);
                orderTrackViewModel.delivered_date.setValue(orderTracks.get(0).getConfirmDate());
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

}