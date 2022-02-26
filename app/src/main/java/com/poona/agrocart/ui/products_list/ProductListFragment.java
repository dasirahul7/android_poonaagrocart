package com.poona.agrocart.ui.products_list;

import static com.poona.agrocart.app.AppConstants.AllBasket;
import static com.poona.agrocart.app.AppConstants.AllExclusive;
import static com.poona.agrocart.app.AppConstants.AllSelling;
import static com.poona.agrocart.app.AppConstants.BASKET;
import static com.poona.agrocart.app.AppConstants.BASKET_ID;
import static com.poona.agrocart.app.AppConstants.CATEGORY_ID;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.LIST_TITLE;
import static com.poona.agrocart.app.AppConstants.LIST_TYPE;
import static com.poona.agrocart.app.AppConstants.PRODUCT_ID;
import static com.poona.agrocart.app.AppConstants.PU_ID;
import static com.poona.agrocart.app.AppConstants.QUANTITY;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.BasketResponse;
import com.poona.agrocart.data.network.responses.BestSellingResponse;
import com.poona.agrocart.data.network.responses.ExclusiveResponse;
import com.poona.agrocart.data.network.responses.ProductListByResponse;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.data.network.responses.homeResponse.Basket;
import com.poona.agrocart.data.network.responses.homeResponse.Product;
import com.poona.agrocart.databinding.FragmentProductListBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.bottom_sheet.BottomSheetFilterFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.products_list.adapter.BasketGridAdapter;
import com.poona.agrocart.ui.products_list.adapter.ProductGridAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ProductListFragment extends BaseFragment implements NetworkExceptionListener, ProductGridAdapter.OnProductClickListener {
    private static final String TAG = ProductListFragment.class.getSimpleName();
    private final int limit = 10;
    //search variables
    Timer timer = new Timer();
    boolean isTyping = false;
    private FragmentProductListBinding fragmentProductListBinding;
    private ProductListViewModel productListViewModel;
    private RecyclerView rvVegetables;
    private ArrayList<Product> productArrayList;
    private ArrayList<BasketResponse.Basket> basketArrayList;
    private ProductGridAdapter productGridAdapter;
    private BasketGridAdapter basketGridAdapter;
    //argument values
    private String CategoryId, ListTitle, ListType, fromScreen;
    private int visibleItemCount = 0;
    private final int totalCount = 0;
    private int offset = 0;
    private GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentProductListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false);
        fragmentProductListBinding.setLifecycleOwner(this);
        final View view = fragmentProductListBinding.getRoot();
        initView();
        setTitleBar();
        ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setVisibility(View.VISIBLE);
        ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setOnClickListener(v -> {
            // Show the Filter Dialog
            BottomSheetFilterFragment filterFragment = new BottomSheetFilterFragment();
            filterFragment.show(getChildFragmentManager(), "FilterFragment");
        });
        setRVAdapter();
        setScrollListener();
        searchProducts(view);
        return view;
    }


    private void setTitleBar() {
        initTitleWithBackBtn(ListTitle);

    }

    private void setRVAdapter() {
        basketArrayList = new ArrayList<>();
        productArrayList = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        rvVegetables.setHasFixedSize(true);
        rvVegetables.setLayoutManager(gridLayoutManager);
        if (isConnectingToInternet(context)) {
            //add API call here
            checkAndLoadData("load");
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }


    }

    // check and load data
    private void checkAndLoadData(String loading) {
        if (CategoryId == null) {
            fromScreen = getArguments().getString(FROM_SCREEN);
            if (fromScreen.equalsIgnoreCase(AllBasket))
                seeAllBasketListApi(showCircleProgressDialog(context, ""), loading);
            else if (fromScreen.equalsIgnoreCase(AllSelling)) {
                seeAllBestSellingApi(showCircleProgressDialog(context, ""), loading);
            } else if (fromScreen.equalsIgnoreCase(AllExclusive)) {
                seeAllExclusiveApi(showCircleProgressDialog(context, ""), loading);
            }
        } else callProductListApi(showCircleProgressDialog(context, ""), loading);
    }

    /*All Exclusive API*/
    private void seeAllExclusiveApi(ProgressDialog progressDialog, String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled")) {
            offset = offset + 1;
        } else offset = 0;
        Observer<ExclusiveResponse> exclusiveResponseObserver = exclusiveResponse -> {
            if (exclusiveResponse != null) {
                progressDialog.dismiss();
                Log.e(TAG, "seeAllExclusiveApi: " + new Gson().toJson(exclusiveResponse));
                switch (exclusiveResponse.getStatus()) {
                    case STATUS_CODE_200:
                        if (exclusiveResponse.getExclusiveData().getExclusivesList() != null
                                && exclusiveResponse.getExclusiveData().getExclusivesList().size() > 0) {
                            // Exclusive data listing
                            productArrayList.addAll(exclusiveResponse.getExclusiveData().getExclusivesList());
                            makeProductListing();
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        fragmentProductListBinding.tvNoData.setVisibility(View.VISIBLE);
                        // set a dummy banner if no banner is available
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
        productListViewModel.allExclusiveResponseLiveData(progressDialog, seeAllParams(), ProductListFragment.this, loadType)
                .observe(getViewLifecycleOwner(), exclusiveResponseObserver);
    }

    /*All Best Selling API*/
    private void seeAllBestSellingApi(ProgressDialog progressDialog, String load) {
        if (load.equalsIgnoreCase("onScrolled")) {
            offset = offset + 1;
        } else offset = 0;

        Observer<BestSellingResponse> bestSellingResponseObserver = bestSellingResponse -> {
            if (bestSellingResponse != null) {
                progressDialog.dismiss();
                Log.e(TAG, "seeAllBestSellingApi: " + new Gson().toJson(bestSellingResponse));
                switch (bestSellingResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        //Best selling listing
                        if (bestSellingResponse.getBestSellingData().getBestSellingProductList() != null) {
                            //Todo need to change that
                            if (bestSellingResponse.getBestSellingData().getBestSellingProductList().size() > 0) {
                                productArrayList.addAll(bestSellingResponse.getBestSellingData().getBestSellingProductList());
                                makeProductListing();
                            }
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        fragmentProductListBinding.tvNoData.setVisibility(View.VISIBLE);
                        // set a dummy banner if no banner is available
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
        productListViewModel.allBestSellingResponseLiveData(progressDialog, seeAllParams(),
                ProductListFragment.this, load)
                .observe(getViewLifecycleOwner(), bestSellingResponseObserver);
    }

    /*All Basket API*/
    private void seeAllBasketListApi(ProgressDialog showCircleProgressDialog, String apiFrom) {
        if (apiFrom.equalsIgnoreCase("onScrolled")) {
            offset = offset + 1;
        } else offset = 0;
        Observer<BasketResponse> basketResponseObserver = basketResponse -> {
            if (basketResponse != null) {
                Log.e(TAG, "callBasketListApi: " + new Gson().toJson(basketResponse));
                switch (basketResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        //Basket listing
                        if (basketResponse.getData().getBaskets() != null) {
                            if (basketResponse.getData().getBaskets().size() > 0) {
                                basketArrayList.addAll(basketResponse.getData().getBaskets());
                                makeBasketListing();
                            }
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        fragmentProductListBinding.tvNoData.setVisibility(View.VISIBLE);
                        // set a dummy banner if no banner is available
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
        productListViewModel.basketResponseLiveData(showCircleProgressDialog, seeAllParams(),
                ProductListFragment.this, apiFrom)
                .observe(getViewLifecycleOwner(), basketResponseObserver);
    }

    /*Product list API calling*/
    private void callProductListApi(ProgressDialog showCircleProgressDialog, String apiFrom) {
        if (apiFrom.equalsIgnoreCase("onScrolled")) {
            offset = offset + 1;
        } else offset = 0;

        Observer<ProductListByResponse> productListByResponseObserver = productListByResponse -> {
            if (productListByResponse != null) {
                showCircleProgressDialog.dismiss();
                switch (productListByResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (ListType.equalsIgnoreCase(BASKET)) {
                            //Basket listing
                            if (productListByResponse.getProductListResponseDt().getBasketList() != null) {
                                if (productListByResponse.getProductListResponseDt().getBasketList().size() > 0) {
                                    basketArrayList = productListByResponse.getProductListResponseDt().getBasketList();
                                    makeBasketListing();
                                }
                            }
                        } else {
                            if (productListByResponse.getProductListResponseDt().getProductList() != null) {
                                if (productListByResponse.getProductListResponseDt().getProductList().size() > 0) {
                                    productArrayList.addAll(productListByResponse.getProductListResponseDt().getProductList());

                                    makeProductListing();
                                }
                            }
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        fragmentProductListBinding.tvNoData.setVisibility(View.VISIBLE);
                        // set a dummy banner if no banner is available
                        warningToast(context, productListByResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(productListByResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, productListByResponse.getMessage());
                        break;
                }

            }
        };

        productListViewModel.productListByResponseLiveData(showCircleProgressDialog, parameters(), ProductListFragment.this, apiFrom)
                .observe(getViewLifecycleOwner(), productListByResponseObserver);
    }

    /*Search API here*/
    private void searchProducts(View root) {
        fragmentProductListBinding.etSearch.addTextChangedListener(new TextWatcher() {
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
                                        if (fragmentProductListBinding.etSearch.getText() != null) {
                                            if (!fragmentProductListBinding.etSearch.getText().toString().trim().equals("")) {
                                                productArrayList.clear();
                                                basketArrayList.clear();
                                                if (isConnectingToInternet(context)) {
                                                    //add API call here
                                                    checkAndLoadData("load");
                                                } else {
                                                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                                                }
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


    /*API parameters*/
    private HashMap<String, String> parameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.LIMIT, String.valueOf(limit));
        map.put(AppConstants.OFFSET, String.valueOf(offset));
        map.put(AppConstants.SEARCH, fragmentProductListBinding.etSearch.getText().toString().trim());
        map.put(CATEGORY_ID, CategoryId);
        return map;
    }

    private HashMap<String, String> seeAllParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.LIMIT, String.valueOf(limit));
        map.put(AppConstants.OFFSET, String.valueOf(offset));
        map.put(AppConstants.SEARCH, fragmentProductListBinding.etSearch.getText().toString().trim());
        return map;
    }

    /*Listing Basket*/
    private void makeBasketListing() {
        if (basketArrayList != null && basketArrayList.size() > 0) {
            fragmentProductListBinding.tvNoData.setVisibility(View.GONE);
            basketGridAdapter = new BasketGridAdapter(basketArrayList, basket -> {
                redirectToBasketDetails(basket);
            });
            rvVegetables.setAdapter(basketGridAdapter);
        }
    }

    /*Listing products*/
    private void makeProductListing() {
        if (productArrayList != null && productArrayList.size() > 0) {
            fragmentProductListBinding.tvNoData.setVisibility(View.GONE);
            productGridAdapter = new ProductGridAdapter(productArrayList, this);
            rvVegetables.setAdapter(productGridAdapter);
        }

    }

    private void initView() {
        rvVegetables = fragmentProductListBinding.rvProducts;
        productListViewModel = new ViewModelProvider(this).get(ProductListViewModel.class);

        Bundle bundle = this.getArguments();
        assert bundle != null;
        CategoryId = bundle.getString(CATEGORY_ID);
        ListTitle = bundle.getString(LIST_TITLE);
        ListType = bundle.getString(LIST_TYPE);

    }

    private void setScrollListener() {
        fragmentProductListBinding.rvProducts.setNestedScrollingEnabled(true);
        NestedScrollView nestedScrollView = fragmentProductListBinding.nvProductList;

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                visibleItemCount = gridLayoutManager.getItemCount();

                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount != totalCount) {
                    if (isConnectingToInternet(context)) {
                        //add API call here
                        checkAndLoadData("onScrolled");
                    } else {
                        showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                    }
                } else if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount == totalCount) {
                    infoToast(requireActivity(), getString(R.string.no_result_found));  //change
                }
            }
        });

    }

    /* Redirect to product detail screen*/
    private void redirectToProductsDetail(Product product) {
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, product.getProductId());
        NavHostFragment.findNavController(ProductListFragment.this).navigate(R.id.action_nav_products_list_to_product_details, bundle);
    }

    /*Redirect to Basket Detail screen*/
    private void redirectToBasketDetails(BasketResponse.Basket basket) {
        Bundle bundle = new Bundle();
        bundle.putString(BASKET_ID, basket.getId());
        NavHostFragment.findNavController(ProductListFragment.this).navigate(R.id.action_nav_products_list_to_basket_details, bundle);
    }

    @Override
    public void onNetworkException(int from, String apiFrom) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), ProductListFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        callProductListApi(showCircleProgressDialog(context, ""), apiFrom);
                        break;
                    case 1:
                        seeAllBasketListApi(showCircleProgressDialog(context, ""), apiFrom);
                        break;
                    case 2:
                        seeAllBestSellingApi(showCircleProgressDialog(context, ""), apiFrom);
                        break;
                    case 3:
                        seeAllExclusiveApi(showCircleProgressDialog(context, ""), apiFrom);
                }
            }
        }, context);

    }

    @Override
    public void onProductClick(Product product) {
        redirectToProductsDetail(product);
    }

    @Override
    public void onAddClick(String productId, String unitId, int position) {
        callAddToCartProductApi(productId, unitId, position);
    }

    private void callAddToCartProductApi(String productId, String unitId, int position) {
        @SuppressLint("NotifyDataSetChanged") Observer<BaseResponse> addToCartObserver = baseResponse -> {
            if (baseResponse != null) {
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(requireActivity(), baseResponse.getMessage());
                        productArrayList.get(position).setInCart(1);
                        productGridAdapter.notifyDataSetChanged();
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
        productListViewModel.addToCartProductApiCall(addToCartParam(productId, unitId), ProductListFragment.this)
                .observe(getViewLifecycleOwner(), addToCartObserver);
    }

    private HashMap<String, String> addToCartParam(String itemId, String unitId) {
        HashMap<String, String> map = new HashMap<>();
        if (unitId != null) {
            map.put(PRODUCT_ID, itemId);
            map.put(PU_ID, unitId);
        }
//        else {
//            map.put(BASKET_ID, itemId);
//        }
        map.put(QUANTITY, "1");
        return map;
    }
}