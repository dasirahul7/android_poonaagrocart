package com.poona.agrocart.ui.order_summary;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentOrderSummaryBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.order_summary.model.ProductAndPrice;
import java.util.ArrayList;

public class OrderSummaryFragment extends BaseFragment
{
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
        ((AppCompatActivity) requireActivity()).findViewById(R.id.ll_bottom_navigation_view).setVisibility(View.VISIBLE);
        setBottomMarginInDps(50);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) requireActivity()).findViewById(R.id.ll_bottom_navigation_view).setVisibility(View.VISIBLE);
        setBottomMarginInDps(50);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) requireActivity()).findViewById(R.id.ll_bottom_navigation_view).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) requireActivity()).findViewById(R.id.ll_bottom_navigation_view).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentOrderSummaryBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_order_summary, container, false);
        fragmentOrderSummaryBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentOrderSummaryBinding).getRoot();

        initTitleBar(getString(R.string.order_summary));
        initView();
        setRvAdapter();

        return view;
    }

    private void setRvAdapter()
    {
        productAndPriceArrayList=new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvProductsAndPrices.setHasFixedSize(true);
        rvProductsAndPrices.setLayoutManager(linearLayoutManager);

        productAndPriceAdapter = new ProductAndPriceAdapter(productAndPriceArrayList);
        rvProductsAndPrices.setAdapter(productAndPriceAdapter);
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 3; i++)
        {
            ProductAndPrice productAndPrice = new ProductAndPrice();
            productAndPrice.setProductName(getString(R.string.bell_pepper_red_1kg));
            productAndPrice.setDividedPrice(getString(R.string.rs_200_x_4));
            productAndPrice.setFinalPrice(getString(R.string.rs_740));
            productAndPriceArrayList.add(productAndPrice);
        }
    }

    private void setBottomMarginInDps(int i)
    {
        int dpAsPixels = (int) (i*scale + 0.5f);/*
        if(i==0)
            navHostMargins.bottomMargin = 0;
        else*/
            navHostMargins.bottomMargin=dpAsPixels;
    }

    private void initView()
    {
        rvProductsAndPrices=fragmentOrderSummaryBinding.rvProductsAndPrices;
        Typeface font = Typeface.createFromAsset(context.getAssets(), getString(R.string.font_poppins_regular));
        fragmentOrderSummaryBinding.rbCod.setTypeface(font);
        fragmentOrderSummaryBinding.rbOnline.setTypeface(font);
        fragmentOrderSummaryBinding.rbWalletBalace.setTypeface(font);

        scale = getResources().getDisplayMetrics().density;

        ((AppCompatActivity) requireActivity()).findViewById(R.id.ll_bottom_navigation_view).setVisibility(View.GONE);

        navHostFragment=((AppCompatActivity) requireActivity()).findViewById(R.id.nav_host_fragment_content_home);
        navHostMargins = (ViewGroup.MarginLayoutParams) navHostFragment.getLayoutParams();
        navHostMargins.bottomMargin = 0;
    }
}