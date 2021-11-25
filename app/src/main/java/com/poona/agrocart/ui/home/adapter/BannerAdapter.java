package com.poona.agrocart.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.poona.agrocart.R;
import com.poona.agrocart.app.Intro;
import com.poona.agrocart.ui.home.model.Banner;
import com.poona.agrocart.ui.intro.IntroItemFragment;
import com.poona.agrocart.ui.intro.IntroScreenFragment;

import java.util.ArrayList;


public class BannerAdapter extends PagerAdapter {

    private ArrayList<Banner> banner;
    private LayoutInflater layoutInflater;
    private Context context;

    public BannerAdapter(ArrayList<Banner> banner,Context context) {
        this.banner = banner;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = this.layoutInflater.inflate(R.layout.row_banner, container, false);
        ImageView imageView = view.findViewById(R.id.imgAd);
        Glide.with(context)
                .load(banner.get(position).getImage())
                .placeholder(R.drawable.banner_img)
                .into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return banner.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object) ;
    }
}
