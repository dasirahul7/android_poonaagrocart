package com.poona.agrocart.ui.offer_coupon;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.poona.agrocart.databinding.DialogCouponTermsBinding;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        DialogCouponTermsBinding binding = DialogCouponTermsBinding.inflate(LayoutInflater.from(context));
        builder.setView(binding.getRoot());
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDownUp;

        ImageView closeImg = binding.closeBtn;
        LinearLayout walletDialog = binding.walletDialog;
        CustomTextView tvContent = binding.tvContent;
        CustomTextView dialogTitle = binding.dialogTitle;
        dialogTitle.setText(R.string.menu_terms_conditions);
        tvContent.setText(getString(R.string.sample_coupon_terms));
        walletDialog.setVisibility(View.GONE);
        tvContent.setVisibility(View.VISIBLE);
        closeImg.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();

        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        // Set alert dialog width equal to screen width 100%
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        layoutParams.width = dialogWindowWidth;
        dialog.getWindow().setAttributes(layoutParams);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}