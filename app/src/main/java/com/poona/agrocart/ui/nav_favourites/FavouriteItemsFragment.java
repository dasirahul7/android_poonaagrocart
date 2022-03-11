package com.poona.agrocart.ui.nav_favourites;

import static com.poona.agrocart.app.AppConstants.BASKET_ID;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.ITEM_TYPE;
import static com.poona.agrocart.app.AppConstants.PRODUCT_ID;
import static com.poona.agrocart.app.AppConstants.PU_ID;
import static com.poona.agrocart.app.AppConstants.QUANTITY;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.favoutiteResponse.FavouriteListResponse;
import com.poona.agrocart.data.network.responses.favoutiteResponse.RemoveFavouriteListResponse;
import com.poona.agrocart.databinding.FragmentFavouriteItemsBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.CustomTextView;

import java.util.ArrayList;
import java.util.HashMap;


public class FavouriteItemsFragment extends BaseFragment implements FavouriteItemAdapter.OnPlusClick, FavouriteItemAdapter.OnProductClick, FavouriteItemAdapter.OnFavouriteClick {
    View view;
    private FragmentFavouriteItemsBinding fragmentFavouriteItemsBinding;
    private FavouriteViewModel favouriteViewModel;
    private RecyclerView rvFavouriteItems;
    private LinearLayoutManager linearLayoutManager;
    private FavouriteItemAdapter favouriteItemAdapter;
    private ArrayList<FavouriteListResponse.Favourite> favouriteItemsList = new ArrayList<>();
    private final String TAG = FavouriteItemsFragment.class.getSimpleName();
    private SwipeRefreshLayout rlRefreshPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentFavouriteItemsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_items, container, false);
        view = fragmentFavouriteItemsBinding.getRoot();
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
        fragmentFavouriteItemsBinding.setFaqViewModel(favouriteViewModel);
        fragmentFavouriteItemsBinding.setLifecycleOwner(this);

        initTitleBar(getString(R.string.menu_favourite));
        initView();
        setAdaptor();

        rlRefreshPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rlRefreshPage.setRefreshing(true);
                if (isConnectingToInternet(context)) {
                    setAdaptor();
                } else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //setAdaptor();
    }

    private void setAdaptor() {

        //Initializing our superheroes list
        favouriteItemsList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvFavouriteItems.setHasFixedSize(true);
        rvFavouriteItems.setLayoutManager(linearLayoutManager);


        rlRefreshPage.setRefreshing(false);
        //initializing our adapter
        favouriteItemAdapter = new FavouriteItemAdapter(context, favouriteItemsList, this);

        //Adding adapter to recyclerview
        rvFavouriteItems.setAdapter(favouriteItemAdapter);

        //Calling method to get data to fetch data
        if (isConnectingToInternet(context)) {
            callFavouriteAPi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }


    private void initView() {
        rvFavouriteItems = fragmentFavouriteItemsBinding.rvFavouriteItems;
        rlRefreshPage = fragmentFavouriteItemsBinding.rlRefreshPage;
    }

    private void callFavouriteAPi(ProgressDialog progressDialog) {
        @SuppressLint("NotifyDataSetChanged")
        Observer<FavouriteListResponse> favouriteLisResponseObserver = favouriteLisResponse -> {

            if (favouriteLisResponse != null) {
                Log.e("Favourite List Response", new Gson().toJson(favouriteLisResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (favouriteLisResponse.getStatus()) {
                    case STATUS_CODE_200://success

                        favouriteItemsList.clear();
                        if (favouriteLisResponse.getFavouriteList() != null
                                && favouriteLisResponse.getFavouriteList().size() > 0) {

                            fragmentFavouriteItemsBinding.emptyLayout.setVisibility(View.GONE);
                            fragmentFavouriteItemsBinding.rlMain.setVisibility(View.VISIBLE);

                            favouriteItemsList.addAll(favouriteLisResponse.getFavouriteList());
                            favouriteItemAdapter.notifyDataSetChanged();
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, favouriteLisResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        /* show empty screen message */
                        fragmentFavouriteItemsBinding.emptyLayout.setVisibility(View.VISIBLE);
                        fragmentFavouriteItemsBinding.rlMain.setVisibility(View.GONE);
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, favouriteLisResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, favouriteLisResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };

        favouriteViewModel.favouriteLisResponseLiveData(progressDialog, FavouriteItemsFragment.this)
                .observe(getViewLifecycleOwner(), favouriteLisResponseObserver);
    }


    @Override
    public void addToCartClickItem(int position) {
        String basketId = favouriteItemsList.get(position).getBasketId();
        String itemType = favouriteItemsList.get(position).getItemType();
        String productId = favouriteItemsList.get(position).getProductId();
        String puId = favouriteItemsList.get(position).getPuId();
        if (itemType.equalsIgnoreCase("basket")){
            callAddToCartBasketApi(showCircleProgressDialog(context, ""),basketId,position);
        }else {
            callAddToCartProductApi(showCircleProgressDialog(context, ""),productId,puId);
        }
    }

    @Override
    public void toProductDetailClickItem(int position) {
        String basketId = favouriteItemsList.get(position).getBasketId();
        String productId = favouriteItemsList.get(position).getProductId();
        String itemType = favouriteItemsList.get(position).getItemType();
        if (itemType.equalsIgnoreCase("basket")){
            Bundle bundle = new Bundle();
            bundle.putString(BASKET_ID, basketId);
            NavHostFragment.findNavController(FavouriteItemsFragment.this).
                    navigate(R.id.action_nav_favourite_to_basketDetailFragment,bundle);
        }else {
            Bundle bundle = new Bundle();
            bundle.putString(PRODUCT_ID, productId);
            NavHostFragment.findNavController(FavouriteItemsFragment.this).
                    navigate(R.id.action_nav_favourite_to_nav_product_details,bundle);
        }
    }

    @Override
    public void removeFavouriteClickItem(int position) {
        String basketId = favouriteItemsList.get(position).getBasketId();
        String itemType = favouriteItemsList.get(position).getItemType();
        String productId = favouriteItemsList.get(position).getProductId();
        dialogDeleteAddress(basketId, itemType, productId);
    }

    private void callRemoveFavouriteItemAPi(ProgressDialog progressDialog, String basketId, String itemType, String productId) {

        @SuppressLint("NotifyDataSetChanged")
        Observer<RemoveFavouriteListResponse> removeFavouriteListResponseObserver = removeFavouriteListResponse -> {

            if (removeFavouriteListResponse != null) {
                Log.e("Remove Favourite List Response", new Gson().toJson(removeFavouriteListResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (removeFavouriteListResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        successToast(context, removeFavouriteListResponse.getMessage());
                        setAdaptor();
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, removeFavouriteListResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        /* show empty screen message */
                        warningToast(context, removeFavouriteListResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, removeFavouriteListResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, removeFavouriteListResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };

        favouriteViewModel.removeFavouriteLisResponseLiveData(progressDialog, removeFavouriteInputParameter(basketId, itemType, productId), FavouriteItemsFragment.this)
                .observe(getViewLifecycleOwner(), removeFavouriteListResponseObserver);
    }

    private HashMap<String, String> removeFavouriteInputParameter(String basketId, String itemType, String productId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ITEM_TYPE, itemType);
        if (basketId == null) {
            map.put(BASKET_ID, "");
        } else {
            map.put(BASKET_ID, basketId);
        }
        if (productId == null) {
            map.put(PRODUCT_ID, "");
        } else {
            map.put(PRODUCT_ID, productId);
        }
        return map;
    }

    public void dialogDeleteAddress(String basketId, String itemType, String productId) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationUp;
        dialog.setContentView(R.layout.dialog_delete_notification);

        ImageView closeImg = dialog.findViewById(R.id.iv_cross);
        CustomTextView tvHeading = dialog.findViewById(R.id.tv_heading);
        CustomButton buttonYes = dialog.findViewById(R.id.yes_btn);
        CustomButton buttonNo = dialog.findViewById(R.id.no_btn);

        tvHeading.setText("Do you really want to delete from Favourite?");

        closeImg.setOnClickListener(view -> {
            dialog.dismiss();
        });

        buttonYes.setOnClickListener(view -> {
            dialog.dismiss();
            if (isConnectingToInternet(context)) {
                callRemoveFavouriteItemAPi(showCircleProgressDialog(context, ""), basketId, itemType, productId);
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        });

        buttonNo.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void callAddToCartBasketApi(ProgressDialog progressDialog, String basketId,int pos) {

        @SuppressLint("NotifyDataSetChanged")
        Observer<BaseResponse> baseResponseObserver = baseResponse -> {

            if (baseResponse != null) {
                Log.e("Response", new Gson().toJson(baseResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        try {
                            ((HomeActivity) context).setCountBudge(baseResponse.getCartItems());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        setAdaptor();
                        successToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        /* show empty screen message */
                        warningToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, baseResponse.getMessage());
                        goToAskSignInSignUpScreen();
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

        favouriteViewModel.callAddToCartFromBasketFavouriteList(progressDialog, AddToCartFromBasketInputParameter(basketId), FavouriteItemsFragment.this)
                .observe(getViewLifecycleOwner(), baseResponseObserver);
    }

    private HashMap<String, String> AddToCartFromBasketInputParameter(String basketId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(BASKET_ID, basketId);
        map.put(QUANTITY, "1");
        return map;
    }

    private void callAddToCartProductApi(ProgressDialog progressDialog, String productId, String puId) {

        @SuppressLint("NotifyDataSetChanged")
        Observer<BaseResponse> baseResponseObserver = baseResponse -> {

            if (baseResponse != null) {
                Log.e("Response", new Gson().toJson(baseResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        try {
                            ((HomeActivity) context).setCountBudge(baseResponse.getCartItems());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        setAdaptor();
                        successToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        /* show empty screen message */
                        warningToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, baseResponse.getMessage());
                        goToAskSignInSignUpScreen();
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

        favouriteViewModel.callAddToCartFromProductFavouriteList(progressDialog, AddToCartFromProductInputParameter(productId,puId), FavouriteItemsFragment.this)
                .observe(getViewLifecycleOwner(), baseResponseObserver);
    }

    private HashMap<String, String> AddToCartFromProductInputParameter(String productId, String puId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(PRODUCT_ID, productId.toString());
        map.put(PU_ID,puId.toString());
        map.put(QUANTITY, "1");
        return map;
    }

}