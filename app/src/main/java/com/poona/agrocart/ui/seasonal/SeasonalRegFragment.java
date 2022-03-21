package com.poona.agrocart.ui.seasonal;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.SeasonalProductResponse;
import com.poona.agrocart.data.network.responses.homeResponse.SeasonalProduct;
import com.poona.agrocart.databinding.FragmentSeasonalRegBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.product_detail.ProductDetailFragment;
import com.poona.agrocart.ui.product_detail.adapter.ProductImagesAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeasonalRegFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeasonalRegFragment extends BaseFragment {

    private static final String IMAGE = "image";
    public int count = 0;
    public ViewPager vpImages;
    private FragmentSeasonalRegBinding fragmentSeasonalRegBinding;
    private SeasonalViewModel seasonalViewModel;
    private View seasonRoot;
    private SeasonalProduct seasonalProduct;
    private ProductImagesAdapter productImagesAdapter;
    private DotsIndicator dotsIndicator;
    private String image;
    private ArrayList<String> images;
    private Button btnRegister;
    public static SeasonalRegFragment newInstance(String param1, String param2) {
        SeasonalRegFragment fragment = new SeasonalRegFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getString(IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSeasonalRegBinding = FragmentSeasonalRegBinding.inflate(getLayoutInflater());
        seasonalViewModel = new ViewModelProvider(this).get(SeasonalViewModel.class);
        seasonRoot = fragmentSeasonalRegBinding.getRoot();
        initView();


        btnRegister.setOnClickListener(view -> {
            if (isConnectingToInternet(context)){
                callSeasonalProductRegistrationAPI(showCircleProgressDialog(context,""));
            }else showNotifyAlert(requireActivity(), context.getString(R.string.info),
                    context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        });
        return seasonRoot;
    }

    /*Call Seasonal Registration API*/
    private void callSeasonalProductRegistrationAPI(ProgressDialog showCircleProgressDialog) {

    }

    private void initView() {
        fragmentSeasonalRegBinding.etMobileNo.setHint(preferences.getUserMobile());
        fragmentSeasonalRegBinding.etAddress.setHint(preferences.getUserAddress());
        vpImages = fragmentSeasonalRegBinding.vpProductImages;
        dotsIndicator = fragmentSeasonalRegBinding.dotsIndicator;
        btnRegister = fragmentSeasonalRegBinding.btnRegister;
        initTitleWithBackBtn("Seasonal Product Registration");
       images = new ArrayList<>();
        seasonalProduct = new SeasonalProduct();
//        for (int i = 0; i < 3; i++)
//            images.add(getArguments() != null ? image: null);
//        seasonalProduct.setSeasProductImages(images);
//        setViewPagerAdapterItems();
    }

    /*Set images here*/
    private void setViewPagerAdapterItems() {

        count = images.size();
        if (count>0){
            productImagesAdapter = new ProductImagesAdapter(SeasonalRegFragment.this,
                    getChildFragmentManager(), images,1);
            vpImages.setAdapter(productImagesAdapter);
            productImagesAdapter.notifyDataSetChanged();
            vpImages.addOnPageChangeListener(productImagesAdapter);
            dotsIndicator.setViewPager(vpImages);
        }
    }

}