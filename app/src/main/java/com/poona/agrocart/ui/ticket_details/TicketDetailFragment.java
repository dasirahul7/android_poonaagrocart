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
import com.poona.agrocart.data.network.reponses.help_center_response.SendMessageResponse;
import com.poona.agrocart.data.network.reponses.help_center_response.recieveMessage.AllChat;
import com.poona.agrocart.data.network.reponses.help_center_response.recieveMessage.RecieveMessageResponse;
import com.poona.agrocart.data.network.reponses.help_center_response.recieveMessage.UserTicketsDetail;
import com.poona.agrocart.databinding.FragmentTicketDetailBinding;
import com.poona.agrocart.ui.BaseFragment;

import com.poona.agrocart.widgets.CustomEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TicketDetailFragment extends BaseFragment
{
    private FragmentTicketDetailBinding fragmentTicketDetailBinding;
    private TicketDetailsViewModel  ticketDetailsViewModel;
    private RecyclerView rvTicketOrders;
    private LinearLayoutManager linearLayoutManager;
    private TicketCommentsAdapter ticketCommentsAdapter;
    private ArrayList<AllChat> allChatList = new ArrayList<>();
    private List<UserTicketsDetail> userTicketsDetails = new ArrayList<>();
    private View view;
    private static final String TAG = TicketDetailFragment.class.getSimpleName();
    private CustomEditText tvMessage;
    private ImageView ivSendMessage;
    private String strTicketId = "";



    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentTicketDetailBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_ticket_detail, container, false);
        fragmentTicketDetailBinding.setLifecycleOwner(this);
         view = fragmentTicketDetailBinding.getRoot();
        ticketDetailsViewModel=new ViewModelProvider(this).get(TicketDetailsViewModel.class);
        fragmentTicketDetailBinding.setTicket(ticketDetailsViewModel);

        if (getArguments() != null) {
            strTicketId = getArguments().getString(TICKET_ID);
        }
        initTitleWithBackBtn(getString(R.string.menu_help_center));


        /*Bundle bundle=this.getArguments();
        ticketDetailsViewModel.ticketId.setValue(bundle.getString(AppConstants.TICKET_ID));
        ticketDetailsViewModel.status.setValue(bundle.getString(AppConstants.STATUS));
        ticketDetailsViewModel.remark.setValue(bundle.getString(AppConstants.REMARK));
        ticketDetailsViewModel.ticketDate.setValue(bundle.getString(AppConstants.DATE));
        ticketDetailsViewModel.subject.setValue(bundle.getString(AppConstants.SUBJECT));
        if(ticketDetailsViewModel.status.equals(getString(R.string.pending)))
        {
            fragmentTicketDetailBinding.tvTicketStatus.setTextColor(Color.parseColor(context.getString(R.color.color_pending)));
        }
        else if(ticketDetailsViewModel.status.equals(getString(R.string.ongoing)))
        {
            fragmentTicketDetailBinding.tvTicketStatus.setTextColor(Color.parseColor(context.getString(R.color.color_ongoing)));
        }*/

        initView();
        setRvAdapter();
        onClick();
        return view;
    }



    private void initView()
    {
        rvTicketOrders=fragmentTicketDetailBinding.rvTicketComments;
        tvMessage=fragmentTicketDetailBinding.etMessage;
        ivSendMessage=fragmentTicketDetailBinding.ivSendMessage;
    }


    private void onClick() {
        ivSendMessage.setOnClickListener(view1 -> {
            if(isConnectingToInternet(context)){
                    callSendMessageApi(showCircleProgressDialog(context, ""));
            }else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        });
    }

    private void setRvAdapter()
    {
        allChatList=new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        rvTicketOrders.setHasFixedSize(true);
        rvTicketOrders.setLayoutManager(linearLayoutManager);

        //initializing our adapter
        ticketCommentsAdapter = new TicketCommentsAdapter(allChatList);

        //Adding adapter to recyclerview
        rvTicketOrders.setAdapter(ticketCommentsAdapter);
        callReceiveMessageApi(showCircleProgressDialog(context,""));

      /*  linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTicketOrders.setHasFixedSize(true);
        rvTicketOrders.setLayoutManager(linearLayoutManager);

        ticketCommentsAdapter = new TicketCommentsAdapter(allChatList,context);
        rvTicketOrders.setAdapter(ticketCommentsAdapter);*/

    }



    private  void callReceiveMessageApi(ProgressDialog progressDialog){

        @SuppressLint("NotifyDataSetChanged") Observer<RecieveMessageResponse> recieveMessageResponseObserver = recieveMessageResponse -> {
            fragmentTicketDetailBinding.clMainLayout.setVisibility(View.VISIBLE);
            if (recieveMessageResponse != null){

                Log.e("Receive Message Api Response", new Gson().toJson(recieveMessageResponse));

                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (recieveMessageResponse.getStatus()) {
                    case STATUS_CODE_200://success
                       userTicketsDetails.clear();
                       allChatList.clear();

                       if (recieveMessageResponse.getData().getUserTicketsDetails() != null &&
                       recieveMessageResponse.getData().getUserTicketsDetails().size() > 0){
                           userTicketsDetails.addAll(recieveMessageResponse.getData().getUserTicketsDetails());
                           ticketDetailsViewModel.ticketId.setValue(userTicketsDetails.get(0).getTicketNo());
                           ticketDetailsViewModel.ticketDate.setValue(userTicketsDetails.get(0).getCreatedOn());
                           ticketDetailsViewModel.subject.setValue(userTicketsDetails.get(0).getSubject());
                           ticketDetailsViewModel.remark.setValue(userTicketsDetails.get(0).getRemark());

                           if(userTicketsDetails.get(0).getStatus().equalsIgnoreCase("Pending")){
                               fragmentTicketDetailBinding.tvTicketStatus.setTextColor(ContextCompat.getColor(context, R.color.color10));
                               ticketDetailsViewModel.status.setValue(userTicketsDetails.get(0).getStatus());
                           }
                       }

                       if(recieveMessageResponse.getData().getAllChats() != null &&
                       recieveMessageResponse.getData().getAllChats().size() > 0){
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
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }

        };
        ticketDetailsViewModel.getRecieveMessage(progressDialog, context, RecieveMessageInputParameter(), TicketDetailFragment.this)
                .observe(getViewLifecycleOwner(), recieveMessageResponseObserver);
    }

    private HashMap<String, String> RecieveMessageInputParameter(){
        HashMap<String, String> map = new HashMap<>();

        map.put(TICKET_ID, strTicketId);

        return map;
    }



    private  void callSendMessageApi(ProgressDialog progressDialog){
        // print user input parameters
        for (Map.Entry<String, String> entry : SendMessageParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }
        Observer<SendMessageResponse> sendMessageResponseObserver = sendMessageResponse -> {
            if (sendMessageResponse != null){
                Log.e("Support Chat Api Response", new Gson().toJson(sendMessageResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (sendMessageResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        if (sendMessageResponse.getData() != null){
                            ticketDetailsViewModel.etMessage.setValue("");
                            successToast(context,sendMessageResponse.getMessage());
                            callReceiveMessageApi(showCircleProgressDialog(context,""));
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
            }else{
                if (progressDialog !=null){
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
}