package com.poona.agrocart.ui.nav_offers;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.CouponResponse;
import com.poona.agrocart.databinding.DialogCouponTermsBinding;
import com.poona.agrocart.databinding.FragmentCouponBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.widgets.CustomTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CouponFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener, CouponAdapter.TermsAndConditionClickItem {
    private static final String TAG = CouponFragment.class.getSimpleName();
    private CouponViewModel couponViewModel;
    private FragmentCouponBinding fragmentCouponBinding;
    private CouponAdapter couponAdapter;
    private List<CouponResponse.Coupon> couponArrayList = new ArrayList<>();
    private int limit =0,offset = 0;


    public static CouponFragment newInstance() {
        return new CouponFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentCouponBinding = FragmentCouponBinding.inflate(getLayoutInflater());
        couponViewModel = new ViewModelProvider(this).get(CouponViewModel.class);
        initTitleBar(getString(R.string.menu_offer_coupons));
        if (isConnectingToInternet(context))
        setAllCoupons(showCircleProgressDialog(context, ""),limit,offset);
        else showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        View view = fragmentCouponBinding.getRoot();
        return view;
    }

    private void setAllCoupons(ProgressDialog progressDialog,int limit, int offset) {
        limit +=10;
        Observer<CouponResponse> couponResponseObserver = couponResponse -> {
            if (couponResponse != null) {
                progressDialog.dismiss();
                Log.e(TAG, "setAllCoupons: " + couponResponse.getCoupons().size());
                switch (couponResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (couponResponse.getCoupons().size() > 0) {
                            couponArrayList = couponResponse.getCoupons();
                            couponAdapter = new CouponAdapter(couponResponse.getCoupons(), requireActivity(), CouponFragment.this,this);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                            fragmentCouponBinding.rvCoupons.setLayoutManager(layoutManager);
                            fragmentCouponBinding.rvCoupons.setAdapter(couponAdapter);

                            couponAdapter.notifyDataSetChanged();
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, couponResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(couponResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, couponResponse.getMessage());
                        break;
                }

            }
        };
        couponViewModel.couponResponseLiveData(progressDialog, couponParams(limit, offset),CouponFragment.this)
                .observe(getViewLifecycleOwner(), couponResponseObserver);

//        mViewModel.liveCoupons.observe(requireActivity(), coupons -> {
//            couponAdapter = new CouponAdapter(coupons, requireActivity(), CouponFragment.this);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
//            fragmentCouponBinding.rvCoupons.setLayoutManager(layoutManager);
//            fragmentCouponBinding.rvCoupons.setAdapter(couponAdapter);
//        });
    }

    private HashMap<String, String> couponParams(int limit,int offset) {
            HashMap<String, String> map = new HashMap<>();
            map.put(AppConstants.LIMIT, String.valueOf(limit));
            map.put(AppConstants.OFFSET, String.valueOf(offset));
            return map;
    }

    public void termsDialog(String termsAndCondition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        DialogCouponTermsBinding binding = DialogCouponTermsBinding.inflate(LayoutInflater.from(context));
        builder.setView(binding.getRoot());
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;

        ImageView closeImg = binding.closeBtn;
        LinearLayout walletDialog = binding.walletDialog;
        CustomTextView tvContent = binding.tvContent;
        CustomTextView dialogTitle = binding.dialogTitle;
        dialogTitle.setText(R.string.menu_terms_conditions);


        walletDialog.setVisibility(View.GONE);
        tvContent.setVisibility(View.VISIBLE);
        closeImg.setOnClickListener(v -> {
            dialog.dismiss();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContent.setText(Html.fromHtml(""+termsAndCondition, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.tvContent.setText(Html.fromHtml(""+termsAndCondition));
        }

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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), CouponFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        // Call Banner API after network error
                        setAllCoupons(showCircleProgressDialog(context, ""), limit, offset);
                        break;
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }

    @Override
    public void itemViewClick(int position) {
        String termsConditions = couponArrayList.get(position).getTermsAndCond();
            termsDialog(termsConditions);

    }

    @Override
    public void onCopyClick(CouponResponse.Coupon coupon) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("CouponCode", coupon.getCouponCode());
        clipboard.setPrimaryClip(clip);
        successToast(context,"Copied to clipBoard");
    }
}