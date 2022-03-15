package com.poona.agrocart.ui.order_summary;

import static com.poona.agrocart.app.AppConstants.ADDRESS_P_ID;
import static com.poona.agrocart.app.AppConstants.ADD_ADDRESS_DETAILS;
import static com.poona.agrocart.app.AppConstants.ADD_UPDATE_ADDRESS_DETAILS;
import static com.poona.agrocart.app.AppConstants.COUPON_CODE;
import static com.poona.agrocart.app.AppConstants.COUPON_ID;
import static com.poona.agrocart.app.AppConstants.DELIVERY_DATE;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.ORDER_SUMMARY;
import static com.poona.agrocart.app.AppConstants.PAYMENT_MODE_ID;
import static com.poona.agrocart.app.AppConstants.SLOT_ID;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.AddressesResponse;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.Coupon;
import com.poona.agrocart.data.network.responses.orderResponse.ApplyCouponResponse;
import com.poona.agrocart.data.network.responses.orderResponse.ItemsDetail;
import com.poona.agrocart.data.network.responses.orderResponse.OrderSummaryResponse;
import com.poona.agrocart.databinding.DialogDeliveryOptionsBinding;
import com.poona.agrocart.databinding.FragmentOrderSummaryBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.order_summary.adapter.AddressDialogAdapter;
import com.poona.agrocart.ui.order_summary.adapter.DeliveryDialogAdapter;
import com.poona.agrocart.ui.order_summary.adapter.PromoCodeDialogAdapter;
import com.poona.agrocart.data.network.responses.orderResponse.DeliverySlot;
import com.poona.agrocart.widgets.CustomTextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OrderSummaryFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener, AddressDialogAdapter.OnAddressClickListener, DeliveryDialogAdapter.OnSlotClickListener {
    private static String TAG = OrderSummaryFragment.class.getSimpleName();
    private FragmentOrderSummaryBinding fragmentOrderSummaryBinding;
    private DialogDeliveryOptionsBinding deliveryOptionsBinding;
    private OrderSummaryViewModel orderSummaryViewModel;
    private RecyclerView rvProductsAndPrices;
    private LinearLayoutManager linearLayoutManager;
    private ProductAndPriceAdapter productAndPriceAdapter;
    private ArrayList<ItemsDetail> itemsDetailArrayList;
    private ProgressBar itemProgress;

    private View navHostFragment;
    private ViewGroup.MarginLayoutParams navHostMargins;
    private float scale;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private ConstraintLayout mainLayout;
    private SwipeRefreshLayout rlRefreshPage;
    private String stepAddress, stepOrder, stepPayment;
    private ArrayList<AddressesResponse.Address> addressArrayList;
    private ArrayList<Coupon> couponArrayList;
    //    private Delivery deliveryArrayList;
    private ArrayList<DeliverySlot> deliverySlotArrayList;
    private AddressDialogAdapter addressDialogAdapter;
    private PromoCodeDialogAdapter codeDialogAdapter;
    private DeliveryDialogAdapter deliveryDialogAdapter;
    private Dialog addressDialog;
    private Dialog deliveryDialog;
    private Dialog promoCodeDialog;
    private NestedScrollView scrollView;
    private String CouponId;
    private String PaymentModeId;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentOrderSummaryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_summary, container, false);
        fragmentOrderSummaryBinding.setLifecycleOwner(this);
        orderSummaryViewModel = new ViewModelProvider(this).get(OrderSummaryViewModel.class);
        final View view = fragmentOrderSummaryBinding.getRoot();

        initTitleWithBackBtn(getString(R.string.order_summary));
        initView();

        /*OnScrollview scrolled*/
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {

            }
        });

        rlRefreshPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rlRefreshPage.setRefreshing(true);
                mainLayout.setVisibility(View.GONE);
                if (isConnectingToInternet(context)) {
                    callOrderSummaryAPI(showCircleProgressDialog(context, ""));
                } else {
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });

        /*Coupon apply or remove button*/
        fragmentOrderSummaryBinding.etCouponCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    fragmentOrderSummaryBinding.btnRemove.setText("Apply");
                } else {
                    CouponId = "";
                }
            }
        });
        return view;
    }

    private void setBottomMarginInDps(int i) {
        int dpAsPixels = (int) (i * scale + 0.5f);
        navHostMargins.bottomMargin = dpAsPixels;
    }

    private void initView() {
        preferences.setDeliveryAddressId("");
        preferences.setDeliverySlot("");
        preferences.setCouponId("");
        preferences.setPaymentMode("");
        preferences.setDeliveryDate("");
        rvProductsAndPrices = fragmentOrderSummaryBinding.rvProductsAndPrices;
        mainLayout = fragmentOrderSummaryBinding.mainLayout;
        itemProgress = fragmentOrderSummaryBinding.itemsProgress;
        rlRefreshPage = fragmentOrderSummaryBinding.rlRefreshPage;
        scrollView = fragmentOrderSummaryBinding.scrollView;
        mainLayout.setVisibility(View.GONE);
        Typeface font = Typeface.createFromAsset(context.getAssets(), getString(R.string.font_poppins_medium));
        fragmentOrderSummaryBinding.rbCod.setTypeface(font);
        fragmentOrderSummaryBinding.rbOnline.setTypeface(font);
        fragmentOrderSummaryBinding.rbWalletBalance.setTypeface(font);
        fragmentOrderSummaryBinding.rbCod.setOnClickListener(this);
        fragmentOrderSummaryBinding.rbOnline.setOnClickListener(this);
        fragmentOrderSummaryBinding.rbWalletBalance.setOnClickListener(this);
        fragmentOrderSummaryBinding.ivAddressOption.setOnClickListener(this);
        fragmentOrderSummaryBinding.tbnGetCode.setOnClickListener(this);
        fragmentOrderSummaryBinding.btnRemove.setOnClickListener(this);
        fragmentOrderSummaryBinding.ivEditDate.setOnClickListener(this);

        scale = getResources().getDisplayMetrics().density;

        requireActivity().findViewById(R.id.bottom_navigation_view).setVisibility(View.GONE);

        navHostFragment = requireActivity().findViewById(R.id.nav_host_fragment_content_home);
        navHostMargins = (ViewGroup.MarginLayoutParams) navHostFragment.getLayoutParams();
        navHostMargins.bottomMargin = 0;
        fragmentOrderSummaryBinding.btnMakePayment.setOnClickListener(this::onClick);

        if (isConnectingToInternet(context)) {
            callOrderSummaryAPI(showCircleProgressDialog(context, ""));
        } else showNotifyAlert(requireActivity(), context.getString(R.string.info),
                context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
    }

    /*Call Order place api here*/
    private void callOrderPlaceAPI(ProgressDialog progressDialog) {
        Observer<BaseResponse> baseResponseObserver = response -> {
            if (response != null) {
                progressDialog.dismiss();
                switch (response.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, response.getMessage());
                        redirectToOrderDetails();
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        //show no data msg here
                        warningToast(context, response.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(response.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, response.getMessage());
                        break;
                }

            }
        };
        orderSummaryViewModel.getOrderPlaceAPIResponse(progressDialog, placeOrderMaps(), OrderSummaryFragment.this)
                .observe(getViewLifecycleOwner(), baseResponseObserver);
    }

    private HashMap<String, String> placeOrderMaps() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ADDRESS_P_ID, preferences.getDeliveryAddressId());
        map.put(DELIVERY_DATE, preferences.getDeliveryDate());
        map.put(SLOT_ID, preferences.getDeliverySlot());
        map.put(PAYMENT_MODE_ID, preferences.getPaymentMode());
        map.put(COUPON_ID, preferences.getCouponId());
        return map;
    }

    /*Call Order Summary API here*/
    private void callOrderSummaryAPI(ProgressDialog progressDialog) {
        Observer<OrderSummaryResponse> orderSummaryResponseObserver = orderSummaryResponse -> {
            if (orderSummaryResponse != null) {
                rlRefreshPage.setRefreshing(false);
                if (progressDialog != null)
                    progressDialog.dismiss();
                Log.e(TAG, "callOrderSummaryAPI: " + new Gson().toJson(orderSummaryResponse));
                switch (orderSummaryResponse.status) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        mainLayout.setVisibility(View.VISIBLE);
                        if (orderSummaryResponse.address.size() == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putString(ADD_UPDATE_ADDRESS_DETAILS, ADD_ADDRESS_DETAILS);
                            bundle.putString(FROM_SCREEN, ORDER_SUMMARY);
                            NavHostFragment.findNavController(OrderSummaryFragment.this).navigate(R.id.action_nav_order_summary_to_addAddressFragment, bundle);
                        } else {
                            stepAddress = "";
                            stepOrder = "";
                            stepPayment = "";
                            checkOrderSummaryStatus();
                            orderSummaryViewModel.initViewModel(orderSummaryResponse, context);
                            fragmentOrderSummaryBinding.setOrderSummaryViewModel(orderSummaryViewModel);
                            fragmentOrderSummaryBinding.setVariable(BR.orderSummaryViewModel, orderSummaryViewModel);
                            initAddressDialog();
                            initExpectedDeliveryDialog();
                            initPromoCodeDialog();
                            initItemsDetails();
                            initPaymentTypes();
                            checkValuesAndViews();

                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        //show no data msg here
                        warningToast(context, orderSummaryResponse.message);
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(orderSummaryResponse.message, context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, orderSummaryResponse.message);
                        break;
                }

            }
        };
        orderSummaryViewModel.getOrderSummaryResponse(progressDialog, OrderSummaryFragment.this)
                .observe(getViewLifecycleOwner(), orderSummaryResponseObserver);
    }

    /*check if value null or empty view should be hide*/
    private void checkValuesAndViews() {
        /*hide views if value is empty or null*/
        if (orderSummaryViewModel.discountMutable.getValue() == null ||
                Float.parseFloat(orderSummaryViewModel.discountMutable.getValue()) == 0.0){
            fragmentOrderSummaryBinding.llDiscount.setVisibility(View.GONE);
            fragmentOrderSummaryBinding.tvYouWillSave.setVisibility(View.GONE);
        }
        else {
            fragmentOrderSummaryBinding.tvYouWillSave.setVisibility(View.VISIBLE);
            fragmentOrderSummaryBinding.llDiscount.setVisibility(View.VISIBLE);
        }

    }

    /*Call Apply Coupon API*/
    private void callApplyCouponAPI(ProgressDialog progressDialog) {
        Observer<ApplyCouponResponse> applyCouponResponseObserver = applyCouponResponse -> {
            if (applyCouponResponse != null) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    Log.e(TAG, "callApplyCouponAPI: " + new Gson().toJson(applyCouponResponse));
                    switch (applyCouponResponse.status) {
                        case STATUS_CODE_200://Record Create/Update Successfully
                            orderSummaryViewModel.couponMessageMutable.setValue(applyCouponResponse.message);
                            if (applyCouponResponse.message.contains("remove") || applyCouponResponse.message.contains("valid")) {
                                fragmentOrderSummaryBinding.tvCouponApplied.setTextColor(context.getColor(R.color.red_text_color));
                            } else {
                                fragmentOrderSummaryBinding.tvCongratulations.setVisibility(View.VISIBLE);
                                fragmentOrderSummaryBinding.btnRemove.setText("Remove");
                            }
//                            successToast(context,applyCouponResponse.message);
                            preferences.setCouponId(applyCouponResponse.couponId);
                            if (applyCouponResponse.getItemsDetails() != null
                                    && applyCouponResponse.getItemsDetails().size() > 0) {
                                if (itemProgress != null && itemProgress.isShown())
                                    itemProgress.setVisibility(View.GONE);
                                ArrayList<ItemsDetail> itemsDetails = new ArrayList<>();
                                orderSummaryViewModel.arrayItemListMutableLiveData.setValue(itemsDetails);
                                orderSummaryViewModel.arrayItemListMutableLiveData.setValue(applyCouponResponse.getItemsDetails());
                                orderSummaryViewModel.subTotalMutable.setValue(String.valueOf(applyCouponResponse.subTotal).trim());
                                orderSummaryViewModel.discountMutable.setValue(String.valueOf(applyCouponResponse.discount).trim());
                                orderSummaryViewModel.deliveryChargesMutable.setValue(String.valueOf(applyCouponResponse.deliveryCharges).trim());
                                orderSummaryViewModel.finalTotalMutable.setValue(String.valueOf(applyCouponResponse.totalAmount).trim());
                                initItemsDetails();
                                checkValuesAndViews();
                            }
                            break;
                        case STATUS_CODE_403://Validation Errors
                        case STATUS_CODE_400://Validation Errors
                        case STATUS_CODE_404://Validation Errors
                            //show no data msg here
                            warningToast(context, applyCouponResponse.message);
                            break;
                        case STATUS_CODE_401://Unauthorized user
                            goToAskSignInSignUpScreen(applyCouponResponse.message, context);
                            break;
                        case STATUS_CODE_405://Method Not Allowed
                            infoToast(context, applyCouponResponse.message);
                            break;
                    }

                }
            }
        };
        orderSummaryViewModel.getApplyCouponResponse(progressDialog, applyCouponParams(),
                OrderSummaryFragment.this).observe(getViewLifecycleOwner(), applyCouponResponseObserver);
