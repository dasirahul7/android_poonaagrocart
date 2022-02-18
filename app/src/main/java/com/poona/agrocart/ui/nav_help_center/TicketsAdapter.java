package com.poona.agrocart.ui.nav_help_center;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.responses.help_center_response.TicketListResponse;
import com.poona.agrocart.databinding.RvTicketBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>
{
    private List<TicketListResponse.TicketList.UserTicket> ticketArrayList = new ArrayList<>();
    private  Context context;
    private HelpCenterFragment helpCenterFragment;
    public OnTicketClickListener onTicketClickListener;

    public TicketsAdapter(List<TicketListResponse.TicketList.UserTicket> ticketArrayList,
                          HelpCenterFragment helpCenterFragment,OnTicketClickListener onTicketClickListener)
    {
        this.ticketArrayList = ticketArrayList;
        this.onTicketClickListener = onTicketClickListener;
        this.helpCenterFragment = helpCenterFragment;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RvTicketBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_ticket, parent, false);
        return new TicketsAdapter.TicketViewHolder(binding);
    }

    public interface OnTicketClickListener{
        void itemViewClick(TicketListResponse.TicketList.UserTicket ticket);
    }
    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position)
    {
        TicketListResponse.TicketList.UserTicket ticket = ticketArrayList.get(position);
        holder.rvTicketBinding.setTicket(ticket);
        holder.bind(ticket);

        String selectedDate = ticket.getCreatedOn();

        String txtDisplayDate="";
        try {
            txtDisplayDate = helpCenterFragment.formatDate(selectedDate, "yyyy-mm-dd hh:mm:ss", "MMM dd, yyyy hh:mm aa");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.rvTicketBinding.tvDate.setText(txtDisplayDate);
    }

    @Override
    public int getItemCount()
    {
        return ticketArrayList.size();
    }

    public  class TicketViewHolder extends RecyclerView.ViewHolder
    {

        public RvTicketBinding rvTicketBinding;


        public TicketViewHolder(RvTicketBinding rvTicketBinding)
        {
            super(rvTicketBinding.getRoot());
            this.rvTicketBinding=rvTicketBinding;

        }

        @SuppressLint("ResourceType")
        public void bind(TicketListResponse.TicketList.UserTicket ticket)
        {
            rvTicketBinding.setVariable(BR.ticket,ticket);

            if(ticket.getStatus().equals("Pending"))
            {
                rvTicketBinding.tvTicketStatus.setTextColor(Color.parseColor(helpCenterFragment.context.getString(R.color.color_pending)));
            }
            else if(ticket.getStatus().equals("Ongoing"))
            {
                rvTicketBinding.tvTicketStatus.setTextColor(Color.parseColor(helpCenterFragment.context.getString(R.color.color_ongoing)));
            }
            else
            {
                rvTicketBinding.tvTicketStatus.setTextColor(Color.parseColor(helpCenterFragment.context.getString(R.color.color_pending)));
            }
            rvTicketBinding.executePendingBindings();

            itemView.setOnClickListener(v ->{
                if (onTicketClickListener != null) {
                    int postion = getAdapterPosition();

                    onTicketClickListener.itemViewClick(ticket);
                }
            });

        }



    }
}
