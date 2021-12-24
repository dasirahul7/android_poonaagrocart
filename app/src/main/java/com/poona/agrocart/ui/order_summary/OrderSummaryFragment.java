package com.poona.agrocart.ui.order_summary;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentOrderSummaryBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.order_summary.adapter.AddressDialogAdapter;
import com.poona.agrocart.ui.order_summary.model.Address;
import com.poona.agrocart.ui.order_summary.model.ProductAndPrice;

import java.util.ArrayList;

public class OrderSummaryFragment extends BaseFragment implements View.OnClickListener {
    private FragmentOrderSummaryBinding fragmentOrderSummaryBinding;
    private RecyclerView rvProductsAndPrices;
    private LinearLayoutManager linearLayoutManager;
    private ProductAndPriceAdapter productAndPriceAdapter;
    private ArrayList<ProductAndPrice> productAndPriceArrayList;

    private View navHostFragment;
    private ViewGroup.MarginLayoutParams navHostMargins;
    private float scale;

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        setBottomMarginInDps(50);
    }

    @Override
    public void onStop() {
        super.onStop();
        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        setBottomMarginInDps(50);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentOrderSummaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_summary, container, false);
        fragmentOrderSummaryBinding.setLifecycleOwner(this);
        final View view = fragmentOrderSummaryBinding.getRoot();

        initTitleWithBackBtn(getString(R.string.order_summary));
        initView();
        setRvAdapter();

        return view;
    }

    private void setRvAdapter() {
        productAndPriceArrayList = new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvProductsAndPrices.setHasFixedSize(true);
        rvProductsAndPrices.setLayoutManager(linearLayoutManager);

        productAndPriceAdapter = new ProductAndPriceAdapter(productAndPriceArrayList);
        rvProductsAndPrices.setAdapter(productAndPriceAdapter);
    }

    private void prepareListingData() {
        for (int i = 0; i < 3; i++) {
            ProductAndPrice productAndPrice = new ProductAndPrice();
            productAndPrice.setProductName(getString(R.string.bell_pepper_red_1kg));
            productAndPrice.setDividedPrice(getString(R.string.rs_200_x_4));
            productAndPrice.setFinalPrice(getString(R.string.rs_740));
            productAndPriceArrayList.add(productAndPrice);
        }
    }

    private void setBottomMarginInDps(int i) {
        int dpAsPixels = (int) (i * scale + 0.5f);
        navHostMargins.bottomMargin = dpAsPixels;
    }

    private void initView() {
        rvProductsAndPrices = fragmentOrderSummaryBinding.rvProductsAndPrices;
        Typeface font = Typeface.createFromAsset(context.getAssets(), getString(R.string.font_poppins_medium));
        fragmentOrderSummaryBinding.rbCod.setTypeface(font);
        fragmentOrderSummaryBinding.rbOnline.setTypeface(font);
        fragmentOrderSummaryBinding.rbWalletBalance.setTypeface(font);
        fragmentOrderSummaryBinding.rbCod.setOnClickListener(this);
        fragmentOrderSummaryBinding.rbOnline.setOnClickListener(this);
        fragmentOrderSummaryBinding.rbWalletBalance.setOnClickListener(this);
        fragmentOrderSummaryBinding.ivAddressOption.setOnClickListener(this);

        scale = getResources().getDisplayMetrics().density;

        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);

        navHostFragment = requireActivity().findViewById(R.id.nav_host_fragment_content_home);
        navHostMargins = (ViewGroup.MarginLayoutParams) navHostFragment.getLayoutParams();
        navHostMargins.bottomMargin = 0;
        fragmentOrderSummaryBinding.btnMakePayment.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_make_payment:
                try {
                    redirectToOrderDetails(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rb_cod:
            case R.id.rb_online:
                summaryRadioAction(true);
            case R.id.rb_wallet_balance:
                summaryRadioAction(false);
                break;
            case R.id.iv_address_option:
                openAddressOptionsDialog();
                break;
        }
    }

    private void summaryRadioAction(boolean group) {
        if (group){
            if (fragmentOrderSummaryBinding.rbCod.isChecked()||fragmentOrderSummaryBinding.rbOnline.isChecked()){
                fragmentOrderSummaryBinding.rbWalletBalance.setChecked(false);
            }
        }else { if (fragmentOrderSummaryBinding.rbWalletBalance.isChecked()){
                fragmentOrderSummaryBinding.rbCod.setChecked(false);
                fragmentOrderSummaryBinding.rbOnline.setChecked(false);
            }
        }

    }

    private void redirectToOrderDetails(View v) {
        if (isConnectingToInternet(context)) {
            Navigation.findNavController(v).navigate(R.id.action_nav_order_summary_to_orderDetailsFragment);
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    public void openAddressOptionsDialog() {
        Dialog dialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogAnimationUp));
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_order_summary);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        ImageView closeImg = dialog.findViewById(R.id.close_btn);
        RecyclerView rvAddress = dialog.findViewById(R.id.rv_address);
        ArrayList<Address> addresses = new ArrayList<Address>();
        Address address = new Address("Home",getString(R.string.sample_address1));
        Address address1 = new Address("Office",getString(R.string.sample_address2));
        Address address2 = new Address("Others",getString(R.string.sample_address1));
        addresses.add(address);
        addresses.add(address1);
        addresses.add(address2);
        AddressDialogAdapter addressDialogAdapter = new AddressDialogAdapter(addresses,getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rvAddress.setLayoutManager(layoutManager);
        rvAddress.setAdapter(addressDialogAdapter);
        closeImg.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

}