package com.poona.agrocart.ui.product_detail;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentProductDetailBinding;
import com.poona.agrocart.ui.intro.IntroPagerAdapter;
import com.poona.agrocart.ui.intro.IntroScreenFragment;

import java.util.ArrayList;

import me.huseyinozer.TooltipIndicator;

public class ProductDetailFragment extends Fragment {

    public int count=0;
    private FragmentProductDetailBinding fragmentProductDetailBinding;
    private ArrayList<Integer> imgsList;
    public ViewPager vpImages;
    private TooltipIndicator tbIndicator;
    private ProductImagesAdapter productImagesAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentProductDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false);
        fragmentProductDetailBinding.setLifecycleOwner(this);
        final View view = fragmentProductDetailBinding.getRoot();

        initView();

        return view;
    }

    private void initView()
    {
        vpImages=fragmentProductDetailBinding.vpProductImages;
        tbIndicator = fragmentProductDetailBinding.tlIndicators;

        imgsList=new ArrayList<>();
        imgsList.add(R.drawable.img_apple);
        imgsList.add(R.drawable.img_apple);
        imgsList.add(R.drawable.img_apple);

        count=imgsList.size();

        if(count>0) {
            setViewPagerAdapterItems();
        }


    }

    private void setViewPagerAdapterItems()
    {
        // set the viewPager
        productImagesAdapter = new ProductImagesAdapter(ProductDetailFragment.this, getChildFragmentManager(), imgsList);
        vpImages.setAdapter(productImagesAdapter);
        productImagesAdapter.notifyDataSetChanged();

        vpImages.addOnPageChangeListener(productImagesAdapter);

        // Set up tab indicators
        tbIndicator.setupViewPager(vpImages);
    }
}