package com.poona.agrocart.ui.nav_orders.order_view;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.poona.agrocart.app.AppConstants.CANCEL_ID;
import static com.poona.agrocart.app.AppConstants.IMAGE_DOC_BASE_URL;
import static com.poona.agrocart.app.AppConstants.ITEM_LIST;
import static com.poona.agrocart.app.AppConstants.LIMIT;
import static com.poona.agrocart.app.AppConstants.OFFSET;
import static com.poona.agrocart.app.AppConstants.ORDER_ID;
import static com.poona.agrocart.app.AppConstants.ORDER_SUBSCRIPTION_ID;
import static com.poona.agrocart.app.AppConstants.RATING;
import static com.poona.agrocart.app.AppConstants.REVIEW;
import static com.poona.agrocart.app.AppConstants.REVIEW_LIST;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.myOrderResponse.OrderCancelReasonResponse;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.BasketSubscriptionDetail;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.ItemsDetail;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.MyOrderDetailsResponse;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.MyOrderDetial;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.MyOrderReview;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.Review;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.SubscribeBasketDetailsResponse;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.SubscribeBasketItemListResponse;

import com.poona.agrocart.databinding.FragmentOrderViewBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_orders.model.CancelOrderCategoryList;
import com.poona.agrocart.ui.nav_orders.order_view.adaptor.OrderCancelCategoryAdaptor;
import com.poona.agrocart.ui.nav_orders.order_view.adaptor.OrderCancelReasonAdaptor;
import com.poona.agrocart.ui.product_detail.ProductDetailFragment;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.CustomEditText;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class OrderViewFragment extends BaseFragment implements OrderCancelReasonAdaptor.OnTypeClickListener, NetworkExceptionListener, OrderCancelCategoryAdaptor.OnCategoryClickListener, BasketItemsAdapter.OnTrackListener {

    private FragmentOrderViewBinding fragmentOrderViewBinding;
    private OrderViewDetailsViewModel orderViewDetailsViewModel;
    private String order_id = "", subscriptionBasketOrderId = "", subOrderId = "";
    private RecyclerView rvBasketListItems;
    private LinearLayoutManager linearLayoutManager;
    private BasketItemsAdapter basketItemsAdapter;
    private ArrayList<ItemsDetail> basketItemList;
    private boolean isBasketVisible = true;
    private RatingBar ratingBar;
    private CustomEditText feedbackComment;
    private CustomButton btnSubmitFeedback, btnDownloadInvoice, btnOrderTrack;
    private SwipeRefreshLayout refreshLayout;
    private final int limit = 5;
    private int offset = 0;
    private int visibleItemCount = 0;
    private int totalCount = 0;
    private ArrayList<String> orderIds = new ArrayList<>();

    /*My Product Details*/
    private List<MyOrderDetial> orderDetials = new ArrayList<>();
    private List<MyOrderReview> myOrderReviews = new ArrayList<>();

    /*My Subscription Basket Details*/
    private List<BasketSubscriptionDetail> subscriptBasketDetails = new ArrayList<>();
    private List<Review> subscriptBasketReviews = new ArrayList<>();

    /*Cancel Order dialog */
    private RecyclerView orderCancelCategory, orderCancelReason;
    // private List<ItemsDetail> cancelOrderCategoryList;
    private OrderCancelCategoryAdaptor orderCancelCategoryAdaptor;

    private List<OrderCancelReasonResponse.OrderCancelReason> cancelOrderReasonList;
    private OrderCancelReasonAdaptor orderCancelReasonAdaptor;
    private View view;
    private String strReasonType = "", strCancelId = "" ;
    private ImageView imageView;
    long downloadID;
    private DownloadManager downloadManager;
    private String strInvioceDownload, strInvoiceBasketDownload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order_id = getArguments().getString(ORDER_ID);
            subscriptionBasketOrderId = getArguments().getString(ORDER_SUBSCRIPTION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentOrderViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_view, container, false);
        fragmentOrderViewBinding.setLifecycleOwner(this);
        orderViewDetailsViewModel = new ViewModelProvider(this).get(OrderViewDetailsViewModel.class);
        fragmentOrderViewBinding.setOrderViewDetailsViewModel(orderViewDetailsViewModel);
        view = fragmentOrderViewBinding.getRoot();
        fragmentOrderViewBinding.clOrderViewMainLayout.setVisibility(View.GONE);

        initView();

        setRVAdapter();

        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        ratingBar = fragmentOrderViewBinding.ratingBarInput;
        feedbackComment = fragmentOrderViewBinding.etFeedback;
        btnSubmitFeedback = fragmentOrderViewBinding.btnSubmitFeedback;
        refreshLayout = fragmentOrderViewBinding.rlMyOrderDetails;
        btnDownloadInvoice =fragmentOrderViewBinding.btnDownloadInvoice;
        btnOrderTrack = fragmentOrderViewBinding.btnTrackOrder;

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

                    if (isBasketVisible){
                        callSubBasketRatingAndFeedBackApi(showCircleProgressDialog(context, ""));
                        fragmentOrderViewBinding.cardviewComment.setVisibility(View.VISIBLE);
                    }else {
                        callRatingAndFeedBackApi(showCircleProgressDialog(context, ""));
                        fragmentOrderViewBinding.cardviewComment.setVisibility(View.VISIBLE);
                    }

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

        btnDownloadInvoice.setOnClickListener(view1 -> {
            if(isConnectingToInternet(context)){
                if(strInvioceDownload != null && !strInvioceDownload.equals("")){

                    if (isBasketVisible){
                        beginDownload(strInvoiceBasketDownload);
                    }else {
                        beginDownload(strInvioceDownload);
                    }
                }else{
                    infoToast(context, "Something Went wrong!  please wait for while..");
                }
            }else{
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        });

        fragmentOrderViewBinding.tvSeeMore.setOnClickListener(view -> {
            isBasketVisible = bundle.getBoolean("isBasketVisible");
            NavHostFragment.findNavController(OrderViewFragment.this).
                    navigate(R.id.action_order_view_fragment_to_nav_basket_item_fragment, bundle);
        });

        btnOrderTrack.setOnClickListener(view1 -> {
            if (isConnectingToInternet(context)){
                bundle.putString(ORDER_ID, order_id);
                Navigation.findNavController(view).navigate(R.id.action_orderViewFragment_to_nav_order_track, bundle);
            }
            else
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        });

    }

    private void showProductDetails() {
        initTitleWithBackBtn(getString(R.string.order_view));
        fragmentOrderViewBinding.tvBasketDetails.setVisibility(View.GONE);
        fragmentOrderViewBinding.tvProductDetails.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.btnTrackOrder.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.llSubTotal.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.viewline1.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.tvSeeMore.setVisibility(View.GONE);

        if(isConnectingToInternet(context)){
            callOrderDetailsApi(showCircleProgressDialog(context, ""));
        }else{
            showNotifyAlert(requireActivity(),context.getString(R.string.info),context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);

                if(isConnectingToInternet(context)){
                    callOrderDetailsApi(showCircleProgressDialog(context, ""));
                }else{
                    showNotifyAlert(requireActivity(),context.getString(R.string.info),context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });


    }

    private void setBasketContentsVisible() {
        initTitleWithBackBtn(getString(R.string.basket_view));
        fragmentOrderViewBinding.tvBasketDetails.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.tvProductDetails.setVisibility(View.GONE);
        fragmentOrderViewBinding.btnTrackOrder.setVisibility(View.GONE);
        fragmentOrderViewBinding.llSubTotal.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.viewline1.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.tvSeeMore.setVisibility(View.VISIBLE);
        fragmentOrderViewBinding.llExpectedDate.setVisibility(View.GONE);

        if(isConnectingToInternet(context)){
            callSubscriptBasketDetailsApi(showCircleProgressDialog(context, ""));
        }else{
            showNotifyAlert(requireActivity(),context.getString(R.string.info),context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);

                if(isConnectingToInternet(context)){
                    callSubscriptBasketDetailsApi(showCircleProgressDialog(context, ""));
                }else{
                    showNotifyAlert(requireActivity(),context.getString(R.string.info),context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });

    }

    private void setRVAdapter() {
        basketItemList = new ArrayList<>();

        if(isConnectingToInternet(context)){
            if(isBasketVisible){
                callSubscriptionBasketItemList(showCircleProgressDialog(context, ""));

            }else {
                callOrderDetailsApi(showCircleProgressDialog(context, ""));
            }
        }else{
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        linearLayoutManager = new LinearLayoutManager(requireContext());

        rvBasketListItems.setHasFixedSize(true);
        rvBasketListItems.setLayoutManager(linearLayoutManager);

        basketItemsAdapter = new BasketItemsAdapter(basketItemList, isBasketVisible, getContext(),0,this);
        rvBasketListItems.setAdapter(basketItemsAdapter);

    }

    /* My Subscription Basket Details Api and managements */
    private void callSubscriptBasketDetailsApi(ProgressDialog progressDialog) {
        Observer<SubscribeBasketDetailsResponse> subscribeBasketDetailsResponseObserver = subscribeBasketDetailsResponse -> {

            if (subscribeBasketDetailsResponse != null) {
                Log.e(" My Subscription Basket Details Api ResponseData", new Gson().toJson(subscribeBasketDetailsResponse));
                fragmentOrderViewBinding.clOrderViewMainLayout.setVisibility(View.VISIBLE);
                refreshLayout.setRefreshing(false);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (subscribeBasketDetailsResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create Update Successfully

                        subscriptBasketDetails.clear();
                        if (subscribeBasketDetailsResponse.getBasketSubscriptionDetails()!= null &&
                                subscribeBasketDetailsResponse.getBasketSubscriptionDetails().size() > 0) {

                            subscriptBasketDetails.addAll(subscribeBasketDetailsResponse.getBasketSubscriptionDetails());
                            setSubscriptBasketValue(subscriptBasketDetails);


                            subscriptBasketReviews.addAll(subscribeBasketDetailsResponse.getBasketSubscriptionDetails().get(0).getReviews());
                            setSubscriptionBasketReviewValue(subscriptBasketReviews);
                            setSubscriptionBasketRatingViewHideShow(subscriptBasketReviews);


                        }
                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, subscribeBasketDetailsResponse.getMessage());

                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(subscribeBasketDetailsResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, subscribeBasketDetailsResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };
        orderViewDetailsViewModel.getMySubscriptionBasketDetails(progressDialog, context, MySubscriptionBasketDetailsInputParameter(), OrderViewFragment.this)
                .observe(getViewLifecycleOwner(), subscribeBasketDetailsResponseObserver);
    }

    private HashMap<String, String> MySubscriptionBasketDetailsInputParameter(){
        HashMap<String, String> map = new HashMap<>();
        map.put(ORDER_SUBSCRIPTION_ID, subscriptionBasketOrderId);
        return map;
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    private void setSubscriptBasketValue(List<BasketSubscriptionDetail> basketSubscriptionDetailList) {
        orderViewDetailsViewModel.orderId.setValue(basketSubscriptionDetailList.get(0).getOrderCode());
        orderViewDetailsViewModel.orderDate.setValue(basketSubscriptionDetailList.get(0).getCreatedAt()); //set date format
        orderViewDetailsViewModel.orderStatus.setValue(basketSubscriptionDetailList.get(0).getOrderStatus()); //changes in status format
        orderViewDetailsViewModel.deliveryCode.setValue(basketSubscriptionDetailList.get(0).getDeliveryCode());
        orderViewDetailsViewModel.customerName.setValue(basketSubscriptionDetailList.get(0).getName());
        orderViewDetailsViewModel.customerNumber.setValue(basketSubscriptionDetailList.get(0).getMobile());
        orderViewDetailsViewModel.customerAddress.setValue(basketSubscriptionDetailList.get(0).getOrderAddressText());
        orderViewDetailsViewModel.customerArea.setValue(basketSubscriptionDetailList.get(0).getOrderAreaName());
        orderViewDetailsViewModel.customerCity.setValue(basketSubscriptionDetailList.get(0).getOrderCityName());
        orderViewDetailsViewModel.paymentType.setValue(basketSubscriptionDetailList.get(0).getPaymentType()); //changes in status format
        orderViewDetailsViewModel.discountAmount.setValue(basketSubscriptionDetailList.get(0).getDiscount());
        orderViewDetailsViewModel.deliveryCharges.setValue(basketSubscriptionDetailList.get(0).getDeliveryCharges());
        orderViewDetailsViewModel.totalAmount.setValue(basketSubscriptionDetailList.get(0).getPaidAmount());
        orderViewDetailsViewModel.subTotalAmount.setValue(basketSubscriptionDetailList.get(0).getProductAmount());

        strInvoiceBasketDownload = basketSubscriptionDetailList.get(0).getInvoiceFile();

        switch (basketSubscriptionDetailList.get(0).getOrderStatus()) {
            case "3":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.in_process));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_in_process)));
                break;
            case "4":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.delivered));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_delivered)));

                fragmentOrderViewBinding.llMainLayoutRatingReview.setVisibility(View.VISIBLE);

                break;
            case "2":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.confirmed));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_confirmed)));
                break;
            case "5":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.cancelled));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_cancelled)));

                orderViewDetailsViewModel.savedAmount.setValue(getString(R.string.cancelled_order_message)); //Get the saved value
                fragmentOrderViewBinding.tvSavings.setTextColor(R.color.color_cancelled);

                fragmentOrderViewBinding.llMainLayoutRatingReview.setVisibility(View.VISIBLE);
                fragmentOrderViewBinding.llCancel.setVisibility(View.GONE);
                fragmentOrderViewBinding.btnTrackOrder.setVisibility(View.GONE);

                break;
            default:
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.pending));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color4)));
                break;
        }

        switch (basketSubscriptionDetailList.get(0).getPaymentType()){
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

        String selectedDate = basketSubscriptionDetailList.get(0).getCreatedAt();

        String txtDisplayDate = "";
        try {
            txtDisplayDate = formatDate(selectedDate, "yyyy-mm-dd hh:mm:ss", "MMM dd, yyyy hh:mm aa");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fragmentOrderViewBinding.tvOrderDate.setText(txtDisplayDate);

        if(basketSubscriptionDetailList.get(0).getTransactionId() != null){
            orderViewDetailsViewModel.transactionId.setValue(basketSubscriptionDetailList.get(0).getTransactionId());
        }else{
            orderViewDetailsViewModel.transactionId.setValue("Not Available");
        }

    }

    private void setSubscriptionBasketReviewValue(List<Review>subscriptBasketReviews) {

        orderViewDetailsViewModel.reviewName.setValue(subscriptBasketReviews.get(0).getName());
        orderViewDetailsViewModel.reviewDate.setValue(subscriptBasketReviews.get(0).getDate());
        orderViewDetailsViewModel.customerFeedback.setValue(subscriptBasketReviews.get(0).getReview());

        fragmentOrderViewBinding.ratingBar.setRating(Float.parseFloat(subscriptBasketReviews.get(0).getRating()));

        imageView = fragmentOrderViewBinding.ivUserImage;
        Glide.with(context)
                .load(IMAGE_DOC_BASE_URL + subscriptBasketReviews.get(0).getImage())
                .placeholder(R.drawable.ic_profile_white)
                .error(R.drawable.ic_profile_white)
                .into(imageView);

        if(subscriptBasketReviews.get(0).getName() != null){
            fragmentOrderViewBinding.cardviewComment.setVisibility(View.VISIBLE);
        }else{
            fragmentOrderViewBinding.cardviewComment.setVisibility(View.GONE);
        }

    }

    private void setSubscriptionBasketRatingViewHideShow(List<Review> reviews) {
        if (!reviews.get(0).getRating().isEmpty() && !reviews.get(0).getReview().isEmpty() ||
                !reviews.get(0).getRating().equalsIgnoreCase("") &&
                        !reviews.get(0).getReview().equalsIgnoreCase("")) {
            fragmentOrderViewBinding.llRatingReview.setVisibility(View.GONE);
        } else {
            fragmentOrderViewBinding.llRatingReview.setVisibility(View.VISIBLE);
        }
    }

    private void callSubscriptionBasketItemList(ProgressDialog progressDialog){
        @SuppressLint("NotifyDataSetChanged") Observer<SubscribeBasketItemListResponse> subscribeBasketItemListResponseObserver = subscribeBasketItemListResponse -> {
            if (subscribeBasketItemListResponse != null) {
                Log.e("Subscription Basket Item List List Api ResponseData", new Gson().toJson(subscribeBasketItemListResponse));
                refreshLayout.setRefreshing(false);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (subscribeBasketItemListResponse.getStatus()) {

                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (offset == 0)
                            basketItemList.clear();

                        totalCount = Integer.parseInt(subscribeBasketItemListResponse.getTotalCount());
                        if (subscribeBasketItemListResponse.getBasketSubscriptionDetails() != null
                                && subscribeBasketItemListResponse.getBasketSubscriptionDetails().getItemsDetails().size() > 0) {
                            basketItemList.addAll(subscribeBasketItemListResponse.getBasketSubscriptionDetails().getItemsDetails());
                            /*allBasketItem.clear();
                            allBasketItem.addAll(basketItemList);*/
                            basketItemsAdapter.notifyDataSetChanged();

                        }

                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, subscribeBasketItemListResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(subscribeBasketItemListResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, subscribeBasketItemListResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };
        orderViewDetailsViewModel.getSubscriptionBasketItemList(context, progressDialog, BasketItemInputParameter(), OrderViewFragment.this)
                .observe(getViewLifecycleOwner(), subscribeBasketItemListResponseObserver);
    }

    private HashMap<String,String> BasketItemInputParameter() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ORDER_SUBSCRIPTION_ID,subscriptionBasketOrderId);
        map.put(OFFSET, String.valueOf(offset));
        map.put(LIMIT, String.valueOf(limit));

        return map;
    }

    /*Order Details Api and managements */
    private void callOrderDetailsApi(ProgressDialog progressDialog) {
        @SuppressLint({"NotifyDataSetChanged", "ResourceAsColor"})
        Observer<MyOrderDetailsResponse> myOrderDetailsResponseObserver = myOrderDetailsResponse -> {
            fragmentOrderViewBinding.clOrderViewMainLayout.setVisibility(View.VISIBLE);
            refreshLayout.setRefreshing(false);
            if (myOrderDetailsResponse != null) {
                Log.e(" My Order Details Api Response Data", new Gson().toJson(myOrderDetailsResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (myOrderDetailsResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create Update Successfully

                        orderDetials.clear();
                        if (myOrderDetailsResponse.getOrderDetials()!= null &&
                                myOrderDetailsResponse.getOrderDetials().size() > 0) {

                            orderViewDetailsViewModel.savedAmount.setValue(myOrderDetailsResponse.getDiscountMessage()); //Get the saved value
                            fragmentOrderViewBinding.tvSavings.setTextColor(R.color.colorPrimary);

                            orderDetials.addAll(myOrderDetailsResponse.getOrderDetials());
                            setValue(orderDetials);

                            basketItemList.clear();
                            if(myOrderDetailsResponse.getOrderDetials().get(0).getItemsDetails() != null &&
                                    myOrderDetailsResponse.getOrderDetials().get(0).getItemsDetails().size() > 0){
                                basketItemList.addAll(myOrderDetailsResponse.getOrderDetials().get(0).getItemsDetails());
                                basketItemsAdapter.notifyDataSetChanged();
                            }


                            myOrderReviews.addAll(myOrderDetailsResponse.getOrderDetials().get(0).getReviews());
                            setReviewValue(myOrderReviews);
                            setRatingViewHideShow(myOrderReviews);



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

    @SuppressLint({"ResourceType", "SetTextI18n"})
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
        orderViewDetailsViewModel.discountAmount.setValue(orderDetials.get(0).getDiscount());
        orderViewDetailsViewModel.deliveryCharges.setValue(orderDetials.get(0).getDeliveryCharges());
        orderViewDetailsViewModel.totalAmount.setValue(orderDetials.get(0).getPaidAmount());
        orderViewDetailsViewModel.subTotalAmount.setValue(orderDetials.get(0).getProductAmount());

        strInvioceDownload = orderDetials.get(0).getInvoiceFile();

        switch (orderDetials.get(0).getOrderStatus()) {
            case "3":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.in_process));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_in_process)));
                break;
            case "4":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.delivered));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_delivered)));

                fragmentOrderViewBinding.llMainLayoutRatingReview.setVisibility(View.VISIBLE);
                break;
            case "2":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.confirmed));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_confirmed)));
                break;
            case "5":
                fragmentOrderViewBinding.tvOrderStatus.setText(context.getString(R.string.cancelled));
                fragmentOrderViewBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_cancelled)));

                orderViewDetailsViewModel.savedAmount.setValue(getString(R.string.cancelled_order_message)); //Get the saved value
                fragmentOrderViewBinding.tvSavings.setTextColor(R.color.color_cancelled);
                fragmentOrderViewBinding.llMainLayoutRatingReview.setVisibility(View.VISIBLE);
                fragmentOrderViewBinding.llCancel.setVisibility(View.GONE);
                fragmentOrderViewBinding.btnTrackOrder.setVisibility(View.GONE);
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

        if(orderDetials.get(0).getTransactionId() != null){
            orderViewDetailsViewModel.transactionId.setValue(orderDetials.get(0).getTransactionId());
        }else{
            orderViewDetailsViewModel.transactionId.setValue("Not Available");
        }

    }

    private void setReviewValue(List<MyOrderReview>reviewValue) {

        orderViewDetailsViewModel.reviewName.setValue(reviewValue.get(0).getName());
        orderViewDetailsViewModel.reviewDate.setValue(reviewValue.get(0).getDate());
        orderViewDetailsViewModel.customerFeedback.setValue(reviewValue.get(0).getReview());

        fragmentOrderViewBinding.ratingBar.setRating(Float.parseFloat(reviewValue.get(0).getRating()));

        imageView = fragmentOrderViewBinding.ivUserImage;
        Glide.with(context)
                .load(IMAGE_DOC_BASE_URL + reviewValue.get(0).getImage())
                .placeholder(R.drawable.ic_profile_white)
                .error(R.drawable.ic_profile_white)
                .into(imageView);

        if(reviewValue.get(0).getName() != null){
            fragmentOrderViewBinding.cardviewComment.setVisibility(View.VISIBLE);
        }else{
            fragmentOrderViewBinding.cardviewComment.setVisibility(View.GONE);
        }

    }

    private void setRatingViewHideShow(List<MyOrderReview> ratingList) {
        if (!ratingList.get(0).getRating().isEmpty() && !ratingList.get(0).getReview().isEmpty() ||
                !ratingList.get(0).getRating().equalsIgnoreCase("") &&
                        !ratingList.get(0).getReview().equalsIgnoreCase("")) {
            fragmentOrderViewBinding.llRatingReview.setVisibility(View.GONE);
        } else {
            fragmentOrderViewBinding.llRatingReview.setVisibility(View.VISIBLE);
        }
    }

    /*Download the document*/
