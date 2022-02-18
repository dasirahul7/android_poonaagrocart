package com.poona.agrocart.ui.search;

import static com.poona.agrocart.app.AppConstants.PRODUCT_ID;
import static com.poona.agrocart.app.AppConstants.PU_ID;
import static com.poona.agrocart.app.AppConstants.QUANTITY;
import static com.poona.agrocart.app.AppConstants.SEARCH_KEY;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.databinding.FragmentSearchBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.adapter.ProductListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SearchFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {
    public static final String TAG = SearchFragment.class.getSimpleName();
    private FragmentSearchBinding fragmentSearchBinding;
    private SearchViewModel searchViewModel;
    private View searchView;
    private Bundle bundle;
    private int limit = 10, offset = 0;
    private int visibleItemCount = 0;
    private int totalCount = 0;
    private String searchKey = "",searchType ="";
    private ProductListAdapter searchAdapter;
    Timer timer = new Timer();
    boolean isTyping = false;
    private ArrayList<ProductListResponse.Product> productList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSearchBinding = FragmentSearchBinding.inflate(LayoutInflater.from(context));
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchView = fragmentSearchBinding.getRoot();
        initViews();
        searchItems(searchView);
        productList.clear();
        if (isConnectingToInternet(context)){
            callSearchProductApi(searchView,showCircleProgressDialog(context,""),"load");
        }else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
        //pagination here
        setScrollListener();
        return searchView;
    }


    private void searchItems(View searchView) {
        fragmentSearchBinding.etSearch.addTextChangedListener(new TextWatcher() {
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
                                    if (isConnectingToInternet(context)){
                                        productList.clear();
                                        callSearchProductApi(searchView,showCircleProgressDialog(context,""),"load");
                                    }else {
                                        showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
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

    private void initViews() {
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.menu_icon_toggle, null);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setNavigationIcon(d);
        });
        linearLayoutManager = new LinearLayoutManager(requireContext());

        bundle = getArguments();
        assert bundle != null;
        searchKey= bundle.getString(SEARCH_KEY);
        searchType = bundle.getString(SEARCH_TYPE);
        fragmentSearchBinding.etSearch.setText(searchKey);
    }

    //Search ProductOld API here
    private void callSearchProductApi(View searchView, ProgressDialog progressDialog, String apiFrom) {
        if (apiFrom.equalsIgnoreCase("onScrolled")) {
            offset = offset + 1;
        } else offset = 0;
        Observer<ProductListResponse> productListResponseObserver = productListResponse -> {
            if (productListResponse != null) {
                progressDialog.dismiss();
                Log.e(TAG, "callSearchProductApi: " + productListResponse.getMessage());
                switch (productListResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (productListResponse.getProductResponseDt().getProductList().size() > 0) {
                            for (ProductListResponse.Product product :productListResponse.getProductResponseDt().getProductList()){
                                product.setUnit(product.getProductUnits().get(0));
                                product.setAccurateWeight(product.getUnit().getWeight()+product.getUnit().getUnitName());
                                productList.add(product);
                            }

//                            productList = productListResponse.getProductResponseDt().getProductList();
                            setSearchProductList();
                        }else fragmentSearchBinding.tvNoData.setVisibility(View.VISIBLE);
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        fragmentSearchBinding.tvNoData.setVisibility(View.VISIBLE);
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
        searchViewModel.searchListResponseLiveData(progressDialog, listingParams(),
                SearchFragment.this)
                .observe(getViewLifecycleOwner(), productListResponseObserver);
    }

    /*Search pagination*/
    private void setScrollListener() {
        fragmentSearchBinding.recProduct.setNestedScrollingEnabled(true);
        NestedScrollView nestedScrollView = fragmentSearchBinding.nvProductList;

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                visibleItemCount = linearLayoutManager.getItemCount();

                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount != totalCount) {
                    if (isConnectingToInternet(context)){
                        callSearchProductApi(searchView,showCircleProgressDialog(context, ""), "onScrolled");
                    }else showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                } else if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount == totalCount) {
                    infoToast(requireActivity(), getString(R.string.no_result_found));  //change
                }
            }
        });

    }

    private void setSearchProductList() {
        fragmentSearchBinding.tvNoData.setVisibility(View.GONE);
        fragmentSearchBinding.recProduct.setNestedScrollingEnabled(false);
        fragmentSearchBinding.recProduct.setHasFixedSize(true);
        fragmentSearchBinding.recProduct.setLayoutManager(linearLayoutManager);
        searchAdapter = new ProductListAdapter(productList, getActivity(), this::toProductDetail, this::addToCartProduct);
        fragmentSearchBinding.recProduct.setAdapter(searchAdapter);
    }

    private void addToCartProduct(ProductListResponse.Product product, int position) {
    @SuppressLint("NotifyDataSetChanged") Observer<BaseResponse> baseResponseObserver = response -> {
        if (response != null) {
            Log.e(TAG, "addToCartProduct: " + new Gson().toJson(response));
            switch (response.getStatus()) {
                case STATUS_CODE_200://Record Create/Update Successfully
                    successToast(context, response.getMessage());
                        productList.get(position).setInCart(1);
                        searchAdapter.notifyDataSetChanged();
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
    searchViewModel.addToCartProductLiveData(paramAddToCart(product),SearchFragment.this)
            .observe(getViewLifecycleOwner(),baseResponseObserver);
    }

    public void toProductDetail(ProductListResponse.Product product) {
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, product.getProductId());
        NavHostFragment.findNavController(SearchFragment.this).navigate(R.id.action_nav_home_to_nav_product_details,bundle);
    }


    private HashMap<String, String> listingParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.LIMIT, String.valueOf(limit));
        map.put(AppConstants.OFFSET, String.valueOf(offset));
        map.put(AppConstants.SEARCH, fragmentSearchBinding.etSearch.getText().toString().trim());
        return map;
    }
  private HashMap<String, String> paramAddToCart(ProductListResponse.Product product) {
      HashMap<String, String> map = new HashMap<>();
      map.put(PRODUCT_ID, product.getProductId());
      map.put(PU_ID, product.getUnit().getpId());
      map.put(QUANTITY, "1");
        return map;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), SearchFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        // Call ProductOld List API after network error
                        productList.clear();
                        callSearchProductApi(searchView, showCircleProgressDialog(context, ""), type);
                        break;

                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}
