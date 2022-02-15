package com.poona.agrocart.ui.nav_help_center;

import static com.poona.agrocart.app.AppConstants.ISSUE_ID;
import static com.poona.agrocart.app.AppConstants.LIMIT;
import static com.poona.agrocart.app.AppConstants.OFFSET;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.TICKET_REMARK;
import static com.poona.agrocart.app.AppConstants.TICKET_SUBJECT;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.help_center_response.CreateTicketResponse;
import com.poona.agrocart.data.network.reponses.help_center_response.TicketListResponse;
import com.poona.agrocart.data.network.reponses.help_center_response.TicketTypeResponse;
import com.poona.agrocart.databinding.FragmentHelpCenterBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_faq.FaQFragment;
import com.poona.agrocart.ui.nav_help_center.Adaptor.TicketTypeAdaptor;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.CustomEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpCenterFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener
{
    private FragmentHelpCenterBinding fragmentHelpCenterBinding;
    private HelpCenterViewModel helpCenterViewModel;
    private RecyclerView rvTickets;
    private LinearLayoutManager linearLayoutManager;
    private TicketsAdapter ticketsAdapter;
    private List<TicketListResponse.TicketList.UserTicket> ticketArrayList = new ArrayList<>();
    private List<TicketTypeResponse.TicketType> ticketTypeList = new ArrayList<>();
    private Spinner spinTicketType;
    private String ticketId = "", strTicketName = "";
    private CustomEditText etSubject, etDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentHelpCenterBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_help_center, container, false);
        fragmentHelpCenterBinding.setLifecycleOwner(this);
        helpCenterViewModel=new ViewModelProvider(this).get(HelpCenterViewModel.class);
        fragmentHelpCenterBinding.setHelpCenterViewModel(helpCenterViewModel);
        final View view = fragmentHelpCenterBinding.getRoot();

        initView();


        initTitleBar(getString(R.string.menu_help_center));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isConnectingToInternet(context)) {
            setRvAdapter();
        }else{
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private void initView()
    {
        rvTickets=fragmentHelpCenterBinding.rvTickets;
        fragmentHelpCenterBinding.btnCreateNewTicket.setOnClickListener(this);
        initTitleBar(getString(R.string.menu_addresses));
        fragmentHelpCenterBinding.btnCreateNewTicket.setOnClickListener(this);
    }

    private void setRvAdapter()
    {
        ticketArrayList=new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvTickets.setHasFixedSize(true);
        rvTickets.setLayoutManager(linearLayoutManager);

        callTicketListApi(showCircleProgressDialog(context, ""),"RecyclerView");
        ticketsAdapter = new TicketsAdapter(ticketArrayList,getContext());
        rvTickets.setAdapter(ticketsAdapter);

        //Pagination in scroll view
        setScrollListener();
    }

    /* Pagination adding to the recycler view */

    private int offset = 0;
    private final int limit = 10;
    private int visibleItemCount = 0;
    private int totalCount = 0;

    private void setScrollListener() {
        rvTickets.setNestedScrollingEnabled(true);
        NestedScrollView nestedScrollView= fragmentHelpCenterBinding.nvMain;

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(v.getChildAt(v.getChildCount() - 1) != null) {
                visibleItemCount = linearLayoutManager.getItemCount();

                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount != totalCount) {
                    callTicketListApi(showCircleProgressDialog(context, ""), "onScrolled");
                }
                else if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount == totalCount) {
                    infoToast(getActivity(), getString(R.string.no_more_records));
                }
            }
        });

    }

    /*Upload the Ticket list api*/
    private void callTicketListApi(ProgressDialog progressDialog,String fromFunction) {
        if (fromFunction.equals("onScrolled")) {
            offset = offset + 1;
        } else {
            offset = 0;
        }
        @SuppressLint("NotifyDataSetChanged")
        Observer<TicketListResponse> ticketListResponseObserver = ticketListResponse -> {
            if (ticketListResponse != null){
                Log.e(" Ticket list Api Response", new Gson().toJson(ticketListResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (ticketListResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        if (offset == 0)
                        ticketArrayList.clear();

                        totalCount = Integer.parseInt(ticketListResponse.getData().getCountTickets());
                        if(ticketListResponse.getData() != null){
                            ticketArrayList.addAll(ticketListResponse.getData().getUserTickets());
                            ticketsAdapter.notifyDataSetChanged();
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, ticketListResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        //llEmptyLayout.setVisibility(View.VISIBLE);
                        //llMainLayout.setVisibility(View.INVISIBLE);
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen();
                        errorToast(context, ticketListResponse.getMessage());
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, ticketListResponse.getMessage());
                        break;
                }
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }
        };

        helpCenterViewModel.getTicketListResponse(progressDialog, context,TicketListInputParameter(),
                HelpCenterFragment.this)
                .observe(getViewLifecycleOwner(), ticketListResponseObserver);
    }

    private HashMap<String , String> TicketListInputParameter() {
        HashMap<String , String> map = new HashMap<>();
        map.put(LIMIT, String.valueOf(limit));
        map.put(OFFSET, String.valueOf(offset));
        return map;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create_new_ticket:
               raiseNewTicketDialog();
                break;
        }
    }

    // Dialog for create ticket
    public void raiseNewTicketDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;
        dialog.setContentView(R.layout.dialog_new_ticket);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        /*Validation and Logical part on the component */

        ImageView closeImage = dialog.findViewById(R.id.close_btn);
        CustomButton submitButton = dialog.findViewById(R.id.btn_submit);
        etSubject = dialog.findViewById(R.id.et_subject);
        etDescription = dialog.findViewById(R.id.et_description);
        spinTicketType = dialog.findViewById(R.id.spin_ticket);

        if(isConnectingToInternet(context)){
            callTicketTypeApi(showCircleProgressDialog(context, ""));
        }else{
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }


        closeImage.setOnClickListener(view -> {
            dialog.dismiss();
        });

        submitButton.setOnClickListener(view -> {
            String strSubject = etSubject.getText().toString();
            String strDescription =etDescription.getText().toString();
            if(strTicketName.equalsIgnoreCase("Ticket Type")){
                warningToast(context, "Please select the Ticket Type");
            }else if( strSubject.equalsIgnoreCase("")){
                warningToast(context, "Subject Field Should not be empty");
            }else if(strDescription.equalsIgnoreCase("")){
                warningToast(context, "Remark Field Should not be empty");
            }else {

                if(isConnectingToInternet(context)){
                    callCreateTicketApi(showCircleProgressDialog(context,""));
                }else{
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }


                dialog.dismiss();
            }

        });

        dialog.show();
    }
    
    private void callCreateTicketApi (ProgressDialog progressDialog) {
        
        Observer<CreateTicketResponse> createTicketResponseObserver = createTicketResponse -> {
            if (createTicketResponse != null){
                etDescription.setText("");
                etSubject.setText("");
                Log.e("Create  tickets Api Response", new Gson().toJson(createTicketResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (createTicketResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        successToast(context,createTicketResponse.getMessage());
                        /*listing details */
                        callTicketListApi(showCircleProgressDialog(context, ""),"RecyclerView");
                        break;
                    case STATUS_CODE_400:
                        /* validation message */
                        warningToast(context, createTicketResponse.getMessage());
                        break;
                    case STATUS_CODE_404:
                        /*  No record found */
                        errorToast(context, createTicketResponse.getMessage());
                        break;
                    case STATUS_CODE_401:
                        /*  Unauthorized user */
                        errorToast(context, createTicketResponse.getMessage());
                        break;
                }
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }
        };
        helpCenterViewModel.getCreateTicketResponse(progressDialog, context, CreateTicketInputParameter(),HelpCenterFragment.this)
                .observe(getViewLifecycleOwner(), createTicketResponseObserver);
    }
    
    private HashMap<String, String> CreateTicketInputParameter() {
        HashMap<String , String> map = new HashMap<>();
        map.put(ISSUE_ID, ticketId);
        map.put(TICKET_SUBJECT, etSubject.getText().toString());
        map.put(TICKET_REMARK, etDescription.getText().toString());
        return  map;
    }

    private void bindingSpinner(List<TicketTypeResponse.TicketType> ticketTypes) {

        TicketTypeAdaptor ticketTypeAdaptor = new TicketTypeAdaptor(getContext(), ticketTypes);
        spinTicketType.setAdapter(ticketTypeAdaptor);

        spinTicketType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyBoard(requireActivity());
                ticketId = ticketTypes.get(i).getId();
                strTicketName = ticketTypes.get(i).getTicketType();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                hideKeyBoard(requireActivity());
            }
        });
    }

    private void callTicketTypeApi (ProgressDialog progressDialog){
        @SuppressLint("NotifyDataSetChanged")
        Observer<TicketTypeResponse> ticketTypeResponseObserver = ticketTypeResponse -> {
            if (ticketTypeResponse != null){
                Log.e("Ticket Type Api Response", new Gson().toJson(ticketTypeResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (ticketTypeResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        if (ticketTypeResponse.getData() != null &&
                                ticketTypeResponse.getData().size()>0){
                            ticketTypeList.clear();
                            ticketTypeList.add(new TicketTypeResponse.TicketType("0", "Select Type"));
                            ticketTypeList.addAll(ticketTypeResponse.getData());
                            bindingSpinner(ticketTypeList);
                        }else{
                            ticketTypeList.add(new TicketTypeResponse.TicketType("0", "Select Type"));
                            bindingSpinner(ticketTypeList);
                        }
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, ticketTypeResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, ticketTypeResponse.getMessage());
                        break;
                }
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }
        };

        helpCenterViewModel.getTicketTypeResponse(progressDialog, context, HelpCenterFragment.this)
                .observe(getViewLifecycleOwner(), ticketTypeResponseObserver);
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), HelpCenterFragment.this,() -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if(from == 0) {
                    callTicketTypeApi(showCircleProgressDialog(context, ""));
                }else if(from == 1){
                    callTicketListApi(showCircleProgressDialog(context,""), "RecyclerView");
                }else if(from == 2){
                    callCreateTicketApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}