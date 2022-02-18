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
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.ExclusiveResponse;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BannerResponse;
import com.poona.agrocart.data.network.responses.BasketResponse;
import com.poona.agrocart.data.network.responses.BestSellingResponse;
import com.poona.agrocart.data.network.responses.CategoryResponse;
import com.poona.agrocart.data.network.responses.HomeResponse;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.data.network.responses.ProfileResponse;
import com.poona.agrocart.data.network.responses.SeasonalProductResponse;
import com.poona.agrocart.data.network.responses.StoreBannerResponse;
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
    private final long DELAY_MS = 500;
    private final long PERIOD_MS = 3000;
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

    private final int limit = 10;
    private int categoryOffset = 0,basketOffset = 0,bestSellingOffset=0,
            seasonalOffset=0,exclusiveOffset=0,productOffset=0;
    private View root;
    private int currentBanner = 0;
    private int NumberOfBanners = 0;
    private Timer bannerTimer;
    Timer timer = new Timer();
    boolean isTyping = false;
    private String BeforeSerach;
    private int visibleCategoryCount;
    private LinearLayoutManager categoryManager, basketManager, productManager,
            seasonalManager, bestSellingManager, exclusiveOfferManager;
    private boolean scrolling = false;
    private int  offset = 0;
    private static final int CATEGORY = 0;
    private static final int BASKET =1;
    private static final int BEST_SELLING=2;
    private static final int SEASONAL=3;
    private static final int EXCLUSIVE=4;
    private static final int PRODUCT=5;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        root = fragmentHomeBinding.getRoot();
        clearLists();
        setCategoryRv();
