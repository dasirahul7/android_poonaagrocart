package com.poona.agrocart.ui.ticket_details;

import static com.poona.agrocart.app.AppConstants.MESSAGE;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.TICKET_ID;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.help_center_response.SendMessageResponse;
import com.poona.agrocart.data.network.responses.help_center_response.recieveMessage.AllChat;
import com.poona.agrocart.data.network.responses.help_center_response.recieveMessage.RecieveMessageResponse;
import com.poona.agrocart.data.network.responses.help_center_response.recieveMessage.UserTicketsDetail;
import com.poona.agrocart.databinding.FragmentTicketDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.widgets.CustomEditText;
import com.poona.agrocart.widgets.CustomTextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TicketDetailFragment extends BaseFragment implements NetworkExceptionListener {
    private static final String TAG = TicketDetailFragment.class.getSimpleName();
    private FragmentTicketDetailBinding fragmentTicketDetailBinding;
    private TicketDetailsViewModel ticketDetailsViewModel;
    private RecyclerView rvTicketOrders;
    private LinearLayoutManager linearLayoutManager;
    private TicketCommentsAdapter ticketCommentsAdapter;
    private ArrayList<AllChat> allChatList = new ArrayList<>();
    private final List<UserTicketsDetail> userTicketsDetails = new ArrayList<>();
    private View view;
    private CustomEditText tvMessage;
    private CustomTextView tvDate;
    private ImageView ivSendMessage;
    private String strTicketId = "";
    private String strMessage = "";
    private float scale;
    private View navHostFragment;
    private ViewGroup.MarginLayoutParams navHostMargins;


    @Override
    public void onStart() {
        super.onStart();
        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.GONE);
        setBottomMarginInDps(0);
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentTicketDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ticket_detail, container, false);
        fragmentTicketDetailBinding.setLifecycleOwner(this);
        view = fragmentTicketDetailBinding.getRoot();
        ticketDetailsViewModel = new ViewModelProvider(this).get(TicketDetailsViewModel.class);
        fragmentTicketDetailBinding.setTicket(ticketDetailsViewModel);

        fragmentTicketDetailBinding.clMainLayout.setVisibility(View.GONE);

        if (getArguments() != null) {
            strTicketId = getArguments().getString(TICKET_ID);
        }
        initTitleWithBackBtn(getString(R.string.menu_help_center));
        initView();
        setRvAdapter();
        onClick();

        scale = getResources().getDisplayMetrics().density;

        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.GONE);

        navHostFragment = requireActivity().findViewById(R.id.nav_host_fragment_content_home);
        navHostMargins = (ViewGroup.MarginLayoutParams) navHostFragment.getLayoutParams();
        navHostMargins.bottomMargin = 0;


        return view;
    }


    private void setBottomMarginInDps(int i) {
        int dpAsPixels = (int) (i * scale + 0.5f);
        navHostMargins.bottomMargin = dpAsPixels;
    }

    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.bottom_menu_card).setVisibility(View.GONE);
        setBottomMarginInDps(0);

        if (isConnectingToInternet(context)) {
            setRvAdapter();
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

    }

    private void initView() {
        rvTicketOrders = fragmentTicketDetailBinding.rvTicketComments;
        tvMessage = fragmentTicketDetailBinding.etChatMessage;
        ivSendMessage = fragmentTicketDetailBinding.ivSendMessage;
        tvDate = fragmentTicketDetailBinding.tvDate;
    }


    private void onClick() {


        ivSendMessage.setOnClickListener(view1 -> {
            strMessage = ticketDetailsViewModel.etMessage.getValue();
            if (isConnectingToInternet(context)) {
                if (!strMessage.equalsIgnoreCase(""))
                    callSendMessageApi(showCircleProgressDialog(context, ""));
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        });

        tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTicketDetailBinding.nvMainScroll.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        View lastChild = fragmentTicketDetailBinding.nvMainScroll.getChildAt(fragmentTicketDetailBinding.nvMainScroll.getChildCount() - 1);
                        int bottom = lastChild.getBottom() + fragmentTicketDetailBinding.nvMainScroll.getPaddingBottom();
                        int sy = fragmentTicketDetailBinding.nvMainScroll.getScrollY();
                        int sh = fragmentTicketDetailBinding.nvMainScroll.getHeight();
                        int delta = bottom - (sy + sh);
                        fragmentTicketDetailBinding.nvMainScroll.smoothScrollBy(0, delta);
                    }
                }, 100);
            }
        });
    }

    private void setRvAdapter() {

        allChatList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);

        rvTicketOrders.setHasFixedSize(true);

        rvTicketOrders.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(false);
        //initializing our adapter
        ticketCommentsAdapter = new TicketCommentsAdapter(allChatList, this, context);

        //Adding adapter to recyclerview
        rvTicketOrders.setAdapter(ticketCommentsAdapter);

        if (isConnectingToInternet(context)) {
            callReceiveMessageApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }


    }


    private void callReceiveMessageApi(ProgressDialog progressDialog) {

        @SuppressLint("NotifyDataSetChanged") Observer<RecieveMessageResponse> recieveMessageResponseObserver = recieveMessageResponse -> {
            fragmentTicketDetailBinding.clMainLayout.setVisibility(View.VISIBLE);
            if (recieveMessageResponse != null) {

                Log.e("Receive Message Api Response", new Gson().toJson(recieveMessageResponse));

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (recieveMessageResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        userTicketsDetails.clear();
                        allChatList.clear();

                        if (recieveMessageResponse.getData().getUserTicketsDetails() != null &&
                                recieveMessageResponse.getData().getUserTicketsDetails().size() > 0) {
                            userTicketsDetails.addAll(recieveMessageResponse.getData().getUserTicketsDetails());
                            ticketDetailsViewModel.ticketId.setValue(userTicketsDetails.get(0).getTicketNo());
                            ticketDetailsViewModel.ticketDate.setValue(userTicketsDetails.get(0).getCreatedOn());
                            ticketDetailsViewModel.subject.setValue(userTicketsDetails.get(0).getSubject());
                            ticketDetailsViewModel.remark.setValue(userTicketsDetails.get(0).getRemark());

                            if (userTicketsDetails.get(0).getStatus().equalsIgnoreCase("Pending")) {
                                fragmentTicketDetailBinding.tvTicketStatus.setTextColor(ContextCompat.getColor(context, R.color.color_pending));
                                ticketDetailsViewModel.status.setValue(userTicketsDetails.get(0).getStatus());
                            } else if (userTicketsDetails.get(0).getStatus().equalsIgnoreCase("Ongoing")) {
                                fragmentTicketDetailBinding.tvTicketStatus.setTextColor(ContextCompat.getColor(context, R.color.color_ongoing));
                                ticketDetailsViewModel.status.setValue(userTicketsDetails.get(0).getStatus());

                            }

                            String selectedDate = userTicketsDetails.get(0).getCreatedOn();

                            String txtDisplayDate = "";
                            try {
                                txtDisplayDate = formatDate(selectedDate, "yyyy-mm-dd hh:mm:ss", "MMM dd, yyyy hh:mm aa");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            ticketDetailsViewModel.ticketDate.setValue(txtDisplayDate);


                        }

                        if (recieveMessageResponse.getData().getAllChats() != null &&
                                recieveMessageResponse.getData().getAllChats().size() > 0) {
                            allChatList.addAll(recieveMessageResponse.getData().getAllChats());
                            ticketCommentsAdapter.notifyDataSetChanged();
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, recieveMessageResponse.getMsg());
                        break;
                    case STATUS_CODE_404://Record not Found
                        errorToast(context, recieveMessageResponse.getMsg());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, recieveMessageResponse.getMsg());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

        };
        ticketDetailsViewModel.getRecieveMessage(progressDialog, context, RecieveMessageInputParameter(), TicketDetailFragment.this)
                .observe(getViewLifecycleOwner(), recieveMessageResponseObserver);
    }

    private HashMap<String, String> RecieveMessageInputParameter() {
        HashMap<String, String> map = new HashMap<>();
        map.put(TICKET_ID, strTicketId);
        return map;
    }


    private void callSendMessageApi(ProgressDialog progressDialog) {
        // print user input parameters
        for (Map.Entry<String, String> entry : SendMessageParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }
        Observer<SendMessageResponse> sendMessageResponseObserver = sendMessageResponse -> {
            if (sendMessageResponse != null) {
                Log.e("Send Message Api Response", new Gson().toJson(sendMessageResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (sendMessageResponse.getStatus()) {
                    case STATUS_CODE_200://success

                        if (sendMessageResponse.getData() != null) {
                            ticketDetailsViewModel.etMessage.setValue("");
                            successToast(context, sendMessageResponse.getMessage());
                            callReceiveMessageApi(showCircleProgressDialog(context, ""));
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, sendMessageResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        errorToast(context, sendMessageResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, sendMessageResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };

        ticketDetailsViewModel.sendMessageResponse(progressDialog, context, SendMessageParameters(), TicketDetailFragment.this)
                .observe(getViewLifecycleOwner(), sendMessageResponseObserver);
    }

    private HashMap<String, String> SendMessageParameters() {
        HashMap<String, String> map = new HashMap<>();

        map.put(TICKET_ID, strTicketId);
        map.put(MESSAGE, ticketDetailsViewModel.etMessage.getValue());

        return map;
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), TicketDetailFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if (from == 0) {
                    callReceiveMessageApi(showCircleProgressDialog(context, ""));
                } else if (from == 1) {
                    callSendMessageApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}