package com.poona.agrocart.ui.ticket_details;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.databinding.FragmentTicketDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.help_center.model.Ticket;
import com.poona.agrocart.ui.ticket_details.model.Comment;

import java.util.ArrayList;

public class TicketDetailFragment extends BaseFragment
{
    private FragmentTicketDetailBinding fragmentTicketDetailBinding;
    private RecyclerView rvTicketOrders;
    private LinearLayoutManager linearLayoutManager;
    private TicketCommentsAdapter ticketCommentsAdapter;
    private ArrayList<Comment> commentArrayList;

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentTicketDetailBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_ticket_detail, container, false);
        fragmentTicketDetailBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentTicketDetailBinding).getRoot();

        initTitleWithBackBtn(getString(R.string.help_center));

        Bundle bundle=this.getArguments();
        Ticket ticket=new Ticket();
        ticket.setTicketId(bundle.getString(AppConstants.TICKET_ID));
        ticket.setStatus(bundle.getString(AppConstants.STATUS));
        ticket.setRemark(bundle.getString(AppConstants.REMARK));
        ticket.setDateAndTime(bundle.getString(AppConstants.DATE));
        ticket.setSubject(bundle.getString(AppConstants.SUBJECT));
        if(ticket.getStatus().equals(getString(R.string.pending)))
        {
            fragmentTicketDetailBinding.tvTicketStatus.setTextColor(Color.parseColor(context.getString(R.color.color_pending)));
        }
        else if(ticket.getStatus().equals(getString(R.string.ongoing)))
        {
            fragmentTicketDetailBinding.tvTicketStatus.setTextColor(Color.parseColor(context.getString(R.color.color_ongoing)));
        }
        fragmentTicketDetailBinding.setTicket(ticket);

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
}