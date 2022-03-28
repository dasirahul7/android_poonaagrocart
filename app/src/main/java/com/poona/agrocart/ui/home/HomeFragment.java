package com.poona.agrocart.ui.home;

import static com.poona.agrocart.app.AppConstants.AllBasket;
import static com.poona.agrocart.app.AppConstants.AllExclusive;
import static com.poona.agrocart.app.AppConstants.AllSelling;
import static com.poona.agrocart.app.AppConstants.BASKET_ID;
import static com.poona.agrocart.app.AppConstants.CATEGORY_ID;
import static com.poona.agrocart.app.AppConstants.CUSTOMER_ID;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.LIST_TITLE;
import static com.poona.agrocart.app.AppConstants.LIST_TYPE;
import static com.poona.agrocart.app.AppConstants.PRODUCT_ID;
import static com.poona.agrocart.app.AppConstants.PU_ID;
import static com.poona.agrocart.app.AppConstants.QUANTITY;
import static com.poona.agrocart.app.AppConstants.SEARCH_KEY;
import static com.poona.agrocart.app.AppConstants.SEARCH_PRODUCT;
import static com.poona.agrocart.app.AppConstants.SEARCH_TYPE;
import static com.poona.agrocart.app.AppConstants.SEASONAL_P_ID;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.demono.AutoScrollViewPager;
import com.google.gson.Gson;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.BestSellingResponse;
import com.poona.agrocart.data.network.responses.CategoryResponse;
import com.poona.agrocart.data.network.responses.ExclusiveResponse;
import com.poona.agrocart.data.network.responses.HomeBasketResponse;
import com.poona.agrocart.data.network.responses.homeResponse.Banner;
import com.poona.agrocart.data.network.responses.homeResponse.Basket;
import com.poona.agrocart.data.network.responses.homeResponse.HomeResponse;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.data.network.responses.SeasonalProductResponse;
import com.poona.agrocart.data.network.responses.homeResponse.Product;
import com.poona.agrocart.data.network.responses.homeResponse.SeasonalProduct;
import com.poona.agrocart.data.network.responses.homeResponse.StoreBanner;
import com.poona.agrocart.databinding.FragmentHomeBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.adapter.BannerAdapter;
import com.poona.agrocart.ui.home.adapter.BasketAdapter;
import com.poona.agrocart.ui.home.adapter.CategoryAdapter;
import com.poona.agrocart.ui.home.adapter.ExclusiveOfferListAdapter;
import com.poona.agrocart.ui.home.adapter.ProductListAdapter;
import com.poona.agrocart.ui.home.adapter.SeasonalBannerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener, BannerAdapter.OnBannerClickListener {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int CATEGORY = 0;
    private static final int BASKET = 1;
    private static final int BEST_SELLING = 2;
    private static final int SEASONAL = 3;
    private static final int EXCLUSIVE = 4;
    private static final int PRODUCT = 5;

    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 3000;

    private final long DELAY_MS = 500;
    private final long PERIOD_MS = 3000;

    private final int limit = 8;
    private final int offset = 0;
    private Timer timer = new Timer();
    private boolean isTyping = false;

    private ActivityResultLauncher<Intent> recognizerIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                ArrayList<String> resultArrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (resultArrayList.get(0) != null && !TextUtils.isEmpty(resultArrayList.get(0))) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SEARCH_TYPE, SEARCH_PRODUCT);
                    bundle.putString(SEARCH_KEY, resultArrayList.get(0));
                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_searchFragment, bundle);
                }
            }
        }
    });
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding fragmentHomeBinding;

    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductListAdapter productListAdapter;
    private ExclusiveOfferListAdapter bestsellingAdapter;
    private ExclusiveOfferListAdapter offerListAdapter;
    private BasketAdapter basketAdapter;
    private SeasonalBannerAdapter seasonalBannerAdapter;

    //    private ArrayList<Product> bestSellings = new ArrayList<>();
//    private ArrayList<Product> offerProducts = new ArrayList<>();
//    private ArrayList<Product> productList = new ArrayList<>();
    //    private ArrayList<BannerResponse.Banner> banners = new ArrayList<>();
