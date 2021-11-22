package com.poona.agrocart.ui.product_detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poona.agrocart.R;
import com.poona.agrocart.app.Intro;
import com.poona.agrocart.databinding.FragmentIntroScreenItemBinding;
import com.poona.agrocart.databinding.FragmentProductImageBinding;
import com.poona.agrocart.ui.intro.IntroItemFragment;
import com.poona.agrocart.ui.intro.IntroScreenFragment;

import java.util.ArrayList;

import me.huseyinozer.TooltipIndicator;

public class ProductImageFragment extends Fragment {

    private static Context context;
    private static ArrayList<Integer> productImgsList;
    private FragmentProductImageBinding fragmentProductImageBinding;
    private View view;
    private static final String POSITION = "position";
    private ImageView productImg;

    public static ProductImageFragment newInstance(ProductDetailFragment productDetailFragment, int pos, ArrayList<Integer> imgs) {
        context = productDetailFragment.getContext();
        productImgsList = imgs;
        ProductImageFragment fragment = new ProductImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final int position = this.getArguments().getInt(POSITION);
        fragmentProductImageBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_image,container,false);
        fragmentProductImageBinding.setLifecycleOwner(this);
        view = fragmentProductImageBinding.getRoot();
        productImg = fragmentProductImageBinding.ivProduct;
        productImg.setImageResource(productImgsList.get(position));
        return view;
    }
}