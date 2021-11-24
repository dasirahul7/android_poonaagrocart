package com.poona.agrocart.ui.product_detail;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;

public class ProductImagesAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener
{
    private ProductDetailFragment context;
    private FragmentManager fragmentManager;
    private int lastPosition = 0;
    private ArrayList<Integer> imgsList;

    public ProductImagesAdapter(ProductDetailFragment context, FragmentManager fm, ArrayList<Integer> imgsList)
    {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.imgsList = imgsList;
    }

    @Override
    public Fragment getItem(int position)
    {
        return ProductImageFragment.newInstance(context, position,imgsList);
    }

    @Override
    public int getCount()
    {
        return context.count;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position)
    {
        lastPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) { }
}
