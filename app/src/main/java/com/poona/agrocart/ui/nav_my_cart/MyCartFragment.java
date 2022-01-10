package com.poona.agrocart.ui.nav_my_cart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.databinding.FragmentMyCartBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.CustomDialogInterface;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class MyCartFragment extends BaseFragment implements View.OnClickListener {
    private FragmentMyCartBinding fragmentMyCartBinding;
    private MyCartViewModel myCartViewModel;
    private RecyclerView rvCart;
    private LinearLayoutManager linearLayoutManager;
    private CartItemsAdapter cartItemsAdapter;
    private ArrayList<Product> cartItemArrayList = new ArrayList<>();
    ArrayList<Product> cartList = new ArrayList<>();
    private View navHostFragment;
    private ViewGroup.MarginLayoutParams navHostMargins;
    private float scale;
    private AppSharedPreferences mSessionManager;

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
        initView();
        setRvAdapter();
        return view;
    }

    private void initView() {
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
        cartList = mSessionManager.getSavedCartList(AppConstants.CART_LIST);
            myCartViewModel.liveProductList.setValue(cartList);
            if (cartList!=null&&cartList.size()>0) {
                myCartViewModel.getCartList().observe(getViewLifecycleOwner(), products -> {
                    cartItemArrayList = products;
                    cartItemsAdapter = new CartItemsAdapter(cartItemArrayList);
                    rvCart.setAdapter(cartItemsAdapter);
                    linearLayoutManager = new LinearLayoutManager(requireContext());
                    rvCart.setHasFixedSize(true);
                    rvCart.setLayoutManager(linearLayoutManager);
                    // adapter interface
                    cartItemsAdapter.setOnCartItemClick(position -> {
                        cartItemArrayList.remove(position);
                        cartItemsAdapter.notifyItemRemoved(position);
                        checkEmptyCart();
                    });
                });
            }else checkEmptyCart();

    }

    private void checkEmptyCart() {
        if (cartItemArrayList.size()>0)
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
        cartItemArrayList.clear();
        cartItemsAdapter.notifyDataSetChanged();
        setRvAdapter();
    }

    private void redirectToOrderSummary(View v) {
        if (isConnectingToInternet(context)) {
            Navigation.findNavController(v).navigate(R.id.action_nav_cart_to_nav_order_summary);
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

}