//    private ArrayList<Banner> banners = new ArrayList<>();
//    private ArrayList<Category> categories = new ArrayList<>();
//    private ArrayList<Basket> baskets = new ArrayList<>();
//    private ArrayList<SeasonalProduct> seasonalProductList = new ArrayList<>();
    private StoreBanner storeBanner;
    private ArrayList<String> BasketIds = new ArrayList<>();
    private int categoryOffset = 0, basketOffset = 0, bestSellingOffset = 0,
            seasonalOffset = 0, exclusiveOffset = 0, productOffset = 0;
    private View root;
    private int currentBanner = 0;
    private int NumberOfBanners = 0;
    private Timer bannerTimer;
    private String BeforeSerach;
    private int visibleCategoryCount;
    private LinearLayoutManager categoryManager, basketManager, productManager,
            seasonalManager, bestSellingManager, exclusiveOfferManager;
    private boolean scrolling = false;
    private SwipeRefreshLayout rlRefreshPage;
    private int addType;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        root = fragmentHomeBinding.getRoot();
        rlRefreshPage = fragmentHomeBinding.rlRefreshPage;

        if (isConnectingToInternet(context)) {
            clearLists();
            callHomeScreenApi(showCircleProgressDialog(context, ""));
            searchProducts();
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
        initClick();

        rlRefreshPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rlRefreshPage.setRefreshing(true);
                if (isConnectingToInternet(context)) {
                    clearLists();
                    callHomeScreenApi(showCircleProgressDialog(context, ""));
                } else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });
//        setPaginationForLists();


        return root;
    }

    private void setRvCategory() {
        categoryManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        fragmentHomeBinding.recCategory.setLayoutManager(categoryManager);
        fragmentHomeBinding.recCategory.setNestedScrollingEnabled(false);
        homeViewModel.categoryMutableList.observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter = new CategoryAdapter(categories, requireActivity(), category -> {
                String cat_id = category.getId();
                Bundle bundle = new Bundle();
                bundle.putString(CATEGORY_ID, cat_id);
                bundle.putString(LIST_TITLE, category.getCategoryName());
                bundle.putString(LIST_TYPE, category.getCategoryType());
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_products_list, bundle);
            });

            fragmentHomeBinding.recCategory.setAdapter(categoryAdapter);
        });

