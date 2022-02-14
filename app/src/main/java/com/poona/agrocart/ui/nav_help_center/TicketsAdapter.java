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
import com.poona.agrocart.data.network.reponses.help_center_response.TicketListResponse;
import com.poona.agrocart.databinding.RvTicketBinding;

import java.util.ArrayList;
import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>
{
    private List<TicketListResponse.TicketList> ticketArrayList = new ArrayList<>();
    private final Context context;

    public TicketsAdapter(List<TicketListResponse.TicketList> ticketArrayList,Context context)
    {
        this.ticketArrayList = ticketArrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RvTicketBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_ticket, parent, false);
        return new TicketsAdapter.TicketViewHolder(binding,ticketArrayList);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position)
    {
         TicketListResponse.TicketList ticket = ticketArrayList.get(position);
        holder.rvTicketBinding.setTicket(ticket);
        holder.bind(ticket,context);
    }

    @Override
    public int getItemCount()
    {
        return ticketArrayList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder
    {
        RvTicketBinding rvTicketBinding;
        private  List<TicketListResponse.TicketList> ticketArrayList = new ArrayList<>();

        public TicketViewHolder(RvTicketBinding rvTicketBinding,List<TicketListResponse.TicketList> ticketArrayList)
        {
            super(rvTicketBinding.getRoot());

            this.ticketArrayList=ticketArrayList;
            this.rvTicketBinding=rvTicketBinding;

            rvTicketBinding.cardViewTicket.setOnClickListener(v -> {

                Bundle bundle=new Bundle();
                bundle.putString(AppConstants.TICKET_ID,ticketArrayList.get(getAdapterPosition()).getTicketId());
                bundle.putString(AppConstants.STATUS,ticketArrayList.get(getAdapterPosition()).getStatus());
                bundle.putString(AppConstants.REMARK,ticketArrayList.get(getAdapterPosition()).getRemark());
                bundle.putString(AppConstants.DATE,ticketArrayList.get(getAdapterPosition()).getCreatedOn());
                bundle.putString(AppConstants.SUBJECT,ticketArrayList.get(getAdapterPosition()).getSubject());
                Navigation.findNavController(v).navigate(R.id.action_nav_help_center_to_nav_ticket_detail,bundle);
            });
        }

        @SuppressLint("ResourceType")
        public void bind(TicketListResponse.TicketList ticket, Context context)
        {
            rvTicketBinding.setVariable(BR.ticket,ticket);
            if(ticket.getStatus().equals("Pending"))
            {
                rvTicketBinding.tvTicketStatus.setTextColor(Color.parseColor(context.getString(R.color.color_pending)));
            }
            else if(ticket.getStatus().equals("Ongoing"))
            {
                rvTicketBinding.tvTicketStatus.setTextColor(Color.parseColor(context.getString(R.color.color_ongoing)));
            }
            else
            {
                rvTicketBinding.tvTicketStatus.setTextColor(Color.parseColor(context.getString(R.color.color_pending)));
            }
            rvTicketBinding.executePendingBindings();
        }



    }
}
