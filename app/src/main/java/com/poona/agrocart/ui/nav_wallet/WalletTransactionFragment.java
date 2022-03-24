package com.poona.agrocart.ui.nav_wallet;

import static com.poona.agrocart.app.AppConstants.IMAGE_DOC_BASE_URL;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_402;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.payment.RazorPayCredentialResponse;
import com.poona.agrocart.data.network.responses.walletTransaction.TransactionTypeResponse;
import com.poona.agrocart.data.network.responses.walletTransaction.WalletTransaction;
import com.poona.agrocart.data.network.responses.walletTransaction.WalletTransactionListResponse;
import com.poona.agrocart.databinding.FragmentWalletTransactionBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.HomeFragment;
import com.poona.agrocart.ui.nav_wallet.adapter.TransactionAdapter;
import com.poona.agrocart.ui.nav_wallet.adapter.TypeAdaptor;
import com.poona.agrocart.ui.order_summary.OrderSummaryFragment;
import com.poona.agrocart.ui.order_summary.OrderSummaryViewModel;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.CustomEditText;
import com.poona.agrocart.widgets.CustomTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WalletTransactionFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener ,TransactionAdapter.OnInvoiceClickListener {
    private final boolean isWallet = true;
    private final String[] status = {"Paid", "Refund", "Added"};
    long fromTime = 0;
    private FragmentWalletTransactionBinding fragmentWalletTransactionBinding;
    private WalletTransactionViewModel walletTransactionViewModel;
    private RecyclerView rvTransactions;
    private LinearLayoutManager linearLayoutManager;
    private TransactionAdapter transactionAdapter;
    private Calendar calendarFrom, calendarTo;
    private DatePickerDialog datePickerDialog;
    private boolean onCreate, onResume;
    private String walletAddAmount ="";
    private String TAG = WalletTransactionFragment.class.getSimpleName();

    @Override
    public void onStop() {
        super.onStop();
        try {
            initTitleBar(getString(R.string.wallet_and_transaction)); // crashed after logout
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            initTitleBar(getString(R.string.wallet_and_transaction));// crashed after logout
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentWalletTransactionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet_transaction, container, false);
        fragmentWalletTransactionBinding.setLifecycleOwner(this);
        walletTransactionViewModel = new ViewModelProvider(this).get(WalletTransactionViewModel.class);
        final View view = fragmentWalletTransactionBinding.getRoot();
        onCreate = true;
        initView();
        return view;
    }

    private void initView() {
        Calendar todayCal;
        todayCal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String today = dateFormat.format(todayCal.getTime());
        System.out.println("today "+today);
        todayCal.add(Calendar.DAY_OF_MONTH,1);
        String tomorrow =  dateFormat.format(todayCal.getTime());
        System.out.println("tomorrow "+tomorrow);
        preferences.setPaymentReferenceId("");
        walletTransactionViewModel.walletFromDateMutable.setValue(today);
        walletTransactionViewModel.walletToDateMutable.setValue(tomorrow);
        fragmentWalletTransactionBinding.tvFromDate.setOnClickListener(this);
        fragmentWalletTransactionBinding.tvToDate.setOnClickListener(this);
        fragmentWalletTransactionBinding.btnAdd.setOnClickListener(this);
        if (isConnectingToInternet(context)){
            fragmentWalletTransactionBinding.mainLayout.setVisibility(View.GONE);
            callTransactionTypeListAPI(showCircleProgressDialog(context,""));
            callWalletTransactionListAPI(showCircleProgressDialog(context,""));
            CallPaymentCredentialApi(showCircleProgressDialog(context,""));
        }else showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        initCalendarVars();

        initGreenTitleBar(getString(R.string.wallet_and_transaction));
        rvTransactions = fragmentWalletTransactionBinding.rvTransactions;
//        setRvAdapter();
    }

    private void CallPaymentCredentialApi(ProgressDialog progressDialog) {
        Observer<RazorPayCredentialResponse> razorPayCredentialResponseObserver = razorPayResponse -> {
            if (razorPayResponse!=null){
                if (progressDialog!=null)
                    progressDialog.dismiss();
                switch (razorPayResponse.getStatus()){
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (razorPayResponse.getData().getKeyId()!=null
                                && !razorPayResponse.getData().getKeyId().equalsIgnoreCase("")){
                            preferences.setRazorCredentials(razorPayResponse.getData().getKeyId(),
                                    razorPayResponse.getData().getType(),
                                    razorPayResponse.getData().getCurrency());
                            Log.d("TAG", "CallPaymentCredentialApi: "+razorPayResponse.getData().getKeyId());
                            preferences.setPaymentReferenceId("");
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        //show no data msg here
                        warningToast(context, razorPayResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(razorPayResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, razorPayResponse.getMessage());
                        break;
                }

            }
        };
        OrderSummaryViewModel orderSummaryViewModel = new ViewModelProvider(this).get(OrderSummaryViewModel.class);
        orderSummaryViewModel.getRazorPayCredentialResponse(progressDialog, context)
                .observe(getViewLifecycleOwner(),razorPayCredentialResponseObserver);

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

    private void setRvAdapter() {
        /*Wallet Transaction list here */
        walletTransactionViewModel.walletTransactionMutableList.observe(getViewLifecycleOwner(),transactionTypes -> {
            if (transactionTypes.size()>0){
                linearLayoutManager = new LinearLayoutManager(requireContext());
                rvTransactions.setHasFixedSize(true);
                rvTransactions.setLayoutManager(linearLayoutManager);
                transactionAdapter = new TransactionAdapter(transactionTypes);
                rvTransactions.setAdapter(transactionAdapter);
            }
        });
    }


    /*set up type spinner*/
    private void setupSpinner() {
        walletTransactionViewModel.transactionTypeMutableList.observe(getViewLifecycleOwner(),transactionTypes -> {
            try {
                TypeAdaptor typeAdaptor = new TypeAdaptor(context, transactionTypes);
                fragmentWalletTransactionBinding.spinnerPaid.setAdapter(typeAdaptor);
            } catch (IndexOutOfBoundsException exception) {
                exception.printStackTrace();
            }
            fragmentWalletTransactionBinding.spinnerPaid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    walletTransactionViewModel.transactionTypeMutable.setValue(transactionTypes.get(position).getWalletTransactionTypeId());
                    //refresh after type changed
                    callWalletTransactionListAPI(showCircleProgressDialog(context,""));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        });

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
                    txtDisplayDate = formatDate(selectedDate, "yyyy-MM-dd", "dd-MMM-yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                fragmentWalletTransactionBinding.tvToDate.setText(txtDisplayDate);
                walletTransactionViewModel.walletToDateMutable.setValue(txtDisplayDate);
                // refresh after date changed
                callWalletTransactionListAPI(showCircleProgressDialog(context,""));
                calendarTo.set(year, month, dayOfMonth);
            }
        },
                calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void showFromCalendar() {
        datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String txtDisplayDate = null;
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                try {
                    txtDisplayDate = formatDate(selectedDate, "yyyy-MM-dd", "dd-MMM-yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                walletTransactionViewModel.walletFromDateMutable.setValue(txtDisplayDate);
                fragmentWalletTransactionBinding.tvToDate.setEnabled(true);

                fromTime = getTimeInMillies(txtDisplayDate);

                calendarFrom.set(year, month, dayOfMonth);
            }
        },
                calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), calendarFrom.get(Calendar.DAY_OF_MONTH)
        );
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
                preferences.setPaymentAmount(Integer.parseInt(walletAddAmount));
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
            Log.e(TAG, "onResume: Payment ref is "+preferences.getPaymentReference());
        if (preferences.getPaymentStatus()){
            fragmentWalletTransactionBinding.mainLayout.setVisibility(View.GONE);
            if (isConnectingToInternet(context))
                callPaymentToWalletAPI(showCircleProgressDialog(context,""));
            else showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
        else if (!onCreate)goToAskAndDismiss("Payment Failed",context);
    }

    /*Call Transaction type list API*/
    private void callTransactionTypeListAPI(ProgressDialog progressDialog){
        Observer<TransactionTypeResponse> transactionTypeResponseObserver = transactionTypeResponse -> {
            if (transactionTypeResponse!=null){
                if (progressDialog!=null)
                    progressDialog.dismiss();
                fragmentWalletTransactionBinding.mainLayout.setVisibility(View.VISIBLE);
                switch (transactionTypeResponse.getStatus()){
                    case STATUS_CODE_200://Record Create/Update Successfully
                        walletTransactionViewModel.transactionTypeMutableList.setValue(transactionTypeResponse.getTransactionTypeList());
                        walletTransactionViewModel.transactionTypeMutable.setValue(transactionTypeResponse.getTransactionTypeList().get(0).getWalletTransactionTypeId());
                        setupSpinner();
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        //show no data msg here
                        warningToast(context, transactionTypeResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                    case STATUS_CODE_402://Unauthorized user
                        goToAskSignInSignUpScreen(transactionTypeResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, transactionTypeResponse.getMessage());
                        break;
                }
            }
        };
        walletTransactionViewModel.getTransactionTypeResponseLiveData(progressDialog,WalletTransactionFragment.this)
                .observe(getViewLifecycleOwner(),transactionTypeResponseObserver);
    }

    /*Call Wallet Transaction list API */
    private void callWalletTransactionListAPI(ProgressDialog progressDialog){
        Observer<WalletTransactionListResponse> transactionListResponseObserver = walletTransResponse -> {
            if (walletTransResponse!=null){
                if (progressDialog!=null){
                    progressDialog.dismiss();
                    switch (walletTransResponse.getStatus()){
                        case STATUS_CODE_200://Record Create/Update Successfully
                            fragmentWalletTransactionBinding.mainLayout.setVisibility(View.VISIBLE);
                            //set wallet balance
                            if (walletTransResponse.getWalletBalance()!=null)
                                walletTransactionViewModel.walletBalanceMutable.setValue(walletTransResponse.getWalletBalance());
                            //
                            if (walletTransResponse.getWalletTransactionList()!=null&&walletTransResponse.getWalletTransactionList().size()>0){
                                fragmentWalletTransactionBinding.tvNoData.setVisibility(View.GONE);
                                fragmentWalletTransactionBinding.rvTransactions.setVisibility(View.VISIBLE);
                                walletTransactionViewModel.walletTransactionMutableList.setValue(walletTransResponse.getWalletTransactionList());
                                setRvAdapter();
                            }else {
                                fragmentWalletTransactionBinding.rvTransactions.setVisibility(View.INVISIBLE);
                                fragmentWalletTransactionBinding.tvNoData.setVisibility(View.VISIBLE);
                            }

                            fragmentWalletTransactionBinding.setWallet(walletTransactionViewModel);
                            fragmentWalletTransactionBinding.setVariable(BR.wallet,walletTransactionViewModel);
                            fragmentWalletTransactionBinding.executePendingBindings();

                            break;
                        case STATUS_CODE_400://Validation Errors
                        case STATUS_CODE_402://Validation Errors
                            goToAskAndDismiss(walletTransResponse.getMessage(), context);
                            break;
                        case STATUS_CODE_403://Validation Errors
                        case STATUS_CODE_404://Validation Errors
                            warningToast(context, walletTransResponse.getMessage());
                            break;
                        case STATUS_CODE_401://Unauthorized user
                            goToAskSignInSignUpScreen(walletTransResponse.getMessage(), context);
                            break;
                        case STATUS_CODE_405://Method Not Allowed
                            infoToast(context, walletTransResponse.getMessage());
                            break;
                    }
                }
            }
        };
        walletTransactionViewModel.getWalletTransactionListResponse(progressDialog, walletTransactionParams(),
                WalletTransactionFragment.this).observe(getViewLifecycleOwner(),transactionListResponseObserver);
    }

    /*Add payment to wallet API here*/
    private void callPaymentToWalletAPI(ProgressDialog progressDialog) {
        Observer<BaseResponse> baseResponseObserver = response -> {
            if (response!=null){
                if (progressDialog!=null){
                    progressDialog.dismiss();
                    switch (response.getStatus()) {
                        case STATUS_CODE_200://Record Create/Update Successfully
                            successToast(context, response.getMessage());
                            preferences.setPaymentStatus(false);
                            preferences.setPaymentReferenceId("");
                            callWalletTransactionListAPI(showCircleProgressDialog(context,""));
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
        walletTransactionViewModel.callPaymentToWalletApiResponse(progressDialog,addWalletParams(),WalletTransactionFragment.this)
                .observe(getViewLifecycleOwner(),baseResponseObserver);
    }


    /* wallet transaction params*/
    private HashMap<String, String> walletTransactionParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.TRANSACTION_TYPE_ID, walletTransactionViewModel.transactionTypeMutable.getValue());
        map.put(AppConstants.FROM_DATE, fragmentWalletTransactionBinding.tvFromDate.getText().toString());
        map.put(AppConstants.TO_DATE, fragmentWalletTransactionBinding.tvToDate.getText().toString());
        return map;
    }
    //    Add wallet amount params
    private HashMap<String, String> addWalletParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.WALLET_AMOUNT, walletAddAmount);
        map.put(AppConstants.PAYMENT_REFERENCE_ID, preferences.getPaymentReference());
        return map;
    }

    @Override
    public void onInvoiceClick(WalletTransaction transaction) {

        String strInvoiceDownload = transaction.getInvoiceFile();
        if(strInvoiceDownload != null){
            beginDownload(IMAGE_DOC_BASE_URL+strInvoiceDownload);
        }else{
            infoToast(context, "invoice not available for download");
        }
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), WalletTransactionFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        callTransactionTypeListAPI(showCircleProgressDialog(context,""));
                        break;
                    case 1:
                        callWalletTransactionListAPI(showCircleProgressDialog(context, ""));
                        break;
                    case 2:
                        callPaymentToWalletAPI(showCircleProgressDialog(context, ""));
                        break;
                }
            }
        }, context);

    }
}