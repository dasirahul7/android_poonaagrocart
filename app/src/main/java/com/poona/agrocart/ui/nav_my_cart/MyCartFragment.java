package com.poona.agrocart.ui.nav_my_cart;

import static com.poona.agrocart.app.AppConstants.BASKET_ID;
import static com.poona.agrocart.app.AppConstants.CART_ID;
import static com.poona.agrocart.app.AppConstants.CUSTOMER_ID;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.PRODUCT_ID;
import static com.poona.agrocart.app.AppConstants.PU_ID;
import static com.poona.agrocart.app.AppConstants.QUANTITY;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_402;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.SUBSCRIPTION;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.cartResponse.CartData;
import com.poona.agrocart.data.network.responses.cartResponse.MyCartResponse;
import com.poona.agrocart.databinding.FragmentMyCartBinding;
import com.poona.agrocart.databinding.RowProductItemBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.CustomDialogInterface;
import com.poona.agrocart.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyCartFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MyCartFragment.class.getSimpleName();
    private FragmentMyCartBinding fragmentMyCartBinding;
    private MyCartViewModel myCartViewModel;

    private View view;

    private RecyclerView rvMyCart;
    private LinearLayoutManager linearLayoutManager;
    private CartItemsAdapter cartItemAdapter;
    private ArrayList<CartData> cartItemsList = new ArrayList<>();
    private View navHostFragment;
    private ViewGroup.MarginLayoutParams navHostMargins;
    private float scale;

    private int deleteItemPosition = 0;

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentMyCartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_cart, container, false);

        myCartViewModel = new ViewModelProvider(this).get(MyCartViewModel.class);
        fragmentMyCartBinding.setMyCartViewModel(myCartViewModel);
        fragmentMyCartBinding.setLifecycleOwner(this);

        view = fragmentMyCartBinding.getRoot();

        initView();
        setRvAdapter();

        return view;
    }

    private void initView() {
        initTitleBar(getString(R.string.my_cart));

        rvMyCart = fragmentMyCartBinding.rvMyCart;
        scale = getResources().getDisplayMetrics().density;

        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.GONE);

        fragmentMyCartBinding.btnPlaceOrder.setOnClickListener(this);
        fragmentMyCartBinding.continueBtn.setOnClickListener(this);

        navHostFragment = requireActivity().findViewById(R.id.nav_host_fragment_content_home);
        navHostMargins = (ViewGroup.MarginLayoutParams) navHostFragment.getLayoutParams();
        navHostMargins.bottomMargin = 0;
    }

    private void setBottomMarginInDps(int i) {
        int dpAsPixels = (int) (i * scale + 0.5f);
        navHostMargins.bottomMargin = dpAsPixels;
    }

    private View cartItemView;
    @SuppressLint("NotifyDataSetChanged")
    private void setRvAdapter() {
        cartItemsList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvMyCart.setHasFixedSize(true);
        rvMyCart.setLayoutManager(linearLayoutManager);

        cartItemAdapter = new CartItemsAdapter(cartItemsList);
        rvMyCart.setAdapter(cartItemAdapter);

        cartItemAdapter.setOnCartItemClick(cartData -> {
            if (cartData.getItemType().equalsIgnoreCase("product")){
                toProductDetail(cartData.getProductId());
            }else {
                toBasketDetail(cartData.getBasketIdFk());
            }
        });

        cartItemAdapter.setOnCartAddCountClick((position, binding) -> {

            String quantity = binding.etQuantity.getText().toString();

            String basketId = cartItemsList.get(position).getBasketIdFk();
            String itemType = cartItemsList.get(position).getItemType();
            String productId = cartItemsList.get(position).getProductId();
            String puId = cartItemsList.get(position).getPuId();
            if (itemType.equalsIgnoreCase("basket")){
                callAddToCartBasketApi(showCircleProgressDialog(context, ""),basketId ,quantity);
            }else {
                callAddToCartProductApi(showCircleProgressDialog(context, ""),productId,puId,quantity);
            }

        });

        cartItemAdapter.setOnCartMinusCountClick((position, binding) -> {

            String quantity = binding.etQuantity.getText().toString();
            String basketId = cartItemsList.get(position).getBasketIdFk();
            String itemType = cartItemsList.get(position).getItemType();
            String productId = cartItemsList.get(position).getProductId();
            String puId = cartItemsList.get(position).getPuId();
            if (itemType.equalsIgnoreCase("basket")){
                callAddToCartBasketApi(showCircleProgressDialog(context, ""),basketId ,quantity);
            }else {
                callAddToCartProductApi(showCircleProgressDialog(context, ""),productId,puId,quantity);
            }

        });

        cartItemAdapter.setOnDeleteCartItemClick((position, binding) -> {
            this.deleteItemPosition = position;
            this.cartItemView = binding.getRoot();
            if (isConnectingToInternet(context)) {
                removeCartItem(binding);
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        });

        ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setOnClickListener(view -> {
            if (isConnectingToInternet(context)) {
                showConfirmPopup(requireActivity(), getString(R.string.confirm_delete), new CustomDialogInterface() {
                    @Override
                    public void onYesClick() {
                        removeAllCartItems();
                    }

                    @Override
                    public void onNoClick() {
                    }
                });
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        });

        fragmentMyCartBinding.llMain.setVisibility(View.GONE);
        if (isConnectingToInternet(context)) {
            callMyCarListApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private void callMyCarListApi(ProgressDialog progressDialog) {
        /*print user input parameters*/
        for (Map.Entry<String, String> entry : myCartParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        @SuppressLint("NotifyDataSetChanged")
        Observer<MyCartResponse> myCartResponseObserver = myCartResponse -> {
            if (myCartResponse != null) {
                Log.e("My Cart List Response", new Gson().toJson(myCartResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (myCartResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        cartItemsList.clear();
                        if (myCartResponse.getData() != null
                                && myCartResponse.getData().size() > 0) {
                            fragmentMyCartBinding.emptyLayout.setVisibility(View.GONE);
                            fragmentMyCartBinding.llMain.setVisibility(View.VISIBLE);
                            ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.VISIBLE);
                            cartItemsList.addAll(myCartResponse.getData());
                            cartItemAdapter.notifyDataSetChanged();

                            myCartViewModel.totalTotal.setValue(String.valueOf(myCartResponse.getTotalAmount()));
                            myCartViewModel.totalItems.setValue(String.valueOf(myCartResponse.getCartItems()));
                        } else {
                            requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.VISIBLE);
                            setBottomMarginInDps(50);
                            fragmentMyCartBinding.emptyLayout.setVisibility(View.VISIBLE);
                            fragmentMyCartBinding.llMain.setVisibility(View.GONE);
                            ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.GONE);
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, myCartResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        /* show empty screen message */
                        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.VISIBLE);
                        setBottomMarginInDps(50);
                        fragmentMyCartBinding.emptyLayout.setVisibility(View.VISIBLE);
                        fragmentMyCartBinding.llMain.setVisibility(View.GONE);
                        ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.GONE);
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, myCartResponse.getMessage());
                        goToAskSignInSignUpScreen(myCartResponse.getMessage(),context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, myCartResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };
        myCartViewModel
                .getMyCartListingResponse(progressDialog, myCartParameters(), MyCartFragment.this)
                .observe(getViewLifecycleOwner(), myCartResponseObserver);
    }

    private HashMap<String, String> myCartParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.CUSTOMER_ID, preferences.getUid());
        return map;
    }

    private void deleteCartItemApi(RowProductItemBinding binding) {
        //binding.rlMain.setVisibility(View.GONE);
        //binding.rlLoader.setVisibility(View.VISIBLE);
        /*print user input parameters*/
        for (Map.Entry<String, String> entry : getCartItemIdParameter().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        androidx.lifecycle.Observer<BaseResponse> responseObserver = baseResponse -> {
            if (baseResponse != null) {
                //binding.rlMain.setVisibility(View.VISIBLE);
                //binding.rlLoader.setVisibility(View.GONE);
                Log.e("Delete Address Api Response", new Gson().toJson(baseResponse));
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        //removeCartItem();
                        //successToast(context, baseResponse.getMessage());
                        callMyCarListApi(showCircleProgressDialog(context, ""));
                        break;
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_402://Validation Errors
                        goToAskAndDismiss(baseResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_404://no records found
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(baseResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, baseResponse.getMessage());
                        break;
                }
            } else {
                //binding.rlMain.setVisibility(View.VISIBLE);
                //binding.rlLoader.setVisibility(View.GONE);
            }
        };

        myCartViewModel
                .deleteCartItemResponse(getCartItemIdParameter(), MyCartFragment.this)
                .observe(getViewLifecycleOwner(), responseObserver);
    }

    private HashMap<String, String> getCartItemIdParameter() {
        HashMap<String, String> map = new HashMap<>();
        map.put(CART_ID, cartItemsList.get(deleteItemPosition).getCartId());
        return map;
    }

    private void removeCartItem(RowProductItemBinding binding) {
        Animation anim = AnimationUtils.loadAnimation(requireContext(),
                android.R.anim.slide_out_right);
        anim.setDuration(300);
        cartItemView.startAnimation(anim);

        new Handler().postDelayed(() -> {
            deleteCartItemApi(binding);
            cartItemsList.remove(deleteItemPosition);
            cartItemAdapter.notifyDataSetChanged();
            if (cartItemsList != null && cartItemsList.size() > 0) {
                fragmentMyCartBinding.emptyLayout.setVisibility(View.GONE);
                fragmentMyCartBinding.llMain.setVisibility(View.VISIBLE);
                ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.VISIBLE);
            } else {
                requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.VISIBLE);
                setBottomMarginInDps(50);
                fragmentMyCartBinding.emptyLayout.setVisibility(View.VISIBLE);
                fragmentMyCartBinding.llMain.setVisibility(View.GONE);
                ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.GONE);
            }
        }, anim.getDuration());
    }

    private void deleteCartListApi() {
        /*print user input parameters*/
        for (Map.Entry<String, String> entry : getCustomerIdParameter().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        androidx.lifecycle.Observer<BaseResponse> responseObserver = baseResponse -> {
            if (baseResponse != null) {
                Log.e("Delete List Api ResponseData", new Gson().toJson(baseResponse));
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        try {
                            ((HomeActivity)context).setCountBudge(baseResponse.getCartItems());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_402://Validation Errors
                        goToAskAndDismiss(baseResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_404://no records found
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(baseResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, baseResponse.getMessage());
                        break;
                }
            } else {

            }
        };

        myCartViewModel
                .deleteCartListResponse(getCustomerIdParameter(), MyCartFragment.this)
                .observe(getViewLifecycleOwner(), responseObserver);
    }

    private HashMap<String, String> getCustomerIdParameter() {
        HashMap<String, String> map = new HashMap<>();
        map.put(CUSTOMER_ID, preferences.getUid());
        return map;
    }

    private void removeAllCartItems() {
        deleteCartListApi();
        cartItemsList.clear();
        cartItemAdapter.notifyDataSetChanged();
        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.VISIBLE);
        setBottomMarginInDps(50);
        fragmentMyCartBinding.emptyLayout.setVisibility(View.VISIBLE);
        fragmentMyCartBinding.llMain.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.GONE);
    }



    private void callAddToCartBasketApi(ProgressDialog progressDialog, String basketId, String quantity) {

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
                            ((HomeActivity)context).setCountBudge(baseResponse.getCartItems());
                            myCartViewModel.totalTotal.setValue(String.valueOf(baseResponse.getTotalAmount()));
                            myCartViewModel.totalItems.setValue(String.valueOf(baseResponse.getCartItems()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Record not Found
                        /* show empty screen message */
                        warningToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, baseResponse.getMessage());
                        goToAskSignInSignUpScreen(baseResponse.getMessage(),context);
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

        myCartViewModel.callUpdateToCartFromBasketFavouriteList(progressDialog, AddToCartFromBasketInputParameter(basketId,quantity), MyCartFragment.this)
                .observe(getViewLifecycleOwner(), baseResponseObserver);
    }

    private HashMap<String, String> AddToCartFromBasketInputParameter(String basketId,String quantity) {
        HashMap<String, String> map = new HashMap<>();
        map.put(BASKET_ID, basketId);
        map.put(QUANTITY, quantity);
        return map;
    }

    private void callAddToCartProductApi(ProgressDialog progressDialog, String productId, String puId, String quantity) {

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
                            ((HomeActivity)context).setCountBudge(baseResponse.getCartItems());
                            myCartViewModel.totalTotal.setValue(String.valueOf(baseResponse.getTotalAmount()));
                            myCartViewModel.totalItems.setValue(String.valueOf(baseResponse.getCartItems()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Record not Found
                        /* show empty screen message */
                        warningToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
//                        warningToast(context, baseResponse.getMessage());
                        goToAskSignInSignUpScreen(baseResponse.getMessage(),context);
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

        myCartViewModel.callUpdateToCartFromProductFavouriteList(progressDialog, AddToCartFromProductInputParameter(productId,puId,quantity), MyCartFragment.this)
                .observe(getViewLifecycleOwner(), baseResponseObserver);
    }

    private HashMap<String, String> AddToCartFromProductInputParameter(String productId, String puId, String quantity) {
        HashMap<String, String> map = new HashMap<>();
        map.put(PRODUCT_ID, productId.toString());
        map.put(PU_ID,puId.toString());
        map.put(QUANTITY, quantity);
        return map;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_place_order:
                redirectToOrderSummary(v);
                break;
            case R.id.continue_btn:
                redirectToExplore(v);
                break;

        }
    }

    private void redirectToExplore(View v) {
        if (isConnectingToInternet(context)) {
            try {
                Navigation.findNavController(v).navigate(R.id.action_nav_cart_to_nav_explore);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.retry), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private void redirectToOrderSummary(View v) {
        if (isConnectingToInternet(context)) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(SUBSCRIPTION, false);
            Navigation.findNavController(v).navigate(R.id.action_nav_cart_to_nav_order_summary, bundle);
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.VISIBLE);
        setBottomMarginInDps(50);
    }

    @Override
    public void onStop() {
        super.onStop();
        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.VISIBLE);
        setBottomMarginInDps(50);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

    private void toProductDetail(String productId) {
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, productId);
        Navigation.findNavController(view).navigate(R.id.action_nav_cart_to_productDetails, bundle);
    }
    private void toBasketDetail(String basketId) {
        Bundle bundle = new Bundle();
        bundle.putString(BASKET_ID, basketId);
        Navigation.findNavController(view).navigate(R.id.action_nav_cart_to_basketDetails, bundle);
    }
}