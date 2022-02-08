package com.poona.agrocart.ui.home;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.data.network.ExclusiveResponse;
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
import com.poona.agrocart.ui.home.model.Banner;
import com.poona.agrocart.ui.home.model.Basket;
import com.poona.agrocart.ui.home.model.Category;
import com.poona.agrocart.ui.home.model.Exclusive;
import com.poona.agrocart.ui.home.model.Product;
import com.poona.agrocart.ui.home.model.SeasonalProduct;
import com.poona.agrocart.ui.home.model.StoreBanner;

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
    private ArrayList<Exclusive> bestSellings = new ArrayList<>();
    private ArrayList<Exclusive> offerProducts = new ArrayList<>();
    private ArrayList<Exclusive> productList = new ArrayList<>();
    private ArrayList<Banner> banners = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Basket> baskets = new ArrayList<>();
    private ArrayList<SeasonalProduct> seasonalProductList = new ArrayList<>();
    private final ArrayList<Product> mCartList = new ArrayList<Product>();
    private ArrayList<StoreBanner> storeBannerList = new ArrayList<>();
    private ArrayList<String> BasketIds = new ArrayList<>();

    private int limit = 0;
    private int offset = 0;
    private View root;
    private int currentBanner = 0;
    private int NumberOfBanners = 0;
    private Timer bannerTimer;
    Timer timer = new Timer();
    boolean isTyping = false;
    private String BeforeSerach;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        root = fragmentHomeBinding.getRoot();
        clearLists();
        callBannerApi(showCircleProgressDialog(context, ""), limit, offset);
        callCategoryApi(root, showCircleProgressDialog(context, ""), limit, offset);
        callBasketApi(root, showCircleProgressDialog(context, ""), limit, offset);
        callExclusiveOfferApi(root, showCircleProgressDialog(context, ""), limit, offset);
        callBestSellingApi(root, showCircleProgressDialog(context, ""), limit, offset);
        callSeasonalProductApi(root, showCircleProgressDialog(context, ""), limit, offset);
        callProductListApi(root, showCircleProgressDialog(context, ""), limit, offset);
        callStoreBannerApi(root, showCircleProgressDialog(context, ""));
        getBasketItems();
