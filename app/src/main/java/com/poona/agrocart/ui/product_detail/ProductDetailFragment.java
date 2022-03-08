package com.poona.agrocart.ui.product_detail;

import static com.poona.agrocart.app.AppConstants.PRODUCT_ID;
import static com.poona.agrocart.app.AppConstants.PU_ID;
import static com.poona.agrocart.app.AppConstants.QUANTITY;
import static com.poona.agrocart.app.AppConstants.RATING;
import static com.poona.agrocart.app.AppConstants.REVIEW;
import static com.poona.agrocart.app.AppConstants.REVIEW_LIST;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.ProductDetailsResponse;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.data.network.responses.Review;
import com.poona.agrocart.databinding.FragmentProductDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.model.ProductOld;
import com.poona.agrocart.ui.nav_favourites.FavouriteItemAdapter;
import com.poona.agrocart.ui.nav_favourites.FavouriteItemsFragment;
import com.poona.agrocart.ui.product_detail.adapter.BasketProductAdapter;
import com.poona.agrocart.ui.product_detail.adapter.ProductRatingReviewAdapter;
import com.poona.agrocart.ui.product_detail.adapter.ProductImagesAdapter;
import com.poona.agrocart.ui.product_detail.adapter.UnitAdapter;
import com.poona.agrocart.ui.product_detail.model.AllReview;
import com.poona.agrocart.ui.product_detail.model.BasketContent;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.CustomEditText;
import com.poona.agrocart.widgets.CustomTextView;
import com.poona.agrocart.widgets.ExpandIconView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    private int quantity = 1;
    private DotsIndicator dotsIndicator;
    private RecyclerView rvProductReview;
    private LinearLayoutManager linearLayoutManager;
    private ProductRatingReviewAdapter productRatingReviewAdapter;
    private ArrayList<BasketContent> basketContentArrayList;
    private ProductDetailsResponse.ProductDetails details;
    private ProductListResponse.ProductUnit unit;
    private View root;
    private final boolean BasketType = false;
    private ImageView txtOrganic;
    private CustomTextView txtBrand;
    private String strProductId = "";
    private String unitId;
    private SwipeRefreshLayout rlRefreshPage;

    private RatingBar ratingBarInput;
    private CustomEditText etFeedback;
    private CustomButton btnRateSubmit;
    private List<ProductDetailsResponse.Rating> ratingList = new ArrayList<>();
    private ArrayList<Review> reviewsList = new ArrayList<>();
    private ArrayList<Review> allReview = new ArrayList<>();
    private ScrollView scrollView;
    private RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strProductId = getArguments().getString(PRODUCT_ID);
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
                    callProductDetailsApi(showCircleProgressDialog(context, ""));
                } else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });

        scrollView.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            if (i3>0){
                ((HomeActivity)context).binding.appBarHome.textTitle.setText(details.getProductName());
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                ((HomeActivity)context).binding.appBarHome.textTitle.setLayoutParams(layoutParams);
            }
            else {
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                ((HomeActivity)context).binding.appBarHome.textTitle.setLayoutParams(layoutParams);
                ((HomeActivity)context).binding.appBarHome.textTitle.setText("");
            }
        });

        btnRateSubmit.setOnClickListener(view -> {
            if (isConnectingToInternet(context)) {
                if (!Objects.requireNonNull(etFeedback.getText()).toString().isEmpty() && !(ratingBarInput.getRating() == 0.0)) {
                    callRatingAndReviewAPi(showCircleProgressDialog(context, ""));
                } else {
                    warningToast(context, "Please fill the field");
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        });
        fragmentProductDetailBinding.tvSeeMoreReview.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(REVIEW_LIST, allReview);
            NavHostFragment.findNavController(ProductDetailFragment.this).
                    navigate(R.id.action_nav_product_details_to_nav_product_review, bundle);
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
        scrollView = fragmentProductDetailBinding.scrollView;
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
                    strProductId = bundle.getString(AppConstants.PRODUCT_ID);
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


        vpImages = fragmentProductDetailBinding.vpProductImages;
        dotsIndicator = fragmentProductDetailBinding.dotsIndicator;
        rvProductReview = fragmentProductDetailBinding.rvProductReview;
        ratingBarInput = fragmentProductDetailBinding.ratingBarInput;
        etFeedback = fragmentProductDetailBinding.etFeedback;
        btnRateSubmit = fragmentProductDetailBinding.btnSubmit;

        setHideOrShowValues();

    }

    private void callProductDetailsApi(ProgressDialog progressDialog) {
        @SuppressLint("NotifyDataSetChanged") Observer<ProductDetailsResponse> productDetailsResponseObserver = productDetailsResponse -> {
            if (productDetailsResponse != null) {
                progressDialog.dismiss();
                switch (productDetailsResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (productDetailsResponse.getProductDetails() != null) {
                            fragmentProductDetailBinding.itemLayout.setVisibility(View.VISIBLE);
                            details = productDetailsResponse.getProductDetails();
                            rlRefreshPage.setRefreshing(false);
                            for (ProductListResponse.ProductUnit unit : details.getProductUnits()) {
                                details.setUnit(details.getProductUnits().get(0));
                            }
                            UnitAdapter unitAdapter = new UnitAdapter(details.getProductUnits(), details.getIsCart(), requireActivity(), this::changePriceAndUnit);
                            fragmentProductDetailBinding.rvWeights.setAdapter(unitAdapter);
                            setDetailsValue();
                            fragmentProductDetailBinding.setProductDetailModule(productDetailViewModel);
                            fragmentProductDetailBinding.setVariable(BR.productDetailModule, productDetailViewModel);
                            fragmentProductDetailBinding.rvWeights.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false));
                            setViewPagerAdapterItems();

                            /*Rating and Reviews*/
                            if (productDetailsResponse.getProductDetails().getRating() != null) {
                                ratingList.clear();
                                ratingList.add(productDetailsResponse.getProductDetails().getRating());
                                setRatingViewHideShow(ratingList);
                            }
                            if (productDetailsResponse.getProductDetails().getReviews() != null ||
                                    productDetailsResponse.getProductDetails().getReviews().size() > 0) {
                                reviewsList.clear();
                                allReview.clear();
                                reviewsList.addAll(productDetailsResponse.getProductDetails().getReviews());
                                allReview.addAll(reviewsList);
                                setAdaptor(reviewsList);
                                productRatingReviewAdapter.notifyDataSetChanged();
                            }
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

    private void setRatingViewHideShow(List<ProductDetailsResponse.Rating> ratingList) {
        if (!ratingList.get(0).getRating().isEmpty() && !ratingList.get(0).getReview().isEmpty() ||
                !ratingList.get(0).getRating().equalsIgnoreCase("") &&
                        !ratingList.get(0).getReview().equalsIgnoreCase("")) {
            fragmentProductDetailBinding.llRateView.setVisibility(View.GONE);
        } else {
            fragmentProductDetailBinding.llRateView.setVisibility(View.VISIBLE);
        }
    }

    private void setDetailsValue() {
        unit = details.getUnit();
        productDetailViewModel.productName.setValue(details.getProductName());
        productDetailViewModel.productLocation.setValue(details.getLocation());
        productDetailViewModel.unitMutableLiveData.setValue(details.getUnit());
        productDetailViewModel.sellingPrice.setValue("Rs." + details.getUnit().getSellingPrice());
        productDetailViewModel.offerPrice.setValue("Rs" + details.getUnit().getOfferPrice());
        productDetailViewModel.offer.setValue(details.getSpecialOffer());
        if (productDetailViewModel.unitMutableLiveData.getValue().getInCart() == 1) {
            productDetailViewModel.isInCart.setValue(true);
        } else
            productDetailViewModel.isInCart.setValue(false);
        if (details.getIsFavourite() == 1)
            productDetailViewModel.isInFav.setValue(true);
        else
            productDetailViewModel.isInFav.setValue(false);
        productDetailViewModel.productQuantity.setValue(String.valueOf(productDetailViewModel.unitMutableLiveData.getValue().getQty()));
        productDetailViewModel.productDetail.setValue(details.getProductDetails());
        productDetailViewModel.productAbout.setValue(details.getAboutProduct());
        productDetailViewModel.productBenefit.setValue(details.getBenifit());
        productDetailViewModel.productStorageUses.setValue(details.getStoragesUses());
        productDetailViewModel.productOtherInfo.setValue(details.getOtherProuctInfo());
        productDetailViewModel.productWeightPolicy.setValue(details.getWeightPolicy());
        productDetailViewModel.productNutrition.setValue(details.getNutrition());
        productDetailViewModel.productBrand.setValue(details.getBrandName());
        if (details.getIsO3().equalsIgnoreCase("yes"))
            productDetailViewModel.isOrganic.setValue(true);
        else
            productDetailViewModel.isOrganic.setValue(false);

        if (details.getBrandName() != null && !details.getBrandName().isEmpty()) {
            txtBrand.setText(productDetailViewModel.productBrand.getValue());
            txtBrand.setVisibility(View.VISIBLE);
        } else txtBrand.setVisibility(View.GONE);
        if (productDetailViewModel.isOrganic.getValue()) {
            txtOrganic.setVisibility(View.VISIBLE);
        } else txtOrganic.setVisibility(View.GONE);
        if (productDetailViewModel.isInFav.getValue()) {
            isFavourite = true;
            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_filled_heart);
        } else {
            isFavourite = false;
            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_heart_without_colour);
        }
        unitId = productDetailViewModel.unitMutableLiveData.getValue().getpId();
        if (productDetailViewModel.isInCart.getValue()) {
            fragmentProductDetailBinding.ivMinus.setVisibility(View.VISIBLE);
            fragmentProductDetailBinding.etQuantity.setVisibility(View.VISIBLE);
            if (Integer.parseInt(productDetailViewModel.productQuantity.getValue()) > 1) {
                fragmentProductDetailBinding.ivMinus.setBackground(requireActivity().getDrawable(R.drawable.bg_green_square));
            } else
                fragmentProductDetailBinding.ivMinus.setBackground(requireActivity().getDrawable(R.drawable.bg_grey_square));
        } else {
            fragmentProductDetailBinding.ivMinus.setVisibility(View.GONE);
            fragmentProductDetailBinding.etQuantity.setVisibility(View.GONE);
        }
    }

    private void changePriceAndUnit(ProductListResponse.ProductUnit unit) {
        details.setUnit(unit);
        setDetailsValue();
    }

    private HashMap<String, String> detailsParams(int detail) {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.PRODUCT_ID, strProductId);
        return map;
    }

    private void setProductList() {
        basketContentArrayList = new ArrayList<>();
        prepareListingData();
        linearLayoutManager = new LinearLayoutManager(requireContext());
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
                if (Integer.parseInt(fragmentProductDetailBinding.etQuantity.getText().toString()) > 1) {
                    decreaseQuantity(fragmentProductDetailBinding.etQuantity.getText().toString(),
                            fragmentProductDetailBinding.etQuantity, fragmentProductDetailBinding.ivMinus);
                } else fragmentProductDetailBinding.ivMinus.setEnabled(false);
                break;
            case R.id.iv_plus:
                addOrRemoveFromCart();
                break;
            case R.id.iv_favourite:
                addOrRemoveFromFavourite();
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
        if (details.getUnit().getInCart() == 0 || fragmentProductDetailBinding.etQuantity.getText().toString().isEmpty()) {
            fragmentProductDetailBinding.etQuantity.setText("");
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
                                   ProductDetailsResponse.ProductDetails details, String qty) {
        Observer<BaseResponse> addToCartResponseObserver = addToCartResponse -> {
            if (addToCartResponse != null) {
                showCircleProgressDialog.dismiss();
                Log.e(TAG, "callUpdateQty: " + addToCartResponse.getMessage());
                switch (addToCartResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(requireActivity(), addToCartResponse.getMessage());
                        fragmentProductDetailBinding.ivMinus.setVisibility(View.VISIBLE);
                        fragmentProductDetailBinding.etQuantity.setVisibility(View.VISIBLE);
                        setDetailsValue();
//                        changePriceAndUnit(details.getUnit());
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
                updateCartParam(details, qty), ProductDetailFragment.this)
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
        map.put(QUANTITY, "1");
        return map;
    }

    private HashMap<String, String> updateCartParam(ProductDetailsResponse.ProductDetails product, String qty) {
        HashMap<String, String> map = new HashMap<>();
        map.put(PRODUCT_ID, product.getId());
        map.put(PU_ID, unitId);
        map.put(QUANTITY, qty);
        return map;
    }

    //use for me trupti

    private void increaseQuantity(String qty, CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        quantity++;
        etQuantity.setText(String.valueOf(quantity));
        AppUtils.setMinusButton(quantity, view);
        details.getUnit().setQty(quantity);
        updateQuantityApi(showCircleProgressDialog(context, ""), details, String.valueOf(quantity));
    }

    private void decreaseQuantity(String qty, CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        if (quantity == 1) {
            warningToast(requireActivity(), getString(R.string.quantity_less_than_one));
        } else {
            quantity--;
            etQuantity.setText(String.valueOf(quantity));
            updateQuantityApi(showCircleProgressDialog(context, ""), details, String.valueOf(quantity));
        }
        details.getUnit().setQty(quantity);
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
                        updateQuantityApi(showCircleProgressDialog(context, ""), details, String.valueOf(details.getUnit().getQty()));
                        break;
                }
            }
        }, context);

    }


    /* Rating and Review API */

    private void callRatingAndReviewAPi(ProgressDialog progressDialog) {
        @SuppressLint("NotifyDataSetChanged")
        Observer<BaseResponse> ratingResponseObserver = baseResponse -> {

            if (baseResponse != null) {
                Log.e("Rating Response", new Gson().toJson(baseResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        successToast(context, baseResponse.getMessage());
                        callProductDetailsApi(showCircleProgressDialog(context, ""));
                        break;
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Record not Found
                        warningToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, baseResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, baseResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };

        productDetailViewModel.callSubmitRatingResponseApi(progressDialog, submitRatingInputParameter(), ProductDetailFragment.this)
                .observe(getViewLifecycleOwner(), ratingResponseObserver);
    }

    private HashMap<String, String> submitRatingInputParameter() {
        HashMap<String, String> map = new HashMap<>();
        map.put(PRODUCT_ID, strProductId);
        map.put(RATING, String.valueOf(ratingBarInput.getRating()));
        map.put(REVIEW, etFeedback.getText().toString().trim());
        return map;
    }


    private void setAdaptor(List<Review> reviewsList) {

        //Initializing our superheroes list
        this.reviewsList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvProductReview.setHasFixedSize(true);
        rvProductReview.setLayoutManager(linearLayoutManager);
        //initializing our adapter
        productRatingReviewAdapter = new ProductRatingReviewAdapter(context, reviewsList,0);
        //Adding adapter to recyclerview
        rvProductReview.setAdapter(productRatingReviewAdapter);
    }

}