//        fragmentOrderSummaryBinding.etCouponCode.setText("");
    }

    private HashMap<String, String> applyCouponParams() {
        HashMap<String, String> map = new HashMap<>();
        if (fragmentOrderSummaryBinding.etCouponCode.getText().toString().trim() != null
                && !fragmentOrderSummaryBinding.etCouponCode.getText().toString().trim().isEmpty()
                && fragmentOrderSummaryBinding.btnRemove.getText().toString().equalsIgnoreCase("Apply"))
            map.put(COUPON_CODE, fragmentOrderSummaryBinding.etCouponCode.getText().toString().trim());
        else {
            map.put(COUPON_CODE, "");
            fragmentOrderSummaryBinding.etCouponCode.setText("");
        }
        return map;
    }

    /*Init payment options here*/
    private void initPaymentTypes() {
        orderSummaryViewModel.arrayPaymentListMutableLiveData.observe(getViewLifecycleOwner(), payments -> {
            if (payments.size() > 0) {
                if (payments.get(0).paymentModeStatus.equalsIgnoreCase("1"))
                    orderSummaryViewModel.paymentCashMutable.setValue(payments.get(0).paymentType);
                if (payments.get(1).paymentModeStatus.equalsIgnoreCase("1"))
                    orderSummaryViewModel.paymentOnlineMutable.setValue(payments.get(0).paymentType);
                if (payments.get(2).paymentModeStatus.equalsIgnoreCase("1"))
                    orderSummaryViewModel.paymentWalletMutable.setValue(payments.get(0).paymentType);
            }
        });
    }

    //Init Dialog Lists here
    private void initAddressDialog() {
        addressArrayList = new ArrayList<>();
        orderSummaryViewModel.arrayAddressListMutableLiveData.observe(getViewLifecycleOwner(), address -> {
            addressArrayList.addAll(address);
            for (int i = 0; i < addressArrayList.size(); i++) {
                AddressesResponse.Address newAddress = setCompletedAddress(addressArrayList.get(i));
                addressArrayList.set(i, newAddress);
                if (addressArrayList.get(i).getIsDefault().equalsIgnoreCase("yes")) {
                    preferences.setDeliveryAddress(newAddress.getFullAddress());
                    orderSummaryViewModel.deliveryAddressMutable.setValue(newAddress.getFullAddress());
                    preferences.setDeliveryAddressId(newAddress.getAddressPrimaryId());
                    stepAddress = "yes";
                }
            }
            addressDialogAdapter = new AddressDialogAdapter(addressArrayList, getActivity(), this);
        });
    }

    /*Expected delivery here*/
    private void initExpectedDeliveryDialog() {
        deliverySlotArrayList = new ArrayList<>();
        orderSummaryViewModel.arrayDeliveryListMutableLiveData.observe(getViewLifecycleOwner(), deliveries -> {
//            deliveryArrayList= deliveries;
            ArrayList<DeliverySlot> tempSlots = new ArrayList<>();
            for (DeliverySlot deliverySlot : deliveries.deliverySlots) {
                if (deliverySlot.isAvailable > 0)
                    tempSlots.add(deliverySlot);
            }
            deliverySlotArrayList.addAll(tempSlots);
            deliveryDialogAdapter = new DeliveryDialogAdapter(deliverySlotArrayList, context, this);
        });
    }

    /*Promo code here*/
    private void initPromoCodeDialog() {
        couponArrayList = new ArrayList<>();
        orderSummaryViewModel.arrayCouponListMutableLiveData.observe(getViewLifecycleOwner(), coupons -> {
            couponArrayList.addAll(coupons);
            codeDialogAdapter = new PromoCodeDialogAdapter(couponArrayList, context);
        });
    }

    /*Item details here*/
    private void initItemsDetails() {
        itemsDetailArrayList = new ArrayList<>();
        orderSummaryViewModel.arrayItemListMutableLiveData.observe(getViewLifecycleOwner(),
                itemsDetails -> {
                    itemsDetailArrayList.clear();
                    if (itemsDetails.size() > 0) {
                        itemsDetailArrayList.addAll(itemsDetails);
                        linearLayoutManager = new LinearLayoutManager(requireContext());
                        rvProductsAndPrices.setHasFixedSize(true);
                        rvProductsAndPrices.setLayoutManager(linearLayoutManager);
                        productAndPriceAdapter = new ProductAndPriceAdapter(itemsDetailArrayList);
                        rvProductsAndPrices.setAdapter(productAndPriceAdapter);
                    }
                });
    }

    private void checkOrderSummaryStatus() {
        if (stepAddress.equalsIgnoreCase("yes"))
            fragmentOrderSummaryBinding.tvStep1.setBackground(context.getDrawable(R.drawable.ic_step_done));
        else if (stepAddress.equalsIgnoreCase("no"))
            fragmentOrderSummaryBinding.tvStep1.setBackground(context.getDrawable(R.drawable.ic_step_active));

        else
            fragmentOrderSummaryBinding.tvStep1.setBackground(context.getDrawable(R.drawable.ic_step_inactive));
        if (stepOrder.equalsIgnoreCase("yes"))
            fragmentOrderSummaryBinding.tvStep2.setBackground(context.getDrawable(R.drawable.ic_step_done));
        else if (stepOrder.equalsIgnoreCase("no"))
            fragmentOrderSummaryBinding.tvStep2.setBackground(context.getDrawable(R.drawable.ic_step_active));
        else
            fragmentOrderSummaryBinding.tvStep2.setBackground(context.getDrawable(R.drawable.ic_step_inactive));
        if (stepPayment.equalsIgnoreCase("yes"))
            fragmentOrderSummaryBinding.tvStep3.setBackground(context.getDrawable(R.drawable.ic_step_done));
        else if (stepPayment.equalsIgnoreCase("no"))
            fragmentOrderSummaryBinding.tvStep3.setBackground(context.getDrawable(R.drawable.ic_step_active));
        else
            fragmentOrderSummaryBinding.tvStep3.setBackground(context.getDrawable(R.drawable.ic_step_inactive));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_make_payment:
                try {
                    System.out.println("Delivery options: " + preferences.getDeliveryDate() + preferences.getDeliverySlot());
                    if (preferences.getDeliveryAddressId() != null && !preferences.getDeliveryAddressId().isEmpty()) {
                        if ((preferences.getDeliveryDate() != null && !preferences.getDeliveryDate().isEmpty()) &&
                                (preferences.getDeliverySlot() != null && !preferences.getDeliverySlot().isEmpty())) {
                            if (preferences.getPaymentMode() != null && !preferences.getPaymentMode().isEmpty()) {
                                /*add order place api*/
                                if (isConnectingToInternet(context))
                                    callOrderPlaceAPI(showCircleProgressDialog(context, ""));
                                else
                                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
//                                redirectToOrderDetails(v);
                            } else errorToast(context, "Select payment");
                        } else errorToast(context, "select delivery options");
                    } else errorToast(context, "Please select address");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rb_cod:
                PaymentModeId = orderSummaryViewModel.arrayPaymentListMutableLiveData.getValue().get(0).paymentModeId;
                preferences.setPaymentMode(PaymentModeId);
                summaryRadioAction(true);
                break;
            case R.id.rb_online:
                PaymentModeId = orderSummaryViewModel.arrayPaymentListMutableLiveData.getValue().get(1).paymentModeId;
                preferences.setPaymentMode(PaymentModeId);
                summaryRadioAction(true);
                break;
            case R.id.rb_wallet_balance:
                PaymentModeId = orderSummaryViewModel.arrayPaymentListMutableLiveData.getValue().get(2).paymentModeId;
                preferences.setPaymentMode(PaymentModeId);
                summaryRadioAction(false);
                break;
            case R.id.iv_address_option:
                openAddressOptionsDialog();
                break;
            case R.id.tbn_get_code:
                openPromoOptionsDialog();
                break;
            case R.id.btn_remove:
                if (fragmentOrderSummaryBinding.etCouponCode.getText().toString().isEmpty()) {
                    fragmentOrderSummaryBinding.etCouponCode.setError("please enter code");
                } else callApplyCouponAPI(showCircleProgressDialog(context, ""));
                break;
            case R.id.iv_edit_date:
                openDeliveryOptionsDialog();
                break;

        }
    }

    private void summaryRadioAction(boolean group) {
        if (group) {
            if (fragmentOrderSummaryBinding.rbCod.isChecked() || fragmentOrderSummaryBinding.rbOnline.isChecked()) {
                fragmentOrderSummaryBinding.rbWalletBalance.setChecked(false);
            }
        } else {
            if (fragmentOrderSummaryBinding.rbWalletBalance.isChecked()) {
                fragmentOrderSummaryBinding.rbCod.setChecked(false);
                fragmentOrderSummaryBinding.rbOnline.setChecked(false);
            }
        }

    }

    private void redirectToOrderDetails() {
        if (isConnectingToInternet(context)) {
            NavHostFragment.findNavController(OrderSummaryFragment.this).navigate(R.id.action_nav_order_summary_to_orderDetailsFragment);
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    public void openAddressOptionsDialog() {
        addressDialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogAnimationUp));
        addressDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        addressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        addressDialog.setContentView(R.layout.dialog_order_summary);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(addressDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        addressDialog.getWindow().setAttributes(lp);
        addressDialog.getWindow().setGravity(Gravity.BOTTOM);
        ImageView closeImg = addressDialog.findViewById(R.id.close_btn);
        RecyclerView rvAddress = addressDialog.findViewById(R.id.rv_address);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvAddress.setLayoutManager(layoutManager);
        rvAddress.setAdapter(addressDialogAdapter);
        closeImg.setOnClickListener(v -> {
            addressDialog.dismiss();
        });

        addressDialog.show();
    }

    public void openPromoOptionsDialog() {
        promoCodeDialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogAnimationUp));
        promoCodeDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        promoCodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        promoCodeDialog.setContentView(R.layout.dialog_order_summary);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(promoCodeDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        promoCodeDialog.getWindow().setAttributes(lp);
        promoCodeDialog.getWindow().setGravity(Gravity.BOTTOM);
        ImageView closeImg = promoCodeDialog.findViewById(R.id.close_btn);
        CustomTextView tvtTitle = promoCodeDialog.findViewById(R.id.tv_title);
        tvtTitle.setText(R.string.promo_code);
        RecyclerView rvPromoCode = promoCodeDialog.findViewById(R.id.rv_address);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvPromoCode.setLayoutManager(layoutManager);
        rvPromoCode.setAdapter(codeDialogAdapter);
        closeImg.setOnClickListener(v -> {
            promoCodeDialog.dismiss();
        });
        codeDialogAdapter.setOnPromoCodeListener(coupons1 -> {
            CouponId = coupons1.getId();
            fragmentOrderSummaryBinding.etCouponCode.setText(coupons1.getCouponCode());
            promoCodeDialog.dismiss();
//            callApplyCouponAPI(showCircleProgressDialog(context,""));
        });
        promoCodeDialog.show();
    }

    public void openDeliveryOptionsDialog() {
        deliveryDialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogAnimationUp));
        deliveryDialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        deliveryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        deliveryDialog.setContentView(R.layout.dialog_delivery_options);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(deliveryDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        deliveryDialog.getWindow().setAttributes(lp);
        deliveryOptionsBinding = DialogDeliveryOptionsBinding.inflate(LayoutInflater.from(context));
        deliveryDialog.getWindow().setGravity(Gravity.BOTTOM);
        ImageView closeImg = deliveryDialog.findViewById(R.id.close_btn);
        CustomTextView tvtTitle = deliveryDialog.findViewById(R.id.tv_title);
        CustomTextView tvDate = deliveryDialog.findViewById(R.id.tv_date_of_delivery);
        tvtTitle.setText(R.string.exp_date);
        RecyclerView rvDelivery = deliveryDialog.findViewById(R.id.rv_address);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rvDelivery.setLayoutManager(layoutManager);
        rvDelivery.setAdapter(deliveryDialogAdapter);
        closeImg.setOnClickListener(v -> {
            deliveryDialog.dismiss();
        });
        tvDate.setText(orderSummaryViewModel.deliveryDateMutable.getValue());
        preferences.setDeliveryDate(orderSummaryViewModel.deliveryDateMutable.getValue().trim());
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar(tvDate);
            }
        });
        deliveryDialog.show();
    }

    private void showCalendar(CustomTextView tvDate) {
        //showing date picker dialog
        DatePickerDialog dpd;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        //Set next date
        c.add(Calendar.DAY_OF_MONTH, 1);
        System.out.println("mYear :" + mYear + ", mMonth " + mMonth + ", mDay " + mDay);
        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                String txtDisplayDate = "";
                try {
                    txtDisplayDate = formatDate(selectedDate, "yyyy-MM-dd", "dd-MMM-yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println("selectedDate :" + selectedDate);
                    System.out.println("txtDisplayDate :" + txtDisplayDate);
                    if (tvDate != null) {
                        tvDate.setText(txtDisplayDate);
                        orderSummaryViewModel.deliveryDateMutable.setValue(txtDisplayDate);
                        preferences.setDeliveryDate(txtDisplayDate);
                        callDeliverySlotByDateAPI(showCircleProgressDialog(context, ""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        dpd = new DatePickerDialog(requireActivity(), mDateListener, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(c.getTimeInMillis());
        dpd.show();
    }

    /*Call slots by Date*/
    private void callDeliverySlotByDateAPI(ProgressDialog progressDialog) {
        Observer<OrderSummaryResponse> deliverySlotResponseObserver = delverySlotResponse -> {
            if (delverySlotResponse != null) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    switch (delverySlotResponse.status) {
                        case STATUS_CODE_200://Record Create/Update Successfully
                            orderSummaryViewModel.arrayDeliveryListMutableLiveData.setValue(delverySlotResponse.delivery);
                            break;
                        case STATUS_CODE_403://Validation Errors
                        case STATUS_CODE_400://Validation Errors
                        case STATUS_CODE_404://Validation Errors
                            //show no data msg here
                            warningToast(context, delverySlotResponse.getMessage());
                            break;
                        case STATUS_CODE_401://Unauthorized user
                            goToAskSignInSignUpScreen(delverySlotResponse.getMessage(), context);
                            break;
                        case STATUS_CODE_405://Method Not Allowed
                            infoToast(context, delverySlotResponse.getMessage());
                            break;
                    }
                }
            }
        };
        orderSummaryViewModel.deliverySlotResponseLiveData(progressDialog, deliverySlotParam(),
                OrderSummaryFragment.this)
                .observe(getViewLifecycleOwner(), deliverySlotResponseObserver);
    }

    private HashMap<String, String> deliverySlotParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put(DELIVERY_DATE, preferences.getDeliveryDate());
        return map;
    }

    @Override
    public void OnAddressClick(AddressesResponse.Address address) {
        addressDialog.dismiss();
        preferences.setDeliveryAddress(address.getFullAddress());
        fragmentOrderSummaryBinding.tvDeliveryAddress.setText(address.getFullAddress());
        orderSummaryViewModel.deliveryAddressMutable.setValue(address.getFullAddress());
        preferences.setDeliveryAddressId(address.getAddressPrimaryId());
        checkOrderSummaryStatus();
    }

    @Override
    public void OnSlotClick(DeliverySlot deliverySlot) {
        if (deliverySlot != null)
            preferences.setDeliverySlot(deliverySlot.slotId);
        orderSummaryViewModel.deliverySlotMutable.setValue(deliverySlot.slotTime);
        fragmentOrderSummaryBinding.tvTime.setText(deliverySlot.slotTime);
        stepOrder = "yes";
        deliveryDialog.dismiss();
        checkOrderSummaryStatus();
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), OrderSummaryFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        callOrderSummaryAPI(showCircleProgressDialog(context, ""));
                        break;
                    case 1:
                        callApplyCouponAPI(showCircleProgressDialog(context, ""));
                        break;
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}