package com.poona.agrocart.ui.search;

import static com.poona.agrocart.app.AppConstants.SEARCH_KEY;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.CategoryResponse;
import com.poona.agrocart.data.network.reponses.ProductListResponse;
import com.poona.agrocart.databinding.FragmentSearchBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.adapter.ProductListAdapter;
import com.poona.agrocart.ui.home.model.Exclusive;
import com.poona.agrocart.ui.home.model.Product;

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
    private int limit = 0, offset = 0;
    private String searchKey = "";
    private ProductListAdapter searchAdapter;
    Timer timer = new Timer();
    boolean isTyping = false;
    private ArrayList<Exclusive> productList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSearchBinding = FragmentSearchBinding.inflate(LayoutInflater.from(context));
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchView = fragmentSearchBinding.getRoot();
        initViews();
        searchItems(searchView);
        productList.clear();
        callSearchProductApi(searchView,showCircleProgressDialog(context,""),limit,offset,fragmentSearchBinding.etSearch.getText().toString());
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
                                    productList.clear();
                                    callSearchProductApi(searchView,showCircleProgressDialog(context,""),limit,offset,fragmentSearchBinding.etSearch.getText().toString());
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
        bundle = getArguments();
        assert bundle != null;
        searchKey= bundle.getString(SEARCH_KEY);
        fragmentSearchBinding.etSearch.setText(searchKey);
    }

    //Search Product API here
    private void callSearchProductApi(View searchView, ProgressDialog progressDialog, int limit, int offset, String searchKey) {
        limit = limit + 10;
        Observer<ProductListResponse> productListResponseObserver = productListResponse -> {
            if (productListResponse != null) {
                progressDialog.dismiss();
                Log.e(TAG, "callSearchProductApi: " + productListResponse.getProductResponseDt().getProductList().size());
                switch (productListResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (productListResponse.getProductResponseDt().getProductList().size() > 0) {
                            fragmentSearchBinding.tvNoData.setVisibility(View.GONE);
                            productList = productListResponse.getProductResponseDt().getProductList();
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                            fragmentSearchBinding.recProduct.setNestedScrollingEnabled(false);
                            fragmentSearchBinding.recProduct.setHasFixedSize(true);
                            fragmentSearchBinding.recProduct.setLayoutManager(linearLayoutManager);
                            searchAdapter = new ProductListAdapter(productList, getActivity(), searchView);
                            fragmentSearchBinding.recProduct.setAdapter(searchAdapter);
                            searchAdapter.setOnProductClick(product -> {
                                toProductDetail(product, searchView);
                            });
                        }else fragmentSearchBinding.tvNoData.setVisibility(View.VISIBLE);
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
        searchViewModel.searchListResponseLiveData(progressDialog, listingParams(limit, offset, searchKey),
                SearchFragment.this)
                .observe(getViewLifecycleOwner(), productListResponseObserver);
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


    private HashMap<String, String> listingParams(int limit, int offset, String search) {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.LIMIT, String.valueOf(limit));
        map.put(AppConstants.OFFSET, String.valueOf(offset));
        map.put(AppConstants.SEARCH, search);
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
    public void onNetworkException(int from) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), SearchFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        // Call Product List API after network error
                        productList.clear();
                        callSearchProductApi(searchView, showCircleProgressDialog(context, ""), limit, offset, fragmentSearchBinding.etSearch.getText().toString());
                        break;

                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}
