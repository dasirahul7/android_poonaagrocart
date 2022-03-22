package com.poona.agrocart.ui.basket_detail;

import static com.poona.agrocart.app.AppConstants.BASKET_ID;
import static com.poona.agrocart.app.AppConstants.DELIVERY_DATE;
import static com.poona.agrocart.app.AppConstants.ITEM_TYPE;
import static com.poona.agrocart.app.AppConstants.NO_OF_SUBSCRIPTION;
import static com.poona.agrocart.app.AppConstants.ORDER_ID;
import static com.poona.agrocart.app.AppConstants.PAYMENT_MODE_ID;
import static com.poona.agrocart.app.AppConstants.QUANTITY;
import static com.poona.agrocart.app.AppConstants.RATING;
import static com.poona.agrocart.app.AppConstants.REVIEW;
import static com.poona.agrocart.app.AppConstants.REVIEW_LIST;
import static com.poona.agrocart.app.AppConstants.SLOT_END_TIME;
import static com.poona.agrocart.app.AppConstants.SLOT_ID;
import static com.poona.agrocart.app.AppConstants.SLOT_START_TIME;
import static com.poona.agrocart.app.AppConstants.START_DATE;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.SUBSCRIPTION_TYPE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.BasketDetailsResponse;
import com.poona.agrocart.data.network.responses.Review;
import com.poona.agrocart.data.network.responses.orderResponse.DeliverySlot;
import com.poona.agrocart.data.network.responses.orderResponse.OrderSummaryResponse;
import com.poona.agrocart.databinding.FragmentBasketDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.basket_detail.adapter.BasketImagesAdapter;
import com.poona.agrocart.ui.basket_detail.adapter.SlotAdaptor;
import com.poona.agrocart.ui.basket_detail.adapter.SubscriptionAdapter;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.order_summary.OrderSummaryFragment;
import com.poona.agrocart.ui.product_detail.adapter.BasketProductAdapter;
import com.poona.agrocart.ui.product_detail.adapter.ProductRatingReviewAdapter;
import com.poona.agrocart.widgets.CustomEditText;
import com.poona.agrocart.widgets.CustomTextView;
import com.poona.agrocart.widgets.ExpandIconView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class BasketDetailFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener, SubscriptionAdapter.onSubTypeClickListener {

    private static final String TAG = BasketDetailFragment.class.getSimpleName();
    public ViewPager vpImages;
    public int count = 0;
    private BasketDetailViewModel basketDetailViewModel;
    private FragmentBasketDetailBinding basketDetailsBinding;
    private View rootView;
    private DotsIndicator dotsIndicator;
    private RecyclerView rvProductComment;
    private BasketDetailsResponse.BasketDetails details;
    private ArrayList<BasketDetailsResponse.BasketProduct> basketProducts;
    private BasketProductAdapter basketProductAdapter;
    private RecyclerView rvBasketProducts;
    private LinearLayoutManager linearLayoutManager;
    private BasketImagesAdapter basketImagesAdapter;
    private CustomTextView tvSubAmount, tvSubQty, tvSubTotalAmount, tvSubQuatity;

//    private SubscriptionPlanAdaptor subscriptionPlanAdaptor;
//    private ArrayList<SubscriptionPlan> subscriptionPlans ;

    /*Basket comment adapters*/
    private ArrayList<Review> reviewsArrayList;
    private ProductRatingReviewAdapter reviewsAdapter;
    private Bundle bundle;
    private String basketId = "";
    private SwipeRefreshLayout rlRefreshPage;
//    private ScrollView scrollView;


    private boolean isProductDetailsVisible = true, isNutritionDetailsVisible = true, isAboutThisProductVisible = true,
            isBasketContentsVisible = true, isBenefitsVisible = true, isStorageVisible = true, isOtherProductInfo = true,
            isVariableWtPolicyVisible = true, isFavourite = false;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private CustomEditText etFeedback;
    private RecyclerView rvProductReview;
    private RatingBar ratingBarInput;
    private BasketDetailsResponse.Rating ratingList;
    private ScrollView scrollView;
    private Spinner spinerSlots;
    private String subscriptionSlotId;
    private String strAmount;
    private int quantity;
    private String strSubscriptionBasket = "",subTypeId ="", subStartDate = "", slotStartTime = "", slotEndTime = "";



    public static BasketDetailFragment newInstance() {
        return new BasketDetailFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            basketId = getArguments().getString(BASKET_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        basketDetailViewModel = new ViewModelProvider(this).get(BasketDetailViewModel.class);
        basketDetailsBinding = FragmentBasketDetailBinding.inflate(LayoutInflater.from(context));
        rootView = basketDetailsBinding.getRoot();
        initTitleWithBackBtn("");
        initView();
        rlRefreshPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rlRefreshPage.setRefreshing(true);
                if (isConnectingToInternet(context)) {
                    callBasketDetailsApi(showCircleProgressDialog(context, ""));
                } else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });
        scrollView.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            if (i3 > 0)
                ((HomeActivity) context).binding.appBarHome.textTitle.setText(details.getBasketName());
            else ((HomeActivity) context).binding.appBarHome.textTitle.setText("");
        });

        return rootView;
    }


    private void initView() {
        //subcription init
        spinerSlots = basketDetailsBinding.layoutAdded.spSlots;
        rlRefreshPage = basketDetailsBinding.rlRefreshPage;
        scrollView = basketDetailsBinding.scrollView;
        basketDetailsBinding.itemLayout.setVisibility(View.GONE);
        if (isConnectingToInternet(context)) {
            callBasketDetailsApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
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
        basketDetailsBinding.btnSubmit.setOnClickListener(this);
        basketDetailsBinding.tvSeeMoreReview.setOnClickListener(this);
        // BasketDetail ProductOld views
        basketDetailsBinding.llProductList.setOnClickListener(this);
        basketDetailsBinding.layoutAdded.imgPlus.setOnClickListener(this);
        basketDetailsBinding.layoutAdded.imgMinus.setOnClickListener(this);
        basketDetailsBinding.layoutAdded.tvStartDate.setOnClickListener(this);
        basketDetailsBinding.layoutAdded.btnLogin.setOnClickListener(this);

        vpImages = basketDetailsBinding.vpProductImages;
        dotsIndicator = basketDetailsBinding.dotsIndicator;
        rvProductComment = basketDetailsBinding.rvProductComment;

        //rating and feedback views
        rvProductReview = basketDetailsBinding.rvProductComment;
        ratingBarInput = basketDetailsBinding.ratingBarInput;
        etFeedback = basketDetailsBinding.etFeedback;

        tvSubAmount = basketDetailsBinding.layoutAdded.tvSubUnitPrice;
        tvSubQty = basketDetailsBinding.layoutAdded.tvSubQty;
        tvSubTotalAmount = basketDetailsBinding.layoutAdded.tvSubAmount;
        tvSubQuatity = basketDetailsBinding.layoutAdded.tvSubQuality;


        setHodeOrShowValue();


    }
    private void setReviewsHide(BasketDetailsResponse.Rating rating) {
        if (details.getAlreadyPurchased() == 0) {
            basketDetailsBinding.llRateView.setVisibility(View.GONE);
        } else if (!rating.getRatingId().isEmpty() || !rating.getReview().isEmpty()) {
            basketDetailsBinding.llRateView.setVisibility(View.GONE);
        } else if (basketDetailViewModel.alreadyPurchased.getValue() == 1) {
//            basketDetailsBinding.ratingBarInput.setEnabled(false);
            basketDetailsBinding.llRateView.setVisibility(View.VISIBLE);
        }
    }

    private void setBasketValue() {
        basketDetailViewModel.basketName.setValue(details.getBasketName());
        basketDetailViewModel.basketRate.setValue(details.getBasketRate());
        basketDetailViewModel.basketSubRate.setValue(details.getSubscriptionBasketRate());
        if (details.getIsO3().equalsIgnoreCase("yes"))
            basketDetailViewModel.isOrganic.setValue(true);
        else basketDetailViewModel.isOrganic.setValue(false);
        basketDetailViewModel.basketProducts.setValue(details.getBasketProduct());
        if (details.getInCart() == 1)
            basketDetailViewModel.isInCart.setValue(true);
        else basketDetailViewModel.isInCart.setValue(false);
        if (details.getIsFavourite() == 1)
            basketDetailViewModel.isInFav.setValue(true);
        else basketDetailViewModel.isInFav.setValue(false);
        /*Basket About and Benefit*/
        basketDetailViewModel.basketQuantity.setValue(details.getQuantity());
        basketDetailViewModel.basketDetail.setValue(details.getProductDetails());
        basketDetailViewModel.basketAbout.setValue(details.getAboutProduct());
        basketDetailViewModel.basketBenefit.setValue(details.getBenifit());
        basketDetailViewModel.basketStorageUses.setValue(details.getStoragesUses());
        basketDetailViewModel.basketOtherInfo.setValue(details.getOtherProuctInfo());
        basketDetailViewModel.basketWeightPolicy.setValue(details.getWeightPolicy());
        basketDetailViewModel.basketNutrition.setValue(details.getNutrition());
        basketDetailViewModel.basketAvgRating.setValue(details.getAverageRating());
        basketDetailViewModel.reviewLiveData.setValue(details.getReviews());
        basketDetailViewModel.alreadyPurchased.setValue(details.getAlreadyPurchased());
        /*Is in Favourite*/
        if (basketDetailViewModel.isInFav.getValue()) {
            isFavourite = true;
            basketDetailsBinding.ivFavourite.setImageResource(R.drawable.ic_filled_heart);
        } else {
            basketDetailsBinding.ivFavourite.setImageResource(R.drawable.ic_heart_without_colour);
            isFavourite = false;
        }
        /*check if eligible for Rating*/
        if (basketDetailViewModel.alreadyPurchased.getValue() == 1) {
            basketDetailsBinding.llRateView.setVisibility(View.VISIBLE);
        } else basketDetailsBinding.llRateView.setVisibility(View.GONE);

        /*Rating and reviews*/
        basketDetailViewModel.basketRating.setValue(details.getBasketRating());
        basketDetailViewModel.basketAvgRating.setValue(details.getAverageRating());
        if (details.getAverageRating() == null) {
            basketDetailsBinding.llAvgRating.setVisibility(View.GONE);
            basketDetailsBinding.ratingBelowLine.setVisibility(View.GONE);
        }
        if (details.getNoOrUsersRated() != null)
            basketDetailViewModel.basketNoOfRatings.setValue(details.getNoOrUsersRated() + " Ratings");
        else {
            basketDetailsBinding.llAvgRating.setVisibility(View.GONE);
            basketDetailsBinding.ratingBelowLine.setVisibility(View.GONE);
        }
        /*is in Cart*/
        if (basketDetailViewModel.isInCart.getValue()) {
            basketDetailsBinding.ivMinus.setVisibility(View.VISIBLE);
            basketDetailsBinding.etQuantity.setVisibility(View.VISIBLE);
            System.out.println("quantity: " + basketDetailViewModel.basketQuantity.getValue());
            if (Integer.parseInt(basketDetailViewModel.basketQuantity.getValue()) > 1) {
                basketDetailsBinding.ivMinus.setBackground(requireActivity().getDrawable(R.drawable.bg_green_square));
            } else
                basketDetailsBinding.ivMinus.setBackground(requireActivity().getDrawable(R.drawable.bg_grey_square));
        } else {
            basketDetailsBinding.ivMinus.setVisibility(View.GONE);
            basketDetailsBinding.etQuantity.setVisibility(View.GONE);
        }
        /*Average rating and reviews*/
        if (details.getNoOrUsersRated() == null) {
            basketDetailsBinding.llAvgRating.setVisibility(View.GONE);
            basketDetailsBinding.ratingBelowLine.setVisibility(View.GONE);
        } else {
            basketDetailsBinding.ratingBelowLine.setVisibility(View.VISIBLE);
            basketDetailsBinding.llAvgRating.setVisibility(View.VISIBLE);
        }
        if (details.getBasketRating().getRating() != null && !details.getBasketRating().getRating().isEmpty()) {
            basketDetailViewModel.yourRating.setValue(Float.parseFloat(details.getBasketRating().getRating()));
            ratingBarInput.setRating(basketDetailViewModel.yourRating.getValue());
            ratingBarInput.setIsIndicator(true);
        } else ratingBarInput.setIsIndicator(false);

    }

    /*Submit rating and reviews*/
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
                        callBasketDetailsApi(showCircleProgressDialog(context, ""));
                        break;
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Record not Found
                        warningToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, baseResponse.getMessage());
                        goToAskSignInSignUpScreen(baseResponse.getMessage(), context);
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

        basketDetailViewModel.callSubmitRatingResponseApi(progressDialog, submitRatingInputParameter(),
                BasketDetailFragment.this)
                .observe(getViewLifecycleOwner(), ratingResponseObserver);

    }

    //Basket Details API
    private void callBasketDetailsApi(ProgressDialog progressDialog) {
        Observer<BasketDetailsResponse> basketDetailsResponseObserver = basketDetailsResponse -> {
            if (basketDetailsResponse != null) {
                progressDialog.dismiss();
                switch (basketDetailsResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (basketDetailsResponse.getBasketDetail() != null) {
                            rlRefreshPage.setRefreshing(false);
                            basketDetailsBinding.itemLayout.setVisibility(View.VISIBLE);
                            BasketDetailsResponse.BasketDetails basket = basketDetailsResponse.getBasketDetail();
                            details = basket;
                            hideOrShowProductDetails();
                            setViewPagerAdapterItems();
                            setBasketValue();
                            basketDetailsBinding.setBasketViewModel(basketDetailViewModel);
                            basketDetailsBinding.setVariable(BR.basketViewModel, basketDetailViewModel);
                            setBasketContents();

                            /*Rating and Reviews*/
                            if (basketDetailsResponse.getBasketDetail().getBasketRating() != null) {
                                setReviewsHide(basketDetailsResponse.getBasketDetail().getBasketRating());
                            }
                            /*Raviews and rating*/
                            if (basketDetailsResponse.getBasketDetail().getReviews() != null
                                    && !basketDetailsResponse.getBasketDetail().getReviews().isEmpty()) {
                                reviewsArrayList = new ArrayList<>();
                                basketDetailViewModel.reviewLiveData.setValue(basketDetailsResponse.getBasketDetail().getReviews());
                                setBasketReviewsView();
                            }
                            /*set Basket Subscription view*/
                            if (basketDetailsResponse.getSubscription() != null
                                    && basketDetailsResponse.getSubscription().getSubscriptionTypes().size() > 0) {
                                /*init subscription*/
                                setBasketSubscription(basketDetailsResponse.getSubscription());
                            }
                            basketDetailsBinding.layoutAdded.setSubscriptionModule(basketDetailViewModel);
                            basketDetailsBinding.layoutAdded.setVariable(BR.subscriptionModule, basketDetailViewModel);
                            basketDetailsBinding.executePendingBindings();
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


    /*Reviews and comments list*/
    private void setBasketReviewsView() {
        reviewsArrayList.clear();
        basketDetailViewModel.reviewLiveData.observe(getViewLifecycleOwner(), reviews -> {
            if (reviews != null && reviews.size() > 0) {
                reviewsArrayList.addAll(reviews);
                rvProductComment.setLayoutManager(new LinearLayoutManager(context));
                rvProductComment.setHasFixedSize(true);
                reviewsAdapter = new ProductRatingReviewAdapter(context, reviewsArrayList, 0);
                rvProductComment.setAdapter(reviewsAdapter);
                basketDetailsBinding.tvSeeMoreReview.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setBasketSubscription(BasketDetailsResponse.Subscription subscription) {
        basketDetailViewModel.subscriptionStartDateMutable.setValue(subscription.getDeliveryDate());
        basketDetailViewModel.subscriptionTypeMutableList.setValue(subscription.getSubscriptionTypes());
        basketDetailViewModel.subscriptionTypeMutableList.observe(getViewLifecycleOwner(),
                subscriptionTypes -> {
                    if (subscriptionTypes != null) {
                        SubscriptionAdapter subscribeTypeAdapter = new SubscriptionAdapter(subscriptionTypes,context,this);
                        basketDetailsBinding.layoutAdded.rvSubType.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false));
                        basketDetailsBinding.layoutAdded.rvSubType.setAdapter(subscribeTypeAdapter);
                    }
                });

//        float TotalAmount = basketDetailViewModel.basketQuantity * Integer.parseInt(basketDetailsBinding.layoutAdded.tvSubQty.getText().toString().trim());
    }

    //Add to Cart Basket API
    private void callAddToCartBasketApi(ProgressDialog progressDialog) {
        Observer<BaseResponse> addToCartBasketObserver = baseResponse -> {
            if (baseResponse != null && progressDialog != null) {
                progressDialog.dismiss();
                Log.e(TAG, "callAddToCartBasketApi: " + baseResponse.getMessage());
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        try {
                            ((HomeActivity) context).setCountBudge(baseResponse.getCartItems());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        successToast(context, baseResponse.getMessage());
                        basketDetailViewModel.isInCart.setValue(true);
                        details.setQuantity("1");
                        setBasketValue();
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

    //increase quantity of basket
    private void updateQuantityApi(ProgressDialog progressDialog, int quantity) {
        Observer<BaseResponse> addToCartBasketObserver = baseResponse -> {
            if (baseResponse != null && progressDialog != null) {
                progressDialog.dismiss();
                Log.e(TAG, "callUpdateCartBasketApi: " + baseResponse.getMessage());
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        try {
                            ((HomeActivity) context).setCountBudge(baseResponse.getCartItems());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        successToast(context, baseResponse.getMessage());
                        details.setQuantity(String.valueOf(quantity));
                        setBasketValue();
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
                        if (addToFav) {
                            isFavourite = true;
                            basketDetailsBinding.ivFavourite.setImageResource(R.drawable.ic_filled_heart);
                        } else {
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
//        basketProducts = new ArrayList<>();
//        basketProducts = details.getBasketProduct();
        if (basketDetailViewModel.basketProducts.getValue().size() > 0) {
            rvBasketProducts = basketDetailsBinding.rvBasketContents;
            linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            rvBasketProducts.setLayoutManager(linearLayoutManager);
            basketProductAdapter = new BasketProductAdapter(basketDetailViewModel.basketProducts.getValue());
            rvBasketProducts.setAdapter(basketProductAdapter);
        }
    }

    private HashMap<String, String> basketParams(boolean addTo, boolean favTo) {
        HashMap<String, String> map = new HashMap<>();
        if (addTo) {
            if (basketDetailsBinding.etQuantity.getText().toString().isEmpty()) {
                map.put(QUANTITY, "1");
            } else {
                map.put(QUANTITY, basketDetailsBinding.etQuantity.getText().toString());
            }
        }
        if (favTo)
            map.put(ITEM_TYPE, "basket");
        map.put(BASKET_ID, basketId);
        return map;
    }

    private void setHodeOrShowValue() {
        //hide all expanded views initially

        hideOrShowAboutThisProduct();
        hideOrShowBenefits();
        hideOrShowStorageAndUses();
        hideOrShowOtherProductInfo();
        hideOrShowVariableWeightPolicy();
        hideOrShowNutritionDetails();
    }

    private void setViewPagerAdapterItems() {
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < details.getBasketImgs().size(); i++)
            images.add(details.getBasketImgs().get(i).getBasketImg());
        count = details.getBasketImgs().size();
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
        callAddOrRemoveFavouriteApi(showCircleProgressDialog(context, ""), !isFavourite);
    }

    private void increaseQuantity(String qty, CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        quantity++;
        etQuantity.setText(String.valueOf(quantity));
        AppUtils.setMinusButton(quantity, view);
        details.setQuantity(String.valueOf(quantity));
        updateQuantityApi(showCircleProgressDialog(context, ""), quantity);
    }

    private void decreaseQuantity(String qty, CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        if (quantity == 1) {
            warningToast(requireActivity(), getString(R.string.quantity_less_than_one));
        } else {
            quantity--;
            etQuantity.setText(String.valueOf(quantity));
            details.setQuantity(String.valueOf(quantity));
            updateQuantityApi(showCircleProgressDialog(context, ""), quantity);
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
                        basketDetailViewModel.subscriptionStartDateMutable.setValue(txtDisplayDate);
                        callDeliverySlotByDateAPI(showCircleProgressDialog(context, ""));
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
                if (Integer.parseInt(basketDetailsBinding.etQuantity.getText().toString()) > 1) {
                    decreaseQuantity(basketDetailsBinding.etQuantity.getText().toString(),
                            basketDetailsBinding.etQuantity, basketDetailsBinding.ivMinus);
                } else basketDetailsBinding.ivMinus.setEnabled(false);

                break;
            case R.id.iv_plus:
                addOrRemoveFromCart();
                break;

            case R.id.iv_favourite:
                addOrRemoveFromFavourite();
                break;

            case R.id.btn_submit:
                if (isConnectingToInternet(context)) {
                    if (!Objects.requireNonNull(etFeedback.getText()).toString().isEmpty() && !(ratingBarInput.getRating() == 0.0)) {
                        callRatingAndReviewAPi(showCircleProgressDialog(context, ""));
                    } else {
                        errorToast(context, "Please fill the field");
                    }
                } else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
                break;
            case R.id.tv_see_more_review:
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(REVIEW_LIST, reviewsArrayList);
                NavHostFragment.findNavController(BasketDetailFragment.this).
                        navigate(R.id.action_nav_basket_details_to_nav_product_review, bundle);
                break;

                //subscription basket details

            case R.id.img_minus:
                decreaseQuantitySubscriptionBasket(basketDetailsBinding.layoutAdded.tvSubQty.getText().toString(),
                        basketDetailsBinding.layoutAdded.tvSubQty, basketDetailsBinding.layoutAdded.imgMinus);
                break;

            case R.id.img_plus:
                increaseQuantitySubscriptionBasket(basketDetailsBinding.layoutAdded.tvSubQty.getText().toString(),
                        basketDetailsBinding.layoutAdded.tvSubQty, basketDetailsBinding.layoutAdded.imgMinus);
                break;
            case R.id.tv_start_date:
                showCalendar(basketDetailsBinding.layoutAdded.tvStartDate);
                break;
            case R.id.btn_login:
                Bundle bundle1 = new Bundle();
                //bundle1.putString(SUBSCRIPTION_TYPE, subTypeId);
                bundle1.putBoolean("isSubscriptionBasketVisible", false);
                NavHostFragment.findNavController(BasketDetailFragment.this).
                        navigate(R.id.action_nav_basket_details_to_nav_order_summary,bundle1);
               /* strSubscriptionBasket = tvSubQuatity.getText().toString();
                subStartDate = basketDetailsBinding.layoutAdded.tvStartDate.getText().toString();
                if(!strSubscriptionBasket.isEmpty() && !subStartDate.isEmpty() && !subTypeId.isEmpty()
                    && !slotStartTime.isEmpty() && !slotEndTime.isEmpty()){
                    callSubscribeBasketApi(showCircleProgressDialog(context, ""));
                }else {
                    warningToast(context, "Please check the details ");
                }*/
                break;

        }
    }

    private void addOrRemoveFromCart() {
        if (!basketDetailViewModel.isInCart.getValue() || basketDetailsBinding.etQuantity.getText().toString().isEmpty()) {
            basketDetailsBinding.etQuantity.setText("");
            callAddToCartBasketApi(showCircleProgressDialog(context, ""));
        } else {
            basketDetailsBinding.ivMinus.setEnabled(true);
            increaseQuantity(basketDetailsBinding.etQuantity.getText().toString(),
                    basketDetailsBinding.etQuantity, basketDetailsBinding.ivPlus);
        }
    }

    private HashMap<String, String> submitRatingInputParameter() {
        HashMap<String, String> map = new HashMap<>();
        map.put(BASKET_ID, basketId);
        map.put(RATING, String.valueOf(ratingBarInput.getRating()));
        map.put(REVIEW, etFeedback.getText().toString().trim());
        return map;
    }

    /*Subscribe Basket parameter and api */

   /* private void callSubscribeBasketApi(ProgressDialog progressDialog){
        Observer<BaseResponse> baseResponseObserver = baseResponse -> {
            if (baseResponse != null) {
                Log.e("Subscribe Basket  Api ResponseData", new Gson().toJson(baseResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, baseResponse.getMessage());

                      //  callOrderDetailsApi(showCircleProgressDialog(context, ""));

                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, baseResponse.getMessage());

                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(baseResponse.getMessage(), context);
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
        basketDetailViewModel.getSubscrideBasketApi(context, progressDialog, SubscriptionBasketInputParameter(), BasketDetailFragment.this)
                .observe(getViewLifecycleOwner(), baseResponseObserver);
    }

    private HashMap<String, String> SubscriptionBasketInputParameter(){
        HashMap<String, String> map = new HashMap<>();

        map.put(SUBSCRIPTION_TYPE, subTypeId);
        map.put(NO_OF_SUBSCRIPTION, String.valueOf(strSubscriptionBasket));
        map.put(START_DATE, subStartDate);
        map.put(SLOT_ID, subscriptionSlotId);
        map.put(PAYMENT_MODE_ID, "1");
        map.put(BASKET_ID, basketId);
        map.put(SLOT_START_TIME, slotStartTime);
        map.put(SLOT_END_TIME, slotEndTime);

        return map;
    }*/

    @SuppressLint("SetTextI18n")
    private void increaseQuantitySubscriptionBasket(String qty , CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        quantity++;
        etQuantity.setText(String.valueOf(quantity));
        AppUtils.setMinusButton(quantity, view);
        details.setQuantity(String.valueOf(quantity));


           try {
            int multiplication = Integer.parseInt(details.getBasketRate()) * Integer.parseInt(String.valueOf(quantity));
            tvSubTotalAmount.setText("Rs. "+ String.valueOf(multiplication));

        }catch (NullPointerException e) {
            e.printStackTrace();
        }

        tvSubQuatity.setText(String.valueOf(quantity));


    }

    @SuppressLint("SetTextI18n")
    private void decreaseQuantitySubscriptionBasket(String qty, CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        if (quantity == 1) {
            warningToast(requireActivity(), getString(R.string.quantity_less_than_one));
        } else {
            quantity--;
            etQuantity.setText(String.valueOf(quantity));
            details.setQuantity(String.valueOf(quantity));


            try {
                int multiplication = Integer.parseInt(details.getBasketRate()) * Integer.parseInt(String.valueOf(quantity));
                tvSubTotalAmount.setText(String.valueOf(multiplication));

            }catch (NullPointerException e) {
                e.printStackTrace();
            }
            tvSubQuatity.setText("Rs. "+String.valueOf(quantity));

        }
        AppUtils.setMinusButton(quantity, view);
    }

    /*Call slots by Date*/
    private void callDeliverySlotByDateAPI(ProgressDialog progressDialog) {
        Observer<OrderSummaryResponse> deliverySlotResponseObserver = delverySlotResponse -> {
            if (delverySlotResponse != null) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    switch (delverySlotResponse.status) {
                        case STATUS_CODE_200://Record Create/Update Successfully
                            basketDetailViewModel.subscriptionDeliverySlotsLists.setValue(delverySlotResponse.getDelivery().getDeliverySlots());
                            basketDetailViewModel.subscriptionDeliverySlotsLists.observe(getViewLifecycleOwner(),deliverySlots -> {
                                if (deliverySlots.size()>0){
                                    SlotAdaptor slotAdaptor = new SlotAdaptor(getContext(), deliverySlots);
                                    spinerSlots.setAdapter(slotAdaptor);

                                    spinerSlots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            hideKeyBoard(requireActivity());
                                            subscriptionSlotId = deliverySlots.get(i).slotId;
                                            slotStartTime = deliverySlots.get(i).slotStartTime;
                                            slotEndTime = deliverySlots.get(i).slotEndTime;
                                            basketDetailViewModel.subscriptionSlotMutable.setValue(deliverySlots.get(i));
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                            hideKeyBoard(requireActivity());
                                        }
                                    });
                                }
                            });
                            break;
                        case STATUS_CODE_403://Validation Errors
                        case STATUS_CODE_400://Validation Errors
                        case STATUS_CODE_404://Validation Errors
                            //show no data msg here
                            warningToast(context, delverySlotResponse.getMessage());
                            break;
                        case STATUS_CODE_401://Unauthorized user
                            goToAskSignInSignUpScreen(delverySlotResponse.getMessage(), context);
                            break;
                        case STATUS_CODE_405://Method Not Allowed
                            infoToast(context, delverySlotResponse.getMessage());
                            break;
                    }
                }
            }
        };
        basketDetailViewModel.deliverySlotResponseLiveData(progressDialog, deliverySlotParam(),
                BasketDetailFragment.this)
                .observe(getViewLifecycleOwner(), deliverySlotResponseObserver);
    }

    private HashMap<String, String> deliverySlotParam(){
        HashMap<String, String> map = new HashMap<>();

        map.put(DELIVERY_DATE, preferences.getDeliveryDate());
        return map;
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
                        callAddOrRemoveFavouriteApi(showCircleProgressDialog(context, ""), !isFavourite);
                        break;

                }
            }
        }, context);

    }

    @Override
    public void OnSubTypeClick(BasketDetailsResponse.SubscriptionType type) {
        subTypeId = type.getSubscriptionType();
    }
}