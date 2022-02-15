package com.poona.agrocart.ui.ticket_details;

import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.reponses.help_center_response.SendReplayResponse;
import com.poona.agrocart.data.network.reponses.help_center_response.TicketListResponse;
import com.poona.agrocart.databinding.FragmentTicketDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_help_center.HelpCenterFragment;
import com.poona.agrocart.ui.nav_help_center.HelpCenterViewModel;
import com.poona.agrocart.ui.nav_help_center.model.Ticket;
import com.poona.agrocart.ui.ticket_details.model.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketDetailFragment extends BaseFragment
{
    private FragmentTicketDetailBinding fragmentTicketDetailBinding;
    private TicketDetailsViewModel  ticketDetailsViewModel;
    private RecyclerView rvTicketOrders;
    private LinearLayoutManager linearLayoutManager;
    private TicketCommentsAdapter ticketCommentsAdapter;
    private ArrayList<Comment> commentArrayList;
    private View view;

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentTicketDetailBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_ticket_detail, container, false);
        fragmentTicketDetailBinding.setLifecycleOwner(this);
         view = fragmentTicketDetailBinding.getRoot();
        ticketDetailsViewModel=new ViewModelProvider(this).get(TicketDetailsViewModel.class);
        fragmentTicketDetailBinding.setTicket(ticketDetailsViewModel);

        initTitleWithBackBtn(getString(R.string.menu_help_center));

        Bundle bundle=this.getArguments();
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
        }


        initView();
        setRvAdapter();

        return view;
    }

    private void setRvAdapter()
    {
        commentArrayList=new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvTicketOrders.setHasFixedSize(true);
        rvTicketOrders.setLayoutManager(linearLayoutManager);

        ticketCommentsAdapter = new TicketCommentsAdapter(commentArrayList);
        rvTicketOrders.setAdapter(ticketCommentsAdapter);
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 2; i++)
        {
            Comment comment = new Comment();
            comment.setName(getString(R.string.johnson_doe));
            comment.setReplyDate(getString(R.string._12_jan_2021));
            comment.setComment(getString(R.string.lorem_ipsum_dolor_sit_amet_consectetur_adipiscing));
            commentArrayList.add(comment);
        }
    }

    private void initView()
    {
        rvTicketOrders=fragmentTicketDetailBinding.rvTicketComments;
    }

   /* private  void callSendMessageApi(ProgressDialog progressDialog){
        *//* print user input parameters *//*
        for (Map.Entry<String, String> entry : messageParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }
        Observer<SendReplayResponse> sendReplayResponseObserver = sendReplayResponse -> {
            if (sendReplayResponse != null){
                Log.e("Support Chat Api Response", new Gson().toJson(SendReplayResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (SendReplayResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        if (SendReplayResponse. != null){
                            supportTicketDetailsViewModel.etMessage.setValue("");
                            successToast(context,sendMessageToSupportResponse.getMsg());
                            getSupportTicketDetailsList(showCircleProgressDialog(context, ""));
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, sendMessageToSupportResponse.getMsg());
                        break;
                    case STATUS_CODE_404://Record not Found
                        errorToast(context, sendMessageToSupportResponse.getMsg());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, sendMessageToSupportResponse.getMsg());
                        break;
                }
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }
        };

        ticketDetailsViewModel.sendMessageData(progressDialog, context, messageParameters(), TicketDetailFragment.this)
                .observe(getViewLifecycleOwner(), sendReplayResponseObserver);
    }

    private HashMap<String, String> messageParameters() {

    }*/
}