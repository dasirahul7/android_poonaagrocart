package com.poona.agrocart.ui.product_detail;

import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentProductDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.CommonViewModel;

import java.util.ArrayList;
import me.huseyinozer.TooltipIndicator;

public class ProductDetailFragment extends BaseFragment implements View.OnClickListener {

    public int count=0;
    private FragmentProductDetailBinding fragmentProductDetailBinding;
    private ProductDetailViewModel productDetailViewModel;
    private ArrayList<Integer> imgsList;
    public ViewPager vpImages;
    private TooltipIndicator tbIndicator;
    private ProductImagesAdapter productImagesAdapter;
    private boolean isProductDetailsVisible=true,isNutritionDetailsVisible=true;
    private int quantity=1;
    private boolean isFavourite=false;

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
        fragmentProductDetailBinding.ivProductDetailHideShow.setOnClickListener(this);
        fragmentProductDetailBinding.ivNutritionDetailHideShow.setOnClickListener(this);
        fragmentProductDetailBinding.ivPlus.setOnClickListener(this);
        fragmentProductDetailBinding.ivMinus.setOnClickListener(this);
        fragmentProductDetailBinding.ivFavourite.setOnClickListener(this);

        setValues();

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

    private void setValues()
    {
        productDetailViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        fragmentProductDetailBinding.setProductDetailViewModel(productDetailViewModel);
        productDetailViewModel.productName.setValue(getString(R.string.naturel_red_apple));
        productDetailViewModel.weightOfProduct.setValue(getString(R.string._1kg));
        productDetailViewModel.price.setValue(getString(R.string.rs_150));
        productDetailViewModel.productDetailBrief.setValue(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        productDetailViewModel.nutritionDetailBrief.setValue(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
    }

    private void setViewPagerAdapterItems()
    {
        productImagesAdapter = new ProductImagesAdapter(ProductDetailFragment.this, getChildFragmentManager(), imgsList);
        vpImages.setAdapter(productImagesAdapter);
        productImagesAdapter.notifyDataSetChanged();

        vpImages.addOnPageChangeListener(productImagesAdapter);

        tbIndicator.setupViewPager(vpImages);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.iv_product_detail_hide_show:
                hideOrShowProductDetails();
                break;
            case R.id.iv_nutrition_detail_hide_show:
                hideOrShowNutritionDetails();
                break;
            case R.id.iv_minus:
                decreaseQuantity();
                break;
            case R.id.iv_plus:
                increaseQuantity();
                break;
            case R.id.iv_favourite:
                addOrRemoveFromFavourite();
                break;
        }

    }

    private void addOrRemoveFromFavourite()
    {
        if(isFavourite)
        {
            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_heart_without_colour);
        }
        else
        {
            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_red_heart);
        }
        isFavourite=!isFavourite;
    }

    private void increaseQuantity()
    {
        quantity++;
        fragmentProductDetailBinding.etQuantity.setText(String.valueOf(quantity));
    }

    private void decreaseQuantity()
    {
        if(quantity==1)
        {
            infoToast(requireActivity(),getString(R.string.quantity_less_than_one));
        }
        else
        {
            quantity--;
            fragmentProductDetailBinding.etQuantity.setText(String.valueOf(quantity));
        }
    }

    private void hideOrShowNutritionDetails()
    {
        if(isNutritionDetailsVisible)
        {
            fragmentProductDetailBinding.tvNutritionsBrief.setVisibility(View.GONE);
        }
        else
        {
            fragmentProductDetailBinding.tvNutritionsBrief.setVisibility(View.VISIBLE);
        }
        isNutritionDetailsVisible=!isNutritionDetailsVisible;
    }

    private void hideOrShowProductDetails()
    {
        if(isProductDetailsVisible)
        {
            fragmentProductDetailBinding.tvProductDetailBrief.setVisibility(View.GONE);
        }
        else
        {
            fragmentProductDetailBinding.tvProductDetailBrief.setVisibility(View.VISIBLE);
        }
        isProductDetailsVisible=!isProductDetailsVisible;
    }
}