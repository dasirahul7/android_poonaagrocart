package com.poona.agrocart.ui.nav_orders.track_order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentOrderTrackBinding;
import com.poona.agrocart.ui.BaseFragment;

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
        orderTrackViewModel.getOrderMutableLiveData().observe(getViewLifecycleOwner(),
                order -> {
            fragmentOrderTrackBinding.setOrderTrackViewModel(orderTrackViewModel);
        });
    }

    private void initView()
    {
        initTitleWithBackBtn(getString(R.string.order_track));
    }
}