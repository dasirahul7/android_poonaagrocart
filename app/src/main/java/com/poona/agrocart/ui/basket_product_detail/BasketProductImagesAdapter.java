package com.poona.agrocart.ui.basket_product_detail;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;

public class BasketProductImagesAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener
{
    private BasketProductDetailFragment context;
    private FragmentManager fragmentManager;
    private int lastPosition = 0;
    private ArrayList<Integer> imgsList;

    public BasketProductImagesAdapter(BasketProductDetailFragment context, FragmentManager fm, ArrayList<Integer> imgsList)
    {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.imgsList = imgsList;
    }

    @Override
    public Fragment getItem(int position)
    {
        return BasketProductImgFragment.newInstance(context, position,imgsList);
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
