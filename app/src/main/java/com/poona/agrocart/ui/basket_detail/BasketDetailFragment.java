package com.poona.agrocart.ui.basket_detail;

import static com.poona.agrocart.app.AppConstants.ITEM_TYPE;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.BasketDetailsResponse;
import com.poona.agrocart.data.network.responses.BasketResponse;
import com.poona.agrocart.databinding.FragmentBasketDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.basket_detail.adapter.BasketImagesAdapter;
import com.poona.agrocart.ui.product_detail.adapter.BasketProductAdapter;
import com.poona.agrocart.ui.product_detail.adapter.ProductCommentsAdapter;
import com.poona.agrocart.ui.product_detail.model.ProductComment;
import com.poona.agrocart.widgets.CustomTextView;
import com.poona.agrocart.widgets.ExpandIconView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class BasketDetailFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {

    private static final String TAG = BasketDetailFragment.class.getSimpleName();
    private BasketDetailViewModel basketDetailViewModel;
    private FragmentBasketDetailBinding basketDetailsBinding;
    private View rootView;
    public ViewPager vpImages;
    private DotsIndicator dotsIndicator;
    private RecyclerView rvProductComment;
    private BasketResponse.Basket details;
    private ArrayList<BasketResponse.BasketProduct> basketProducts;
    private BasketProductAdapter basketProductAdapter;
    private RecyclerView rvBasketProducts;
    private LinearLayoutManager linearLayoutManager;
    public int count = 0;
    private BasketImagesAdapter basketImagesAdapter;
    private ArrayList<ProductComment> commentArrayList;
    private ProductCommentsAdapter productCommentsAdapter;
    private Bundle bundle;
    private String itemId;
    private boolean isProductDetailsVisible = true, isNutritionDetailsVisible = true, isAboutThisProductVisible = true,
            isBasketContentsVisible = true, isBenefitsVisible = true, isStorageVisible = true, isOtherProductInfo = true,
            isVariableWtPolicyVisible = true, isFavourite = false;
    private Calendar calendar;
    private int mYear, mMonth, mDay;

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
    }

    private void initView() {
        basketDetailsBinding.itemLayout.setVisibility(View.GONE);
        try {
            if (getArguments() != null) {
                bundle = getArguments();
                if (bundle.getString(AppConstants.BASKET_ID) != null) {
                    itemId = bundle.getString(AppConstants.BASKET_ID);
                    if (isConnectingToInternet(context)) {
                        callBasketDetailsApi(showCircleProgressDialog(context, ""));
                    } else {
                        showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        basketDetailsBinding.llProductList.setOnClickListener(this);
        basketDetailsBinding.layoutAdded.imgPlus.setOnClickListener(this);
        basketDetailsBinding.layoutAdded.imgMinus.setOnClickListener(this);
        basketDetailsBinding.layoutAdded.tvStartDate.setOnClickListener(this);


        vpImages = basketDetailsBinding.vpProductImages;
        dotsIndicator = basketDetailsBinding.dotsIndicator;
        rvProductComment = basketDetailsBinding.rvProductComment;

        setValues();


    }

    //Basket Details API
    private void callBasketDetailsApi(ProgressDialog progressDialog) {
        Observer<BasketDetailsResponse> basketDetailsResponseObserver = basketDetailsResponse -> {
            if (basketDetailsResponse != null) {
                progressDialog.dismiss();
                switch (basketDetailsResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (basketDetailsResponse.getBasketDetail() != null) {
                            basketDetailsBinding.itemLayout.setVisibility(View.VISIBLE);
                            BasketResponse.Basket basket = basketDetailsResponse.getBasketDetail();
                            basket.setBasketUnit(basket.getBasketUnits().get(0));
                            basket.setAccurateWeight(basket.getBasketUnit().getWeightAndUnit());
                            details = basket;
                            basketDetailsBinding.setModuleBasket(details);
                            basketDetailsBinding.setVariable(BR.moduleBasket, details);
                            basketDetailsBinding.layoutAdded.setSubscriptionModule(basket);
                            basketDetailsBinding.layoutAdded.setVariable(BR.subscriptionModule, basket);
                            hideOrShowProductDetails();
                            setBasketImages();
                            setBasketContents();
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        //show no data msg here
                        warningToast(context, basketDetailsResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(basketDetailsResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, basketDetailsResponse.getMessage());
                        break;
                }


            }
        };
        basketDetailViewModel.basketDetailsResponseLiveData(progressDialog, basketParams(false, false), BasketDetailFragment.this)
                .observe(getViewLifecycleOwner(), basketDetailsResponseObserver);
    }

    //Add to Cart Basket API
    private void callAddToCartBasketApi(ProgressDialog progressDialog) {
        Observer<BaseResponse> addToCartBasketObserver = baseResponse -> {
            if (baseResponse != null && progressDialog != null) {
                progressDialog.dismiss();
                Log.e(TAG, "callAddToCartBasketApi: " + baseResponse.getMessage());
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, baseResponse.getMessage());
                        basketDetailsBinding.ivMinus.setVisibility(View.VISIBLE);
                        basketDetailsBinding.etQuantity.setVisibility(View.VISIBLE);
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        //show no data msg here
                        warningToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(baseResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, baseResponse.getMessage());
                        break;
                }

            }
        };
        basketDetailViewModel.addToBasketResponse(progressDialog, basketParams(true, false),
                BasketDetailFragment.this)
                .observe(getViewLifecycleOwner(), addToCartBasketObserver);
    }

    /*Add To favourite API*/
    private void callAddOrRemoveFavouriteApi(ProgressDialog progressDialog, boolean addToFav) {
        Observer<BaseResponse> favouriteBasketObserver = baseResponse -> {
            if (baseResponse != null && progressDialog != null) {
                progressDialog.dismiss();
                Log.e(TAG, "AddOrRemoveFavBasketApi: " + baseResponse.getMessage());
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, baseResponse.getMessage());
                        if (addToFav){
                            isFavourite=true;
                            basketDetailsBinding.ivFavourite.setImageResource(R.drawable.ic_filled_heart);
                        } else{
                            isFavourite = false;
                            basketDetailsBinding.ivFavourite.setImageResource(R.drawable.ic_heart_without_colour);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        //show no data msg here
                        warningToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(baseResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, baseResponse.getMessage());
                        break;
                }

            }
        };
        if (addToFav) {
            basketDetailViewModel.addToFavoriteBasketResponse(progressDialog, basketParams(false, true),
                    BasketDetailFragment.this)
                    .observe(getViewLifecycleOwner(), favouriteBasketObserver);
        } else
            basketDetailViewModel.removeFromFavoriteResponse(progressDialog, basketParams(false, true),
                    BasketDetailFragment.this)
                    .observe(getViewLifecycleOwner(), favouriteBasketObserver);
    }

    private void setBasketContents() {
        basketProducts = new ArrayList<>();
        basketProducts = details.getBasketUnits();
        if (basketProducts.size() > 0) {
            rvBasketProducts = basketDetailsBinding.rvBasketContents;
            linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            rvBasketProducts.setLayoutManager(linearLayoutManager);
            basketProductAdapter = new BasketProductAdapter(basketProducts);
            rvBasketProducts.setAdapter(basketProductAdapter);
        }

        if (details.getIsFavourite() == 1) {
            isFavourite = true;
            basketDetailsBinding.ivFavourite.setImageResource(R.drawable.ic_filled_heart);
        } else {
            basketDetailsBinding.ivFavourite.setImageResource(R.drawable.ic_heart_without_colour);
            isFavourite = false;
        }
        if (details.getInCart() == 1) {
            basketDetailsBinding.ivMinus.setVisibility(View.VISIBLE);
            basketDetailsBinding.etQuantity.setVisibility(View.VISIBLE);
            if (details.getQuantity() > 0)
                basketDetailsBinding.etQuantity.setText(String.valueOf(details.getQuantity()));
            else basketDetailsBinding.etQuantity.setText("1");
        } else {
            basketDetailsBinding.ivPlus.setVisibility(View.VISIBLE);
            basketDetailsBinding.ivMinus.setVisibility(View.GONE);
            basketDetailsBinding.etQuantity.setVisibility(View.GONE);
        }
    }

    private HashMap<String, String> basketParams(boolean addTo, boolean favTo) {
        HashMap<String, String> map = new HashMap<>();
        if (addTo)
            map.put(AppConstants.QUANTITY, basketDetailsBinding.etQuantity.getText().toString());
        if (favTo)
            map.put(ITEM_TYPE, "basket");
        map.put(AppConstants.BASKET_ID, itemId);
        return map;
    }

    private void setValues() {
        //hide all expanded views initially

        hideOrShowAboutThisProduct();
        hideOrShowBenefits();
        hideOrShowStorageAndUses();
        hideOrShowOtherProductInfo();
        hideOrShowVariableWeightPolicy();
        hideOrShowNutritionDetails();
    }

    private void setBasketImages() {
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < details.getBasketImges().size(); i++)
            images.add(details.getBasketImges().get(i).getBasketImg());
        count = details.getBasketImges().size();
        if (count > 0) {
            basketImagesAdapter = new BasketImagesAdapter(BasketDetailFragment.this,
                    getChildFragmentManager(), images);
            vpImages.setAdapter(basketImagesAdapter);
            basketImagesAdapter.notifyDataSetChanged();
            vpImages.addOnPageChangeListener(basketImagesAdapter);
            dotsIndicator.setViewPager(vpImages);
            Log.e(TAG, "setBasketImages: " + images.size());
        }
    }


    /*Hide and Show contents*/

    private void hideOrShowVariableWeightPolicy() {
        if (isVariableWtPolicyVisible) {
            basketDetailsBinding.ivVariableWeightPolicyShowHide.setState(ExpandIconView.MORE, true);
            collapse(basketDetailsBinding.tvVariableWeightPolicyBreif);
        } else {
            basketDetailsBinding.ivVariableWeightPolicyShowHide.setState(ExpandIconView.LESS, true);
            expand(basketDetailsBinding.tvVariableWeightPolicyBreif);
        }
        isVariableWtPolicyVisible = !isVariableWtPolicyVisible;
    }

    private void hideOrShowOtherProductInfo() {
        if (isOtherProductInfo) {
            basketDetailsBinding.ivOtherProductInfoHideShow.setState(ExpandIconView.MORE, true);
            collapse(basketDetailsBinding.tvOtherProductInfoBrief);
        } else {
            basketDetailsBinding.ivOtherProductInfoHideShow.setState(ExpandIconView.LESS, true);
            expand(basketDetailsBinding.tvOtherProductInfoBrief);
        }
        isOtherProductInfo = !isOtherProductInfo;
    }

    private void hideOrShowStorageAndUses() {
        if (isStorageVisible) {
            basketDetailsBinding.ivStorageUseHideShow.setState(ExpandIconView.MORE, true);
            collapse(basketDetailsBinding.tvStorageAndUseBrief);
        } else {
            basketDetailsBinding.ivStorageUseHideShow.setState(ExpandIconView.LESS, true);
            expand(basketDetailsBinding.tvStorageAndUseBrief);
        }
        isStorageVisible = !isStorageVisible;
    }

    private void hideOrShowBenefits() {
        if (isBenefitsVisible) {
            basketDetailsBinding.ivBenefitsHideShow.setState(ExpandIconView.MORE, true);
            collapse(basketDetailsBinding.tvBenefitsBrief);
        } else {
            basketDetailsBinding.ivBenefitsHideShow.setState(ExpandIconView.LESS, true);
            expand(basketDetailsBinding.tvBenefitsBrief);
        }
        isBenefitsVisible = !isBenefitsVisible;
    }

    private void hideOrShowAboutThisProduct() {
        if (isAboutThisProductVisible) {
            basketDetailsBinding.ivAboutThisProductHideShow.setState(ExpandIconView.MORE, true);
            collapse(basketDetailsBinding.tvAboutThisProductBrief);
        } else {
            basketDetailsBinding.ivAboutThisProductHideShow.setState(ExpandIconView.LESS, true);
            expand(basketDetailsBinding.tvAboutThisProductBrief);
        }
        isAboutThisProductVisible = !isAboutThisProductVisible;
    }

    private void addOrRemoveFromFavourite() {
        if (!isFavourite) {
            callAddOrRemoveFavouriteApi(showCircleProgressDialog(context, ""), true);
        } else {
            callAddOrRemoveFavouriteApi(showCircleProgressDialog(context, ""), false);
        }
    }

    private void increaseQuantity(String qty, CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        quantity++;
        etQuantity.setText(String.valueOf(quantity));
        AppUtils.setMinusButton(quantity, view);
        details.setQuantity(quantity);
    }

    private void decreaseQuantity(String qty, CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        if (quantity == 1) {
            warningToast(requireActivity(), getString(R.string.quantity_less_than_one));
        } else {
            quantity--;
            etQuantity.setText(String.valueOf(quantity));
        }
        AppUtils.setMinusButton(quantity, view);
    }


    private void hideOrShowNutritionDetails() {
        if (isNutritionDetailsVisible) {
            basketDetailsBinding.ivNutritionsShowHide.setState(ExpandIconView.MORE, true);
            collapse(basketDetailsBinding.tvNutritionsBrief);
        } else {
            basketDetailsBinding.ivNutritionsShowHide.setState(ExpandIconView.LESS, true);
            expand(basketDetailsBinding.tvNutritionsBrief);
        }
        isNutritionDetailsVisible = !isNutritionDetailsVisible;
    }

    private void hideOrShowProductDetails() {
        if (isProductDetailsVisible) {
            basketDetailsBinding.ivProductDetailHideShow.setState(ExpandIconView.MORE, true);
            collapse(basketDetailsBinding.tvProductDetailBrief);
        } else {
            basketDetailsBinding.ivProductDetailHideShow.setState(ExpandIconView.LESS, true);
            expand(basketDetailsBinding.tvProductDetailBrief);
        }
        isProductDetailsVisible = !isProductDetailsVisible;
    }

    private void hideOrShowBasketContentsList() {
        if (isBasketContentsVisible) {
            basketDetailsBinding.ivProductLists.setState(ExpandIconView.MORE, true);
            collapse(basketDetailsBinding.rvBasketContents);
        } else {
            basketDetailsBinding.ivProductLists.setState(ExpandIconView.LESS, true);
            expand(basketDetailsBinding.rvBasketContents);
        }
        isBasketContentsVisible = !isBasketContentsVisible;
    }

    //Show calender
    private void showCalendar(CustomTextView tvDate) {
        //showing date picker dialog
        DatePickerDialog dpd;
        calendar = Calendar.getInstance();
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String txtDisplayDate = null;
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                try {
                    txtDisplayDate = formatDate(selectedDate, "yyyy-MM-dd", "dd MMM yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {

                    if (tvDate != null) {
                        tvDate.setText(txtDisplayDate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                calendar.set(year, month, dayOfMonth);
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dpd.show();

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
                decreaseQuantity(basketDetailsBinding.etQuantity.getText().toString(),
                        basketDetailsBinding.etQuantity, basketDetailsBinding.ivMinus);
                break;
            case R.id.iv_plus:
                if (details.getInCart() == 0)
                    callAddToCartBasketApi(showCircleProgressDialog(context, ""));
                else {
                    increaseQuantity(basketDetailsBinding.etQuantity.getText().toString(),
                            basketDetailsBinding.etQuantity, basketDetailsBinding.ivPlus);
                    callAddToCartBasketApi(showCircleProgressDialog(context,""));
                }
                break;
            case R.id.img_minus:
                decreaseQuantity(basketDetailsBinding.layoutAdded.tvSubQty.getText().toString(),
                        basketDetailsBinding.layoutAdded.tvSubQty, basketDetailsBinding.layoutAdded.imgMinus);
                break;
            case R.id.iv_favourite:
                addOrRemoveFromFavourite();
                break;
            case R.id.tv_start_date:
                showCalendar(basketDetailsBinding.layoutAdded.tvStartDate);
                break;
        }
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), BasketDetailFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        callBasketDetailsApi(showCircleProgressDialog(context, ""));
                        break;
                    case 1:
                        callAddToCartBasketApi(showCircleProgressDialog(context, ""));
                        break;
                    case 2:
                        callAddOrRemoveFavouriteApi(showCircleProgressDialog(context, ""), true);
                        break;
                    case 3:
                        callAddOrRemoveFavouriteApi(showCircleProgressDialog(context, ""), false);
                        break;

                }
            }
        }, context);

    }
}