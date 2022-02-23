package com.poona.agrocart.ui.product_detail.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.poona.agrocart.ui.product_detail.ProductDetailFragment;
import com.poona.agrocart.ui.product_detail.ProductImageFragment;
import com.poona.agrocart.ui.seasonal.SeasonalRegFragment;

import java.util.ArrayList;

public class ProductImagesAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private final FragmentManager fragmentManager;
    private final ArrayList<String> imgsList;
    private ProductDetailFragment context;
    private SeasonalRegFragment fragment;
    private int lastPosition = 0;
    private int imageType = 0;


    public ProductImagesAdapter(ProductDetailFragment context, FragmentManager fm, ArrayList<String> imgsList) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.imgsList = imgsList;
    }

    public ProductImagesAdapter(SeasonalRegFragment context, FragmentManager fragmentManager,
                                ArrayList<String> imgsList, int imageType) {
        super(fragmentManager);
        this.fragment = context;
        this.fragmentManager = fragmentManager;
        this.imgsList = imgsList;
        this.imageType = imageType;
    }


    @Override
    public Fragment getItem(int position) {
        System.out.println("imageType: "+imageType);
        if (imageType == 1)
            return ProductImageFragment.newInstance(fragment, position, imgsList);
        else return ProductImageFragment.newInstance(context, position, imgsList);
    }

    @Override
    public int getCount() {
        if (imageType == 1)
            return fragment.count;
        else return context.count;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        lastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
