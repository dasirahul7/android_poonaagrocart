package com.poona.agrocart.ui.product_detail;

import static com.poona.agrocart.app.AppConstants.PRODUCT_ID;
import static com.poona.agrocart.app.AppConstants.PU_ID;
import static com.poona.agrocart.app.AppConstants.QUANTITY;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.ProductDetailsResponse;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.databinding.FragmentProductDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.model.ProductOld;
import com.poona.agrocart.ui.product_detail.adapter.BasketProductAdapter;
import com.poona.agrocart.ui.product_detail.adapter.OfferProductListAdapter;
import com.poona.agrocart.ui.product_detail.adapter.ProductCommentsAdapter;
import com.poona.agrocart.ui.product_detail.adapter.ProductImagesAdapter;
import com.poona.agrocart.ui.product_detail.adapter.UnitAdapter;
import com.poona.agrocart.ui.product_detail.model.BasketContent;
import com.poona.agrocart.ui.product_detail.model.ProductComment;
import com.poona.agrocart.widgets.CustomTextView;
import com.poona.agrocart.widgets.ExpandIconView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {
    private static final String TAG = ProductDetailFragment.class.getSimpleName();
    public int count = 0;
    public ViewPager vpImages;
    private FragmentProductDetailBinding fragmentProductDetailBinding;
    private ProductDetailViewModel productDetailViewModel;
    private ProductImagesAdapter productImagesAdapter;
    private boolean isProductDetailsVisible = true;
    private boolean isNutritionDetailsVisible = true;
    private boolean isAboutThisProductVisible = true;
    private boolean isBasketContentsVisible = true;
    private boolean isBenefitsVisible = true;
    private boolean isStorageVisible = true;
    private boolean isOtherProductInfo = true;
    private boolean isVariableWtPolicyVisible = true;
    private boolean isFavourite = false;
    private final boolean isInCart = false;
    private final int quantity = 1;
    private DotsIndicator dotsIndicator;
    private RecyclerView rvProductComment;
    private LinearLayoutManager linearLayoutManager;
    private ProductCommentsAdapter productCommentsAdapter;
    private ArrayList<ProductComment> commentArrayList;
    private ArrayList<BasketContent> basketContentArrayList;
    private ArrayList<ProductOld> similarProductOlds;
    private BasketProductAdapter basketProductAdapter;
    private ProductDetailsResponse.ProductDetails details;
    private OfferProductListAdapter productListAdapter;
    private View root;
    private final boolean BasketType = false;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private ImageView txtOrganic;
    private CustomTextView txtBrand;
    private String itemId ="";
    private Bundle bundle;
    private String unitId;
    private SwipeRefreshLayout rlRefreshPage;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemId = getArguments().getString(PRODUCT_ID);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentProductDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false);
        productDetailViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        fragmentProductDetailBinding.setLifecycleOwner(this);
        root = fragmentProductDetailBinding.getRoot();
        initTitleWithBackBtn("");
        initView();
        setSimilarItems();
        rlRefreshPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rlRefreshPage.setRefreshing(true);
                if (isConnectingToInternet(context)) {
                    callProductDetailsApi(showCircleProgressDialog(context,""));
                } else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });


        return root;
    }

    private void setSimilarItems() {
//        productDetailViewModel.getSimilarProductLiveData().observe(getViewLifecycleOwner(), similarItems -> {
//            this.similarProductOlds = similarItems;
//            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
//            productListAdapter = new OfferProductListAdapter(this.similarProductOlds, requireActivity(), root);
//            fragmentProductDetailBinding.rvSimilar.setNestedScrollingEnabled(false);
//            fragmentProductDetailBinding.rvSimilar.setLayoutManager(layoutManager);
//            fragmentProductDetailBinding.rvSimilar.setAdapter(productListAdapter);
//            productListAdapter.setOnProductClick(product -> {
//                toProductDetail(product, root);
//            });
//        });

    }

    private void initView() {
        fragmentProductDetailBinding.itemLayout.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.rlProductTag.setVisibility(View.VISIBLE);
        txtOrganic = ((HomeActivity) requireActivity()).binding.appBarHome.txtOrganic;
        txtBrand = ((HomeActivity) requireActivity()).binding.appBarHome.tvBrand;
        rlRefreshPage = fragmentProductDetailBinding.rlRefreshPage;
        if (isConnectingToInternet(context)) {
            callProductDetailsApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
       /* try {
            if (getArguments() != null) {
                bundle = getArguments();
                if (bundle.getString(AppConstants.PRODUCT_ID) != null) {
                    itemId = bundle.getString(AppConstants.PRODUCT_ID);
                    callProductDetailsApi(showCircleProgressDialog(context, ""));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
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
        // BasketDetail ProductOld views
//        fragmentProductDetailBinding.layoutAdded.llProductList.setOnClickListener(this);
//        fragmentProductDetailBinding.layoutAdded.imgPlus.setOnClickListener(this);
//        fragmentProductDetailBinding.layoutAdded.imgMinus.setOnClickListener(this);
//        fragmentProductDetailBinding.layoutAdded.tvStartDate.setOnClickListener(this);


        vpImages = fragmentProductDetailBinding.vpProductImages;
        dotsIndicator = fragmentProductDetailBinding.dotsIndicator;
        rvProductComment = fragmentProductDetailBinding.rvProductComment;

        setHideOrShowValues();

    }

    private void callProductDetailsApi(ProgressDialog progressDialog) {
        Observer<ProductDetailsResponse> productDetailsResponseObserver = productDetailsResponse -> {
            if (productDetailsResponse != null) {
                progressDialog.dismiss();
                switch (productDetailsResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (productDetailsResponse.getProductDetails() != null) {
                            fragmentProductDetailBinding.itemLayout.setVisibility(View.VISIBLE);
                            details = productDetailsResponse.getProductDetails();
                            rlRefreshPage.setRefreshing(false);
                            details.setUnit(productDetailsResponse.getProductDetails().getProductUnits().get(0));
                            setDetailsValue();
                            changePriceAndUnit(details.getUnit());
                            fragmentProductDetailBinding.setProductDetailModule(details);
                            fragmentProductDetailBinding.setVariable(BR.productDetailModule, details);
                            System.out.println("product name" + details.getProductName());
                            UnitAdapter unitAdapter = new UnitAdapter(details.getProductUnits(), details.getIsCart(), requireActivity(), this::changePriceAndUnit);
                            fragmentProductDetailBinding.rvWeights.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false));
                            fragmentProductDetailBinding.rvWeights.setAdapter(unitAdapter);
                            // Redirect to ProductOld details
                            setViewPagerAdapterItems();
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        //show no data msg here
                        warningToast(context, productDetailsResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(productDetailsResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, productDetailsResponse.getMessage());
                        break;
                }

            }
        };
        productDetailViewModel.productDetailsResponseLiveData(progressDialog, detailsParams(0), ProductDetailFragment.this)
                .observe(getViewLifecycleOwner(), productDetailsResponseObserver);
    }

    private void setDetailsValue() {
        if (details.getBrandName() != null && !details.getBrandName().isEmpty()) {
            txtBrand.setText(details.getBrandName());
            txtBrand.setVisibility(View.VISIBLE);
        } else txtBrand.setVisibility(View.GONE);
        if (details.getIsO3().equalsIgnoreCase("yes")) {
            txtOrganic.setVisibility(View.VISIBLE);
        } else txtOrganic.setVisibility(View.GONE);
        if (details.getIsFavourite() == 1) {
            isFavourite = true;
            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_filled_heart);
        } else {
            isFavourite = false;
            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_heart_without_colour);
        }
        changePriceAndUnit(details.getUnit());
    }

    private void changePriceAndUnit(ProductListResponse.ProductUnit unit) {
        unitId = unit.getpId();
        int sellingPrice = Integer.parseInt(unit.getSellingPrice());
        int offerPrice = Integer.parseInt(unit.getOfferPrice());
            fragmentProductDetailBinding.tvPrice.setText("Rs." + sellingPrice);
            fragmentProductDetailBinding.tvOfferPrice.setText("Rs." + offerPrice);
        if (unit.getInCart() == 1) {
            details.setIsCart(1);
            fragmentProductDetailBinding.ivMinus.setVisibility(View.VISIBLE);
            fragmentProductDetailBinding.etQuantity.setVisibility(View.VISIBLE);
            if (unit.getQty() > 0) {
                fragmentProductDetailBinding.ivPlus.setBackground(requireActivity().getDrawable(R.drawable.bg_green_square));
                if (unit.getQty()>1){
                    fragmentProductDetailBinding.ivMinus.setEnabled(true);
                    fragmentProductDetailBinding.ivMinus.setBackground(requireActivity().getDrawable(R.drawable.bg_green_square));
                }
                else {
                    fragmentProductDetailBinding.ivMinus.setEnabled(false);
                    fragmentProductDetailBinding.ivMinus.setBackground(requireActivity().getDrawable(R.drawable.bg_grey_square));
                }
                fragmentProductDetailBinding.etQuantity.setText(String.valueOf(unit.getQty()));
            }else {
                fragmentProductDetailBinding.ivMinus.setBackground(requireActivity().getDrawable(R.drawable.bg_grey_square));
            }

        } else {
            details.setIsCart(0);
            fragmentProductDetailBinding.ivPlus.setVisibility(View.VISIBLE);
            fragmentProductDetailBinding.ivMinus.setVisibility(View.GONE);
            fragmentProductDetailBinding.etQuantity.setVisibility(View.GONE);
        }
    }

    private HashMap<String, String> detailsParams(int detail) {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.PRODUCT_ID, itemId);
        return map;
    }

    private void setProductList() {
        basketContentArrayList = new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
//        fragmentProductDetailBinding.layoutAdded.rvBasketContents.setHasFixedSize(true);
//        fragmentProductDetailBinding.layoutAdded.rvBasketContents.setLayoutManager(linearLayoutManager);
//
//        basketProductAdapter = new BasketProductAdapter(basketContentArrayList);
//        fragmentProductDetailBinding.layoutAdded.rvBasketContents.setAdapter(basketProductAdapter);
    }

    private void prepareListingData() {
        for (int i = 0; i < 4; i++) {
            BasketContent basketContent = new BasketContent();
            basketContent.setContentName(getString(R.string.moong));
            basketContent.setContentWeight(getString(R.string._500gm));
            basketContentArrayList.add(basketContent);
        }
    }

    private void setHideOrShowValues() {
        hideOrShowAboutThisProduct();
        hideOrShowBenefits();
        hideOrShowStorageAndUses();
        hideOrShowOtherProductInfo();
        hideOrShowVariableWeightPolicy();
        hideOrShowNutritionDetails();

    }

    private void outOfStockView() {
        fragmentProductDetailBinding.llOutOfStock.setVisibility(View.VISIBLE);
        fragmentProductDetailBinding.tvLocation.setVisibility(View.GONE);
        fragmentProductDetailBinding.rvWeights.setVisibility(View.GONE);
        fragmentProductDetailBinding.llPriceAndQty.setVisibility(View.GONE);
        fragmentProductDetailBinding.cardOfferMsg.setVisibility(View.GONE);

    }

//    private void setSubscription() {
//        Subscription subscription = details.getSubscription();
//        fragmentProductDetailBinding.layoutAdded.tvSubPrice.setText(subscription.getSubPrice());
//        fragmentProductDetailBinding.layoutAdded.tvSubUnit.setText(subscription.getSubUnit());
//        fragmentProductDetailBinding.layoutAdded.tvSubQty.setText(subscription.getSubQty());
//        fragmentProductDetailBinding.layoutAdded.tvSubUnitPrice.setText(subscription.getSubUnitPrice());
//        fragmentProductDetailBinding.layoutAdded.tvSubAmount.setText(subscription.getSubTotalAmount());
//        fragmentProductDetailBinding.layoutAdded.tvSubMsg.setText(subscription.getSubMsg());
//        RecyclerView rvType = fragmentProductDetailBinding.layoutAdded.rvSubType;
//        UnitAdapter subTypeAdapter = new UnitAdapter(details.getSubscription().getSubTypes(), getActivity());
//        rvType.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        rvType.setHasFixedSize(true);
//        rvType.setAdapter(subTypeAdapter);
//    }

//    private void setCommentsAdapter() {
//        commentArrayList = new ArrayList<>();
//        commentArrayList = details.getCommentList();
//        linearLayoutManager = new LinearLayoutManager(requireContext());
//        rvProductComment.setHasFixedSize(true);
//        rvProductComment.setLayoutManager(linearLayoutManager);
//        rvProductComment.setNestedScrollingEnabled(false);
//        productCommentsAdapter = new ProductCommentsAdapter(commentArrayList, false);
//        rvProductComment.setAdapter(productCommentsAdapter);
//    }

    private void setViewPagerAdapterItems() {
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < details.getProductImgs().size(); i++)
            images.add(details.getProductImgs().get(i).getProductImg());
        count = details.getProductImgs().size();
        if (count >= 0) {
            productImagesAdapter = new ProductImagesAdapter(ProductDetailFragment.this,
                    getChildFragmentManager(), images);
            vpImages.setAdapter(productImagesAdapter);
            productImagesAdapter.notifyDataSetChanged();
            vpImages.addOnPageChangeListener(productImagesAdapter);
            dotsIndicator.setViewPager(vpImages);
        }
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
                if (Integer.parseInt(fragmentProductDetailBinding.etQuantity.getText().toString())>1){
                    decreaseQuantity(fragmentProductDetailBinding.etQuantity.getText().toString(),
                            fragmentProductDetailBinding.etQuantity, fragmentProductDetailBinding.ivMinus);
                }else fragmentProductDetailBinding.ivMinus.setEnabled(false);
                break;
            case R.id.iv_plus:
                addOrRemoveFromCart();
                break;
            case R.id.img_minus:
                //remove subscription
                break;
            case R.id.iv_favourite:
                addOrRemoveFromFavourite();
                break;
            case R.id.tv_start_date:
//                showCalendar(fragmentProductDetailBinding.layoutAdded.tvStartDate);
                break;
        }

    }


    private void hideOrShowVariableWeightPolicy() {
        if (isVariableWtPolicyVisible) {
            fragmentProductDetailBinding.ivVariableWeightPolicyShowHide.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvVariableWeightPolicyBreif);
        } else {
            fragmentProductDetailBinding.ivVariableWeightPolicyShowHide.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvVariableWeightPolicyBreif);
        }
        isVariableWtPolicyVisible = !isVariableWtPolicyVisible;
    }

    private void hideOrShowOtherProductInfo() {
        if (isOtherProductInfo) {
            fragmentProductDetailBinding.ivOtherProductInfoHideShow.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvOtherProductInfoBrief);
        } else {
            fragmentProductDetailBinding.ivOtherProductInfoHideShow.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvOtherProductInfoBrief);
        }
        isOtherProductInfo = !isOtherProductInfo;
    }

    private void hideOrShowStorageAndUses() {
        if (isStorageVisible) {
            fragmentProductDetailBinding.ivStorageUseHideShow.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvStorageAndUseBrief);
        } else {
            fragmentProductDetailBinding.ivStorageUseHideShow.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvStorageAndUseBrief);
        }
        isStorageVisible = !isStorageVisible;
    }

    private void hideOrShowBenefits() {
        if (isBenefitsVisible) {
            fragmentProductDetailBinding.ivBenefitsHideShow.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvBenefitsBrief);
        } else {
            fragmentProductDetailBinding.ivBenefitsHideShow.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvBenefitsBrief);
        }
        isBenefitsVisible = !isBenefitsVisible;
    }

    private void hideOrShowAboutThisProduct() {
        if (isAboutThisProductVisible) {
            fragmentProductDetailBinding.ivAboutThisProductHideShow.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvAboutThisProductBrief);
        } else {
            fragmentProductDetailBinding.ivAboutThisProductHideShow.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvAboutThisProductBrief);
        }
        isAboutThisProductVisible = !isAboutThisProductVisible;
    }

    private void addOrRemoveFromFavourite() {
        addOrRemoveFavouriteApi(showCircleProgressDialog(context, ""), !isFavourite);
    }

    private void addOrRemoveFromCart() {
        if (details.getUnit().getInCart() == 0) {
            callAddToCartApi(showCircleProgressDialog(context, ""), details);
        } else {
            increaseQuantity(fragmentProductDetailBinding.etQuantity.getText().toString(),
                    fragmentProductDetailBinding.etQuantity, fragmentProductDetailBinding.ivPlus);
        }
    }

    //Add or Remove from CArt
    private void callAddToCartApi(ProgressDialog showCircleProgressDialog,
                                  ProductDetailsResponse.ProductDetails details) {
        Observer<BaseResponse> addToCartResponseObserver = addToCartResponse -> {
            if (addToCartResponse != null) {
                showCircleProgressDialog.dismiss();
                Log.e(TAG, "callAddToCartApi: " + addToCartResponse.getMessage());
                switch (addToCartResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(requireActivity(), addToCartResponse.getMessage());
                        fragmentProductDetailBinding.ivMinus.setVisibility(View.VISIBLE);
                        fragmentProductDetailBinding.ivMinus.setVisibility(View.VISIBLE);
                        fragmentProductDetailBinding.etQuantity.setVisibility(View.VISIBLE);
                        details.getUnit().setQty(1);
                        details.getUnit().setInCart(1);
                        setDetailsValue();
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        //show no data msg here
                        warningToast(context, addToCartResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(addToCartResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, addToCartResponse.getMessage());
                        break;
                }

            }
        };
        productDetailViewModel.addToCartProductLiveData(showCircleProgressDialog,
                addToCartParam(details), ProductDetailFragment.this)
                .observe(getViewLifecycleOwner(), addToCartResponseObserver);
    }

    private void updateQuantityApi(ProgressDialog showCircleProgressDialog,
                                   ProductDetailsResponse.ProductDetails details) {
        Observer<BaseResponse> addToCartResponseObserver = addToCartResponse -> {
            if (addToCartResponse != null) {
                showCircleProgressDialog.dismiss();
                Log.e(TAG, "callAddToCartApi: " + addToCartResponse.getMessage());
                switch (addToCartResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(requireActivity(), addToCartResponse.getMessage());
                        fragmentProductDetailBinding.ivMinus.setVisibility(View.VISIBLE);
                        fragmentProductDetailBinding.etQuantity.setVisibility(View.VISIBLE);
                        setDetailsValue();
//                        changePriceAndUnit(details.getUnit(), true);
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        //show no data msg here
                        warningToast(context, addToCartResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(addToCartResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, addToCartResponse.getMessage());
                        break;
                }

            }
        };
        productDetailViewModel.addToCartProductLiveData(showCircleProgressDialog,
                addToCartParam(details), ProductDetailFragment.this)
                .observe(getViewLifecycleOwner(), addToCartResponseObserver);
    }


    //ADd to favourite API
    private void addOrRemoveFavouriteApi(ProgressDialog showCircleProgressDialog, boolean addToFav) {
        Observer<BaseResponse> favouriteResponseObserver = responseFavourite -> {
            if (responseFavourite != null) {
                showCircleProgressDialog.dismiss();
                Log.e(TAG, "callAddToFavouriteApi: " + responseFavourite.getMessage());
                switch (responseFavourite.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        Log.e(TAG, "addOrRemoveFavouriteApi: " + responseFavourite.getMessage());
                        if (addToFav) {
                            isFavourite = true;
                            successToast(requireActivity(), "Added to favourite");
                            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_filled_heart);
                        } else {
                            isFavourite = false;
                            successToast(context, "Removed from favourite");
                            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_heart_without_colour);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        //show no data msg here
                        warningToast(context, responseFavourite.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(responseFavourite.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, responseFavourite.getMessage());
                        break;
                }

            }
        };
        if (addToFav) {

            productDetailViewModel.addToFavourite(showCircleProgressDialog, favParams(), ProductDetailFragment.this)
                    .observe(getViewLifecycleOwner(), favouriteResponseObserver);
        } else {
            productDetailViewModel.removeFromFavoriteResponse(showCircleProgressDialog, favParams(),
                    ProductDetailFragment.this)
                    .observe(getViewLifecycleOwner(), favouriteResponseObserver);
        }
    }

    private HashMap<String, String> favParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.ITEM_TYPE, "product");
        map.put(AppConstants.PRODUCT_ID, details.getId());
        map.put(AppConstants.PU_ID, unitId);
        return map;
    }

    private HashMap<String, String> addToCartParam(ProductDetailsResponse.ProductDetails product) {
        HashMap<String, String> map = new HashMap<>();
        map.put(PRODUCT_ID, product.getId());
        map.put(PU_ID, unitId);
        if (fragmentProductDetailBinding.etQuantity.getText().toString().isEmpty())
            map.put(QUANTITY, "1");
        else
            map.put(QUANTITY, fragmentProductDetailBinding.etQuantity.getText().toString());
        return map;
    }

    private void increaseQuantity(String qty, CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        quantity++;
        etQuantity.setText(String.valueOf(quantity));
        AppUtils.setMinusButton(quantity, view);
        details.getUnit().setQty(quantity);
        updateQuantityApi(showCircleProgressDialog(context,""),details);
    }

    private void decreaseQuantity(String qty, CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        if (quantity == 1) {
            warningToast(requireActivity(), getString(R.string.quantity_less_than_one));
        } else {
            quantity--;
            etQuantity.setText(String.valueOf(quantity));
            details.setQuantity(quantity);
            updateQuantityApi(showCircleProgressDialog(context, ""), details);
        }
        details.setQuantity(quantity);
        AppUtils.setMinusButton(quantity, view);
    }

    private void hideOrShowNutritionDetails() {
        if (isNutritionDetailsVisible) {
            fragmentProductDetailBinding.ivNutritionsShowHide.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvNutritionsBrief);
        } else {
            fragmentProductDetailBinding.ivNutritionsShowHide.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvNutritionsBrief);
        }
        isNutritionDetailsVisible = !isNutritionDetailsVisible;
    }

    private void hideOrShowProductDetails() {
        if (isProductDetailsVisible) {
            fragmentProductDetailBinding.ivProductDetailHideShow.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvProductDetailBrief);
        } else {
            fragmentProductDetailBinding.ivProductDetailHideShow.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvProductDetailBrief);
        }
        isProductDetailsVisible = !isProductDetailsVisible;
    }

    private void hideOrShowBasketContentsList() {
//        if (isBasketContentsVisible) {
//            fragmentProductDetailBinding.layoutAdded.ivProductLists.setState(ExpandIconView.MORE, true);
//            collapse(fragmentProductDetailBinding.layoutAdded.rvBasketContents);
//        } else {
//            fragmentProductDetailBinding.layoutAdded.ivProductLists.setState(ExpandIconView.LESS, true);
//            expand(fragmentProductDetailBinding.layoutAdded.rvBasketContents);
//        }
        isBasketContentsVisible = !isBasketContentsVisible;
    }

    private void toProductDetail(ProductOld productOld, View root) {
        Bundle bundle = new Bundle();
        bundle.putString("name", productOld.getName());
        bundle.putString("image", productOld.getImg());
        bundle.putString("price", productOld.getPrice());
        bundle.putString("brand", productOld.getBrand());
        bundle.putString("quantity", productOld.getQuantity());
        bundle.putBoolean("organic", productOld.isOrganic());
        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_product_details, bundle);
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), ProductDetailFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        callProductDetailsApi(showCircleProgressDialog(context, ""));
                        break;
                    case 1:
                        addOrRemoveFavouriteApi(showCircleProgressDialog(context, ""), !isFavourite);
                        break;
                    case 2:
                        updateQuantityApi(showCircleProgressDialog(context, ""), details);
                        break;
                }
            }
        }, context);

    }
}