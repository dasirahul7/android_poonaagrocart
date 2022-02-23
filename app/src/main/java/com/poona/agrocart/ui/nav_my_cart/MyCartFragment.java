package com.poona.agrocart.ui.nav_my_cart;

import static com.poona.agrocart.app.AppConstants.ADDRESS_ID;
import static com.poona.agrocart.app.AppConstants.BASKET_ID;
import static com.poona.agrocart.app.AppConstants.CART_ID;
import static com.poona.agrocart.app.AppConstants.CUSTOMER_ID;
import static com.poona.agrocart.app.AppConstants.PRODUCT_ID;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_402;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

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
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.data.network.responses.cartResponse.CartData;
import com.poona.agrocart.data.network.responses.cartResponse.MyCartResponse;
import com.poona.agrocart.databinding.FragmentMyCartBinding;
import com.poona.agrocart.databinding.RowProductItemBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.CustomDialogInterface;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.HomeFragment;
import com.poona.agrocart.ui.nav_addresses.AddressesAdapter;
import com.poona.agrocart.ui.nav_addresses.AddressesFragment;
import com.poona.agrocart.ui.nav_addresses.AddressesViewModel;
import com.poona.agrocart.widgets.custom_otp_edit_text.CustomOtpEditText;

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

        cartItemAdapter.setOnCartAddMinusCountClick((position, binding) -> {

        });

        cartItemAdapter.setOnCartMinusCountClick((position, binding) -> {

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
                        if (myCartResponse.getData() != null
                                && myCartResponse.getData().size() > 0) {
                            fragmentMyCartBinding.emptyLayout.setVisibility(View.GONE);
                            fragmentMyCartBinding.llMain.setVisibility(View.VISIBLE);
                            ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.VISIBLE);
                            cartItemsList.addAll(myCartResponse.getData());
                            cartItemAdapter.notifyDataSetChanged();
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
                        goToAskSignInSignUpScreen();
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
                        //successToast(context, baseResponse.getMessage());
                        //deleteItem();
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
            Navigation.findNavController(v).navigate(R.id.action_nav_cart_to_nav_order_summary);
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