//        fragmentHomeBinding.recCategory.getRecycledViewPool().setMaxRecycledViews(0, 0);

    }

    private void setRvBasket() {
        basketManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        fragmentHomeBinding.recBasket.setLayoutManager(basketManager);
        homeViewModel.basketMutableList.observe(getViewLifecycleOwner(), basketsList -> {
            basketAdapter = new BasketAdapter(basketsList, getActivity(), this::toBasketDetail);
            fragmentHomeBinding.recBasket.setAdapter(basketAdapter);
            fragmentHomeBinding.recBasket.getRecycledViewPool().setMaxRecycledViews(0, 0);
        });
    }

    private void setRvBestSelling() {
        bestSellingManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        fragmentHomeBinding.recBestSelling.setHasFixedSize(true);
        fragmentHomeBinding.recBestSelling.setLayoutManager(bestSellingManager);
        homeViewModel.bestSellingMutableList.observe(getViewLifecycleOwner(), products -> {
            bestsellingAdapter = new ExclusiveOfferListAdapter(products, requireActivity(), this::toProductDetail, (binding, product, position) -> {
                if (product.getInCart() == 0) {
                    addToCartProduct(product, BEST_SELLING, position);
                }
            });
            fragmentHomeBinding.recBestSelling.setAdapter(bestsellingAdapter);
        });
    }

    private int visibleItemCount = 0;
    private final int totalCount = 6;


    private void setRvExclusive() {
        makeVisible(fragmentHomeBinding.recExOffers, fragmentHomeBinding.rlExclusiveOffer);
        exclusiveOfferManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
//        fragmentHomeBinding.recExOffers.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recExOffers.setLayoutManager(exclusiveOfferManager);
        homeViewModel.exclusiveMutableList.observe(getViewLifecycleOwner(), products -> {
            offerListAdapter = new ExclusiveOfferListAdapter(products, getActivity(), this::toProductDetail, (binding, product, position) -> {
                addToCartProduct(product, EXCLUSIVE, position);
            });
            fragmentHomeBinding.recExOffers.setAdapter(offerListAdapter);
        });

//        fragmentHomeBinding.recExOffers.getRecycledViewPool().setMaxRecycledViews(0, 0);

    }

    private void setRvProduct() {
        productManager = new LinearLayoutManager(requireContext());
        fragmentHomeBinding.recProduct.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recProduct.setHasFixedSize(true);
        fragmentHomeBinding.recProduct.setLayoutManager(productManager);
        homeViewModel.productMutableList.observe(getViewLifecycleOwner(), products -> {
            productListAdapter = new ProductListAdapter(products, getActivity(), this::toProductDetail, (binding, product, position) -> {
                addToCartProduct(product, PRODUCT, position);
            });
            fragmentHomeBinding.recProduct.setAdapter(productListAdapter);
        });

//        fragmentHomeBinding.recProduct.getRecycledViewPool().setMaxRecycledViews(0, 0);
    }


    private void setRvSeasonal() {

        seasonalManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        fragmentHomeBinding.recSeasonal.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recSeasonal.setHasFixedSize(true);
        fragmentHomeBinding.recSeasonal.setLayoutManager(seasonalManager);
        homeViewModel.seasonalProductMutableList.observe(getViewLifecycleOwner(), seasonalProducts -> {
            seasonalBannerAdapter = new SeasonalBannerAdapter(getActivity(), seasonalProducts, seasonalProductId -> {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString(SEASONAL_P_ID, seasonalProductId);
                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_seasonalRegFragment, bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            fragmentHomeBinding.recSeasonal.setAdapter(seasonalBannerAdapter);
        });
//        fragmentHomeBinding.recSeasonal.getRecycledViewPool().setMaxRecycledViews(0, 0);
    }

    private void checkEmpties() {
        //check if banner is empty
        if (homeViewModel.bannerMutableList == null) {
            makeInVisible(fragmentHomeBinding.viewPagerBanner, fragmentHomeBinding.dotsIndicator);
        }
        //check if category is empty
        if (homeViewModel.categoryMutableList == null) {
            makeInVisible(fragmentHomeBinding.recCategory, fragmentHomeBinding.rlCategory);
        }
        //check if Basket is empty
        if (homeViewModel.bestSellingMutableList == null) {
            makeInVisible(fragmentHomeBinding.recBasket, fragmentHomeBinding.rlBasket);
        }
        //check if Best selling is Empty
        if (homeViewModel.bestSellingMutableList.getValue() == null) {
            makeInVisible(fragmentHomeBinding.recBestSelling, fragmentHomeBinding.rlBestSelling);
        }
        //check if seasonalProductList is Empty
        if (homeViewModel.seasonalProductMutableList == null) {
            makeInVisible(fragmentHomeBinding.recSeasonal, null);
        }
        // check if exclusive offer is Empty
        if (homeViewModel.exclusiveMutableList.getValue() == null) {
            makeInVisible(fragmentHomeBinding.recExOffers, fragmentHomeBinding.rlExclusiveOffer);
        }
        //check if storeBannerList is Empty
        if (storeBanner == null) {
            makeInVisible(fragmentHomeBinding.cardviewOurShops, null);
            makeInVisible(null, fragmentHomeBinding.rlO3Banner);
        }
        // check if productList is Empty
        if (homeViewModel.productMutableList == null) {
            makeInVisible(fragmentHomeBinding.recProduct, null);
        }

    }

    private void clearLists() {
        fragmentHomeBinding.homeLayout.setVisibility(View.GONE);
        checkEmpties();
    }

    private void searchProducts() {
        fragmentHomeBinding.etSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
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
                                        if (fragmentHomeBinding.etSearch.getText() != null && !fragmentHomeBinding.etSearch.getText().toString().trim().equals("")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString(SEARCH_TYPE, SEARCH_PRODUCT);
                                            bundle.putString(SEARCH_KEY, fragmentHomeBinding.etSearch.getText().toString());
                                            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_searchFragment, bundle);
                                        } else return;
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
    /*Home screen API*/
    private void callHomeScreenApi(ProgressDialog progressDialog) {
        @SuppressLint("NotifyDataSetChanged") Observer<HomeResponse> homeResponseObserver = homeResponse -> {
            if (homeResponse != null) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                Log.e(TAG, "callHomeAPiResponse: " + new Gson().toJson(homeResponse));
                switch (homeResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        try {
                            ((HomeActivity) context).setCountBudge(homeResponse.getCartItems());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //setHeader
                        if (homeResponse != null) {
                            System.out.println("user name :" + homeResponse.getHomeResponseData().getUserData().getName());
                            if (homeResponse.getHomeResponseData().getUserData().getAreaName() == null
                                    || homeResponse.getHomeResponseData().getUserData().getCityName() == null) {
                                preferences.setUserAddress(null);
                                preferences.setIsLoggedIn(false);
                                warningToast(context, "Address is invalid");
                                goToAskSelectLocationScreen("InValid Address", context);
                            } else {
                                preferences.setUserProfile(homeResponse.getHomeResponseData().getUserData().getImage());
                                preferences.setUserName(homeResponse.getHomeResponseData().getUserData().getName());
                                preferences.setUserAddress(homeResponse.getHomeResponseData().getUserData().getCityName()
                                        + ", " + homeResponse.getHomeResponseData().getUserData().getAreaName());
                                if (homeResponse.getHomeResponseData().getUserData().getImage() != null
                                        && !TextUtils.isEmpty(homeResponse.getHomeResponseData().getUserData().getImage())) {
                                    ((HomeActivity) context).tvUserName.setText("Hello! " + homeResponse.getHomeResponseData().getUserData().getName());
                                    loadingImage(context, homeResponse.getHomeResponseData().getUserData().getImage(), ((HomeActivity) context).civProfilePhoto);
                                }
                            }
                        }
                        //Add All Banner
                        Log.e(TAG, "callHomeScreenApi: " + homeResponse.getHomeResponseData().getCategoryData().size());
                        if (homeResponse.getHomeResponseData().getBannerDetails().size() > 0) {
                            rlRefreshPage.setRefreshing(false);
                            makeVisible(fragmentHomeBinding.viewPagerBanner, fragmentHomeBinding.dotsIndicator);
//                            banners.addAll(homeResponse.getHomeResponseData().getBannerDetails());
                            homeViewModel.bannerMutableList.setValue(homeResponse.getHomeResponseData().getBannerDetails());
                            loadBannerImages();
                        }
//                        setRvBanners();
                        //Add Category
                        if (homeResponse.getHomeResponseData().getCategoryData() != null) {
                            //Should remove this latter
                            rlRefreshPage.setRefreshing(false);
                            if (homeResponse.getHomeResponseData().getCategoryData() != null
                                    && homeResponse.getHomeResponseData().getCategoryData().size() > 0) {
                                makeVisible(fragmentHomeBinding.recCategory, fragmentHomeBinding.rlCategory);
//                                categories.addAll(homeResponse.getHomeResponseData().getCategoryData());
//                                categoryAdapter.notifyDataSetChanged();
                                homeViewModel.categoryMutableList.setValue(homeResponse.getHomeResponseData().getCategoryData());
                                setRvCategory();
//                                System.out.println("categories " + categories.size());
                                fragmentHomeBinding.homeLayout.setVisibility(View.VISIBLE);
                            }
                        }
                        //Add Baskets
                        if (homeResponse.getHomeResponseData().getBasketList() != null
                                && homeResponse.getHomeResponseData().getBasketList().size() > 0) {
                            makeVisible(fragmentHomeBinding.recBasket, fragmentHomeBinding.rlBasket);
//                            baskets.addAll(homeResponse.getHomeResponseData().getBasketList());
                            homeViewModel.basketMutableList.setValue(homeResponse.getHomeResponseData().getBasketList());
                            setRvBasket();
                            basketAdapter.notifyDataSetChanged();
                        }
                        //Add Bestselling
                        if (homeResponse.getHomeResponseData().getBestSellingProductList() != null
                                && homeResponse.getHomeResponseData().getBestSellingProductList().size() > 0) {
                            rlRefreshPage.setRefreshing(false);
                            makeVisible(fragmentHomeBinding.recBestSelling, fragmentHomeBinding.rlBestSelling);
                            homeViewModel.bestSellingMutableList.setValue(homeResponse.getHomeResponseData().getBestSellingProductList());
                            setRvBestSelling();
//                            bestSellings.addAll(homeResponse.getHomeResponseData().getBestSellingProductList());
                            bestsellingAdapter.notifyDataSetChanged();
                        }

                        //Add Seasonal
                        if (homeResponse.getHomeResponseData().getSeasonalProduct() != null
                                && homeResponse.getHomeResponseData().getSeasonalProduct().size() > 0) {
                            makeVisible(fragmentHomeBinding.recSeasonal, null);
                            rlRefreshPage.setRefreshing(false);
                            for (int i = 0; i < homeResponse.getHomeResponseData().getSeasonalProduct().size(); i++) {
                                if (i / 2 == 0) {
                                    homeResponse.getHomeResponseData().getSeasonalProduct().get(i).setType("Green");
                                } else {
                                    homeResponse.getHomeResponseData().getSeasonalProduct().get(i).setType("Yellow");
                                }
                            }
                            homeViewModel.seasonalProductMutableList.setValue(homeResponse.getHomeResponseData().getSeasonalProduct());
                            setRvSeasonal();
//                            seasonalProductList.addAll(homeResponse.getHomeResponseData().getSeasonalProduct());
                            seasonalBannerAdapter.notifyDataSetChanged();
                        }
                        //Add Exclusive
                        if (homeResponse.getHomeResponseData().getExclusiveList() != null
                                && homeResponse.getHomeResponseData().getExclusiveList().size() > 0) {
                            rlRefreshPage.setRefreshing(false);
                            homeViewModel.exclusiveMutableList.setValue(homeResponse.getHomeResponseData().getExclusiveList());
                            setRvExclusive();
//                            offerProducts.addAll(homeResponse.getHomeResponseData().getExclusiveList());
                            offerListAdapter.notifyDataSetChanged();
                        }

                        //Add Product
                        if (homeResponse.getHomeResponseData().getProductList() != null
                                && homeResponse.getHomeResponseData().getProductList().size() > 0) {
                            rlRefreshPage.setRefreshing(false);
                            makeVisible(fragmentHomeBinding.recProduct, null);
                            //Should remove this latter
                            homeViewModel.productMutableList.setValue(homeResponse.getHomeResponseData().getProductList());
                            setRvProduct();
//                            productList.addAll(homeResponse.getHomeResponseData().getProductList());
                            productListAdapter.notifyDataSetChanged();
                        }
                        //Add Store Response
                        if (homeResponse.getHomeResponseData().getStoreBanner().size() > 0) {
                            rlRefreshPage.setRefreshing(false);
                            makeVisible(fragmentHomeBinding.cardviewOurShops, null);
                            makeVisible(null, fragmentHomeBinding.rlO3Banner);
                            storeBanner = homeResponse.getHomeResponseData().getStoreBanner().get(0);
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
                        warningToast(context, homeResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(homeResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, homeResponse.getMessage());
                        break;
                }

            }
        };
        homeViewModel.homeResponseLiveData(progressDialog,
                listingParams(offset, ""), HomeFragment.this)
                .observe(getViewLifecycleOwner(), homeResponseObserver);
    }

    /*Add to cart API*/
    private void addToCartProduct(Product product,
                                  int addType, int position) {
        this.addType = addType;
        @SuppressLint("NotifyDataSetChanged") Observer<BaseResponse> baseResponseObserver = response -> {
            if (response != null) {
                Log.e(TAG, "addToCartProduct: " + new Gson().toJson(response));
                switch (response.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, response.getMessage());
                        try {
                            ((HomeActivity) context).setCountBudge(response.getCartItems());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        switch (addType) {
                            case BEST_SELLING:
                                homeViewModel.bestSellingMutableList.getValue().get(position).setInCart(1);
                                bestsellingAdapter.notifyDataSetChanged();
                                break;
                            case EXCLUSIVE:
                                homeViewModel.exclusiveMutableList.getValue().get(position).setInCart(1);
                                offerListAdapter.notifyDataSetChanged();
                                break;
                            case PRODUCT:
                                homeViewModel.productMutableList.getValue().get(position).setInCart(1);
                                productListAdapter.notifyDataSetChanged();
                                break;
                        }
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
        if (product.getInCart() == 0) {
            try {
                homeViewModel.addToCartProductLiveData(addToCartParam(product), HomeFragment.this)
                        .observe(getViewLifecycleOwner(), baseResponseObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else infoToast(context, "remove product from cart");
    }

    /*Call Category API*/
    private void callCategoryApi(ProgressDialog progressDialog, String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")) {
            categoryOffset = categoryOffset + 1;
        }
        categoryOffset = 0;
        Observer<CategoryResponse> categoryResponseObserver = categoryResponse -> {
            if (categoryResponse != null) {
                if (progressDialog != null) progressDialog.dismiss();
                Log.e("Category Api ResponseData", new Gson().toJson(categoryResponse));
                switch (categoryResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (categoryResponse.getCategoryData() != null) {
                            //Should remove this latter
                            rlRefreshPage.setRefreshing(false);
                            setRvCategory();
                            if (categoryResponse.getCategoryData().getCategoryList() != null
                                    && categoryResponse.getCategoryData().getCategoryList().size() > 0) {
                                makeVisible(fragmentHomeBinding.recCategory, fragmentHomeBinding.rlCategory);
//                                categories.addAll(categoryResponse.getCategoryData().getCategoryList());
//                                System.out.println("categories " + categories.size());
                                fragmentHomeBinding.homeLayout.setVisibility(View.VISIBLE);
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
        homeViewModel.categoryResponseLiveData(progressDialog, listingParams("", CATEGORY), HomeFragment.this)
                .observe(getViewLifecycleOwner(), categoryResponseObserver);
    }

    /*Call Basket API here*/
    private void callBasketApi(ProgressDialog progressDialog, String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")) {
            basketOffset = basketOffset + 1;
        } else {
            basketOffset = 0;
        }

        Observer<HomeBasketResponse> bannerResponseObserver = basketResponse -> {
            if (basketResponse != null) {
                if (progressDialog != null) progressDialog.dismiss();
                Log.e("Basket Api ResponseData", new Gson().toJson(basketResponse));
                switch (basketResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (basketResponse.getData().getBaskets() != null
                                && basketResponse.getData().getBaskets().size() > 0) {
                            rlRefreshPage.setRefreshing(false);
                            makeVisible(fragmentHomeBinding.recBasket, fragmentHomeBinding.rlBasket);
//                            baskets.addAll(basketResponse.getData().getBaskets());
//                            System.out.println("basket list :" + baskets.size());
//                            basketAdapter = new BasketAdapter(baskets, getActivity(), this::toBasketDetail);
                            basketManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
                            fragmentHomeBinding.recBasket.setLayoutManager(basketManager);
                            fragmentHomeBinding.recBasket.setAdapter(basketAdapter);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        if (loadType.equalsIgnoreCase("load"))
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
        homeViewModel.basketResponseLiveData(progressDialog, listingParams("", BASKET), HomeFragment.this)
                .observe(getViewLifecycleOwner(), bannerResponseObserver);

    }

    //    // ProductOld listing here
    private void callProductListApi(ProgressDialog progressDialog, String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")) {
            productOffset = productOffset + 1;
        } else productOffset = 0;
        Observer<ProductListResponse> productListResponseObserver = productListResponse -> {
            if (productListResponse != null) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                Log.e(TAG, "callProductListApi: " + productListResponse.getMessage());
                switch (productListResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (productListResponse.getProductResponseDt().getProductList() != null
                                && productListResponse.getProductResponseDt().getProductList().size() > 0) {
                            rlRefreshPage.setRefreshing(false);
                            makeVisible(fragmentHomeBinding.recProduct, null);
                            //Should remove this latter
//                            productList.clear();
                            setRvProduct();
//                            productList.addAll(productListResponse.getProductResponseDt().getProductList());
                            productListAdapter.notifyDataSetChanged();

                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        if (loadType.equalsIgnoreCase("load"))
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
                listingParams("", PRODUCT), HomeFragment.this)
                .observe(getViewLifecycleOwner(), productListResponseObserver);
    }

    //    //Best selling Data listing here
    private void callBestSellingApi(ProgressDialog showCircleProgressDialog, String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")) {
            bestSellingOffset = bestSellingOffset + 1;
        } else bestSellingOffset = 0;
        @SuppressLint("NotifyDataSetChanged") Observer<BestSellingResponse> bestSellingResponseObserver = bestSellingResponse -> {
            if (bestSellingResponse != null) {
                if (showCircleProgressDialog != null)
                    showCircleProgressDialog.dismiss();
                Log.e("Best selling Api ResponseData", new Gson().toJson(bestSellingResponse));
                switch (bestSellingResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (bestSellingOffset == 0)
//                            bestSellings.clear();

                            if (bestSellingResponse.getBestSellingData().getBestSellingProductList() != null
                                    && bestSellingResponse.getBestSellingData().getBestSellingProductList().size() > 0) {

                                //  makeVisible(fragmentHomeBinding.recBestSelling, fragmentHomeBinding.rlBestSelling);
                                //  setRvBestSelling();
//                            bestSellings.addAll(bestSellingResponse.getBestSellingData().getBestSellingProductList());
                                bestsellingAdapter.notifyDataSetChanged();
//                            setRvBestSelling();
                            }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        if (loadType.equalsIgnoreCase("load"))
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
                listingParams("", BEST_SELLING), HomeFragment.this)
                .observe(getViewLifecycleOwner(), bestSellingResponseObserver);
    }

    //Seasonal ProductOld listing here
    //Todo delete later
    private void callSeasonalProductApi(ProgressDialog progressDialog, String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")) {
            seasonalOffset = seasonalOffset + 1;
        } else seasonalOffset = 0;
        Observer<SeasonalProductResponse> seasonalProductObserver = seasonalProductResponse -> {
            if (seasonalProductResponse != null) {
                if (progressDialog != null) progressDialog.dismiss();
                Log.e("SeasonalProduct Api ResponseData", new Gson().toJson(seasonalProductResponse));
                switch (seasonalProductResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (seasonalProductResponse.getSeasonalProducts() != null
                                && seasonalProductResponse.getSeasonalProducts().size() > 0) {
                            rlRefreshPage.setRefreshing(false);
                            for (int i = 0; i < seasonalProductResponse.getSeasonalProducts().size(); i++) {
                                if (i / 2 == 0) {
                                    seasonalProductResponse.getSeasonalProducts().get(i).setType("Green");
                                } else
                                    seasonalProductResponse.getSeasonalProducts().get(i).setType("Yellow");
                            }
                            makeVisible(fragmentHomeBinding.recSeasonal, null);
//                            seasonalProductList = seasonalProductResponse.getSeasonalProducts();
                            homeViewModel.seasonalProductMutableList.setValue(seasonalProductResponse.getSeasonalProducts());
                            setRvSeasonal();
//                            seasonalBannerAdapter = new SeasonalBannerAdapter(getActivity(), seasonalProductList, seasonalProductId -> {
//                                //TODO crash here
//                                try {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString(SEASONAL_P_ID, seasonalProductId);
//                                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_seasonalRegFragment, bundle);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            });
                            fragmentHomeBinding.recSeasonal.setAdapter(seasonalBannerAdapter);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        if (loadType.equalsIgnoreCase("load"))
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
        homeViewModel.seasonalResponseLiveData(progressDialog, listingParams("", SEASONAL), HomeFragment.this)
                .observe(getViewLifecycleOwner(), seasonalProductObserver);

    }

    //    /*Product Offer Data listing*/
    private void callExclusiveOfferApi(ProgressDialog progressDialog, String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")) {
            exclusiveOffset = exclusiveOffset + 1;
        } else exclusiveOffset = 0;
        Observer<ExclusiveResponse> exclusiveResponseObserver = exclusiveResponse -> {
            if (exclusiveResponse != null) {
                if (progressDialog != null) progressDialog.dismiss();
                Log.e("Exclusive Api ResponseData", new Gson().toJson(exclusiveResponse));
                switch (exclusiveResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (exclusiveResponse.getExclusiveData().getExclusivesList() != null
                                && exclusiveResponse.getExclusiveData().getExclusivesList().size() > 0) {
                            rlRefreshPage.setRefreshing(false);
                            setRvExclusive();
//                            offerProducts.addAll(exclusiveResponse.getExclusiveData().getExclusivesList());
                            offerListAdapter.notifyDataSetChanged();
//
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        if (loadType.equalsIgnoreCase("load"))
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
        homeViewModel.exclusiveResponseLiveData(progressDialog, listingParams("", EXCLUSIVE), HomeFragment.this)
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
        fragmentHomeBinding.imgMice.setOnClickListener(this);
    }

    private void redirectToOurShops(View v) {
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_our_stores);
    }

    /*Create a Dummy banner if no banner is available*/
//    private void bannerAutoSlider() {
//        Log.e("SetSliderAutoTimer", "SetSliderAutoTimer");
//        /*After setting the adapter use the timer */
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentBanner == NumberOfBanners) {
//                    currentBanner = 0;
//                }
//                fragmentHomeBinding.viewPagerBanner.setCurrentItem(currentBanner++, true);
//            }
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer.schedule(new TimerTask() { // task to be scheduled
//            @Override
//            public void run() {
//                handler.post(() -> {
//
//                });
//            }
//        }, DELAY_MS, PERIOD_MS);
//    }

    private void loadBannerImages() {
        AutoScrollViewPager autoScrollViewPager = fragmentHomeBinding.viewPagerBanner;

        homeViewModel.bannerMutableList.observe(getViewLifecycleOwner(), bannersList -> {
            bannerAdapter = new BannerAdapter(bannersList, requireActivity(), this);
            autoScrollViewPager.setAdapter(bannerAdapter);
            fragmentHomeBinding.dotsIndicator.setViewPager(autoScrollViewPager);

            if (bannersList == null || bannersList.size() == 1 || bannersList.size() == 0) {
                fragmentHomeBinding.dotsIndicator.setVisibility(View.VISIBLE);
            }
        });

        // start auto scroll
        autoScrollViewPager.startAutoScroll();

        // set auto scroll time in mili
        autoScrollViewPager.setSlideInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);

        // enable recycling using true
        autoScrollViewPager.setCycle(true);
    }

    private HashMap<String, String> listingParams(int offset, String search) {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.LIMIT, String.valueOf(limit));
        map.put(AppConstants.OFFSET, String.valueOf(offset));
        map.put(AppConstants.SEARCH, search);
        return map;
    }

    private HashMap<String, String> listingParams(String search, int api) {
        HashMap<String, String> map = new HashMap<>();
        switch (api) {
            case CATEGORY:
                System.out.println("categoryOffset" + categoryOffset);
                map.put(AppConstants.OFFSET, String.valueOf(categoryOffset));
                break;
            case BASKET:
                map.put(AppConstants.OFFSET, String.valueOf(basketOffset));
                break;
            case BEST_SELLING:
                map.put(AppConstants.OFFSET, String.valueOf(bestSellingOffset));
                break;
            case SEASONAL:
                map.put(AppConstants.OFFSET, String.valueOf(seasonalOffset));
                break;
            case EXCLUSIVE:
                map.put(AppConstants.OFFSET, String.valueOf(exclusiveOffset));
                break;
            case PRODUCT:
                map.put(AppConstants.OFFSET, String.valueOf(productOffset));
                break;
        }
        map.put(AppConstants.LIMIT, String.valueOf(limit));
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
        ((HomeActivity) requireActivity()).binding.appBarHome.tvAddress.setSelected(true);
        ((HomeActivity) requireActivity()).binding.appBarHome.tvAddress.setText(preferences.getUserAddress());
        ((HomeActivity) requireActivity()).binding.appBarHome.logImg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentHomeBinding = null;
    }

    private HashMap<String, String> addToCartParam(Product product) {
        HashMap<String, String> map = new HashMap<>();
        map.put(PRODUCT_ID, product.getProductId());
        map.put(PU_ID, product.getPuId());
        map.put(QUANTITY, "1");
        return map;
    }

    private HashMap<String, String> profileParam(String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(CUSTOMER_ID, userId);
        return map;
    }

    public void toProductDetail(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, product.getProductId());
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_product_details, bundle);
    }

    public void toBasketDetail(Basket basket) {
        Bundle bundle = new Bundle();
        bundle.putString(BASKET_ID, basket.getBasketId());
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
            case R.id.img_mice:
                startVoiceInput();
                break;
        }
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Search Store...");
        try {
            recognizerIntentLauncher.launch(intent);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), HomeFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        callHomeScreenApi(showCircleProgressDialog(context, ""));
                        break;
                    case 1:
                        infoToast(context, "try again");
                        break;
                         /*
                            0 :- Home API
                            1 :- Add to cart Product API
                            2 :- Banner API
                            3 :- Category API
                            4 :- Basket API
                            5 :- Best selling API
                            6 :- Seasonal API
                            7 :- Exclusive API
                            8 :- Store Banner API
                            9 :-Product List API
                         */

//                    case 2:
//                        ca(showCircleProgressDialog(context,""),type);
//                        break;
//                    case 3:
//                        callCategoryApi(showCircleProgressDialog(context, ""), type);
//                        break;
//                    case 4:
//                        callBestSellingApi(showCircleProgressDialog(context, ""), type);
//                        break;
//                    case 8:
//                        clearLists();
//                        callHomeScreenApi(showCircleProgressDialog(context, ""));
//                        break;
//                    case 5:
//                        callSeasonalProductApi(showCircleProgressDialog(context, ""), type);
//                        break;

                }
            }
        }, context);

    }


    @Override
    public void OnBannerClick(Banner banner) {
        try {
            if (banner.getProductId() != null && !banner.getProductId().isEmpty()) {
                System.out.println("go to Product ");
                try {
                    Bundle bundle = new Bundle();
                    bundle.putString(PRODUCT_ID, banner.getProductId());
                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_product_details, bundle);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else if (banner.getCategoryId() != null && !banner.getCategoryId().isEmpty()) {
                System.out.println("go to category ");
                /*redirect to product list by category*/
                try {
                    String cat_id = banner.getCategoryId();
                    Bundle bundle = new Bundle();
                    bundle.putString(CATEGORY_ID, cat_id);
                    bundle.putString(LIST_TITLE, banner.getCategoryName());
                    bundle.putString(LIST_TYPE, banner.getCategoryType());
                    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_products_list, bundle);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else if (banner.getAdvUrl() != null && !banner.getAdvUrl().isEmpty()) {
                System.out.println("go to URL ");
                Intent viewIntent =
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse(banner.getAdvUrl()));
                startActivity(viewIntent);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}