//        setUserProfile(showCircleProgressDialog(context,""));
        if (isConnectingToInternet(context)) {
            callHomeApi(showCircleProgressDialog(context,""),offset);
            callBannerApi(showCircleProgressDialog(context, ""));
            callCategoryApi(showCircleProgressDialog(context, ""),"load");
            callBasketApi(showCircleProgressDialog(context, ""), "load");
            callExclusiveOfferApi(showCircleProgressDialog(context, ""), "load");
            callBestSellingApi(showCircleProgressDialog(context, ""), "load");
            callSeasonalProductApi(showCircleProgressDialog(context, ""), "load");
            callProductListApi(showCircleProgressDialog(context, ""), "load");
            callStoreBannerApi(showCircleProgressDialog(context, ""));
            searchProducts();
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
        getBasketItems();
//        setStoreBanner(root);
        initClick();
        checkEmpties();
//        setPaginationForLists();

        return root;

    }

    private void setUserProfile(ProgressDialog progressDialog) {
        Observer<ProfileResponse> profileResponseObserver = profileResponse -> {
            if (profileResponse!=null){
                preferences.setUserProfile(profileResponse.getProfile().getImage());
                preferences.setUserName(profileResponse.getProfile().getName());
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                System.out.println("name "+profileResponse.getProfile().getName());
            }
        };
        homeViewModel.getViewProfileResponse(progressDialog,profileParam(preferences.getUid()),
                HomeFragment.this)
                .observe(getViewLifecycleOwner(),profileResponseObserver);
    }

    private void callHomeApi(ProgressDialog progressDialog, int offset) {
    Observer<HomeResponse> homeResponseObserver = homeResponse -> {
        if (homeResponse!=null){
            progressDialog.dismiss();
            Log.e(TAG, "callHomeApi: ");
            switch (homeResponse.getStatus()){
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (homeResponse.getResponse().getUserData() != null){
                            preferences.setUserProfile(homeResponse.getResponse().getUserData().get(0).getImage());
                            preferences.setUserName(homeResponse.getResponse().getUserData().get(0).getUserName());

                            if(homeResponse.getResponse().getUserData().get(0).getImage() != null
                                    && !TextUtils.isEmpty(homeResponse.getResponse().getUserData().get(0).getImage())) {
                                ((HomeActivity)context).tvUserName.setText("Hello! "+homeResponse.getResponse().getUserData().get(0).getUserName());
                                loadingImage(context, homeResponse.getResponse().getUserData().get(0).getImage(), ((HomeActivity)context).civProfilePhoto);
                            }

                            System.out.println("name "+homeResponse.getResponse().getUserData().get(0).getUserName());
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
    homeViewModel.homeResponseLiveData(progressDialog, listingParams(offset,""),
            HomeFragment.this).observe(getViewLifecycleOwner(),
            homeResponseObserver);
    }

    private void setStoreBanner(HomeResponse homeResponse) {
        if (homeResponse.getResponse().getStoreBanner().size() > 0) {
            makeVisible(fragmentHomeBinding.cardviewOurShops, null);
            makeVisible(null, fragmentHomeBinding.rlO3Banner);
            storeBannerList = (ArrayList<StoreBannerResponse.StoreBanner>) homeResponse.getResponse().getStoreBanner();
            StoreBannerResponse.StoreBanner storeBanner = homeResponse.getResponse().getStoreBanner().get(0);
            fragmentHomeBinding.setStoreBanner(storeBanner);
            fragmentHomeBinding.setVariable(BR.storeBanner, storeBanner);
            fragmentHomeBinding.executePendingBindings();
            fragmentHomeBinding.cardviewOurShops.setOnClickListener(v -> {
                redirectToOurShops(root);
            });
        }else {
            makeInVisible(fragmentHomeBinding.cvStoreImg,null);
            makeInVisible(fragmentHomeBinding.rlO3Banner,null);
        }

    }

    private void rvProductLis(HomeResponse homeResponse) {
        if (homeResponse.getResponse().getProductList()!=null
                && homeResponse.getResponse().getProductList().size() > 0) {
            makeVisible(fragmentHomeBinding.recProduct, null);
            //Should remove this latter
            productList.clear();
            for (ProductListResponse.Product product : homeResponse.getResponse().getProductList()) {
                product.setUnit(product.getProductUnits().get(0));
                product.setAccurateWeight(product.getUnit().getWeight() + product.getUnit().getUnitName());
                productList.add(product);
            }
//                            productList = productListResponse.getProductResponseDt().getProductList();
            productManager = new LinearLayoutManager(requireContext());
            fragmentHomeBinding.recProduct.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recProduct.setHasFixedSize(true);
            fragmentHomeBinding.recProduct.setLayoutManager(productManager);
            productListAdapter = new ProductListAdapter(productList, getActivity(), this::toProductDetail,(product, position) ->  {
                if (product.getInCart()==0)
                addToCartProduct(product,"Product", position);
            });
            fragmentHomeBinding.recProduct.setAdapter(productListAdapter);
        }else makeInVisible(fragmentHomeBinding.recProduct, null);

    }

    private void rvSeasonalList(HomeResponse homeResponse) {
        if (homeResponse.getResponse().getSeasonalProduct()!=null
                && homeResponse.getResponse().getSeasonalProduct().size() > 0) {
            for (int i = 0; i < homeResponse.getResponse().getSeasonalProduct().size(); i++) {
                if (i / 2 == 0) {
                    homeResponse.getResponse().getSeasonalProduct().get(i).setType("Green");
                } else
                    homeResponse.getResponse().getSeasonalProduct().get(i).setType("Yellow");
            }
            makeVisible(fragmentHomeBinding.recSeasonal, null);
            seasonalProductList = (ArrayList<SeasonalProductResponse.SeasonalProduct>) homeResponse.getResponse().getSeasonalProduct();
            seasonalManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
            fragmentHomeBinding.recSeasonal.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recSeasonal.setHasFixedSize(true);
            fragmentHomeBinding.recSeasonal.setLayoutManager(seasonalManager);
            seasonalBannerAdapter = new SeasonalBannerAdapter(getActivity(), seasonalProductList, seasonalProduct -> {
                Bundle bundle = new Bundle();
                bundle.putString("image", seasonalProduct.getProduct_image());
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_seasonalRegFragment);
            });
            fragmentHomeBinding.recSeasonal.setAdapter(seasonalBannerAdapter);
        }else makeInVisible(fragmentHomeBinding.recSeasonal,null);

    }

    private void rvCategory(HomeResponse homeResponse) {
        if (homeResponse.getResponse().getCategoryData()!=null
                && homeResponse.getResponse().getCategoryData().size() > 0) {
            makeVisible(fragmentHomeBinding.recCategory, fragmentHomeBinding.rlCategory);
            categories.addAll(homeResponse.getResponse().getCategoryData());
            System.out.println("categories "+categories.size());
            fragmentHomeBinding.homeLayout.setVisibility(View.VISIBLE);
        }else makeInVisible(fragmentHomeBinding.recCategory,
                fragmentHomeBinding.rlCategory);

    }

    private void rvBasketList(HomeResponse homeResponse) {
        if (homeResponse.getResponse().getBasketList()!=null
                && homeResponse.getResponse().getBasketList().size() > 0) {
            makeVisible(fragmentHomeBinding.recBasket, fragmentHomeBinding.rlBasket);
            baskets.addAll(homeResponse.getResponse().getBasketList());
            System.out.println("basket list :"+baskets.size());
            basketAdapter = new BasketAdapter(baskets, getActivity(), this::toBasketDetail);
            basketManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
            fragmentHomeBinding.recBasket.setLayoutManager(basketManager);
            fragmentHomeBinding.recBasket.setAdapter(basketAdapter);
        }else makeInVisible(fragmentHomeBinding.recBasket,fragmentHomeBinding.rlBasket);
    }

    private void rvExclusive(HomeResponse homeResponse) {
        if (homeResponse.getResponse().getExclusiveList()!=null
                && homeResponse.getResponse().getExclusiveList().size() > 0) {
            //Should remove this latter
            offerProducts.clear();
            for (ProductListResponse.Product product : homeResponse.getResponse().getExclusiveList()) {
                product.setUnit(product.getProductUnits().get(0));
                product.setAccurateWeight(product.getUnit().getWeight() + product.getUnit().getUnitName());
                offerProducts.add(product);
            }
//                            offerProducts = exclusiveResponse.getExclusiveData().getExclusivesList();
            makeVisible(fragmentHomeBinding.recExOffers, fragmentHomeBinding.rlExclusiveOffer);
            // Redirect to ProductOld details
            offerListAdapter = new ExclusiveOfferListAdapter(offerProducts, getActivity(), this::toProductDetail, (product, position) -> {
                if(product.getInCart()==0)
                addToCartProduct( product, "Offer",position);
            });
            exclusiveOfferManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
            fragmentHomeBinding.recExOffers.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recExOffers.setLayoutManager(exclusiveOfferManager);
            fragmentHomeBinding.recExOffers.setAdapter(offerListAdapter);
//
        }
        else makeInVisible(fragmentHomeBinding.recExOffers,fragmentHomeBinding.rlExclusiveOffer);

    }

    private void rvBestSelling(HomeResponse homeResponse) {
        if (homeResponse.getResponse().getBestSellingProductList()!=null
                && homeResponse.getResponse().getBestSellingProductList().size() > 0) {
            //Should remove this latter
            makeVisible(fragmentHomeBinding.recBestSelling, fragmentHomeBinding.rlBestSelling);
            for (ProductListResponse.Product product : homeResponse.getResponse().getBestSellingProductList()) {
                product.setUnit(product.getProductUnits().get(0));
                product.setAccurateWeight(product.getUnit().getWeight() + product.getUnit().getUnitName());
                bestSellings.add(product);
            }

//                            bestSellings = bestSellingResponse.getBestSellingData().getBestSellingProductList();
            bestSellingManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
            fragmentHomeBinding.recBestSelling.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recBestSelling.setLayoutManager(bestSellingManager);
            fragmentHomeBinding.recBestSelling.setAdapter(bestsellingAdapter);
        }else makeInVisible(fragmentHomeBinding.recBestSelling,fragmentHomeBinding.rlBestSelling);

    }

    private void rvBanners() {
        makeVisible(fragmentHomeBinding.viewPagerBanner, fragmentHomeBinding.dotsIndicator);
        this.NumberOfBanners = banners.size();
        bannerAdapter = new BannerAdapter(banners, requireActivity());
//
        fragmentHomeBinding.viewPagerBanner.setAdapter(bannerAdapter);
        // Set up tab indicators
        fragmentHomeBinding.dotsIndicator.setViewPager(fragmentHomeBinding.viewPagerBanner);
        bannerAutoSLider();
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
        //Category OnScroll
        fragmentHomeBinding.recCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    scrolling = true;
                    System.out.println("scrolling" + scrolling);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleCategoryCount = categoryManager.getChildCount();
                int totalCategoryCount = categoryManager.getItemCount();
                System.out.println("visibleCategoryCount: " + visibleCategoryCount + " totalCategoryCount " +
                        totalCategoryCount);
                if (scrolling && (categories.size() == totalCategoryCount)) {
                    scrolling = false;
                    try {
                        callCategoryApi(null,"onScrolled");
                        fragmentHomeBinding.recCategory.smoothScrollBy(dx,dy);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("scrolling" + scrolling);
            }
        });

        // Basket OnScroll
        fragmentHomeBinding.recBasket.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    scrolling = true;
                    System.out.println("scrolling" + scrolling);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleBasketCount = 0, totalBasketCount = 0, scrolledBaskets = 0;
                visibleBasketCount = basketManager.getChildCount();
                totalBasketCount = basketManager.getItemCount();
                scrolledBaskets = basketManager.findLastCompletelyVisibleItemPosition();
                System.out.println("visibleBasketCount: " + visibleBasketCount + " totalBasketCount " +
                        totalBasketCount + " scrolledBaskets " + scrolledBaskets);
                if (scrolling && (baskets.size() == totalBasketCount)) {
                    scrolling = false;
                    try {
                        callBasketApi(showCircleProgressDialog(context, ""), "onScrolled");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("scrolling" + scrolling);
            }
        });

        //Best Selling OnScroll
        fragmentHomeBinding.recBestSelling.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    scrolling = true;
                    System.out.println("scrolling" + scrolling);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleSellingCount = bestSellingManager.getChildCount();
                int totalSellingCount = bestSellingManager.getItemCount();
                System.out.println("visibleSellingCount: " + visibleSellingCount + " totalSellingCount " +
                        totalSellingCount);
                if (scrolling && (bestSellings.size() == totalSellingCount)) {
                    scrolling = false;
                    try {
                        callBestSellingApi(null,"onScrolled");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("scrolling" + scrolling);
            }
        });

        //Seasonal OnScroll
        fragmentHomeBinding.recSeasonal.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    scrolling = true;
                    System.out.println("scrolling" + scrolling);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleSeasonalCount = seasonalManager.getChildCount();
                int totalSeasonalCount = seasonalManager.getItemCount();
                System.out.println("visibleSeasonalCount: " + visibleSeasonalCount + " totalSeasonalCount " +
                        totalSeasonalCount);
                if (scrolling && (seasonalProductList.size() == totalSeasonalCount)) {
                    scrolling = false;
                    try {
                        callSeasonalProductApi(null,"onScrolled");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("scrolling" + scrolling);
            }
        });

        //Exclusive OnScrolled
        fragmentHomeBinding.recExOffers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    scrolling = true;
                    System.out.println("scrolling" + scrolling);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleCount = exclusiveOfferManager.getChildCount();
                int totalCount = exclusiveOfferManager.getItemCount();
                System.out.println("visibleExclusive: " + visibleCount + " totalExclusive " +
                        totalCount);
                if (scrolling && (offerProducts.size() == totalCount)) {
                    scrolling = false;
                    try {
                        callExclusiveOfferApi(null,"onScrolled");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("scrolling" + scrolling);
            }
        });

        //Product OnScrolled
        fragmentHomeBinding.recProduct.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                int visibleCount = productManager.getChildCount();
                int totalCount = productManager.getItemCount();
                System.out.println("visibleExclusive: " + visibleCount + " totalExclusive " +
                        totalCount);
                if (productList.size() == totalCount) {
                    try {
                        callProductListApi(null,"onScrolled");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void checkEmpties() {
        //check if banner is empty
        if (banners == null || banners.size() == 0) {
            makeInVisible(fragmentHomeBinding.viewPagerBanner, fragmentHomeBinding.dotsIndicator);
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
        fragmentHomeBinding.homeLayout.setVisibility(View.GONE);
        banners.clear();
        categories.clear();
        baskets.clear();
        bestSellings.clear();
        offerProducts.clear();
        checkEmpties();
    }

    private void searchProducts() {
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
    private void callStoreBannerApi(ProgressDialog progressDialog) {
        Observer<StoreBannerResponse> bannerResponseObserver = storeBannerResponse -> {
            if (storeBannerResponse != null) {
                if (progressDialog!=null) progressDialog.dismiss();
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
    private void callProductListApi(ProgressDialog progressDialog,String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")){
            productOffset = productOffset+1;
        }else productOffset=0;
        Observer<ProductListResponse> productListResponseObserver = productListResponse -> {
            if (productListResponse != null) {
                if (progressDialog!=null)
                progressDialog.dismiss();
                Log.e(TAG, "callProductListApi: " + productListResponse.getMessage());
                switch (productListResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (productListResponse.getProductResponseDt().getProductList()!=null
                                && productListResponse.getProductResponseDt().getProductList().size() > 0) {
                            makeVisible(fragmentHomeBinding.recProduct, null);
                            //Should remove this latter
                            productList.clear();
                            for (ProductListResponse.Product product : productListResponse.getProductResponseDt().getProductList()) {
                                product.setUnit(product.getProductUnits().get(0));
                                product.setAccurateWeight(product.getUnit().getWeight() + product.getUnit().getUnitName());
                                productList.add(product);
                            }
//                            productList = productListResponse.getProductResponseDt().getProductList();
                            productManager = new LinearLayoutManager(requireContext());
                            fragmentHomeBinding.recProduct.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recProduct.setHasFixedSize(true);
                            fragmentHomeBinding.recProduct.setLayoutManager(productManager);
                            productListAdapter = new ProductListAdapter(productList, getActivity(), this::toProductDetail,(product, position) ->  {
                                   addToCartProduct(product,"Product",position);
                            });
                            fragmentHomeBinding.recProduct.setAdapter(productListAdapter);
//

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
                listingParams("",PRODUCT), HomeFragment.this)
                .observe(getViewLifecycleOwner(), productListResponseObserver);
    }


    //Seasonal ProductOld listing here
    private void callSeasonalProductApi(ProgressDialog progressDialog,String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")){
            seasonalOffset = seasonalOffset+1;
        }else seasonalOffset=0;
        Observer<SeasonalProductResponse> seasonalProductObserver = seasonalProductResponse -> {
            if (seasonalProductResponse != null) {
                if (progressDialog!=null) progressDialog.dismiss();
                Log.e("SeasonalProduct Api ResponseData", new Gson().toJson(seasonalProductResponse));
                switch (seasonalProductResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (seasonalProductResponse.getSeasonalProducts()!=null
                                && seasonalProductResponse.getSeasonalProducts().size() > 0) {
                            for (int i = 0; i < seasonalProductResponse.getSeasonalProducts().size(); i++) {
                                if (i / 2 == 0) {
                                    seasonalProductResponse.getSeasonalProducts().get(i).setType("Green");
                                } else
                                    seasonalProductResponse.getSeasonalProducts().get(i).setType("Yellow");
                            }
                            makeVisible(fragmentHomeBinding.recSeasonal, null);
                            seasonalProductList = seasonalProductResponse.getSeasonalProducts();
                            seasonalManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
                            fragmentHomeBinding.recSeasonal.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recSeasonal.setHasFixedSize(true);
                            fragmentHomeBinding.recSeasonal.setLayoutManager(seasonalManager);
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
        homeViewModel.seasonalResponseLiveData(progressDialog, listingParams("",SEASONAL), HomeFragment.this)
                .observe(getViewLifecycleOwner(), seasonalProductObserver);

    }

    //Best selling Data listing here
    private void callBestSellingApi(ProgressDialog showCircleProgressDialog, String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")){
            bestSellingOffset = bestSellingOffset+1;
        }else bestSellingOffset=0;
        Observer<BestSellingResponse> bestSellingResponseObserver = bestSellingResponse -> {
            if (bestSellingResponse != null) {
                if (showCircleProgressDialog!=null)
                showCircleProgressDialog.dismiss();
                Log.e("Best selling Api ResponseData", new Gson().toJson(bestSellingResponse));
                switch (bestSellingResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (bestSellingResponse.getBestSellingData().getBestSellingProductList()!=null
                                && bestSellingResponse.getBestSellingData().getBestSellingProductList().size() > 0) {
                            //Should remove this latter
                            makeVisible(fragmentHomeBinding.recBestSelling, fragmentHomeBinding.rlBestSelling);
                            for (ProductListResponse.Product product : bestSellingResponse.getBestSellingData().getBestSellingProductList()) {
                                product.setUnit(product.getProductUnits().get(0));
                                product.setAccurateWeight(product.getUnit().getWeight() + product.getUnit().getUnitName());
                                bestSellings.add(product);
                            }

//                            bestSellings = bestSellingResponse.getBestSellingData().getBestSellingProductList();
                            bestSellingManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
                            bestsellingAdapter = new ExclusiveOfferListAdapter(bestSellings, requireActivity(), this::toProductDetail, (product, position) -> {
                                if(product.getInCart()==0)
                                addToCartProduct( product, "Best", position);
                            });
                            fragmentHomeBinding.recBestSelling.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recBestSelling.setLayoutManager(bestSellingManager);
                            fragmentHomeBinding.recBestSelling.setAdapter(bestsellingAdapter);
                            // Redirect to ProductOld details
//
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
                listingParams( "",BEST_SELLING), HomeFragment.this)
                .observe(getViewLifecycleOwner(), bestSellingResponseObserver);
    }

    //Add to CART
    private void addToCartProduct(ProductListResponse.Product product,
                                  String addType, int position) {
        Observer<BaseResponse> baseResponseObserver = response -> {
            if (response != null) {
                Log.e(TAG, "addToCartProduct: " + new Gson().toJson(response));
                switch (response.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, response.getMessage());
                        if (addType.equalsIgnoreCase("BEST")) {
                            bestSellings.clear();
                            callBestSellingApi(null,"load");
                        } else if (addType.equalsIgnoreCase("Offer")){
                            offerProducts.clear();
                            callExclusiveOfferApi(null, "load");
                        }else {
                            productList.clear();
                            callProductListApi(null,"load");
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
        if (product.getInCart()==0) {
            homeViewModel.addToCartProductLiveData(addtoCartParam(product), HomeFragment.this)
                    .observe(getViewLifecycleOwner(), baseResponseObserver);
        }else infoToast(context,"remove product from cart");
    }

    //Product Offer Data listing
    private void callExclusiveOfferApi(ProgressDialog progressDialog, String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")){
            exclusiveOffset = exclusiveOffset+1;
        }else exclusiveOffset =0;
        Observer<ExclusiveResponse> exclusiveResponseObserver = exclusiveResponse -> {
            if (exclusiveResponse != null) {
                if (progressDialog!=null) progressDialog.dismiss();
                Log.e("Category Api ResponseData", new Gson().toJson(exclusiveResponse));
                switch (exclusiveResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (exclusiveResponse.getExclusiveData().getExclusivesList()!=null
                                && exclusiveResponse.getExclusiveData().getExclusivesList().size() > 0) {
                           //Should remove this latter
                            offerProducts.clear();
                            for (ProductListResponse.Product product : exclusiveResponse.getExclusiveData().getExclusivesList()) {
                                product.setUnit(product.getProductUnits().get(0));
                                product.setAccurateWeight(product.getUnit().getWeight() + product.getUnit().getUnitName());
                                offerProducts.add(product);
                            }

//                            offerProducts = exclusiveResponse.getExclusiveData().getExclusivesList();
                            makeVisible(fragmentHomeBinding.recExOffers, fragmentHomeBinding.rlExclusiveOffer);
                            // Redirect to ProductOld details
                            offerListAdapter = new ExclusiveOfferListAdapter(offerProducts, getActivity(), this::toProductDetail, (product, position) ->  {
                                addToCartProduct( product, "Offer", position);
                            });
                            exclusiveOfferManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
                            fragmentHomeBinding.recExOffers.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recExOffers.setLayoutManager(exclusiveOfferManager);
                            fragmentHomeBinding.recExOffers.setAdapter(offerListAdapter);
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
        homeViewModel.exclusiveResponseLiveData(progressDialog, listingParams( "",EXCLUSIVE), HomeFragment.this)
                .observe(getViewLifecycleOwner(), exclusiveResponseObserver);
    }

    //Basket APi ResponseData gere
    private void callBasketApi(ProgressDialog progressDialog,String loadType)
    {
        if (loadType.equalsIgnoreCase("onScrolled")){
            basketOffset = basketOffset+1;
        }basketOffset=0;
        Observer<BasketResponse> bannerResponseObserver = basketResponse -> {
            if (basketResponse != null) {
                if (progressDialog!=null) progressDialog.dismiss();
                Log.e("Category Api ResponseData", new Gson().toJson(basketResponse));
                switch (basketResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (basketResponse.getData().getBaskets()!=null
                                && basketResponse.getData().getBaskets().size() > 0) {
                            makeVisible(fragmentHomeBinding.recBasket, fragmentHomeBinding.rlBasket);
                            baskets.addAll(basketResponse.getData().getBaskets());
                            System.out.println("basket list :"+baskets.size());
                            basketAdapter = new BasketAdapter(baskets, getActivity(), this::toBasketDetail);
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
        homeViewModel.basketResponseLiveData(progressDialog, listingParams( "",BASKET), HomeFragment.this)
                .observe(getViewLifecycleOwner(), bannerResponseObserver);

    }

    /*Category API*/
    private void callCategoryApi(ProgressDialog progressDialog,String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")){
            categoryOffset = categoryOffset+1;
        }categoryOffset=0;
        Observer<CategoryResponse> categoryResponseObserver = categoryResponse -> {
            if (categoryResponse != null) {
                if (progressDialog!=null) progressDialog.dismiss();
                Log.e("Category Api ResponseData", new Gson().toJson(categoryResponse));
                switch (categoryResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (categoryResponse.getCategoryData() != null) {
                           //Should remove this latter
                            if (categoryResponse.getCategoryData().getCategoryList()!=null
                                    && categoryResponse.getCategoryData().getCategoryList().size() > 0) {
                                makeVisible(fragmentHomeBinding.recCategory, fragmentHomeBinding.rlCategory);
                                categories.addAll(categoryResponse.getCategoryData().getCategoryList());
                                categoryAdapter.notifyDataSetChanged();
                                System.out.println("categories "+categories.size());
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
        homeViewModel.categoryResponseLiveData(progressDialog, listingParams("",CATEGORY), HomeFragment.this)
                .observe(getViewLifecycleOwner(), categoryResponseObserver);

    }

    //Banner API here
    private void callBannerApi(ProgressDialog progressDialog) {
        Observer<BannerResponse> bannerResponseObserver = bannerResponse -> {
            if (bannerResponse != null) {
                if (progressDialog!=null) progressDialog.dismiss();
                Log.e("Banner Api ResponseData", new Gson().toJson(bannerResponse));
                switch (bannerResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (bannerResponse.getData().getBanners().size() > 0) {
                            makeVisible(fragmentHomeBinding.viewPagerBanner, fragmentHomeBinding.dotsIndicator);
                            banners = bannerResponse.getData().getBanners();
                            rvBanners();
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
        homeViewModel.bannerResponseLiveData(progressDialog, HomeFragment.this)
                .observe(getViewLifecycleOwner(), bannerResponseObserver);

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

    private void redirectToOurShops(View v) {
        NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_our_stores);
    }



    /*Create a Dummy banner if no banner is available*/
    //Banner Auto Scroll
    private void bannerAutoSLider() {
        Log.e("SetSliderAutoTimer", "SetSliderAutoTimer");
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentBanner == NumberOfBanners) {
                    currentBanner = 0;
                }
                fragmentHomeBinding.viewPagerBanner.setCurrentItem(currentBanner++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(() -> {

                });
            }
        }, DELAY_MS, PERIOD_MS);
    }


    private HashMap<String, String> listingParams( int offset,String search) {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.LIMIT, String.valueOf(limit));
        map.put(AppConstants.OFFSET, String.valueOf(offset));
        map.put(AppConstants.SEARCH, search);
        return map;
    }
    private HashMap<String, String> listingParams(String search,int api) {
        HashMap<String, String> map = new HashMap<>();
        switch (api){
            case CATEGORY:
                System.out.println("categoryOffset"+categoryOffset);
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
//        NavigationView navigationView = ((HomeActivity) requireActivity()).binding.navView;
//        View headerView = navigationView.getHeaderView(0);
//        RelativeLayout rlEditProfile = headerView.findViewById(R.id.rl_edit_profile);
//        rlEditProfile.setOnClickListener(v -> {
//            ((HomeActivity) requireActivity()).drawer.closeDrawer(GravityCompat.START);
//            Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_profile);
////            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.action_nav_home_to_nav_profile);
//        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentHomeBinding = null;
    }


    private HashMap<String, String> addtoCartParam(ProductListResponse.Product product) {
        HashMap<String, String> map = new HashMap<>();
        map.put(PRODUCT_ID, product.getProductId());
        map.put(PU_ID, product.getUnit().getpId());
        map.put(QUANTITY, "1");
        return map;
    }
    private HashMap<String, String> profileParam(String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(CUSTOMER_ID,userId);
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
                        callBannerApi(showCircleProgressDialog(context, ""));
                        break;
                    case 1:
                        //Call Category API after network error
                        callCategoryApi(showCircleProgressDialog(context, ""), type);
                        break;
                    case 2:
                        //Call Basket API after network error
                        callBasketApi(showCircleProgressDialog(context, ""),type);
                        break;
                    case 3:
                        //Call Product API after network error
                        callExclusiveOfferApi(showCircleProgressDialog(context, ""),type);
                        break;
                    case 4:
                        //Call Bestselling API after network error
                        callBestSellingApi(showCircleProgressDialog(context, ""),type);
                        break;
                    case 5:
                        //Call Seasonal ProductOld API after network error
                        callSeasonalProductApi(showCircleProgressDialog(context, ""),type);
                        break;
                    case 6:
                        //Call ProductOld List API after network error
                        callProductListApi(showCircleProgressDialog(context, ""),type);
                        break;
                    case 7:
                        //Call ProductOld List API after network error
                        callStoreBannerApi(showCircleProgressDialog(context, ""));
                        break;

                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}