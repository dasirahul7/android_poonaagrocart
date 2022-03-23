package com.poona.agrocart.ui.nav_wallet;

import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.databinding.FragmentWalletTransactionBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.nav_my_basket.BasketOrdersAdapter;
import com.poona.agrocart.ui.nav_my_basket.model.BasketOrder;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.CustomEditText;
import com.poona.agrocart.widgets.CustomTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WalletTransactionFragment extends BaseFragment implements View.OnClickListener {
    private final boolean isWallet = true;
    private final String[] status = {"Paid", "Refund", "Added"};
    long fromTime = 0;
    private FragmentWalletTransactionBinding fragmentWalletTransactionBinding;
    private WalletTransactionViewModel walletTransactionViewModel;
    private RecyclerView rvTransactions;
    private LinearLayoutManager linearLayoutManager;
    private BasketOrdersAdapter basketOrdersAdapter;
    private ArrayList<BasketOrder> transactionsArrayList;
    private Calendar calendarFrom, calendarTo;
    private DatePickerDialog datePickerDialog;
    private boolean onCreate, onResume;
    private String walletAddAmount ="";

    @Override
    public void onStop() {
        super.onStop();
        initTitleBar(getString(R.string.wallet_and_transaction));
    }

    @Override
    public void onPause() {
        super.onPause();
        initTitleBar(getString(R.string.wallet_and_transaction));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentWalletTransactionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet_transaction, container, false);
        fragmentWalletTransactionBinding.setLifecycleOwner(this);
        walletTransactionViewModel = new ViewModelProvider(this).get(WalletTransactionViewModel.class);
        final View view = fragmentWalletTransactionBinding.getRoot();
        onCreate = true;
        initView();
        setRvAdapter(view);

        return view;
    }

    private void initView() {
        fragmentWalletTransactionBinding.tvFromDate.setOnClickListener(this);
        fragmentWalletTransactionBinding.tvToDate.setOnClickListener(this);
        fragmentWalletTransactionBinding.btnAdd.setOnClickListener(this);

        initCalendarVars();

        initGreenTitleBar(getString(R.string.wallet_and_transaction));
        rvTransactions = fragmentWalletTransactionBinding.rvTransactions;
        setupSpinner();
    }

    private void initCalendarVars() {
        DatePickerDialog datePickerDialog = null;

        calendarTo = Calendar.getInstance();
        calendarFrom = Calendar.getInstance();

        Calendar mcurrentDate = Calendar.getInstance();
        /*mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);*/

    }

    private void setRvAdapter(View view) {
        transactionsArrayList = new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvTransactions.setHasFixedSize(true);
        rvTransactions.setLayoutManager(linearLayoutManager);

       // basketOrdersAdapter = new BasketOrdersAdapter(transactionsArrayList, view, isWallet);
        rvTransactions.setAdapter(basketOrdersAdapter);
    }

    private void prepareListingData() {
        for (int i = 0; i < 2; i++) {
            BasketOrder basketOrder = new BasketOrder();
            basketOrder.setOrderId(getString(R.string._paac002));
            basketOrder.setName(getString(R.string.diet_basket));
            basketOrder.setPrice(getString(R.string.rs_200_x_4));
            basketOrder.setPaymentMode(getString(R.string.qr));
            basketOrder.setTransactionId(getString(R.string._1200283e));
            basketOrder.setDate(getString(R.string._12_september_2021));
            transactionsArrayList.add(basketOrder);
        }
    }

    private void setupSpinner() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions, status);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        fragmentWalletTransactionBinding.spinnerPaid.setAdapter(arrayAdapter);

        /*fragmentWalletTransactionBinding.spinnerPaid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_from_date:
                showFromCalendar();
                break;
            case R.id.tv_to_date:
                showToCalendar();
                break;
            case R.id.btn_add:
                walletDialog();
                break;
        }
    }

    private void showToCalendar() {
        datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String txtDisplayDate = null;
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                try {
                    txtDisplayDate = formatDate(selectedDate, "yyyy-MM-dd", "dd MMM yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fragmentWalletTransactionBinding.tvToDate.setText(txtDisplayDate);
                calendarTo.set(year, month, dayOfMonth);
            }
        },
                calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(calendarTo.getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(fromTime);
        datePickerDialog.show();
    }

    public void showFromCalendar() {
        datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String txtDisplayDate = null;
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                try {
                    txtDisplayDate = formatDate(selectedDate, "yyyy-MM-dd", "dd MMM yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fragmentWalletTransactionBinding.tvFromDate.setText(txtDisplayDate);
                fragmentWalletTransactionBinding.tvToDate.setEnabled(true);

                fromTime = getTimeInMillies(txtDisplayDate);

                calendarFrom.set(year, month, dayOfMonth);
            }
        },
                calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), calendarFrom.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(calendarFrom.getTimeInMillis());
        datePickerDialog.show();
    }

    private long getTimeInMillies(String txtDisplayDate) {
        long timeInLong = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date mDate = sdf.parse(txtDisplayDate);
            timeInLong = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInLong;
    }

    public void walletDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_coupon_terms);
        ImageView closeImg = dialog.findViewById(R.id.close_btn);
        LinearLayout walletDialog = dialog.findViewById(R.id.wallet_dialog);
        CustomTextView tvContent = dialog.findViewById(R.id.tv_content);
        CustomEditText etAmount = dialog.findViewById(R.id.et_amount);
        CustomTextView tvTitle = dialog.findViewById(R.id.dialog_title);
        CustomButton btnPay = dialog.findViewById(R.id.btn_pay);
        tvTitle.setText(R.string.entr_amount);
        walletDialog.setVisibility(View.VISIBLE);
        tvContent.setVisibility(View.INVISIBLE);
        closeImg.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();

        /*pay for wallet*/
        btnPay.setOnClickListener(view -> {
            etAmount.clearFocus();
            if (!TextUtils.isEmpty(etAmount.getText())){
                walletAddAmount = etAmount.getText().toString().trim();
                ((HomeActivity)context).startPayment();
            }
            else{
                etAmount.setError("enter amount");
                etAmount.requestFocus();
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        onResume = true;
        if (preferences.getPaymentReference()!=null || !preferences.getPaymentReference().equalsIgnoreCase(""))
            infoToast(context,"Payment ref is "+preferences.getPaymentReference());
        if (preferences.getPaymentStatus()){
            fragmentWalletTransactionBinding.mainLayout.setVisibility(View.GONE);
            callAddToWalletApi(showCircleProgressDialog(context,""));
        }
        else if (!onCreate)goToAskAndDismiss("Payment Failed",context);
    }

    private void callAddToWalletApi(ProgressDialog progressDialog) {
        Observer<BaseResponse> baseResponseObserver = response -> {
            if (response!=null){
                if (progressDialog!=null){
                    progressDialog.dismiss();
                    switch (response.getStatus()) {
                        case STATUS_CODE_200://Record Create/Update Successfully
                            successToast(context, response.getMessage());
                            preferences.setPaymentStatus(false);
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
            }
        };
        walletTransactionViewModel.addToWalletApiResponse(progressDialog,addWalletParams(),WalletTransactionFragment.this)
                .observe(getViewLifecycleOwner(),baseResponseObserver);
    }

    private HashMap<String, String> addWalletParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.WALLET_AMOUNT, walletAddAmount);
        map.put(AppConstants.PAYMENT_REFERENCE_ID, preferences.getPaymentReference());
        return map;
    }
}