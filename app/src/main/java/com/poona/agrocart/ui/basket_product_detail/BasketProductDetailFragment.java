package com.poona.agrocart.ui.basket_product_detail;

import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentBasketProductDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.basket_product_detail.view_model.BasketProductViewModel;
import com.poona.agrocart.ui.product_detail.ProductDetailFragment;
import java.util.ArrayList;
import me.huseyinozer.TooltipIndicator;

public class BasketProductDetailFragment extends BaseFragment implements View.OnClickListener
{
    public int count=0,quantity=1;
    private boolean isProductDetailsVisible=true,isNutritionDetailsVisible=true,
            isBasketContentsVisible=true,isFavourite=false,isAboutThisProductVisible=true,
            isBenefitsVisible=true,isStorageVisible=true,isOtherProductInfo=true,
            isVariableWtPolicyVisible=true;
    private FragmentBasketProductDetailBinding fragmentBasketProductDetailBinding;
    private BasketProductImagesAdapter productImagesAdapter;  //for images
    private BasketContentsAdapter basketContentsAdapter;     //for basket contents like various n no. of sprouts
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rvBasketContent;

    private ArrayList<Integer> imgsList;
    private ArrayList<BasketContent> basketContentArrayList;

    public ViewPager vpImages;
    private TooltipIndicator tbIndicator;
    private BasketProductViewModel basketProductViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentBasketProductDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket_product_detail, container, false);
        fragmentBasketProductDetailBinding.setLifecycleOwner(this);
        final View view = fragmentBasketProductDetailBinding.getRoot();

        initView();

        return view;
    }

    private void initView()
    {
        initTitleBar("");

        fragmentBasketProductDetailBinding.llProductDetails.setOnClickListener(this);
        fragmentBasketProductDetailBinding.llNutritions.setOnClickListener(this);
        fragmentBasketProductDetailBinding.llProductList.setOnClickListener(this);
        fragmentBasketProductDetailBinding.llAboutThisProduct.setOnClickListener(this);
        fragmentBasketProductDetailBinding.llBenefits.setOnClickListener(this);
        fragmentBasketProductDetailBinding.llStorageAndUse.setOnClickListener(this);
        fragmentBasketProductDetailBinding.llOtherProductInfo.setOnClickListener(this);
        fragmentBasketProductDetailBinding.llVariableWeightPolicy.setOnClickListener(this);
        fragmentBasketProductDetailBinding.ivPlus.setOnClickListener(this);
        fragmentBasketProductDetailBinding.ivMinus.setOnClickListener(this);
        fragmentBasketProductDetailBinding.ivFavourite.setOnClickListener(this);

        vpImages=fragmentBasketProductDetailBinding.vpProductImages;
        tbIndicator = fragmentBasketProductDetailBinding.tlIndicators;
        rvBasketContent=fragmentBasketProductDetailBinding.rvBasketContents;

        imgsList=new ArrayList<>();
        imgsList.add(R.drawable.img_basket);
        imgsList.add(R.drawable.img_basket);
        imgsList.add(R.drawable.img_basket);

        count=imgsList.size();

        if(count>0)
        {
            setViewPagerAdapterItems();
        }

        setRvAdapter();
        setValues();
    }

    private void setRvAdapter()
    {
        basketContentArrayList=new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvBasketContent.setHasFixedSize(true);
        rvBasketContent.setLayoutManager(linearLayoutManager);

        basketContentsAdapter = new BasketContentsAdapter(basketContentArrayList);
        rvBasketContent.setAdapter(basketContentsAdapter);
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 4; i++)
        {
            BasketContent basketContent = new BasketContent();
            basketContent.setContentName(getString(R.string.moong));
            basketContent.setContentWeight(getString(R.string._500gm));
            basketContentArrayList.add(basketContent);
        }
    }

    private void setValues()
    {
        basketProductViewModel = new ViewModelProvider(this).get(BasketProductViewModel.class);
        fragmentBasketProductDetailBinding.setBasketProductViewModel(basketProductViewModel);
        basketProductViewModel.productName.setValue(getString(R.string.sprouts_basket));
        basketProductViewModel.weightOfProduct.setValue(getString(R.string._1kg));
        basketProductViewModel.price.setValue(getString(R.string.rs_150));
        basketProductViewModel.productDetailBrief.setValue(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        basketProductViewModel.nutritionDetailBrief.setValue(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
    }

    private void setViewPagerAdapterItems()
    {
        productImagesAdapter = new BasketProductImagesAdapter(BasketProductDetailFragment.this, getChildFragmentManager(), imgsList);
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
            case R.id.ll_product_details:
                hideOrShowProductDetails();
                break;
            case R.id.ll_about_this_product:
                hideOrShowAboutThisProduct();
                break;
            case R.id.ll_benefits:
                hideOrShowBenefits();
                break;
            case R.id.ll_storage_and_use:
                hideOrShowStorageAndUses();
                break;
            case R.id.ll_other_product_info:
                hideOrShowOtherProductInfo();
                break;
            case R.id.ll_variable_weight_policy:
                hideOrShowVariableWeightPolicy();
                break;
            case R.id.ll_nutritions:
                hideOrShowNutritionDetails();
                break;
            case R.id.ll_product_list:
                hideOrShowBasketContentsList();
                break;
            case R.id.iv_plus:
                increaseQuantity();
                break;
            case R.id.iv_minus:
                decreaseQuantity();
                break;
            case R.id.iv_favourite:
                addOrRemoveFromFavourite();
                break;
        }
    }


    private void hideOrShowVariableWeightPolicy()
    {
        if(isVariableWtPolicyVisible)
        {
            new ProductDetailFragment().collapse(fragmentBasketProductDetailBinding.tvVariableWeightPolicyBreif);
        }
        else
        {
            new ProductDetailFragment().expand(fragmentBasketProductDetailBinding.tvVariableWeightPolicyBreif);
        }
        isVariableWtPolicyVisible=!isVariableWtPolicyVisible;
    }

    private void hideOrShowOtherProductInfo()
    {
        if(isOtherProductInfo)
        {
            new ProductDetailFragment().collapse(fragmentBasketProductDetailBinding.tvOtherProductInfoBrief);
        }
        else
        {
            new ProductDetailFragment().expand(fragmentBasketProductDetailBinding.tvOtherProductInfoBrief);
        }
        isOtherProductInfo=!isOtherProductInfo;
    }

    private void hideOrShowStorageAndUses()
    {
        if(isStorageVisible)
        {
            new ProductDetailFragment().collapse(fragmentBasketProductDetailBinding.tvStorageAndUseBrief);
        }
        else
        {
            new ProductDetailFragment().expand(fragmentBasketProductDetailBinding.tvStorageAndUseBrief);
        }
        isStorageVisible=!isStorageVisible;
    }

    private void hideOrShowBenefits()
    {
        if(isBenefitsVisible)
        {
            new ProductDetailFragment().collapse(fragmentBasketProductDetailBinding.tvBenefitsBrief);
        }
        else
        {
            new ProductDetailFragment().expand(fragmentBasketProductDetailBinding.tvBenefitsBrief);
        }
        isBenefitsVisible=!isBenefitsVisible;
    }

    private void hideOrShowAboutThisProduct()
    {
        if(isAboutThisProductVisible)
        {
            new ProductDetailFragment().collapse(fragmentBasketProductDetailBinding.tvAboutThisProductBrief);
        }
        else
        {
            new ProductDetailFragment().expand(fragmentBasketProductDetailBinding.tvAboutThisProductBrief);
        }
        isAboutThisProductVisible=!isAboutThisProductVisible;
    }

    private void addOrRemoveFromFavourite()
    {
        if(isFavourite)
        {
            fragmentBasketProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_heart_without_colour);
        }
        else
        {
            fragmentBasketProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_red_heart);
        }
        isFavourite=!isFavourite;
    }

    private void increaseQuantity()
    {
        quantity++;
        fragmentBasketProductDetailBinding.etQuantity.setText(String.valueOf(quantity));
    }

    private void decreaseQuantity()
    {
        if(quantity==1)
        {
            errorToast(requireActivity(),getString(R.string.quantity_less_than_one));
        }
        else
        {
            quantity--;
            fragmentBasketProductDetailBinding.etQuantity.setText(String.valueOf(quantity));
        }
    }

    private void hideOrShowBasketContentsList()
    {
        if(isBasketContentsVisible)
        {
            new ProductDetailFragment().collapse(fragmentBasketProductDetailBinding.rvBasketContents);
            fragmentBasketProductDetailBinding.line5.setVisibility(View.INVISIBLE);
        }
        else
        {
            new ProductDetailFragment().expand(fragmentBasketProductDetailBinding.rvBasketContents);
            fragmentBasketProductDetailBinding.line5.setVisibility(View.VISIBLE);
        }
        isBasketContentsVisible=!isBasketContentsVisible;
    }

    private void hideOrShowNutritionDetails()
    {
        if(isNutritionDetailsVisible)
        {
            new ProductDetailFragment().collapse(fragmentBasketProductDetailBinding.tvNutritionsBrief);
        }
        else
        {
            new ProductDetailFragment().expand(fragmentBasketProductDetailBinding.tvNutritionsBrief);
        }
        isNutritionDetailsVisible=!isNutritionDetailsVisible;
    }

    private void hideOrShowProductDetails()
    {
        if(isProductDetailsVisible)
        {
            new ProductDetailFragment().collapse(fragmentBasketProductDetailBinding.tvProductDetailBrief);
        }
        else
        {
            new ProductDetailFragment().expand(fragmentBasketProductDetailBinding.tvProductDetailBrief);
        }
        isProductDetailsVisible=!isProductDetailsVisible;
    }
}