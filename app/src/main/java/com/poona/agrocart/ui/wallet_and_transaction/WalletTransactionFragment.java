package com.poona.agrocart.ui.wallet_and_transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentWalletTransactionBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.my_basket.BasketOrdersAdapter;
import com.poona.agrocart.ui.my_basket.model.BasketOrder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WalletTransactionFragment extends BaseFragment implements View.OnClickListener {
    private FragmentWalletTransactionBinding fragmentWalletTransactionBinding;
    private RecyclerView rvTransactions;
    private LinearLayoutManager linearLayoutManager;
    private BasketOrdersAdapter basketOrdersAdapter;
    private ArrayList<BasketOrder> transactionsArrayList;
    private boolean isWallet = true;
    private String[] status = {"Paid", "Refund", "Added"};
    private Calendar calendar;
    private int mYear, mMonth, mDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentWalletTransactionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet_transaction, container, false);
        fragmentWalletTransactionBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentWalletTransactionBinding).getRoot();

        initView();
        setRvAdapter(view);

        return view;
    }

    private void initView() {
        fragmentWalletTransactionBinding.tvFromDate.setOnClickListener(this);
        fragmentWalletTransactionBinding.tvToDate.setOnClickListener(this);

        initGreenTitleBar(getString(R.string.wallet_and_transaction));
        rvTransactions = fragmentWalletTransactionBinding.rvTransactions;
        setupSpinner();
    }

    private void setRvAdapter(View view) {
        transactionsArrayList = new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvTransactions.setHasFixedSize(true);
        rvTransactions.setLayoutManager(linearLayoutManager);

        basketOrdersAdapter = new BasketOrdersAdapter(transactionsArrayList, view, isWallet);
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
                showCalendar("FROM DATE");
                break;
            case R.id.tv_to_date:
                showCalendar("TO DATE");
                break;
        }
    }

    public void showCalendar(String fromOrTo) {
        //showing date picker dialog
        DatePickerDialog dpd;
        calendar = Calendar.getInstance();
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(requireContext(), R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (fromOrTo.equals("FROM DATE")) {
                    String txtDisplayDate = null;
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    try {
                        txtDisplayDate = formatDate(selectedDate, "yyyy-MM-dd", "dd MMM yyyy");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    fragmentWalletTransactionBinding.tvFromDate.setText(txtDisplayDate);
                } else {
                    String txtDisplayDate = null;
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    try {
                        txtDisplayDate = formatDate(selectedDate, "yyyy-MM-dd", "dd MMM yyyy");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    fragmentWalletTransactionBinding.tvToDate.setText(txtDisplayDate);
                }
                calendar.set(year, month, dayOfMonth);
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dpd.show();
    }
}