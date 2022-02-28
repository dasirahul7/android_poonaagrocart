package com.poona.agrocart.ui.nav_orders.order_view;

import static com.poona.agrocart.app.AppConstants.IMAGE_DOC_BASE_URL;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentOrderViewBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_orders.model.CancelOrderCategoryList;
import com.poona.agrocart.ui.nav_orders.model.CancelOrderReasonList;
import com.poona.agrocart.ui.nav_orders.order_view.adaptor.OrderCancelCategoryAdaptor;
import com.poona.agrocart.ui.nav_orders.order_view.adaptor.OrderCancelReasonAdaptor;

import java.util.ArrayList;
import java.util.List;

public class OrderViewFragment extends BaseFragment implements View.OnClickListener {

    private FragmentOrderViewBinding fragmentOrderViewBinding;
    private RecyclerView rvBasketListItems;
    private LinearLayoutManager linearLayoutManager;
    private BasketItemsAdapter basketItemsAdapter;
    private ArrayList<BasketItem> basketItemList;
    private boolean isBasketVisible = true;

    /*Cancel Order dialog */
    private RecyclerView orderCancelCategory, orderCancelReason;
    private List<CancelOrderCategoryList> cancelOrderCategoryList;
    private OrderCancelCategoryAdaptor orderCancelCategoryAdaptor;
    private List<CancelOrderReasonList> cancelOrderReasonList;
    private OrderCancelReasonAdaptor orderCancelReasonAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentOrderViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_view, container, false);
        fragmentOrderViewBinding.setLifecycleOwner(this);
        final View view = fragmentOrderViewBinding.getRoot();

        initView();
        setRVAdapter();

        initTitleWithBackBtn(getString(R.string.order_view));

        return view;
    }

    private void initView() {
        fragmentOrderViewBinding.btnTrackOrder.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        isBasketVisible = bundle.getBoolean("isBasketVisible");
        rvBasketListItems = fragmentOrderViewBinding.rvBasketItems;
        if (isBasketVisible) {
            setBasketContentsVisible();
        } else {
            showProductDetails();
        }
    }

    private void showProductDetails() {
        fragmentOrderViewBinding.tvBasketDetails.setVisibility(View.GONE);
        fragmentOrderViewBinding.tvProductDetails.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.btnTrackOrder.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.llSubTotal.setVisibility(View.GONE);
        fragmentOrderViewBinding.line2.setVisibility(View.GONE);
    }

    private void setBasketContentsVisible() {
        fragmentOrderViewBinding.tvBasketDetails.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.tvProductDetails.setVisibility(View.GONE);
        fragmentOrderViewBinding.btnTrackOrder.setVisibility(View.GONE);
        fragmentOrderViewBinding.llSubTotal.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.line2.setVisibility(View.VISIBLE);
    }

    private void setRVAdapter() {
        basketItemList = new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvBasketListItems.setHasFixedSize(true);
        rvBasketListItems.setLayoutManager(linearLayoutManager);

        basketItemsAdapter = new BasketItemsAdapter(basketItemList, isBasketVisible, getContext());
        rvBasketListItems.setAdapter(basketItemsAdapter);
    }

    private void prepareListingData() {
        for (int i = 0; i < 4; i++) {
            BasketItem basketItem = new BasketItem();
            basketItem.setNameOfProduct("ABC");
            basketItem.setWeight("250gms");
            basketItem.setQuantity(getString(R.string.sample_unit));
            basketItem.setDate("22nd Sept 2021");
            basketItem.setTime("9.00 am to 9.00 pm");
            if (i == 0)
                basketItem.setDeliveryStatus("Delivered");
            else if (i == 2 || i == 3)
                basketItem.setDeliveryStatus("Confirmed");
            else
                basketItem.setDeliveryStatus("In transist");
            basketItem.setPrice("Rs.200");
            basketItemList.add(basketItem);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_track_order:
                redirectToTrackOrderFragment(v);
                break;
        }
    }

    private void redirectToTrackOrderFragment(View view) {
        if (isConnectingToInternet(context))
            Navigation.findNavController(view).navigate(R.id.action_orderViewFragment_to_nav_order_track);
        else
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
    }

    private void CancelOrderDialogBox(){
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;
        dialog.setContentView(R.layout.order_cancel_dialog_box);
        ImageView crossImage = dialog.findViewById(R.id.close_btn);
        orderCancelCategory = dialog.findViewById(R.id.rv_cancel_category);
        orderCancelReason = dialog.findViewById(R.id.rv_cancel_reason);

        setCancelCategoryAdapter();
        setCancelReasonAdapter();

        crossImage.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();

        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        //int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        //int displayHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        // Set alert dialog width equal to screen width 100%
        int dialogWindowWidth = (int) (displayWidth * 1.0f);
        // Set alert dialog height equal to screen height 100%
        int dialogWindowHeight = (int) (displayHeight * 1.0f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void setCancelCategoryAdapter() {
        cancelOrderCategoryList = new ArrayList<>();
        prepareListCancelCategory();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        orderCancelCategory.setHasFixedSize(true);
        orderCancelCategory.setLayoutManager(linearLayoutManager);

        orderCancelCategoryAdaptor = new OrderCancelCategoryAdaptor(context, cancelOrderCategoryList);
        orderCancelCategory.setAdapter(orderCancelCategoryAdaptor);
    }

    private void prepareListCancelCategory() {
        for (int i = 0; i < 4; i++) {
            CancelOrderCategoryList orderCancelCategoryList = new CancelOrderCategoryList();
            orderCancelCategoryList.setCategoryName("ABC");
            orderCancelCategoryList.setCancelDate("22nd Sept 2021");

            cancelOrderCategoryList.add(orderCancelCategoryList);
        }
    }

    private void setCancelReasonAdapter() {
        cancelOrderReasonList = new ArrayList<>();
        prepareListCancelReason();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        orderCancelReason.setHasFixedSize(true);
        orderCancelReason.setLayoutManager(linearLayoutManager);

        orderCancelReasonAdaptor = new OrderCancelReasonAdaptor(context, cancelOrderReasonList);
        orderCancelReason.setAdapter(orderCancelReasonAdaptor);
    }

    private void prepareListCancelReason() {
        for (int i = 0; i < 4; i++) {
            CancelOrderReasonList orderCancelReasonList = new CancelOrderReasonList();
            orderCancelReasonList.setCancelReason("how are you ?");

            cancelOrderReasonList.add(orderCancelReasonList);
        }
    }
}