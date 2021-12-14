package com.poona.agrocart.ui.offer_coupon;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentCouponBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.widgets.CustomTextView;

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
        mViewModel.liveCoupons.observe(requireActivity(), coupons -> {
            couponAdapter = new CouponAdapter(coupons, requireActivity(), CouponFragment.this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            fragmentCouponBinding.rvCoupons.setLayoutManager(layoutManager);
            fragmentCouponBinding.rvCoupons.setAdapter(couponAdapter);
        });
    }

    public void termsDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_coupon_terms);
        ImageView closeImg = dialog.findViewById(R.id.close_btn);
        LinearLayout walletDialog = dialog.findViewById(R.id.wallet_dialog);
        CustomTextView tvContent = dialog.findViewById(R.id.tv_content);
        CustomTextView tvTitle = dialog.findViewById(R.id.dialog_title);

        tvTitle.setText(R.string.menu_terms_conditions);
        tvContent.setText(getString(R.string.sample_coupon_terms));
        walletDialog.setVisibility(View.GONE);
        tvContent.setVisibility(View.VISIBLE);
        closeImg.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}