/*
    private void beginDownload(String docPdf) {
        DownloadManager.Request request = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            request = new DownloadManager.Request(Uri.parse(docPdf))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);
        } else {
            request = new DownloadManager.Request(Uri.parse(docPdf))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setAllowedOverRoaming(true);
        }
        downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);

        // using query method
        boolean finishDownload = true;
        int progress;
        while (!finishDownload) {
            Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadID));
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_FAILED: {
                        finishDownload = true;
                        break;
                    }
                    case DownloadManager.STATUS_PAUSED:
                        break;
                    case DownloadManager.STATUS_PENDING:
                        break;
                    case DownloadManager.STATUS_RUNNING: {
                        @SuppressLint("Range") final long total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        if (total >= 0) {
                            @SuppressLint("Range") final long downloaded = ((Cursor) cursor).getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            progress = (int) ((downloaded * 100L) / total);
                            // if you use downloadmanger in async task, here you can use like this to display progress.
                            // Don't forget to do the division in long to get more digits rather than double.
                            //  publishProgress((int) ((downloaded * 100L) / total));
                        }
                        break;
                    }
                    case DownloadManager.STATUS_SUCCESSFUL: {
                        progress = 100;
                        // if you use aysnc task
                        // publishProgress(100);
                        finishDownload = true;
                        Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        }
    }
*/

    /* Cancel Order Manager */
    private void CancelOrderDialogBox() {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;
        dialog.setContentView(R.layout.order_cancel_dialog_box);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        /*Validation and Logical part on the component */
        ImageView crossImage = dialog.findViewById(R.id.close_btn);
        orderCancelCategory = dialog.findViewById(R.id.rv_cancel_category);
        orderCancelReason = dialog.findViewById(R.id.rv_cancel_reason);
        CustomButton btnSubmit = dialog.findViewById(R.id.btn_submit);

        setCancelCategoryAdapter();

        if(isConnectingToInternet(context)){
            setCancelReasonAdapter();

        }else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }


        if(isBasketVisible){
            orderCancelCategory.setVisibility(View.VISIBLE);
        }else {

            orderCancelCategory.setVisibility(View.GONE);
        }

        btnSubmit.setOnClickListener(view1 -> {
                if (isConnectingToInternet(context)){
                    if(!strReasonType.equalsIgnoreCase("")) {
                        callOrderCancelSuccessFullyApi(showCircleProgressDialog(context,""));
                        dialog.dismiss();
                    }else {
                        warningToast(context, "Please select Reason");
                    }
                }else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }

        });

        crossImage.setOnClickListener(view -> {
            dialog.dismiss();
        });


        dialog.show();
    }

    private void setCancelReasonAdapter() {
        cancelOrderReasonList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        if(isConnectingToInternet(context)){
            callOrderCancelReasonApi(showCircleProgressDialog(context, ""));
        }else{
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

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
        // prepareListCancelCategory();

        linearLayoutManager = new LinearLayoutManager(requireContext());

        if(isBasketVisible){
            callSubscriptionBasketItemList(showCircleProgressDialog(context, ""));
        }
        //callSubscriptionBasketItemList(showCircleProgressDialog(context, ""));
        orderCancelCategory.setHasFixedSize(true);
        orderCancelCategory.setLayoutManager(linearLayoutManager);

        orderCancelCategoryAdaptor = new OrderCancelCategoryAdaptor(context, basketItemList, this);
        orderCancelCategory.setAdapter(orderCancelCategoryAdaptor);
    }

    /* Cancel Order Api */
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

                        if(isBasketVisible){
                            callSubscriptBasketDetailsApi(showCircleProgressDialog(context, ""));
                            callSubscriptionBasketItemList(showCircleProgressDialog(context, ""));
                        }else {
                            callOrderDetailsApi(showCircleProgressDialog(context, ""));
                        }


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


    /* Rating and FeedBack api */
    private void callRatingAndFeedBackApi(ProgressDialog progressDialog){
        Observer<BaseResponse> ratingAndFeedBackResponseObserver = ratingAndFeedBackResponse ->  {
            if (ratingAndFeedBackResponse != null) {
                Log.e("Rating Response", new Gson().toJson(ratingAndFeedBackResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (ratingAndFeedBackResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        callOrderDetailsApi(showCircleProgressDialog(context, ""));
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, ratingAndFeedBackResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        warningToast(context, ratingAndFeedBackResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
//                        warningToast(context, ratingAndFeedBackResponse.getMessage());
                        goToAskSignInSignUpScreen( ratingAndFeedBackResponse.getMessage(),context);
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

    /* Subscription Rating And feedback basket */

    private void callSubBasketRatingAndFeedBackApi(ProgressDialog progressDialog){
        Observer<BaseResponse> ratingAndFeedBackResponseObserver = ratingAndFeedBackResponse ->  {
            if (ratingAndFeedBackResponse != null) {
                Log.e("Subscription Basket Rating Response", new Gson().toJson(ratingAndFeedBackResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (ratingAndFeedBackResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        callSubscriptBasketDetailsApi(showCircleProgressDialog(context, ""));
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, ratingAndFeedBackResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        warningToast(context, ratingAndFeedBackResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
//                        warningToast(context, ratingAndFeedBackResponse.getMessage());
                        goToAskSignInSignUpScreen( ratingAndFeedBackResponse.getMessage(),context);
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
        orderViewDetailsViewModel.getSubBasketRatingAndFeedBack(progressDialog,context,SubBasketRatingAndFeedBackInputParameter(),OrderViewFragment.this)
                .observe(getViewLifecycleOwner(), ratingAndFeedBackResponseObserver);
    }

    private HashMap<String, String> SubBasketRatingAndFeedBackInputParameter(){
        HashMap<String, String> map = new HashMap<>();
        map.put(ORDER_SUBSCRIPTION_ID, subscriptionBasketOrderId);
        map.put(RATING, String.valueOf(ratingBar.getRating()));
        map.put(REVIEW,feedbackComment.getText().toString().trim());

        return map;
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), OrderViewFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        callOrderDetailsApi(showCircleProgressDialog(context, ""));
                        break;
                    case 1:
                        callRatingAndFeedBackApi(showCircleProgressDialog(context, ""));
                        break;
                    case 2:
                        callOrderCancelReasonApi(showCircleProgressDialog(context, ""));
                        break;
                    case 3:
                        callOrderCancelSuccessFullyApi(showCircleProgressDialog(context, ""));
                        break;
                    case 4:
                        callSubscriptBasketDetailsApi(showCircleProgressDialog(context, ""));
                        break;
                    case 5:
                        callSubscriptionBasketItemList(showCircleProgressDialog(context, ""));
                        break;
                }
            }else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }

    @Override
    public void onItemCheck(ItemsDetail item, ArrayList<String> orderId) {
        if(orderId != null && !orderId.isEmpty()){
            this.orderIds=orderId;
            order_id = TextUtils.join(",", orderId);
        }else {
            this.orderIds=orderId;
            order_id = TextUtils.join(", ", orderId);
        }

    }

    @Override
    public void onItemUncheck(ItemsDetail item, ArrayList<String> orderId) {
        if(orderId != null && !orderId.isEmpty()){
            this.orderIds=orderId;
            order_id = TextUtils.join(",", orderId);
        }else {
            this.orderIds=orderId;
            order_id = TextUtils.join(",", orderId);
        }

    }

    @Override
    public void onTrackButtonClick(String orderId) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(ORDER_ID, orderId);
            Navigation.findNavController(view).navigate(R.id.action_orderViewFragment_to_nav_order_track, bundle);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}