//        setStoreBanner(root);
        initClick();
        bannerAutoSLider();
        searchProducts(root);

        checkEmpties();
        return root;

    }

    private void checkEmpties() {
        //check if banner is empty
        if (banners == null || banners.size() == 0) {
            makeDummyBanner();
        }
        //check if category is empty
        if (categories == null || categories.size() == 0) {
            makeInVisible(fragmentHomeBinding.recCategory, fragmentHomeBinding.rlCategory);
        }
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
            makeInVisible(fragmentHomeBinding.recOffers, fragmentHomeBinding.rlExclusiveOffer);
        }
        //check if storeBannerList is Empty
        if (storeBannerList == null || storeBannerList.size() == 0) {
            makeInVisible(fragmentHomeBinding.cardviewOurShops,null);
        }
        // check if productList is Empty
        if (productList==null||productList.size()==0){
            makeInVisible(fragmentHomeBinding.recProduct,null);
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
                                                bundle.putInt(SEARCH_TYPE, SEARCH_PRODUCT);
                                                bundle.putString(SEARCH_KEY, fragmentHomeBinding.etSearch.getText().toString());
                                                Navigation.findNavController(root).navigate(R.id.action_nav_home_to_searchFragment, bundle);
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
                            makeVisible(fragmentHomeBinding.cardviewOurShops,null);
                            storeBannerList = storeBannerResponse.getStoreBanners();
                            StoreBanner storeBanner = storeBannerResponse.getStoreBanners().get(0);
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

    // Product listing here
    private void callProductListApi(View root, ProgressDialog progressDialog, int limit, int offset) {
        limit = limit + 10;
        Observer<ProductListResponse> productListResponseObserver = productListResponse -> {
            if (productListResponse != null) {
                progressDialog.dismiss();
                Log.e(TAG, "callProductListApi: " + productListResponse.getProductResponseDt().getProductList().size());
                switch (productListResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (productListResponse.getProductResponseDt().getProductList().size() > 0) {
                            makeVisible(fragmentHomeBinding.recProduct,null);
                            productList = productListResponse.getProductResponseDt().getProductList();
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                            fragmentHomeBinding.recProduct.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recProduct.setHasFixedSize(true);
                            fragmentHomeBinding.recProduct.setLayoutManager(linearLayoutManager);
                            productListAdapter = new ProductListAdapter(productList, getActivity(), root);
                            fragmentHomeBinding.recProduct.setAdapter(productListAdapter);
                            productListAdapter.setOnProductClick(product -> {
                                toProductDetail(product, root);
                            });

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


    //Seasonal Product listing here
    private void callSeasonalProductApi(View root, ProgressDialog progressDialog, int limit, int offset) {
        limit = limit + 10;
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
                            seasonalProductList = seasonalProductResponse.getSeasonalProducts();
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
                            fragmentHomeBinding.recSeasonal.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recSeasonal.setHasFixedSize(true);
                            fragmentHomeBinding.recSeasonal.setLayoutManager(linearLayoutManager);
                            seasonalBannerAdapter = new SeasonalBannerAdapter(getActivity(), seasonalProductList, root);
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
        limit = limit + 10;
        Observer<BestSellingResponse> bestSellingResponseObserver = bestSellingResponse -> {
            if (bestSellingResponse != null) {
                showCircleProgressDialog.dismiss();
                Log.e("Category Api Response", new Gson().toJson(bestSellingResponse));
                switch (bestSellingResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (bestSellingResponse.getBestSellingData().getBestSellingProductList().size() > 0) {
                            this.bestSellings = bestSellingResponse.getBestSellingData().getBestSellingProductList();
                            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
                            bestsellingAdapter = new ExclusiveOfferListAdapter(this.bestSellings, requireActivity(), root);
                            fragmentHomeBinding.recBestSelling.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recBestSelling.setLayoutManager(layoutManager);
                            fragmentHomeBinding.recBestSelling.setAdapter(bestsellingAdapter);
                            // Redirect to Product details
                            bestsellingAdapter.setOnProductClick(product -> {
                                toProductDetail(product, root);
                            });
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

    //Exclusive Offer Data listing
    private void callExclusiveOfferApi(View root, ProgressDialog progressDialog, int limit, int offset) {
        limit = limit + 10;
        Observer<ExclusiveResponse> exclusiveResponseObserver = exclusiveResponse -> {
            if (exclusiveResponse != null) {
                progressDialog.dismiss();
                Log.e("Category Api Response", new Gson().toJson(exclusiveResponse));
                switch (exclusiveResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (exclusiveResponse.getExclusiveData().getExclusivesList().size() > 0) {
                            this.offerProducts = exclusiveResponse.getExclusiveData().getExclusivesList();
                            makeVisible(fragmentHomeBinding.recBestSelling, fragmentHomeBinding.rlBestSelling);
                            offerListAdapter = new ExclusiveOfferListAdapter(this.offerProducts, getActivity(), root);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
                            fragmentHomeBinding.recOffers.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recOffers.setLayoutManager(layoutManager);
                            fragmentHomeBinding.recOffers.setAdapter(offerListAdapter);
                            // Redirect to Product details
                            offerListAdapter.setOnProductClick(product -> {
                                toProductDetail(product, root);
                            });
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
        fragmentHomeBinding.tvAllExclusive.setOnClickListener(this);
        fragmentHomeBinding.tvAllSelling.setOnClickListener(this);
    }

    private void getBasketItems() {
        homeViewModel.getSavesProductInBasket().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                for (Product saved : products)
                    BasketIds.add(saved.getId());
            }
        });
    }

    private void setStoreBanner(View root) {
        AppUtils.setImage(fragmentHomeBinding.imgStore, "https://www.linkpicture.com/q/store-2.png");

    }

    private void redirectToOurShops(View v) {
        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_our_stores);
    }


    //Basket APi Response gere
    private void callBasketApi(View root, ProgressDialog progressDialog, int limit, int offset) {
        limit = limit + 10;
        Observer<BasketResponse> bannerResponseObserver = basketResponse -> {
            if (basketResponse != null) {
                progressDialog.dismiss();
                Log.e("Category Api Response", new Gson().toJson(basketResponse));
                switch (basketResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (basketResponse.getData().getBaskets().size() > 0) {
                            makeVisible(fragmentHomeBinding.recBasket, fragmentHomeBinding.rlBasket);
                            this.baskets = basketResponse.getData().getBaskets();
                            basketAdapter = new BasketAdapter(this.baskets, getActivity(), root);
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
        limit = limit + 10;

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
                                LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
                                categoryAdapter = new CategoryAdapter(categories, requireActivity(), view);
                                fragmentHomeBinding.recCategory.setNestedScrollingEnabled(false);
                                fragmentHomeBinding.recCategory.setAdapter(categoryAdapter);
                                fragmentHomeBinding.recCategory.setLayoutManager(layoutManager);
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
        limit = limit + 10;
        Observer<BannerResponse> bannerResponseObserver = bannerResponse -> {
            if (bannerResponse != null) {
                progressDialog.dismiss();
                Log.e("Banner Api Response", new Gson().toJson(bannerResponse));
                switch (bannerResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully

                        if (bannerResponse.getData().getBanners().size() > 0) {

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
    private void makeDummyBanner() {
        Banner banner = new Banner("1", "customer", "", "");
        banner.setDummy(true);
        banners.clear();
        banners.add(banner);
        this.NumberOfBanners = banners.size();
        bannerAdapter = new BannerAdapter(banners, requireActivity());
//
        fragmentHomeBinding.viewPagerBanner.setAdapter(bannerAdapter);
        // Set up tab indicators
        fragmentHomeBinding.dotsIndicator.setViewPager(fragmentHomeBinding.viewPagerBanner);
    }

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
        }, 800, 3000);
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

    private void addToBasket(Product item) {
        item.setQuantity("1");
        mCartList.add(item);
        preferences.saveCartArrayList(mCartList, AppConstants.CART_LIST);
        for (int i = 0; i < mCartList.size(); i++) {
            System.out.println("Added: " + mCartList.get(i).getName());
        }
    }

    public void toProductDetail(Product product, View root) {
        Bundle bundle = new Bundle();
        bundle.putString("name", product.getName());
        bundle.putString("image", product.getImg());
        bundle.putString("price", product.getPrice());
        bundle.putString("brand", product.getBrand());
        bundle.putString("weight", product.getWeight());
        bundle.putString("quantity", product.getQuantity());
        bundle.putBoolean("organic", product.isOrganic());
        bundle.putBoolean("isInBasket", product.isInBasket());
        bundle.putString("Product", "Product");
        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_product_details, bundle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_all_category:
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_explore);
                break;
        }
    }

    @Override
    public void onNetworkException(int from) {
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
                        //Call Exclusive API after network error
                        callExclusiveOfferApi(root, showCircleProgressDialog(context, ""), limit, offset);
                        break;
                    case 4:
                        //Call Bestselling API after network error
                        callBestSellingApi(root, showCircleProgressDialog(context, ""), limit, offset);
                        break;
                    case 5:
                        //Call Seasonal Product API after network error
                        callSeasonalProductApi(root, showCircleProgressDialog(context, ""), limit, offset);
                        break;
                    case 6:
                        //Call Product List API after network error
                        callProductListApi(root, showCircleProgressDialog(context, ""), limit, offset);
                        break;
                    case 7:
                        //Call Product List API after network error
                        callStoreBannerApi(root, showCircleProgressDialog(context, ""));
                        break;

                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}