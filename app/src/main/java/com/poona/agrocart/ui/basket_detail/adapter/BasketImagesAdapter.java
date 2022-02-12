package com.poona.agrocart.ui.basket_detail.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.poona.agrocart.ui.basket_detail.BasketDetailFragment;
import com.poona.agrocart.ui.product_detail.ProductDetailFragment;
import com.poona.agrocart.ui.product_detail.ProductImageFragment;

import java.util.ArrayList;

public class BasketImagesAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener
{
    private final BasketDetailFragment context;
    private final FragmentManager fragmentManager;
    private int lastPosition = 0;
    private final ArrayList<String> imgsList;

    public BasketImagesAdapter(BasketDetailFragment context,
                               FragmentManager fm, ArrayList<String> imgsList)
    {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.imgsList = imgsList;
    }

    @Override
    public Fragment getItem(int position)
    {
        return BasketImageFragment.newInstance(context, position,imgsList);
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
