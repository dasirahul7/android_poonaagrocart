package com.poona.agrocart.ui.home;

import static com.poona.agrocart.app.AppConstants.AllBasket;
import static com.poona.agrocart.app.AppConstants.AllExclusive;
import static com.poona.agrocart.app.AppConstants.AllSelling;
import static com.poona.agrocart.app.AppConstants.BASKET_ID;
import static com.poona.agrocart.app.AppConstants.CATEGORY_ID;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.LIST_TITLE;
import static com.poona.agrocart.app.AppConstants.LIST_TYPE;
import static com.poona.agrocart.app.AppConstants.PRODUCT_ID;
import static com.poona.agrocart.app.AppConstants.PU_ID;
import static com.poona.agrocart.app.AppConstants.QUANTITY;
import static com.poona.agrocart.app.AppConstants.SEARCH_KEY;
import static com.poona.agrocart.app.AppConstants.SEARCH_PRODUCT;
import static com.poona.agrocart.app.AppConstants.SEARCH_TYPE;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.data.network.reponses.BaseResponse;
import com.poona.agrocart.data.network.reponses.ExclusiveResponse;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.BannerResponse;
import com.poona.agrocart.data.network.reponses.BasketResponse;
import com.poona.agrocart.data.network.reponses.BestSellingResponse;
import com.poona.agrocart.data.network.reponses.CategoryResponse;
import com.poona.agrocart.data.network.reponses.ProductListResponse;
import com.poona.agrocart.data.network.reponses.SeasonalProductResponse;
import com.poona.agrocart.data.network.reponses.StoreBannerResponse;
import com.poona.agrocart.databinding.FragmentHomeBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.adapter.BannerAdapter;
import com.poona.agrocart.ui.home.adapter.BasketAdapter;
import com.poona.agrocart.ui.home.adapter.ExclusiveOfferListAdapter;
import com.poona.agrocart.ui.home.adapter.CategoryAdapter;
import com.poona.agrocart.ui.home.adapter.ProductListAdapter;
import com.poona.agrocart.ui.home.adapter.SeasonalBannerAdapter;
import com.poona.agrocart.ui.home.model.ProductOld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {

    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 3000;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding fragmentHomeBinding;
    //Adapters here
    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductListAdapter productListAdapter;
    private ExclusiveOfferListAdapter bestsellingAdapter;
    private ExclusiveOfferListAdapter offerListAdapter;
    private BasketAdapter basketAdapter;
    private SeasonalBannerAdapter seasonalBannerAdapter;
    //ArrayLists here
    private ArrayList<ProductListResponse.Product> bestSellings = new ArrayList<>();
    private ArrayList<ProductListResponse.Product> offerProducts = new ArrayList<>();
    private ArrayList<ProductListResponse.Product> productList = new ArrayList<>();
    private ArrayList<BannerResponse.Banner> banners = new ArrayList<>();
    private ArrayList<CategoryResponse.Category> categories = new ArrayList<>();
    private ArrayList<BasketResponse.Basket> baskets = new ArrayList<>();
    private ArrayList<SeasonalProductResponse.SeasonalProduct> seasonalProductList = new ArrayList<>();
    private final ArrayList<ProductOld> mCartList = new ArrayList<ProductOld>();
    private ArrayList<StoreBannerResponse.StoreBanner> storeBannerList = new ArrayList<>();
    private ArrayList<String> BasketIds = new ArrayList<>();

    private int limit = 10;
    private int offset = 0;
    private View root;
    private int currentBanner = 0;
    private int NumberOfBanners = 0;
    private Timer bannerTimer;
    Timer timer = new Timer();
    boolean isTyping = false;
    private String BeforeSerach;
    private int visibleCategoryCount;
    private LinearLayoutManager categoryManager;
    private String scrollOrListing;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        root = fragmentHomeBinding.getRoot();
        clearLists();
        if (isConnectingToInternet(context)) {
            callBannerApi(showCircleProgressDialog(context, ""), limit, offset);
            callCategoryApi(root, showCircleProgressDialog(context, ""), limit, offset);
            callBasketApi(root, showCircleProgressDialog(context, ""), limit, offset);
            callExclusiveOfferApi(root, showCircleProgressDialog(context, ""), limit, offset);
            callBestSellingApi(root, showCircleProgressDialog(context, ""), limit, offset);
            callSeasonalProductApi(root, showCircleProgressDialog(context, ""), limit, offset);
            callProductListApi(root, showCircleProgressDialog(context, ""), limit, offset);
            callStoreBannerApi(root, showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
        getBasketItems();
//        setStoreBanner(root);
        initClick();
        bannerAutoSLider();

        checkEmpties();
        setPaginationForLists();
        return root;

    }

    private void setCategoryRv() {
        categoryManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        categoryAdapter = new CategoryAdapter(categories, requireActivity(), root, category -> {
            String cat_id = category.getId();
            Bundle bundle = new Bundle();
            bundle.putString(CATEGORY_ID, cat_id);
            bundle.putString(LIST_TITLE, category.getCategoryName());
            bundle.putString(LIST_TYPE, category.getCategoryType());
            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_products_list, bundle);
        });
        fragmentHomeBinding.recCategory.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recCategory.setAdapter(categoryAdapter);
        fragmentHomeBinding.recCategory.setLayoutManager(categoryManager);
    }

    private void setPaginationForLists() {
        fragmentHomeBinding.recCategory.setNestedScrollingEnabled(true);
        fragmentHomeBinding.recBasket.setNestedScrollingEnabled(true);
        fragmentHomeBinding.recBestSelling.setNestedScrollingEnabled(true);
        fragmentHomeBinding.recProduct.setNestedScrollingEnabled(true);
//        category scrolling
        fragmentHomeBinding.recCategory.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            visibleCategoryCount = categoryManager.getItemCount();

        });
    }

    private void checkEmpties() {
        //check if banner is empty
        if (banners == null || banners.size() == 0) {
            makeInVisible(fragmentHomeBinding.viewPagerBanner, fragmentHomeBinding.dotsIndicator);
        }
        //check if category is empty
//        if (categories == null || categories.size() == 0) {
//            makeInVisible(fragmentHomeBinding.recCategory, fragmentHomeBinding.rlCategory);
//        }
        //check if Basket is empty
        if (baskets == null || baskets.size() == 0) {
            makeInVisible(fragmentHomeBinding.recBasket, fragmentHomeBinding.rlBasket);
        }
        //check if Best selling is Empty
        if (bestSellings == null || bestSellings.size() == 0) {
            makeInVisible(fragmentHomeBinding.recBestSelling, fragmentHomeBinding.rlBestSelling);
        }
        //check if seasonalProductList is Empty
        if (seasonalProductList == null || seasonalProductList.size() == 0) {
            makeInVisible(fragmentHomeBinding.recSeasonal, null);
        }
        // check if exclusive offer is Empty
        if (offerProducts == null || offerProducts.size() == 0) {
            makeInVisible(fragmentHomeBinding.recExOffers, fragmentHomeBinding.rlExclusiveOffer);
        }
        //check if storeBannerList is Empty
        if (storeBannerList == null || storeBannerList.size() == 0) {
            makeInVisible(fragmentHomeBinding.cardviewOurShops, null);
            makeInVisible(null, fragmentHomeBinding.rlO3Banner);
        }
        // check if productList is Empty
        if (productList == null || productList.size() == 0) {
            makeInVisible(fragmentHomeBinding.recProduct, null);
        }

    }

    private void clearLists() {
        banners.clear();
    }

    private void searchProducts(View root) {
        fragmentHomeBinding.etSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                BeforeSerach = fragmentHomeBinding.etSearch.getText().toString().trim();
            }

            public void afterTextChanged(Editable arg0) {
                if (!isTyping) {
                    Log.d(TAG, "started typing");
                    isTyping = true;
                }
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        isTyping = false;
                        Log.d(TAG, "stopped typing");
                        timer.cancel();
                        try {
                            hideKeyBoard(requireActivity());
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        if (fragmentHomeBinding.etSearch.getText() != null) {
                                            if (!fragmentHomeBinding.etSearch.getText().toString().trim().equals("")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putString(SEARCH_TYPE, SEARCH_PRODUCT);
                                                bundle.putString(SEARCH_KEY, fragmentHomeBinding.etSearch.getText().toString());
                                                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_searchFragment, bundle);
                                            } else return;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);
            }
        });

    }

    //Store Banner data here
    private void callStoreBannerApi(View root, ProgressDialog progressDialog) {
        Observer<StoreBannerResponse> bannerResponseObserver = storeBannerResponse -> {
            if (storeBannerResponse != null) {
                progressDialog.dismiss();
                Log.e(TAG, "callStoreBannerApi: " + storeBannerResponse.getStoreBanners().size());
                switch (storeBannerResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (storeBannerResponse.getStoreBanners().size() > 0) {
                            makeVisible(fragmentHomeBinding.cardviewOurShops, null);
                            makeVisible(null, fragmentHomeBinding.rlO3Banner);
                            storeBannerList = storeBannerResponse.getStoreBanners();
                            StoreBannerResponse.StoreBanner storeBanner = storeBannerResponse.getStoreBanners().get(0);
                            fragmentHomeBinding.setStoreBanner(storeBanner);
                            fragmentHomeBinding.setVariable(BR.storeBanner, storeBanner);
                            fragmentHomeBinding.executePendingBindings();
                            fragmentHomeBinding.cardviewOurShops.setOnClickListener(v -> {
                                redirectToOurShops(root);
                            });
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, storeBannerResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(storeBannerResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, storeBannerResponse.getMessage());
                        break;
                }

            }
        };
        homeViewModel.storeBannerResponseLiveData(progressDialog, HomeFragment.this)
                .observe(getViewLifecycleOwner(), bannerResponseObserver);
    }

    // ProductOld listing here
    private void callProductListApi(View root, ProgressDialog progressDialog, int limit, int offset) {
        Observer<ProductListResponse> productListResponseObserver = productListResponse -> {
            if (productListResponse != null) {
                progressDialog.dismiss();
                Log.e(TAG, "callProductListApi: " + productListResponse.getProductResponseDt().getProductList().size());
                switch (productListResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (productListResponse.getProductResponseDt().getProductList().size() > 0) {
                            makeVisible(fragmentHomeBinding.recProduct, null);
                            for (ProductListResponse.Product product : productListResponse.getProductResponseDt().getProductList()) {
                                product.setUnit(product.getProductUnits().get(0));
                                product.setAccurateWeight(product.getUnit().getWeight() + product.getUnit().getUnitName());
                                productList.add(product);
                            }
//                            productList = productListResponse.getProductResponseDt().getProductList();
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                            fragmentHomeBinding.recProduct.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recProduct.setHasFixedSize(true);
                            fragmentHomeBinding.recProduct.setLayoutManager(linearLayoutManager);
                            productListAdapter = new ProductListAdapter(productList, getActivity(), this::toProductDetail);
                            fragmentHomeBinding.recProduct.setAdapter(productListAdapter);
//

                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, productListResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(productListResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, productListResponse.getMessage());
                        break;
                }

            }
        };
        homeViewModel.homeProductListResponseLiveData(progressDialog,
                listingParams(limit, offset, ""), HomeFragment.this)
                .observe(getViewLifecycleOwner(), productListResponseObserver);
    }


    //Seasonal ProductOld listing here
    private void callSeasonalProductApi(View root, ProgressDialog progressDialog, int limit, int offset) {
        Observer<SeasonalProductResponse> seasonalProductObserver = seasonalProductResponse -> {
            if (seasonalProductResponse != null) {
                progressDialog.dismiss();
                Log.e("SeasonalProduct Api Response", new Gson().toJson(seasonalProductResponse));
                switch (seasonalProductResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (seasonalProductResponse.getSeasonalProducts().size() > 0) {
                            for (int i = 0; i < seasonalProductResponse.getSeasonalProducts().size(); i++) {
                                if (i / 2 == 0) {
                                    seasonalProductResponse.getSeasonalProducts().get(i).setType("Green");
                                } else
                                    seasonalProductResponse.getSeasonalProducts().get(i).setType("Yellow");
                            }
                            makeVisible(fragmentHomeBinding.recSeasonal, null);
                            seasonalProductList = seasonalProductResponse.getSeasonalProducts();
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
                            fragmentHomeBinding.recSeasonal.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recSeasonal.setHasFixedSize(true);
                            fragmentHomeBinding.recSeasonal.setLayoutManager(linearLayoutManager);
                            seasonalBannerAdapter = new SeasonalBannerAdapter(getActivity(), seasonalProductList, seasonalProduct -> {
                                Bundle bundle = new Bundle();
                                bundle.putString("image", seasonalProduct.getProduct_image());
                                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_seasonalRegFragment);
                            });
                            fragmentHomeBinding.recSeasonal.setAdapter(seasonalBannerAdapter);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, seasonalProductResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(seasonalProductResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, seasonalProductResponse.getMessage());
                        break;
                }

            }
        };
        homeViewModel.seasonalResponseLiveData(progressDialog, listingParams(limit, offset, ""), HomeFragment.this)
                .observe(getViewLifecycleOwner(), seasonalProductObserver);

    }

    //Best selling Data listing here
    private void callBestSellingApi(View root, ProgressDialog showCircleProgressDialog, int limit, int offset) {
        Observer<BestSellingResponse> bestSellingResponseObserver = bestSellingResponse -> {
            if (bestSellingResponse != null) {
                showCircleProgressDialog.dismiss();
                Log.e("Best selling Api Response", new Gson().toJson(bestSellingResponse));
                switch (bestSellingResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (bestSellingResponse.getBestSellingData().getBestSellingProductList().size() > 0) {
                            makeVisible(fragmentHomeBinding.recBestSelling, fragmentHomeBinding.rlBestSelling);
                            for (ProductListResponse.Product product : bestSellingResponse.getBestSellingData().getBestSellingProductList()) {
                                product.setUnit(product.getProductUnits().get(0));
                                product.setAccurateWeight(product.getUnit().getWeight() + product.getUnit().getUnitName());
                                bestSellings.add(product);
                            }

//                            bestSellings = bestSellingResponse.getBestSellingData().getBestSellingProductList();
                            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
                            bestsellingAdapter = new ExclusiveOfferListAdapter(bestSellings, requireActivity(), root, this::toProductDetail, product -> {
                                addToCartProduct(showCircleProgressDialog(context,""),product,bestsellingAdapter);
                            });
                            fragmentHomeBinding.recBestSelling.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recBestSelling.setLayoutManager(layoutManager);
                            fragmentHomeBinding.recBestSelling.setAdapter(bestsellingAdapter);
                            // Redirect to ProductOld details
//
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, bestSellingResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(bestSellingResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, bestSellingResponse.getMessage());
                        break;
                }
            }

        };
        homeViewModel.bestSellingResponseLiveData(showCircleProgressDialog,
                listingParams(limit, offset, ""), HomeFragment.this)
                .observe(getViewLifecycleOwner(), bestSellingResponseObserver);
    }

    private void addToCartProduct(ProgressDialog progressDialog, ProductListResponse.Product product, ExclusiveOfferListAdapter adapter) {
        Observer<BaseResponse> baseResponseObserver= response -> {
            if (response!=null){
                progressDialog.dismiss();
                Log.e(TAG, "addToCartProduct: "+new Gson().toJson(response));
                switch (response.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context,response.getMessage());
//                        callBestSellingApi(root,showCircleProgressDialog(context,""),limit,offset);
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, response.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(response.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, response.getMessage());
                        break;
                }

            }
        };
        homeViewModel.addToCartProductLiveData(progressDialog,addtoCartParam(product),HomeFragment.this)
                .observe(getViewLifecycleOwner(),baseResponseObserver);
    }

    //Product Offer Data listing
    private void callExclusiveOfferApi(View root, ProgressDialog progressDialog, int limit, int offset) {
        Observer<ExclusiveResponse> exclusiveResponseObserver = exclusiveResponse -> {
            if (exclusiveResponse != null) {
                progressDialog.dismiss();
                Log.e("Category Api Response", new Gson().toJson(exclusiveResponse));
                switch (exclusiveResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (exclusiveResponse.getExclusiveData().getExclusivesList().size() > 0) {
                            for (ProductListResponse.Product product : exclusiveResponse.getExclusiveData().getExclusivesList()) {
                                product.setUnit(product.getProductUnits().get(0));
                                product.setAccurateWeight(product.getUnit().getWeight() + product.getUnit().getUnitName());
                                offerProducts.add(product);
                            }

//                            offerProducts = exclusiveResponse.getExclusiveData().getExclusivesList();
                            makeVisible(fragmentHomeBinding.recExOffers, fragmentHomeBinding.rlExclusiveOffer);
                            // Redirect to ProductOld details
                            offerListAdapter = new ExclusiveOfferListAdapter(offerProducts, getActivity(), root, this::toProductDetail, product -> {
                                addToCartProduct(showCircleProgressDialog(context,""),product,offerListAdapter);
                            });
                            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
                            fragmentHomeBinding.recExOffers.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recExOffers.setLayoutManager(layoutManager);
                            fragmentHomeBinding.recExOffers.setAdapter(offerListAdapter);
//
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, exclusiveResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(exclusiveResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, exclusiveResponse.getMessage());
                        break;
                }
            }

        };
        homeViewModel.exclusiveResponseLiveData(progressDialog, listingParams(limit, offset, ""), HomeFragment.this)
                .observe(getViewLifecycleOwner(), exclusiveResponseObserver);
    }


    private void makeVisible(View recyclerView, View relativeLayout) {
        try {
            if (recyclerView != null) {
                recyclerView.setVisibility(View.VISIBLE);
            }
            if (relativeLayout != null) {
                relativeLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void makeInVisible(View recyclerView, View relativeLayout) {
        try {
            if (recyclerView != null) {
                recyclerView.setVisibility(View.GONE);
            }
            if (relativeLayout != null) {
                relativeLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initClick() {
        fragmentHomeBinding.tvAllCategory.setOnClickListener(this);
        fragmentHomeBinding.tvAllBasket.setOnClickListener(this);
        fragmentHomeBinding.tvAllExclusive.setOnClickListener(this);
        fragmentHomeBinding.tvAllSelling.setOnClickListener(this);
    }

    private void getBasketItems() {
        homeViewModel.getSavesProductInBasket().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                for (ProductOld saved : products)
                    BasketIds.add(saved.getId());
            }
        });
    }

    private void setStoreBanner(View root) {
        AppUtils.setImage(fragmentHomeBinding.imgStore, "https://www.linkpicture.com/q/store-2.png");

    }

    private void redirectToOurShops(View v) {
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_our_stores);
    }


    //Basket APi Response gere
    private void callBasketApi(View root, ProgressDialog progressDialog, int limit, int offset) {
        Observer<BasketResponse> bannerResponseObserver = basketResponse -> {
            if (basketResponse != null) {
                progressDialog.dismiss();
                Log.e("Category Api Response", new Gson().toJson(basketResponse));
                switch (basketResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (basketResponse.getData().getBaskets().size() > 0) {
                            makeVisible(fragmentHomeBinding.recBasket, fragmentHomeBinding.rlBasket);
                            this.baskets = basketResponse.getData().getBaskets();
                            basketAdapter = new BasketAdapter(this.baskets, getActivity(), this::toBasketDetail);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
                            fragmentHomeBinding.recBasket.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recBasket.setLayoutManager(layoutManager);
                            fragmentHomeBinding.recBasket.setAdapter(basketAdapter);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, basketResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(basketResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, basketResponse.getMessage());
                        break;
                }
            }

        };
        homeViewModel.basketResponseLiveData(progressDialog, listingParams(limit, offset, ""), HomeFragment.this)
                .observe(getViewLifecycleOwner(), bannerResponseObserver);

    }


    private void callCategoryApi(View view, ProgressDialog progressDialog, int limit, int offset) {
        Observer<CategoryResponse> categoryResponseObserver = categoryResponse -> {
            if (categoryResponse != null) {
                progressDialog.dismiss();
                Log.e("Category Api Response", new Gson().toJson(categoryResponse));
                switch (categoryResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (categoryResponse.getCategoryData() != null) {
                            if (categoryResponse.getCategoryData().getCategoryList().size() > 0) {
                                makeVisible(fragmentHomeBinding.recCategory, fragmentHomeBinding.rlCategory);
                                categories = categoryResponse.getCategoryData().getCategoryList();
                                setCategoryRv();
                            }
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        Log.e(TAG, "callCategoryApi: " + categoryResponse.getMessage());
                        warningToast(context, categoryResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(categoryResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, categoryResponse.getMessage());
                        break;
                }
            }
        };
        homeViewModel.categoryResponseLiveData(progressDialog, listingParams(limit, offset, ""), HomeFragment.this)
                .observe(getViewLifecycleOwner(), categoryResponseObserver);

    }

    //Banner API here
    private void callBannerApi(ProgressDialog progressDialog, int limit, int offset) {
        Observer<BannerResponse> bannerResponseObserver = bannerResponse -> {
            if (bannerResponse != null) {
                progressDialog.dismiss();
                Log.e("Banner Api Response", new Gson().toJson(bannerResponse));
                switch (bannerResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully

                        if (bannerResponse.getData().getBanners().size() > 0) {
                            makeVisible(fragmentHomeBinding.viewPagerBanner, fragmentHomeBinding.dotsIndicator);
                            banners = bannerResponse.getData().getBanners();
                            this.NumberOfBanners = banners.size();
                            bannerAdapter = new BannerAdapter(banners, requireActivity());
//
                            fragmentHomeBinding.viewPagerBanner.setAdapter(bannerAdapter);
                            // Set up tab indicators
                            fragmentHomeBinding.dotsIndicator.setViewPager(fragmentHomeBinding.viewPagerBanner);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        // set a dummy banner if no banner is available
                        warningToast(context, bannerResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(bannerResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, bannerResponse.getMessage());
                        break;
                }
            }

        };
        homeViewModel.bannerResponseLiveData(progressDialog, listingParams(limit, offset, ""), HomeFragment.this)
                .observe(getViewLifecycleOwner(), bannerResponseObserver);

    }

    /*Create a Dummy banner if no banner is available*/
    //Banner Auto Scroll
    private void bannerAutoSLider() {
        Log.e("SetSliderAutoTimer", "SetSliderAutoTimer");
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (HomeFragment.this.currentBanner == HomeFragment.this.NumberOfBanners) {
                    HomeFragment.this.currentBanner = 0;
                }
                try {
                    ViewPager viewPager = HomeFragment.this.fragmentHomeBinding.viewPagerBanner;
                    HomeFragment homeFragment = HomeFragment.this;
                    int i = homeFragment.currentBanner;
                    homeFragment.currentBanner = i + 1;
                    viewPager.setCurrentItem(i, true);
                } catch (Exception e) {
                }
            }
        };
        this.bannerTimer = new Timer();
        this.bannerTimer.schedule(new TimerTask() {
            public void run() {
                handler.post(update);
            }
        }, 100, 4000);
    }


    private HashMap<String, String> listingParams(int limit, int offset, String search) {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.LIMIT, String.valueOf(limit));
        map.put(AppConstants.OFFSET, String.valueOf(offset));
        map.put(AppConstants.SEARCH, search);
        return map;
    }

    @Override
    public void onResume() {
        try {
            fragmentHomeBinding.etSearch.setText("");
            activeHomeTitleBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void activeHomeTitleBar() {
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.menu_icon_toggle, null);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setNavigationIcon(d);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setPadding(10, 0, 0, 0);
        });
        if (((HomeActivity) requireActivity()).binding.appBarHome.textTitle.getVisibility() == View.VISIBLE)
            ((HomeActivity) requireActivity()).binding.appBarHome.textTitle.setVisibility(View.GONE);
        if (((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.getVisibility() == View.VISIBLE)
            ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.GONE);
        if (((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.getVisibility() == View.VISIBLE)
            ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setVisibility(View.GONE);
        if (((HomeActivity) requireActivity()).binding.appBarHome.rlProductTag.getVisibility() == View.VISIBLE)
            ((HomeActivity) requireActivity()).binding.appBarHome.rlProductTag.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.tvAddress.setVisibility(View.VISIBLE);
        ((HomeActivity) requireActivity()).binding.appBarHome.tvAddress.setText(preferences.getUserAddress());
        ((HomeActivity) requireActivity()).binding.appBarHome.logImg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentHomeBinding = null;
    }





    private HashMap<String, String> addtoCartParam(ProductListResponse.Product product) {
            HashMap<String, String> map = new HashMap<>();
            map.put(PRODUCT_ID, product.getProductId());
            map.put(PU_ID,product.getUnit().getpId());
            map.put(QUANTITY,"2");
        return map;
    }

    public void toProductDetail(ProductListResponse.Product product) {
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, product.getProductId());
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_product_details, bundle);
    }

    public void toBasketDetail(BasketResponse.Basket basket) {
        Bundle bundle = new Bundle();
        bundle.putString(BASKET_ID, basket.getId());
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_basketDetailFragment, bundle);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.tv_all_category:
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_explore);
                break;
            case R.id.tv_all_basket:
                bundle.putString(LIST_TITLE, AllBasket);
                bundle.putString(FROM_SCREEN, AllBasket);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_products_list, bundle);
                break;
            case R.id.tv_all_selling:
                bundle.putString(LIST_TITLE, AllSelling);
                bundle.putString(FROM_SCREEN, AllSelling);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_products_list, bundle);
                break;
            case R.id.tv_all_exclusive:
                bundle.putString(LIST_TITLE, AllExclusive);
                bundle.putString(FROM_SCREEN, AllExclusive);
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_products_list, bundle);
                break;
        }
    }


    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), HomeFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        // Call Banner API after network error
                        callBannerApi(showCircleProgressDialog(context, ""), limit, offset);
                        break;
                    case 1:
                        //Call Category API after network error
                        callCategoryApi(root, showCircleProgressDialog(context, ""), limit, offset);
                        break;
                    case 2:
                        //Call Basket API after network error
                        callBasketApi(root, showCircleProgressDialog(context, ""), limit, offset);
                        break;
                    case 3:
                        //Call Product API after network error
                        callExclusiveOfferApi(root, showCircleProgressDialog(context, ""), limit, offset);
                        break;
                    case 4:
                        //Call Bestselling API after network error
                        callBestSellingApi(root, showCircleProgressDialog(context, ""), limit, offset);
                        break;
                    case 5:
                        //Call Seasonal ProductOld API after network error
                        callSeasonalProductApi(root, showCircleProgressDialog(context, ""), limit, offset);
                        break;
                    case 6:
                        //Call ProductOld List API after network error
                        callProductListApi(root, showCircleProgressDialog(context, ""), limit, offset);
                        break;
                    case 7:
                        //Call ProductOld List API after network error
                        callStoreBannerApi(root, showCircleProgressDialog(context, ""));
                        break;

                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}