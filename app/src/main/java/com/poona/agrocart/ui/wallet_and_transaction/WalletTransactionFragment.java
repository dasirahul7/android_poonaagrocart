package com.poona.agrocart.ui.wallet_and_transaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentWalletTransactionBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.my_basket.BasketOrdersAdapter;
import com.poona.agrocart.ui.my_basket.model.BasketOrder;
import com.poona.agrocart.widgets.CustomTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WalletTransactionFragment extends BaseFragment implements View.OnClickListener
{
    private FragmentWalletTransactionBinding fragmentWalletTransactionBinding;
    private RecyclerView rvTransactions;
    private LinearLayoutManager linearLayoutManager;
    private BasketOrdersAdapter basketOrdersAdapter;
    private ArrayList<BasketOrder> transactionsArrayList;
    private final boolean isWallet = true;
    private final String[] status = {"Paid", "Refund", "Added"};
    private Calendar calendarFrom,calendarTo;
    long fromTime = 0;
    private DatePickerDialog datePickerDialog;

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
        final View view = fragmentWalletTransactionBinding.getRoot();

        initView();
        setRvAdapter(view);

        return view;
    }

    private void initView()
    {
        fragmentWalletTransactionBinding.tvFromDate.setOnClickListener(this);
        fragmentWalletTransactionBinding.tvToDate.setOnClickListener(this);
        fragmentWalletTransactionBinding.btnAdd.setOnClickListener(this);

        initCalendarVars();

        initGreenTitleBar(getString(R.string.wallet_and_transaction));
        rvTransactions = fragmentWalletTransactionBinding.rvTransactions;
        setupSpinner();
    }

    private void initCalendarVars()
    {
        DatePickerDialog datePickerDialog = null;

        calendarTo = Calendar.getInstance();
        calendarFrom = Calendar.getInstance();

        Calendar mcurrentDate = Calendar.getInstance();
        /*mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);*/

    }

    private void setRvAdapter(View view)
    {
        transactionsArrayList = new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvTransactions.setHasFixedSize(true);
        rvTransactions.setLayoutManager(linearLayoutManager);

        basketOrdersAdapter = new BasketOrdersAdapter(transactionsArrayList, view, isWallet);
        rvTransactions.setAdapter(basketOrdersAdapter);
    }

    private void prepareListingData()
    {
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

    private void setupSpinner()
    {
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
    public void onClick(View v)
    {
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

    private void showToCalendar()
    {
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

    public void showFromCalendar()
    {
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

                    fromTime=getTimeInMillies(txtDisplayDate);

                calendarFrom.set(year, month, dayOfMonth);
            }
        },
                calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), calendarFrom.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(calendarFrom.getTimeInMillis());
        datePickerDialog.show();
    }

    private long getTimeInMillies(String txtDisplayDate)
    {
        long timeInLong=0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        try
        {
            Date mDate = sdf.parse(txtDisplayDate);
            timeInLong = mDate.getTime();
        }
        catch (ParseException e)
        {
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
        CustomTextView tvTitle = dialog.findViewById(R.id.dialog_title);
            tvTitle.setText(R.string.entr_amount);
            walletDialog.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.GONE);
        closeImg.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


}