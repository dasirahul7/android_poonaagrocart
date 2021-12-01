package com.poona.agrocart.ui.order_summary;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private void initView()
    {
        rvProductsAndPrices=fragmentOrderSummaryBinding.rvProductsAndPrices;
        Typeface font = Typeface.createFromAsset(context.getAssets(), getString(R.string.font_poppins_regular));
        fragmentOrderSummaryBinding.rbCod.setTypeface(font);
        fragmentOrderSummaryBinding.rbOnline.setTypeface(font);
        fragmentOrderSummaryBinding.rbWallet.setTypeface(font);
    }
}