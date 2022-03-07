package com.poona.agrocart.ui.nav_orders.order_view;

import static com.poona.agrocart.app.AppConstants.CANCEL_ID;
import static com.poona.agrocart.app.AppConstants.ORDER_ID;
import static com.poona.agrocart.app.AppConstants.RATING;
import static com.poona.agrocart.app.AppConstants.REVIEW;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.myOrderResponse.OrderCancelReasonResponse;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.ItemsDetail;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.MyOrderDetailsResponse;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.MyOrderDetial;
import com.poona.agrocart.databinding.FragmentOrderViewBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_orders.model.CancelOrderCategoryList;
import com.poona.agrocart.ui.nav_orders.order_view.adaptor.OrderCancelCategoryAdaptor;
import com.poona.agrocart.ui.nav_orders.order_view.adaptor.OrderCancelReasonAdaptor;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.CustomEditText;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class OrderViewFragment extends BaseFragment implements View.OnClickListener, OrderCancelReasonAdaptor.OnTypeClickListener {

    private FragmentOrderViewBinding fragmentOrderViewBinding;
    private OrderViewDetailsViewModel orderViewDetailsViewModel;
    private String order_id = "";
    private RecyclerView rvBasketListItems;
    private LinearLayoutManager linearLayoutManager;
    private BasketItemsAdapter basketItemsAdapter;
    private ArrayList<ItemsDetail> basketItemList;
    private boolean isBasketVisible = true;
    private RatingBar ratingBar;
    private CustomEditText feedbackComment;
    private CustomButton btnSubmitFeedback;

    private List<MyOrderDetial> orderDetials = new ArrayList<>();

    /*Cancel Order dialog */
    private RecyclerView orderCancelCategory, orderCancelReason;
    private List<CancelOrderCategoryList> cancelOrderCategoryList;
    private OrderCancelCategoryAdaptor orderCancelCategoryAdaptor;

    private List<OrderCancelReasonResponse.OrderCancelReason> cancelOrderReasonList;
    private OrderCancelReasonAdaptor orderCancelReasonAdaptor;
    private View view;
    private String strReasonType = "", strCancelId = "" ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order_id = getArguments().getString(ORDER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentOrderViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_view, container, false);
        fragmentOrderViewBinding.setLifecycleOwner(this);
        orderViewDetailsViewModel = new ViewModelProvider(this).get(OrderViewDetailsViewModel.class);
        fragmentOrderViewBinding.setOrderViewDetailsViewModel(orderViewDetailsViewModel);
        view = fragmentOrderViewBinding.getRoot();

        initView();
        setRVAdapter();


        initTitleWithBackBtn(getString(R.string.order_view));


        return view;
    }

    private void initView() {
        ratingBar = fragmentOrderViewBinding.ratingBarInput;
        feedbackComment = fragmentOrderViewBinding.etFeedback;
        btnSubmitFeedback = fragmentOrderViewBinding.btnSubmitFeedback;

        fragmentOrderViewBinding.btnTrackOrder.setOnClickListener(this);
        Bundle bundle = this.getArguments();
        isBasketVisible = bundle.getBoolean("isBasketVisible");
        rvBasketListItems = fragmentOrderViewBinding.rvBasketItems;
        if (isBasketVisible) {
            setBasketContentsVisible();
        } else {

            showProductDetails();
        }

        btnSubmitFeedback.setOnClickListener(view1 -> {

            if (isConnectingToInternet(context)) {
                if (!Objects.requireNonNull(feedbackComment.getText()).toString().isEmpty() && !(ratingBar.getRating() == 0.0)) {
                    callRatingAndFeedBackApi(showCircleProgressDialog(context, ""));
                } else {
                    errorToast(context, "Please fill the field");
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }

        });


        fragmentOrderViewBinding.cvCancel.setOnClickListener(view1 -> {

            CancelOrderDialogBox();

        });

    }

    private void showProductDetails() {
        fragmentOrderViewBinding.tvBasketDetails.setVisibility(View.GONE);
        fragmentOrderViewBinding.tvProductDetails.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.btnTrackOrder.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.llSubTotal.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.viewline1.setVisibility(View.VISIBLE);

        callOrderDetailsApi(showCircleProgressDialog(context, ""));
    }

    private void setBasketContentsVisible() {
        fragmentOrderViewBinding.tvBasketDetails.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.tvProductDetails.setVisibility(View.GONE);
        fragmentOrderViewBinding.btnTrackOrder.setVisibility(View.GONE);
        fragmentOrderViewBinding.llSubTotal.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.viewline1.setVisibility(View.VISIBLE);

    }

    private void setRVAdapter() {
        basketItemList = new ArrayList<>();

        callOrderDetailsApi(showCircleProgressDialog(context, ""));

        linearLayoutManager = new LinearLayoutManager(requireContext());

        rvBasketListItems.setHasFixedSize(true);
        rvBasketListItems.setLayoutManager(linearLayoutManager);

        basketItemsAdapter = new BasketItemsAdapter(basketItemList, isBasketVisible, getContext());
        rvBasketListItems.setAdapter(basketItemsAdapter);
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

    /*Order Details Api and managements */

    private void callOrderDetailsApi(ProgressDialog progressDialog) {
        @SuppressLint("NotifyDataSetChanged") Observer<MyOrderDetailsResponse> myOrderDetailsResponseObserver = myOrderDetailsResponse -> {
            if (myOrderDetailsResponse != null) {
                Log.e(" My Order Details Api ResponseData", new Gson().toJson(myOrderDetailsResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (myOrderDetailsResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully

                        if (myOrderDetailsResponse.getOrderDetials()!= null &&
                                myOrderDetailsResponse.getOrderDetials().size() > 0) {

                            orderDetials.addAll(myOrderDetailsResponse.getOrderDetials());
                            setValue(orderDetials);

                            basketItemList.clear();
                            if(myOrderDetailsResponse.getOrderDetials().get(0).getItemsDetails() != null &&
                            myOrderDetailsResponse.getOrderDetials().get(0).getItemsDetails().size() > 0){
                                basketItemList.addAll(myOrderDetailsResponse.getOrderDetials().get(0).getItemsDetails());
                                basketItemsAdapter.notifyDataSetChanged();
                            }
                        }
                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, myOrderDetailsResponse.getMessage());

                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(myOrderDetailsResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, myOrderDetailsResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }


        };
        orderViewDetailsViewModel.getMyOrderDetails(progressDialog, context, MyOrderDetailsInputParameter(), OrderViewFragment.this)
                .observe(getViewLifecycleOwner(), myOrderDetailsResponseObserver);
    }

    private HashMap<String, String> MyOrderDetailsInputParameter(){
        HashMap<String, String> map = new HashMap<>();

        map.put(ORDER_ID, order_id);

        return map;
    }

    @SuppressLint("ResourceType")
    private void setValue(List<MyOrderDetial> orderDetials){
        orderViewDetailsViewModel.orderId.setValue(orderDetials.get(0).getOrderCode());
        orderViewDetailsViewModel.orderDate.setValue(orderDetials.get(0).getPendingDate()); //set date format
        orderViewDetailsViewModel.orderStatus.setValue(orderDetials.get(0).getOrderStatus()); //changes in status format
        orderViewDetailsViewModel.deliveryCode.setValue(orderDetials.get(0).getDeliveryCode());
        orderViewDetailsViewModel.customerName.setValue(orderDetials.get(0).getName());
        orderViewDetailsViewModel.customerNumber.setValue(orderDetials.get(0).getMobile());
        orderViewDetailsViewModel.customerAddress.setValue(orderDetials.get(0).getOrderAddressText());
        orderViewDetailsViewModel.customerArea.setValue(orderDetials.get(0).getOrderAreaName());
        orderViewDetailsViewModel.customerCity.setValue(orderDetials.get(0).getOrderCityName());
        orderViewDetailsViewModel.paymentType.setValue(orderDetials.get(0).getPaymentType()); //changes in status format
        orderViewDetailsViewModel.transactionId.setValue(orderDetials.get(0).getTransactionId());
        orderViewDetailsViewModel.discountAmount.setValue(orderDetials.get(0).getDiscount());
        orderViewDetailsViewModel.deliveryCharges.setValue(orderDetials.get(0).getDeliveryCharges());
        orderViewDetailsViewModel.totalAmount.setValue(orderDetials.get(0).getPaidAmount());
        orderViewDetailsViewModel.subTotalAmount.setValue(orderDetials.get(0).getProductAmount());
        orderViewDetailsViewModel.savedAmount.setValue(getString(R.string.rs_20)); //Get for backend


        switch (orderDetials.get(0).getOrderStatus()) {
            case "3":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.in_process));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_in_process)));
                break;
            case "4":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.delivered));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_delivered)));
                break;
            case "2":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.confirmed));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_confirmed)));
                break;
            case "5":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.cancelled));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_cancelled)));
                break;
            default:
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.pending));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color4)));
                break;
        }

        switch (orderDetials.get(0).getPaymentType()){
            case "1":
                fragmentOrderViewBinding.tvPaymentType.setText(context.getString(R.string.payment_type_1));
                break;
            case "2":
                fragmentOrderViewBinding.tvPaymentType.setText(context.getString(R.string.payment_type_2));
                break;
            case "3":
                fragmentOrderViewBinding.tvPaymentType.setText(context.getString(R.string.payment_type_3));
                break;
            case "4":
                fragmentOrderViewBinding.tvPaymentType.setText(context.getString(R.string.payment_type_4));
                break;
        }

        String selectedDate = orderDetials.get(0).getPendingDate();

        String txtDisplayDate = "";
        try {
            txtDisplayDate = formatDate(selectedDate, "yyyy-mm-dd hh:mm:ss", "MMM dd, yyyy hh:mm aa");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fragmentOrderViewBinding.tvOrderDate.setText(txtDisplayDate);

        String deliveryDate = orderDetials.get(0).getShouldDeliverOnDate();

        String txDeliveryDate = "";
        try {
            txDeliveryDate = formatDate(deliveryDate, "dd/mm/yyyy", "MMM dd, yyyy ");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fragmentOrderViewBinding.tvDeliveryDateAndTime.setText(txDeliveryDate + " "+orderDetials.get(0).getDelierySlotStartAndEndTime());




    }

    /* Cancel Order Manager */

    private void CancelOrderDialogBox(){
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;
        dialog.setContentView(R.layout.order_cancel_dialog_box);
        ImageView crossImage = dialog.findViewById(R.id.close_btn);
        orderCancelCategory = dialog.findViewById(R.id.rv_cancel_category);
        orderCancelReason = dialog.findViewById(R.id.rv_cancel_reason);
        CustomButton btnSubmit = dialog.findViewById(R.id.btn_submit);

        setCancelCategoryAdapter();
        setCancelReasonAdapter();

        if(isBasketVisible){
            orderCancelCategory.setVisibility(View.VISIBLE);
        }else {

            orderCancelCategory.setVisibility(View.GONE);
        }

        btnSubmit.setOnClickListener(view1 -> {
            if(isBasketVisible){

                infoToast(context, "Coming Soon.....");
                dialog.dismiss();
            }else {

                if(!strReasonType.equalsIgnoreCase("")) {
                    callOrderCancelSuccessFullyApi(showCircleProgressDialog(context,""));
                    dialog.dismiss();
                }else {
                    warningToast(context, "Please select Reason");
                }
            }


        });

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

    private void setCancelReasonAdapter() {
        cancelOrderReasonList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        callOrderCancelReasonApi(showCircleProgressDialog(context, ""));
        orderCancelReason.setHasFixedSize(true);
        orderCancelReason.setLayoutManager(linearLayoutManager);

        if(isConnectingToInternet(context)) {
            orderCancelReasonAdaptor = new OrderCancelReasonAdaptor(context, cancelOrderReasonList, this);
        }else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
        orderCancelReason.setAdapter(orderCancelReasonAdaptor);
    }

    @Override
    public void itemClick(String strReasonType, String cancelId) {

        if (!strReasonType.isEmpty()) {
            this.strReasonType = strReasonType;
            this.strCancelId = cancelId;
        }
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

    /*Cancel Order Api */

    private void callOrderCancelReasonApi(ProgressDialog progressDialog) {

        @SuppressLint("NotifyDataSetChanged") Observer<OrderCancelReasonResponse> orderCancelReasonResponseObserver = orderCancelReasonResponse -> {
            if (orderCancelReasonResponse != null) {
                Log.e("Order Cancel Api ResponseData", new Gson().toJson(orderCancelReasonResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (orderCancelReasonResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        cancelOrderReasonList.clear();
                        if (orderCancelReasonResponse.getOrderDetials()!= null &&
                                orderCancelReasonResponse.getOrderDetials().size() > 0) {

                            cancelOrderReasonList.addAll(orderCancelReasonResponse.getOrderDetials());
                            orderCancelReasonAdaptor.notifyDataSetChanged();

                        }
                        break;
                    case STATUS_CODE_404://Validation Errors
                       warningToast(context, orderCancelReasonResponse.getMessage());

                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(orderCancelReasonResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, orderCancelReasonResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };

        orderViewDetailsViewModel.getOrderCancelReasonResponse(progressDialog, context,
                OrderViewFragment.this).observe(getViewLifecycleOwner(), orderCancelReasonResponseObserver);
    }

    private void callOrderCancelSuccessFullyApi(ProgressDialog progressDialog){
        Observer<BaseResponse> orderCancelSuccessfullyResponseObserver = orderCancelSuccessfullyResponse -> {
            if (orderCancelSuccessfullyResponse != null) {
                Log.e("Order Cancel Api ResponseData", new Gson().toJson(orderCancelSuccessfullyResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (orderCancelSuccessfullyResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, orderCancelSuccessfullyResponse.getMessage());


                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, orderCancelSuccessfullyResponse.getMessage());

                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(orderCancelSuccessfullyResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, orderCancelSuccessfullyResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };

        orderViewDetailsViewModel.getOrderCancelSuccessfullyResponse(progressDialog, context,OrderCancelSuccessfullyInputParameter(),
                OrderViewFragment.this).observe(getViewLifecycleOwner(), orderCancelSuccessfullyResponseObserver);
    }

    private HashMap<String, String> OrderCancelSuccessfullyInputParameter(){
        HashMap<String, String> map = new HashMap<>();
        map.put(ORDER_ID, order_id);
        map.put(CANCEL_ID, strCancelId);

        return map;
    }

    /*Rating and FeedBack api*/

    private void callRatingAndFeedBackApi(ProgressDialog progressDialog){
        Observer<BaseResponse> ratingAndFeedBackResponseObserver = ratingAndFeedBackResponse ->  {
            if (ratingAndFeedBackResponse != null) {
                Log.e("Rating Response", new Gson().toJson(ratingAndFeedBackResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (ratingAndFeedBackResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        successToast(context, ratingAndFeedBackResponse.getMessage());
                        //callProductDetailsApi(showCircleProgressDialog(context, ""));
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, ratingAndFeedBackResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        warningToast(context, ratingAndFeedBackResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, ratingAndFeedBackResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, ratingAndFeedBackResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };
        orderViewDetailsViewModel.getRatingAndFeedBack(progressDialog,context,RatingAndFeedBackInputParameter(),OrderViewFragment.this)
                .observe(getViewLifecycleOwner(), ratingAndFeedBackResponseObserver);
    }

    private HashMap<String, String> RatingAndFeedBackInputParameter(){
        HashMap<String, String> map = new HashMap<>();

        map.put(ORDER_ID, order_id);
        map.put(RATING, String.valueOf(ratingBar.getRating()));
        map.put(REVIEW,feedbackComment.getText().toString().trim());

        return map;
    }

}