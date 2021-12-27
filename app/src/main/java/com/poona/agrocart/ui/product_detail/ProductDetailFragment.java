package com.poona.agrocart.ui.product_detail;

import static com.poona.agrocart.app.AppConstants.PORTRAIT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.databinding.FragmentProductDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.adapter.ProductListAdapter;
import com.poona.agrocart.ui.home.model.Product;
import com.poona.agrocart.ui.product_detail.adapter.BasketContentsAdapter;
import com.poona.agrocart.ui.product_detail.adapter.ProductCommentsAdapter;
import com.poona.agrocart.ui.product_detail.adapter.WeightAdapter;
import com.poona.agrocart.ui.product_detail.model.BasketContent;
import com.poona.agrocart.ui.product_detail.model.ProductComment;
import com.poona.agrocart.ui.product_detail.model.ProductDetail;
import com.poona.agrocart.widgets.CustomEditText;

import java.util.ArrayList;

public class ProductDetailFragment extends BaseFragment implements View.OnClickListener {
    public int count = 0;
    private FragmentProductDetailBinding fragmentProductDetailBinding;
    private ProductDetailViewModel productDetailViewModel;
    public ViewPager vpImages;
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
    private ArrayList<Product> similarProducts;
    private BasketContentsAdapter basketContentsAdapter;
    private ProductDetail details;
    private ProductListAdapter productListAdapter;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentProductDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false);
        fragmentProductDetailBinding.setLifecycleOwner(this);
        root = fragmentProductDetailBinding.getRoot();

        initTitleWithBackBtn("");
        initView();
        setSimilarItems();
        setCommentsAdapter();

        return root;
    }

    private void setSimilarItems() {
        productDetailViewModel.getSimilarProductLiveData().observe(getViewLifecycleOwner(),similarItems -> {
            this.similarProducts = similarItems;
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
            productListAdapter = new ProductListAdapter(this.similarProducts, requireActivity(), PORTRAIT, root);
            fragmentProductDetailBinding.rvSimilar.setNestedScrollingEnabled(false);
            fragmentProductDetailBinding.rvSimilar.setLayoutManager(layoutManager);
            fragmentProductDetailBinding.rvSimilar.setAdapter(productListAdapter);
        });
    }

    private void setCommentsAdapter() {
        commentArrayList = new ArrayList<>();
        productDetailViewModel.getProductCommentMutableLiveData().observe(getViewLifecycleOwner(),productComments -> {
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


        vpImages = fragmentProductDetailBinding.vpProductImages;
        //tbIndicator = fragmentProductDetailBinding.tlIndicators;
        rvProductComment = fragmentProductDetailBinding.rvProductComment;

        setValues();


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
        details = new ProductDetail();
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductName(getArguments() != null ? getArguments().getString("name") : null);
        ArrayList<String> images = new ArrayList<>();
        for (int i=0;i<3;i++)
            images.add(getArguments() != null ? getArguments().getString("image") : null);
       // Set Product Details here
        productDetail.setProductLocation("Pune");
        productDetail.setPrice(getArguments()!=null? getArguments().getString("price"):null);
        productDetail.setOfferPrice("Rs. 200");
        productDetail.setOfferValue("24% Off");
        productDetail.setProductOfferMsg(getString(R.string.product_offer_msg));
        productDetail.setProductImages(images);
        ArrayList<String> weight = new ArrayList<>();
        weight.add("1kg");
        weight.add("250gms");
        weight.add("500gms");
        weight.add("1pc");
        productDetail.setWeightOfProduct(weight);
        productDetail.setProductDetailBrief(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        productDetail.setNutritionDetailBrief(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        productDetailViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        fragmentProductDetailBinding.setProductDetailViewModel(productDetailViewModel);
        productDetailViewModel.productDetailMutableLiveData.setValue(productDetail);
        details = productDetailViewModel.productDetailMutableLiveData.getValue();
        setViewPagerAdapterItems();
        assert details != null;
        WeightAdapter weightAdapter = new WeightAdapter(details.getWeightOfProduct(), getActivity());
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

        count = details.getProductImages().size();
        productImagesAdapter = new ProductImagesAdapter(ProductDetailFragment.this,
                getChildFragmentManager(), details.getProductImages());
        vpImages.setAdapter(productImagesAdapter);
        productImagesAdapter.notifyDataSetChanged();
        vpImages.addOnPageChangeListener(productImagesAdapter);
        //tbIndicator.setupViewPager(vpImages);
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
        AppUtils.setMinusButton(quantity,fragmentProductDetailBinding.ivMinus);
    }

    private void decreaseQuantity(CustomEditText etQuantity) {
        if (quantity == 1) {
            warningToast(requireActivity(), getString(R.string.quantity_less_than_one));
        } else {
            quantity--;
            etQuantity.setText(String.valueOf(quantity));
        }
        AppUtils.setMinusButton(quantity,fragmentProductDetailBinding.ivMinus);
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