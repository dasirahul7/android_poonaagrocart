package com.poona.agrocart.ui.home;

import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.data.network.ExclusiveData;
import com.poona.agrocart.data.network.ExclusiveResponse;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.BannerResponse;
import com.poona.agrocart.data.network.reponses.BasketResponse;
import com.poona.agrocart.data.network.reponses.CategoryResponse;
import com.poona.agrocart.databinding.FragmentHomeBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.adapter.BannerAdapter;
import com.poona.agrocart.ui.home.adapter.BasketAdapter;
import com.poona.agrocart.ui.home.adapter.ExclusiveOfferListAdapter;
import com.poona.agrocart.ui.home.adapter.OfferProductListAdapter;
import com.poona.agrocart.ui.home.adapter.CategoryAdapter;
import com.poona.agrocart.ui.home.adapter.ProductListAdapter;
import com.poona.agrocart.ui.home.adapter.SeasonalBannerAdapter;
import com.poona.agrocart.ui.home.model.Banner;
import com.poona.agrocart.ui.home.model.Basket;
import com.poona.agrocart.ui.home.model.Category;
import com.poona.agrocart.ui.home.model.Exclusive;
import com.poona.agrocart.ui.home.model.Product;
import com.poona.agrocart.ui.home.model.SeasonalProduct;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {

    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 3000;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding fragmentHomeBinding;
    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductListAdapter productListAdapter;
    private OfferProductListAdapter bestsellingAdapter;
    private ExclusiveOfferListAdapter offerListAdapter;
    private BasketAdapter basketAdapter;
    private SeasonalBannerAdapter seasonalBannerAdapter;
    private ArrayList<Product> bestSellings = new ArrayList<>();
    private ArrayList<Exclusive> offerProducts = new ArrayList<>();
    private ArrayList<Product> cartProductList = new ArrayList<>();
    private ArrayList<Banner> banners = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Basket> baskets = new ArrayList<>();
    private ArrayList<SeasonalProduct> seasonalProductList = new ArrayList<>();
    private final ArrayList<Product> mCartList = new ArrayList<Product>();
    private ArrayList<String> BasketIds = new ArrayList<>();
    private int limit = 0;
    private int offset = 0;
    private View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        root = fragmentHomeBinding.getRoot();
        callBannerApi(showCircleProgressDialog(context, ""), limit, offset);
        callCategoryApi(root, showCircleProgressDialog(context, ""), limit, offset);
        callBasketApi(root,showCircleProgressDialog(context,""),limit,offset);
        callExclusiveOfferApi(root,showCircleProgressDialog(context,""),limit,offset);
//        setBasketItems(root);
        getBasketItems();
        setBestSellings(root);
//        setOfferProduct(root);
        setCartItems(root);
        setStoreBanner(root);
        setSeasonBanners(root);
        initClick();
        return root;
    }

    private void callExclusiveOfferApi(View root, ProgressDialog progressDialog, int limit, int offset) {
        limit = limit + 10;
        Observer<ExclusiveResponse> exclusiveResponseObserver = exclusiveResponse -> {
            if (exclusiveResponse != null) {
                progressDialog.dismiss();
                Log.e("Category Api Response", new Gson().toJson(exclusiveResponse));
                switch (exclusiveResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (exclusiveResponse.getExclusiveData().getExclusivesList().size()>0){
                            this.offerProducts = exclusiveResponse.getExclusiveData().getExclusivesList();
                            for (Exclusive exclusive:exclusiveResponse.getExclusiveData().getExclusivesList()){
                                System.out.println("exclusive :"+exclusive.getProductName());
                            }
                            offerListAdapter = new ExclusiveOfferListAdapter(this.offerProducts, getActivity(), root);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
                            fragmentHomeBinding.recOffers.setNestedScrollingEnabled(false);
                            fragmentHomeBinding.recOffers.setLayoutManager(layoutManager);
                            fragmentHomeBinding.recOffers.setAdapter(offerListAdapter);
                            // Redirect to Product details
                            offerListAdapter.setOnProductClick(product -> {
                                toProductDetail(product, root);
                            });
//                            offerListAdapter.setOnPlusClick((product, position) -> {
//                                addToBasket(product);
//                                offerProducts.get(position).(true);
//                                offerProducts.get(position).setQuantity("1");
//                                offerListAdapter.notifyItemChanged(position);
//                            });
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
        homeViewModel.exclusiveResponseLiveData(progressDialog,listingParams(limit,offset),HomeFragment.this)
            .observe(getViewLifecycleOwner(),exclusiveResponseObserver);
    }


    private void setSeasonBanners(View root) {
        homeViewModel.getLiveSeasonProducts().observe(getViewLifecycleOwner(), seasonalProducts -> {
            seasonalProductList = seasonalProducts;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
            fragmentHomeBinding.recSeasonal.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recSeasonal.setHasFixedSize(true);
            fragmentHomeBinding.recSeasonal.setLayoutManager(linearLayoutManager);
            seasonalBannerAdapter = new SeasonalBannerAdapter(getActivity(), seasonalProducts, root);
            fragmentHomeBinding.recSeasonal.setAdapter(seasonalBannerAdapter);
        });
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
        fragmentHomeBinding.cardviewOurShops.setOnClickListener(v -> {
            redirectToOurShops(root);
        });
    }

    private void redirectToOurShops(View v) {
        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_our_stores);
    }

    private void setCartItems(View root) {

        homeViewModel.getLiveDataCartProduct().observe(getViewLifecycleOwner(), cartProducts -> {
            this.cartProductList = cartProducts;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
            fragmentHomeBinding.recProduct.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recProduct.setHasFixedSize(true);
            fragmentHomeBinding.recProduct.setLayoutManager(linearLayoutManager);
            productListAdapter = new ProductListAdapter(cartProductList, getActivity(), root);
            fragmentHomeBinding.recProduct.setAdapter(productListAdapter);
            productListAdapter.setOnProductClick(product -> {
                toProductDetail(product, root);
            });
            productListAdapter.setOnPlusClick((product, position) -> {
                addToBasket(product);
                this.cartProductList.get(position).setInBasket(true);
                this.cartProductList.get(position).setQuantity("1");
                productListAdapter.notifyItemChanged(position);
            });
        });

    }

//    private void setBasketItems(View view) {
//        homeViewModel.getLiveDataBaskets().observe(getViewLifecycleOwner(), baskets -> {
//            this.baskets = baskets;
//            basketAdapter = new BasketAdapter(this.baskets, getActivity(), view);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
//            fragmentHomeBinding.recBasket.setNestedScrollingEnabled(false);
//            fragmentHomeBinding.recBasket.setLayoutManager(layoutManager);
//            fragmentHomeBinding.recBasket.setAdapter(basketAdapter);
//        });
//
//    }

//    private void setOfferProduct(View root) {
//        homeViewModel.getLiveDataOffer().observe(getViewLifecycleOwner(), liveProducts -> {
//            this.offerProducts = liveProducts;
//            offerListAdapter = new OfferProductListAdapter(this.offerProducts, getActivity(), root);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
//            fragmentHomeBinding.recOffers.setNestedScrollingEnabled(false);
//            fragmentHomeBinding.recOffers.setLayoutManager(layoutManager);
//            fragmentHomeBinding.recOffers.setAdapter(offerListAdapter);
//            // Redirect to Product details
//            offerListAdapter.setOnProductClick(product -> {
//                toProductDetail(product, root);
//            });
//            offerListAdapter.setOnPlusClick((product, position) -> {
//                addToBasket(product);
//                offerProducts.get(position).setInBasket(true);
//                offerProducts.get(position).setQuantity("1");
//                offerListAdapter.notifyItemChanged(position);
//            });
//        });
//    }

    private void setBestSellings(View root) {
        homeViewModel.getLiveDataBestSelling().observe(getViewLifecycleOwner(), liveList -> {
            this.bestSellings = liveList;
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
            bestsellingAdapter = new OfferProductListAdapter(this.bestSellings, requireActivity(), root);
            fragmentHomeBinding.recOfferProduct.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recOfferProduct.setLayoutManager(layoutManager);
            fragmentHomeBinding.recOfferProduct.setAdapter(bestsellingAdapter);
            // Redirect to Product details
            bestsellingAdapter.setOnProductClick(product -> {
                toProductDetail(product, root);
            });
            bestsellingAdapter.setOnPlusClick((product, position) -> {
                addToBasket(product);
                this.bestSellings.get(position).setInBasket(true);
                this.bestSellings.get(position).setQuantity("1");
                bestsellingAdapter.notifyItemChanged(position);
            });

        });

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
                        if (basketResponse.getData().getBaskets().size()>0){
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
        homeViewModel.basketResponseLiveData(progressDialog, listingParams(limit, offset), HomeFragment.this)
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
                        if(categoryResponse.getCategoryData() != null){
                            if (categoryResponse.getCategoryData().getCategoryList().size() > 0) {
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
        homeViewModel.categoryResponseLiveData(progressDialog, listingParams(limit, offset), HomeFragment.this)
                .observe(getViewLifecycleOwner(), categoryResponseObserver);

//        homeViewModel.getLiveDataCategory().observe(getViewLifecycleOwner(), categories1 -> {
//            categories = categories1;
//            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
//            categoryAdapter = new CategoryAdapter(categories, requireActivity(), view);
//            fragmentHomeBinding.recCategory.setNestedScrollingEnabled(false);
//            fragmentHomeBinding.recCategory.setAdapter(categoryAdapter);
//            fragmentHomeBinding.recCategory.setLayoutManager(layoutManager);
//        });

    }

    private void callBannerApi(ProgressDialog progressDialog, int limit, int offset) {
        limit = limit + 10;
        Observer<BannerResponse> bannerResponseObserver = bannerResponse -> {
            if (bannerResponse != null) {
                if (bannerResponse.getData().getBanners().size() > 0) {
                    banners.clear();
                    banners = bannerResponse.getData().getBanners();
                    bannerAdapter = new BannerAdapter(banners, requireActivity());
//
                    fragmentHomeBinding.viewPagerBanner.setAdapter(bannerAdapter);
                    // Set up tab indicators
                    fragmentHomeBinding.dotsIndicator.setViewPager(fragmentHomeBinding.viewPagerBanner);
                }
            }
        };
        homeViewModel.bannerResponseLiveData(progressDialog, listingParams(limit, offset), HomeFragment.this)
                .observe(getViewLifecycleOwner(), bannerResponseObserver);

    }

    private HashMap<String, String> listingParams(int limit, int offset) {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.LIMIT, String.valueOf(limit));
        map.put(AppConstants.OFFSET, String.valueOf(offset));
        return map;
    }

    @Override
    public void onResume() {
        try {
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
                        callCategoryApi(root,showCircleProgressDialog(context,""),limit,offset);
                        break;
                    case 2:
                        //Call Basket API after network error
                        callBasketApi(root,showCircleProgressDialog(context,""),limit,offset);
                        break;
                    case 3:
                        //Call Exclusive API after network error
                        callExclusiveOfferApi(root,showCircleProgressDialog(context,""),limit,offset);
                        break;

                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}