package com.poona.agrocart.ui.basket_product_detail;

import android.content.Context;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentBasketProductImgBinding;
import java.util.ArrayList;

public class BasketProductImgFragment extends Fragment
{
    private static Context context;
    private static ArrayList<Integer> productImgsList;
    private FragmentBasketProductImgBinding fragmentBasketProductImgBinding;
    private View view;
    private static final String POSITION = "position";
    private ImageView productImg;

    public static BasketProductImgFragment newInstance(BasketProductDetailFragment basketProductDetailFragment, int pos, ArrayList<Integer> imgs)
    {
        context = basketProductDetailFragment.getContext();
        productImgsList = imgs;
        BasketProductImgFragment fragment = new BasketProductImgFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final int position = this.getArguments().getInt(POSITION);
        fragmentBasketProductImgBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket_product_img,container,false);
        fragmentBasketProductImgBinding.setLifecycleOwner(this);
        view = fragmentBasketProductImgBinding.getRoot();
        productImg = fragmentBasketProductImgBinding.ivProduct;
        productImg.setImageResource(productImgsList.get(position));
        return view;
    }
}