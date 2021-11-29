package com.poona.agrocart.ui.order_track;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentOrderTrackBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.basket_product_detail.view_model.BasketProductViewModel;

public class OrderTrackFragment extends BaseFragment
{
    private FragmentOrderTrackBinding fragmentOrderTrackBinding;
    private OrderTrackViewModel orderTrackViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentOrderTrackBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_order_track, container, false);
        fragmentOrderTrackBinding.setLifecycleOwner(this);

        View root = fragmentOrderTrackBinding.getRoot();

        initView();
        setValues();


        return root;
    }

    private void setValues()
    {
        orderTrackViewModel = new ViewModelProvider(this).get(OrderTrackViewModel.class);
        fragmentOrderTrackBinding.setOrderTrackViewModel(orderTrackViewModel);
        orderTrackViewModel.orderId.setValue(getString(R.string._paac002));
        orderTrackViewModel.expectedDeliveryDate.setValue(getString(R.string.sep_30_2021_9_00_am_12_00pm));
        orderTrackViewModel.customerName.setValue(getString(R.string.ayush_shah));
        orderTrackViewModel.mobileNumber.setValue(getString(R.string._91_986_095_3315));
        orderTrackViewModel.address.setValue(getString(R.string.nand_nivas_building_floor_3_b_3_lane_no_13_bhatrau_nivas_vishrantwadi_pune_411015));
    }

    private void initView()
    {
        initTitleBar(getString(R.string.order_track));
    }
}