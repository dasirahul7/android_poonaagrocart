package com.poona.agrocart.ui.product_detail;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.poona.agrocart.R;
import java.util.ArrayList;

public class ProductImagesAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener
{
    private ProductDetailFragment context;
    private FragmentManager fragmentManager;
    private int lastPosition = 0;
    private ArrayList<Integer> imgsList;

    public ProductImagesAdapter(ProductDetailFragment context, FragmentManager fm, ArrayList<Integer> imgsList) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.imgsList = imgsList;
    }

    @Override
    public Fragment getItem(int position) {
        return ProductImageFragment.newInstance(context, position,imgsList);
    }

    @Override
    public int getCount() {
        return context.count;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (lastPosition > position) {
            System.out.println("Scrolled Left");
        }else if (lastPosition < position) {
            System.out.println("Scrolled  Right");
        }
        lastPosition = position;

        System.out.println("Last Position: "+lastPosition);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    /*@SuppressWarnings("ConstantConditions")
    public LinearLayout getRootView(int position) {
        LinearLayout carouselLinearLayout = (LinearLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.itemLayout);

        return carouselLinearLayout;
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + context.vpImages.getId() + ":" + position;
    }*/
}
