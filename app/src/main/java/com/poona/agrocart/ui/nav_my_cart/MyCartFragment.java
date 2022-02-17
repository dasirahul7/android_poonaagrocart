package com.poona.agrocart.ui.nav_my_cart;

import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.responses.cartResponse.CartData;
import com.poona.agrocart.data.network.responses.cartResponse.MyCartResponse;
import com.poona.agrocart.data.network.responses.favoutiteResponse.FavouriteListResponse;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.databinding.FragmentMyCartBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.CustomDialogInterface;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.model.ProductOld;
import com.poona.agrocart.ui.nav_favourites.FavouriteItemAdapter;
import com.poona.agrocart.ui.nav_favourites.FavouriteViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCartFragment extends BaseFragment implements View.OnClickListener, CartItemsAdapter.OnClickCart {
    private FragmentMyCartBinding fragmentMyCartBinding;
    private MyCartViewModel myCartViewModel;
    private RecyclerView rvCart;
    private LinearLayoutManager linearLayoutManager;
    private CartItemsAdapter cartItemAdapter;
    private ArrayList<CartData> cartItemsList = new ArrayList<>();
    private View navHostFragment;
    private ViewGroup.MarginLayoutParams navHostMargins;
    private float scale;
    private AppSharedPreferences mSessionManager;
    private SwipeRefreshLayout rlRefreshPage;

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

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentMyCartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_cart, container, false);
        fragmentMyCartBinding.setLifecycleOwner(this);
        final View view = fragmentMyCartBinding.getRoot();
        myCartViewModel = new ViewModelProvider(this).get(MyCartViewModel.class);
        mSessionManager = new AppSharedPreferences(requireActivity());
        initTitleBar("My Basket");
        initView();
        setRvAdapter();
        return view;
    }

    private void initView() {
        rlRefreshPage = fragmentMyCartBinding.rlRefreshPage;
        fragmentMyCartBinding.btnPlaceOrder.setOnClickListener(this);
        fragmentMyCartBinding.continueBtn.setOnClickListener(this);
        initTitleBar(getString(R.string.my_cart));
        ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.VISIBLE);
        ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setOnClickListener(this::onClick);
        rvCart = fragmentMyCartBinding.rvCart;
        scale = getResources().getDisplayMetrics().density;

        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.GONE);

        navHostFragment = requireActivity().findViewById(R.id.nav_host_fragment_content_home);
        navHostMargins = (ViewGroup.MarginLayoutParams) navHostFragment.getLayoutParams();
        navHostMargins.bottomMargin = 0;
    }

    private void setBottomMarginInDps(int i) {
        int dpAsPixels = (int) (i * scale + 0.5f);
        navHostMargins.bottomMargin = dpAsPixels;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setRvAdapter() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvCart.setHasFixedSize(true);
        rvCart.setLayoutManager(linearLayoutManager);

        //Initializing our superheroes list
        cartItemsList = new ArrayList<>();

        rlRefreshPage.setRefreshing(false);
        //initializing our adapter
        cartItemAdapter = new CartItemsAdapter(cartItemsList, this);

        //Adding adapter to recyclerview
        rvCart.setAdapter(cartItemAdapter);

        //Calling method to get data to fetch data
        if (isConnectingToInternet(context)) {
            callMyCarListApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private void callMyCarListApi(ProgressDialog progressDialog) {
        @SuppressLint("NotifyDataSetChanged")
        Observer<MyCartResponse> myCartResponseObserver = myCartResponse -> {

            if (myCartResponse != null){
                Log.e("Cart List Response", new Gson().toJson(myCartResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (myCartResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        if (myCartResponse.getData() != null
                                && myCartResponse.getData().size() > 0){

                            fragmentMyCartBinding.emptyLayout.setVisibility(View.GONE);
                            fragmentMyCartBinding.itamLayout.setVisibility(View.VISIBLE);
                            cartItemsList.addAll(myCartResponse.getData());
                            cartItemAdapter.notifyDataSetChanged();
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, myCartResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        /* show empty screen message */
                        fragmentMyCartBinding.emptyLayout.setVisibility(View.VISIBLE);
                        fragmentMyCartBinding.itamLayout.setVisibility(View.GONE);
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, myCartResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, myCartResponse.getMessage());
                        break;
                }
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }
        };
        myCartViewModel.CartLisResponseLiveData(progressDialog,paramCart(),MyCartFragment.this);


    }

    private HashMap<String, String> paramCart() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.CUSTOMER_ID, preferences.getUid());
        return map;
    }

    private void checkEmptyCart() {
        if (cartItemsList.size()>0)
            return;
        else {
            fragmentMyCartBinding.emptyLayout.setVisibility(View.VISIBLE);
            fragmentMyCartBinding.continueBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_place_order:
                redirectToOrderSummary(v);
                break;
            case R.id.img_delete:
                showConfirmPopup(requireActivity(), getString(R.string.confirm_delete), new CustomDialogInterface() {
                    @Override
                    public void onYesClick() {
                        deleteAllItems();
                    }

                    @Override
                    public void onNoClick() {
                    }
                });
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

    private void deleteAllItems() {
        fragmentMyCartBinding.emptyLayout.setVisibility(View.VISIBLE);
        fragmentMyCartBinding.continueBtn.setVisibility(View.VISIBLE);
        cartItemsList.clear();
        cartItemAdapter.notifyDataSetChanged();
        setRvAdapter();
    }

    private void redirectToOrderSummary(View v) {
        if (isConnectingToInternet(context)) {
            Navigation.findNavController(v).navigate(R.id.action_nav_cart_to_nav_order_summary);
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    @Override
    public void onItemClick(CartData cartData) {

    }

    @Override
    public void onPlusCart(CartData cartData) {

    }
}