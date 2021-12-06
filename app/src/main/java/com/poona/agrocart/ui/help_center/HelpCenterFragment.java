package com.poona.agrocart.ui.help_center;

import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentHelpCenterBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.help_center.model.Ticket;
import java.util.ArrayList;

public class HelpCenterFragment extends BaseFragment implements View.OnClickListener
{
    private FragmentHelpCenterBinding fragmentHelpCenterBinding;
    private RecyclerView rvTickets;
    private LinearLayoutManager linearLayoutManager;
    private TicketsAdapter ticketsAdapter;
    private ArrayList<Ticket> ticketArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentHelpCenterBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_help_center, container, false);
        fragmentHelpCenterBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentHelpCenterBinding).getRoot();

        initView();
        setRvAdapter();

        initTitleBar(getString(R.string.menu_help_center));

        return view;
    }

    private void initView()
    {
        rvTickets=fragmentHelpCenterBinding.rvTickets;
        fragmentHelpCenterBinding.btnCreateNewTicket.setOnClickListener(this);
        initTitleBar(getString(R.string.menu_addresses));
    }

    private void setRvAdapter()
    {
        ticketArrayList=new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvTickets.setHasFixedSize(true);
        rvTickets.setLayoutManager(linearLayoutManager);

        ticketsAdapter = new TicketsAdapter(ticketArrayList,getContext());
        rvTickets.setAdapter(ticketsAdapter);
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 3; i++)
        {
            Ticket ticket = new Ticket();
            ticket.setTicketId("ABCDEF");
            ticket.setDateAndTime(getString(R.string.date_sep_30_2021_20_15_am));
            if(i==0) {
                ticket.setStatus("Pending");
            }
            else if(i==1) {
                ticket.setStatus("Ongoing");
            }
            else {
                ticket.setStatus("Pending");
            }
            ticket.setSubject(getString(R.string.lorem_ipsum_dolor_sit_amet_consectetur_adipiscing));
            ticket.setRemark(getString(R.string.lorem_ipsum_dolor_sit_amet_consectetur_adipiscing));
            ticketArrayList.add(ticket);
        }
    }

    @Override
    public void onClick(View v) {

    }
}