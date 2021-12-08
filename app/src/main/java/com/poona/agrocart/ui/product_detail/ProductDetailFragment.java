package com.poona.agrocart.ui.product_detail;

import android.annotation.SuppressLint;
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
import com.poona.agrocart.databinding.FragmentProductDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.product_detail.adapter.BasketContentsAdapter;
import com.poona.agrocart.ui.product_detail.adapter.ProductCommentsAdapter;
import com.poona.agrocart.ui.product_detail.adapter.WeightAdapter;
import com.poona.agrocart.ui.product_detail.model.BasketContent;
import com.poona.agrocart.ui.product_detail.model.ProductComment;
import com.poona.agrocart.widgets.CustomEditText;

import java.util.ArrayList;

import me.huseyinozer.TooltipIndicator;

public class ProductDetailFragment extends BaseFragment implements View.OnClickListener {
    public int count = 0;
    private FragmentProductDetailBinding fragmentProductDetailBinding;
    private ProductDetailViewModel productDetailViewModel;
    private ArrayList<Integer> imgsList;
    public ViewPager vpImages;
    private TooltipIndicator tbIndicator;
    private ProductImagesAdapter productImagesAdapter;
    private boolean isProductDetailsVisible = true, isNutritionDetailsVisible = true, isAboutThisProductVisible = true,
            isBasketContentsVisible = true, isBenefitsVisible = true, isStorageVisible = true, isOtherProductInfo = true,
            isVariableWtPolicyVisible = true, isFavourite = false;
    private int quantity = 1;
    private RecyclerView rvProductComment;
    private LinearLayoutManager linearLayoutManager;
    private ProductCommentsAdapter productCommentsAdapter;
    private ArrayList<ProductComment> commentArrayList;
    private ArrayList<BasketContent> basketContentArrayList;
    private BasketContentsAdapter basketContentsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentProductDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false);
        fragmentProductDetailBinding.setLifecycleOwner(this);
        final View view = fragmentProductDetailBinding.getRoot();

        initTitleWithBackBtn("");
        initView();
        setRvAdapter();

        return view;
    }

    private void setRvAdapter() {
        commentArrayList = new ArrayList<>();
        prepareCommentsListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvProductComment.setHasFixedSize(true);
        rvProductComment.setLayoutManager(linearLayoutManager);
        rvProductComment.setNestedScrollingEnabled(false);
        productCommentsAdapter = new ProductCommentsAdapter(commentArrayList, false);
        rvProductComment.setAdapter(productCommentsAdapter);
    }

    private void prepareCommentsListingData() {
        for (int i = 0; i < 5; i++) {
            ProductComment comment = new ProductComment();
            comment.setUserImg("https://www.linkpicture.com/q/pngfuel-1-1.png");
//            comment.setUserImg("https://ibb.co/YXPmn4v");
            comment.setUserName(getString(R.string.johnson_doe));
            comment.setDate(getString(R.string._12_jan_2021));
            comment.setRating(2.5f);
            comment.setComment(getString(R.string.lorem_ipsum_dolor_sit_amet_consectetur_adipiscing));
            commentArrayList.add(comment);
        }
    }

    private void initView() {
        try {
            if (getArguments() != null) {
                if (getArguments().get("Product").equals("Basket")) {
                    setProductList();
                    makeItBasketDetails();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentProductDetailBinding.llProductDetails.setOnClickListener(this);
        fragmentProductDetailBinding.llNutritions.setOnClickListener(this);
        fragmentProductDetailBinding.llAboutThisProduct.setOnClickListener(this);
        fragmentProductDetailBinding.llBenefits.setOnClickListener(this);
        fragmentProductDetailBinding.llStorageAndUse.setOnClickListener(this);
        fragmentProductDetailBinding.llOtherProductInfo.setOnClickListener(this);
        fragmentProductDetailBinding.llVariableWeightPolicy.setOnClickListener(this);
        fragmentProductDetailBinding.ivPlus.setOnClickListener(this);
        fragmentProductDetailBinding.ivMinus.setOnClickListener(this);
        fragmentProductDetailBinding.ivFavourite.setOnClickListener(this);
        // Basket Product views
        fragmentProductDetailBinding.layoutAdded.llProductList.setOnClickListener(this);
        fragmentProductDetailBinding.layoutAdded.imgPlus.setOnClickListener(this);
        fragmentProductDetailBinding.layoutAdded.imgMinus.setOnClickListener(this);

        setValues();

        vpImages = fragmentProductDetailBinding.vpProductImages;
        tbIndicator = fragmentProductDetailBinding.tlIndicators;
        rvProductComment = fragmentProductDetailBinding.rvProductComment;

        imgsList = new ArrayList<>();
        imgsList.add(R.drawable.img_apple);
        imgsList.add(R.drawable.img_apple);
        imgsList.add(R.drawable.img_apple);

        count = imgsList.size();

        if (count > 0) {
            setViewPagerAdapterItems();
        }
    }

    private void setProductList() {
        basketContentArrayList = new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        fragmentProductDetailBinding.layoutAdded.rvBasketContents.setHasFixedSize(true);
        fragmentProductDetailBinding.layoutAdded.rvBasketContents.setLayoutManager(linearLayoutManager);

        basketContentsAdapter = new BasketContentsAdapter(basketContentArrayList);
        fragmentProductDetailBinding.layoutAdded.rvBasketContents.setAdapter(basketContentsAdapter);
    }

    private void prepareListingData() {
        for (int i = 0; i < 4; i++) {
            BasketContent basketContent = new BasketContent();
            basketContent.setContentName(getString(R.string.moong));
            basketContent.setContentWeight(getString(R.string._500gm));
            basketContentArrayList.add(basketContent);
        }
    }

    private void makeItBasketDetails() {
        hideOrShowProductDetails();
        fragmentProductDetailBinding.txtOrganic.setVisibility(View.INVISIBLE);
        fragmentProductDetailBinding.editLayout.setVisibility(View.GONE);
        fragmentProductDetailBinding.inSubscription.setVisibility(View.VISIBLE);
    }

    private void setValues() {
        ArrayList<String> weight = new ArrayList<>();
        weight.add("1kg");
        weight.add("250gms");
        weight.add("500gms");
        weight.add("1pc");
        productDetailViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        fragmentProductDetailBinding.setProductDetailViewModel(productDetailViewModel);
        productDetailViewModel.productName.setValue(getString(R.string.naturel_red_apple));
        productDetailViewModel.productLocation.setValue("Vishrantwadi");
        productDetailViewModel.weightOfProduct.setValue(weight);
        productDetailViewModel.price.setValue(getString(R.string.rs_150));
        productDetailViewModel.productDetailBrief.setValue(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        productDetailViewModel.nutritionDetailBrief.setValue(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));

        WeightAdapter weightAdapter = new WeightAdapter(weight, getActivity());
        fragmentProductDetailBinding.rvWeights.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fragmentProductDetailBinding.rvWeights.setHasFixedSize(true);
        fragmentProductDetailBinding.rvWeights.setAdapter(weightAdapter);

        //hide all expanded views initially

        hideOrShowAboutThisProduct();
        hideOrShowBenefits();
        hideOrShowStorageAndUses();
        hideOrShowOtherProductInfo();
        hideOrShowVariableWeightPolicy();
        hideOrShowNutritionDetails();
    }

    private void setViewPagerAdapterItems() {
        productImagesAdapter = new ProductImagesAdapter(ProductDetailFragment.this, getChildFragmentManager(), imgsList);
        vpImages.setAdapter(productImagesAdapter);
        productImagesAdapter.notifyDataSetChanged();

        vpImages.addOnPageChangeListener(productImagesAdapter);

        tbIndicator.setupViewPager(vpImages);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_product_list:
                hideOrShowBasketContentsList();
                break;
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
            case R.id.iv_minus:
                decreaseQuantity(fragmentProductDetailBinding.etQuantity);
                break;
            case R.id.iv_plus:
                increaseQuantity(fragmentProductDetailBinding.etQuantity);
                break;
            case R.id.img_plus:
                increaseQuantity(fragmentProductDetailBinding.layoutAdded.imgQuantity);
                break;
            case R.id.img_minus:
                decreaseQuantity(fragmentProductDetailBinding.layoutAdded.imgQuantity);
                break;
            case R.id.iv_favourite:
                addOrRemoveFromFavourite();
                break;
        }

    }


    private void hideOrShowVariableWeightPolicy() {
        if (isVariableWtPolicyVisible) {
            new ProductDetailFragment().collapse(fragmentProductDetailBinding.tvVariableWeightPolicyBreif);
        } else {
            new ProductDetailFragment().expand(fragmentProductDetailBinding.tvVariableWeightPolicyBreif);
        }
        isVariableWtPolicyVisible = !isVariableWtPolicyVisible;
    }

    private void hideOrShowOtherProductInfo() {
        if (isOtherProductInfo) {
            new ProductDetailFragment().collapse(fragmentProductDetailBinding.tvOtherProductInfoBrief);
        } else {
            new ProductDetailFragment().expand(fragmentProductDetailBinding.tvOtherProductInfoBrief);
        }
        isOtherProductInfo = !isOtherProductInfo;
    }

    private void hideOrShowStorageAndUses() {
        if (isStorageVisible) {
            new ProductDetailFragment().collapse(fragmentProductDetailBinding.tvStorageAndUseBrief);
        } else {
            new ProductDetailFragment().expand(fragmentProductDetailBinding.tvStorageAndUseBrief);
        }
        isStorageVisible = !isStorageVisible;
    }

    private void hideOrShowBenefits() {
        if (isBenefitsVisible) {
            new ProductDetailFragment().collapse(fragmentProductDetailBinding.tvBenefitsBrief);
        } else {
            new ProductDetailFragment().expand(fragmentProductDetailBinding.tvBenefitsBrief);
        }
        isBenefitsVisible = !isBenefitsVisible;
    }

    private void hideOrShowAboutThisProduct() {
        if (isAboutThisProductVisible) {
            new ProductDetailFragment().collapse(fragmentProductDetailBinding.tvAboutThisProductBrief);
        } else {
            new ProductDetailFragment().expand(fragmentProductDetailBinding.tvAboutThisProductBrief);
        }
        isAboutThisProductVisible = !isAboutThisProductVisible;
    }

    private void addOrRemoveFromFavourite() {
        if (isFavourite) {
            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_heart_without_colour);
        } else {
            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_red_heart);
        }
        isFavourite = !isFavourite;
    }

    private void increaseQuantity(CustomEditText etQuantity) {
        quantity++;
        etQuantity.setText(String.valueOf(quantity));
    }

    private void decreaseQuantity(CustomEditText etQuantity) {
        if (quantity == 1) {
            errorToast(requireActivity(), getString(R.string.quantity_less_than_one));
        } else {
            quantity--;
            etQuantity.setText(String.valueOf(quantity));
        }
    }

    private void hideOrShowNutritionDetails() {
        if (isNutritionDetailsVisible) {
            new ProductDetailFragment().collapse(fragmentProductDetailBinding.tvNutritionsBrief);
        } else {
            new ProductDetailFragment().expand(fragmentProductDetailBinding.tvNutritionsBrief);
        }
        isNutritionDetailsVisible = !isNutritionDetailsVisible;
    }

    private void hideOrShowProductDetails() {
        if (isProductDetailsVisible) {
            new ProductDetailFragment().collapse(fragmentProductDetailBinding.tvProductDetailBrief);
        } else {
            new ProductDetailFragment().expand(fragmentProductDetailBinding.tvProductDetailBrief);
        }
        isProductDetailsVisible = !isProductDetailsVisible;
    }

    private void hideOrShowBasketContentsList() {
        if (isBasketContentsVisible) {
            new ProductDetailFragment().collapse(fragmentProductDetailBinding.layoutAdded.rvBasketContents);
        } else {
            new ProductDetailFragment().expand(fragmentProductDetailBinding.layoutAdded.rvBasketContents);
        }
        isBasketContentsVisible = !isBasketContentsVisible;
    }


}