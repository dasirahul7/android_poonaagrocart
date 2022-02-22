package com.poona.agrocart.ui.nav_orders.order_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentOrderDetailsBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_orders.model.OrderDetails;

public class OrderDetailsFragment extends BaseFragment implements View.OnClickListener {

    private OrderDetailsViewModel orderDetailsViewModel;
    private FragmentOrderDetailsBinding orderDetailsBinding;
    private View orderDetailsRoot;
    private View navHostFragment;
    private ViewGroup.MarginLayoutParams navHostMargins;
    private float scale;

    public static OrderDetailsFragment newInstance() {
        return new OrderDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        orderDetailsBinding = FragmentOrderDetailsBinding.inflate(LayoutInflater.from(getContext()));
        orderDetailsViewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);
        orderDetailsRoot = orderDetailsBinding.getRoot();
        initTitleWithBackBtn(getString(R.string.order_details));
        initView();
        setContent();
        return orderDetailsRoot;
    }

    private void initView() {
        scale = getResources().getDisplayMetrics().density;

        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);

        navHostFragment = requireActivity().findViewById(R.id.nav_host_fragment_content_home);
        navHostMargins = (ViewGroup.MarginLayoutParams) navHostFragment.getLayoutParams();
        navHostMargins.bottomMargin = 0;
    }

    private void setContent() {
        OrderDetails orderDetails = new OrderDetails(getString(R.string.order_success_msg), getString(R.string.order_placed_success));
        orderDetailsViewModel.orderDetailsLiveData.setValue(orderDetails);
        orderDetailsBinding.setOrderDetailsViewModel(orderDetailsViewModel);
        orderDetailsBinding.btnTrackOrder.setOnClickListener(this::onClick);
        orderDetailsBinding.tvBackToHome.setOnClickListener(this::onClick);
    }

    private void redirectToOrderTrack(View v) {
        if (isConnectingToInternet(context)) {
            Navigation.findNavController(v).navigate(R.id.action_orderDetailsFragment_to_nav_order_track);
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private void redirectToHome(View v) {
        if (isConnectingToInternet(context)) {
//            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_home)
//                    .navigateUp();
            Navigation.findNavController(v).navigate(R.id.action_orderDetailsFragment_to_nav_home);
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.retry), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_track_order:
                redirectToOrderTrack(v);
                break;
            case R.id.tv_back_to_home:
                redirectToHome(v);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        setBottomMarginInDps(50);
    }

    @Override
    public void onStop() {
        super.onStop();
        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.VISIBLE);
        setBottomMarginInDps(50);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

    private void setBottomMarginInDps(int i) {
        int dpAsPixels = (int) (i * scale + 0.5f);
        navHostMargins.bottomMargin = dpAsPixels;
    }
}