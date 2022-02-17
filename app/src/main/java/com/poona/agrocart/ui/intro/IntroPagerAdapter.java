package com.poona.agrocart.ui.intro;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.IntroScreenResponse;

import java.util.ArrayList;


public class IntroPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private final IntroScreenFragment context;

    private final FragmentManager fragmentManager;
    private int lastPosition = 0;
    private final ArrayList<IntroScreenResponse.Intro> intros;

    public interface OnChangeButtonCaptionListener {
        void onAddText(int position);
    }

    public OnChangeButtonCaptionListener onChangeButtonCaptionListener;

    public IntroPagerAdapter(IntroScreenFragment context, FragmentManager fm,
                             OnChangeButtonCaptionListener onChangeButtonCaptionListener, ArrayList<IntroScreenResponse.Intro> introList) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
        this.onChangeButtonCaptionListener = onChangeButtonCaptionListener;
        this.intros = introList;
    }

    @Override
    public Fragment getItem(int position) {
        return IntroItemFragment.newInstance(context, position,intros);
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

            onChangeButtonCaptionListener.onAddText(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @SuppressWarnings("ConstantConditions")
    public LinearLayout getRootView(int position) {

        LinearLayout carouselLinearLayout = fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.itemLayout);

        return carouselLinearLayout;
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + context.vpIntro.getId() + ":" + position;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
