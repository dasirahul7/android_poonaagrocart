package com.poona.agrocart.ui.offer_coupon;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentCouponBinding;
import com.poona.agrocart.ui.BaseFragment;

public class CouponFragment extends BaseFragment {

    private CouponViewModel mViewModel;
    private FragmentCouponBinding fragmentCouponBinding;
    private CouponAdapter couponAdapter;


    public static CouponFragment newInstance() {
        return new CouponFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentCouponBinding = FragmentCouponBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(CouponViewModel.class);
        initTitleBar(getString(R.string.menu_offer_coupons));
        setCoupons();
        View view = fragmentCouponBinding.getRoot();
        return view;
    }

    private void setCoupons() {
        mViewModel.liveCoupons.observe(requireActivity(),coupons -> {
            couponAdapter = new CouponAdapter(coupons,requireActivity());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
            fragmentCouponBinding.rvCoupons.setLayoutManager(layoutManager);
            fragmentCouponBinding.rvCoupons.setAdapter(couponAdapter);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}