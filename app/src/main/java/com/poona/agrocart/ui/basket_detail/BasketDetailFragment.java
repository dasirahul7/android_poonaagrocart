package com.poona.agrocart.ui.basket_detail;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentBasketDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.basket_detail.adapter.BasketImagesAdapter;
import com.poona.agrocart.ui.basket_detail.model.BasketDetail;
import com.poona.agrocart.ui.basket_detail.model.ProductItem;
import com.poona.agrocart.ui.basket_detail.model.Subscription;
import com.poona.agrocart.ui.product_detail.adapter.ProductCommentsAdapter;
import com.poona.agrocart.ui.product_detail.model.ProductComment;

import java.util.ArrayList;

public class BasketDetailFragment extends BaseFragment implements View.OnClickListener {

    private BasketDetailViewModel basketDetailViewModel;
    private FragmentBasketDetailBinding basketDetailsBinding;
    private View rootView;
    public ViewPager vpImages;
    private RecyclerView rvProductComment;
    private BasketDetail details;
    public int count = 0;
    private BasketImagesAdapter basketImagesAdapter;
    private ArrayList<ProductComment> commentArrayList;
    private LinearLayoutManager linearLayoutManager;
    private ProductCommentsAdapter productCommentsAdapter;

    public static BasketDetailFragment newInstance() {
        return new BasketDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        basketDetailViewModel = new ViewModelProvider(this).get(BasketDetailViewModel.class);
        basketDetailsBinding = FragmentBasketDetailBinding.inflate(LayoutInflater.from(context));
        rootView = basketDetailsBinding.getRoot();
        initTitleWithBackBtn("");
        initView();
        setCommentsAdapter();
        return rootView;
    }

    private void setCommentsAdapter() {
        commentArrayList = new ArrayList<>();
        basketDetailViewModel.getBasketComments().observe(getViewLifecycleOwner(), productComments -> {
            commentArrayList = productComments;
            linearLayoutManager = new LinearLayoutManager(requireContext());
            rvProductComment.setHasFixedSize(true);
            rvProductComment.setLayoutManager(linearLayoutManager);
            rvProductComment.setNestedScrollingEnabled(false);
            productCommentsAdapter = new ProductCommentsAdapter(commentArrayList, false);
            rvProductComment.setAdapter(productCommentsAdapter);
        });
    }

    private void initView() {
        basketDetailsBinding.llProductDetails.setOnClickListener(this);
        basketDetailsBinding.llNutritions.setOnClickListener(this);
        basketDetailsBinding.llAboutThisProduct.setOnClickListener(this);
        basketDetailsBinding.llBenefits.setOnClickListener(this);
        basketDetailsBinding.llStorageAndUse.setOnClickListener(this);
        basketDetailsBinding.llOtherProductInfo.setOnClickListener(this);
        basketDetailsBinding.llVariableWeightPolicy.setOnClickListener(this);
        basketDetailsBinding.ivPlus.setOnClickListener(this);
        basketDetailsBinding.ivMinus.setOnClickListener(this);
        basketDetailsBinding.ivFavourite.setOnClickListener(this);
        // BasketDetail ProductOld views
        basketDetailsBinding.layoutAdded.llProductList.setOnClickListener(this);
        basketDetailsBinding.layoutAdded.imgPlus.setOnClickListener(this);
        basketDetailsBinding.layoutAdded.imgMinus.setOnClickListener(this);


        vpImages = basketDetailsBinding.vpProductImages;
        //tbIndicator = fragmentProductDetailBinding.tlIndicators;
        rvProductComment = basketDetailsBinding.rvProductComment;

        setValues();


    }

    private void setValues() {
        details = new BasketDetail();
        BasketDetail basketDetail = new BasketDetail();
        basketDetail.setBasketName(getArguments() != null ? getArguments().getString("name") : null);
        ArrayList<String> images = new ArrayList<>();
        for (int i=0;i<3;i++)
            images.add(getArguments() != null ? getArguments().getString("image") : null);
        // Set ProductOld Details here
        basketDetail.setBasketPrice("Rs. 200");
        basketDetail.setBasketImages(images);
        // Add Subscription Details
        ArrayList<String> subType = new ArrayList<>();
        subType.add("Daily");
        subType.add("Weekly");
        subType.add("Monthly");
        Subscription subscription = new Subscription("Rs. 1500","per basket","Special rate for subscription",subType,"5","15 Dec 2021","Rs. 1400 X 5 ","Rs. 7000");
        basketDetail.setSubscription(subscription);
        //Add ProductOld Items list
        ProductItem item = new ProductItem("Moong","500gm");
        ProductItem item1 = new ProductItem("Matki","250gm");
        ProductItem item2 = new ProductItem("Beans sprouts","500gm");
        ProductItem item3 = new ProductItem("Brocoli sprouts","250gm");
        ProductItem item4 = new ProductItem("Lentils sprouts","500gm");
        ProductItem item5 = new ProductItem("Redish sprouts","250gm");
        ArrayList<ProductItem> productItems = new ArrayList<>();
        productItems.add(item);
        productItems.add(item1);
        productItems.add(item2);
        productItems.add(item3);
        productItems.add(item4);
        productItems.add(item5);
        basketDetail.setProductList(productItems);
        basketDetail.setBasketDetails(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        basketDetail.setAboutBasket(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        basketDetail.setBasketBenefit(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        basketDetail.setBasketUses(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        basketDetail.setBasketInfo(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        basketDetail.setBasketWeightPolicy(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        basketDetail.setBasketNutrition(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        basketDetail.setBasketRating("5.0");
        basketDetail.setBasketNoOfRatings("15");
        basketDetailViewModel.basketData.setValue(basketDetail);
        details = basketDetailViewModel.basketData.getValue();
        basketDetailsBinding.setModuleBasket(basketDetailViewModel);
        setBasketImages();

        //hide all expanded views initially

//        hideOrShowAboutThisProduct();
//        hideOrShowBenefits();
//        hideOrShowStorageAndUses();
//        hideOrShowOtherProductInfo();
//        hideOrShowVariableWeightPolicy();
//        hideOrShowNutritionDetails();

    }

    private void setBasketImages() {
        count = details.getBasketImages().size();
        basketImagesAdapter = new BasketImagesAdapter(BasketDetailFragment.this,
                getChildFragmentManager(), details.getBasketImages());
        vpImages.setAdapter(basketImagesAdapter);
        basketImagesAdapter.notifyDataSetChanged();
        vpImages.addOnPageChangeListener(basketImagesAdapter);
    }


    @Override
    public void onClick(View v) {

    }
}