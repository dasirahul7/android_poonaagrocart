package com.poona.agrocart.ui.nav_help_center;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentHelpCenterBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_help_center.model.Ticket;

import java.util.ArrayList;
import java.util.Arrays;

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
        final View view = fragmentHelpCenterBinding.getRoot();

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
        fragmentHelpCenterBinding.btnCreateNewTicket.setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.btn_create_new_ticket:
               raiseNewTicketDialog();
                break;
        }
    }

    // Dialog for create ticket
    public void raiseNewTicketDialog() {
        Dialog dialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogAnimationUp));
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_new_ticket);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        ImageView closeImg = dialog.findViewById(R.id.close_btn);
        RecyclerView rvAddress = dialog.findViewById(R.id.rv_address);
        ArrayList<String> typeList = new ArrayList<String>();
        typeList.add("ordinary");
        typeList.add("special");
        typeList.add("subscription");
        typeList.add("issue");
//        typeList.addAll(R.array.type);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,typeList);
//        arrayAdapter.setDropDownViewResource(android.R.layout.test_list_item);
//        spType.setAdapter(arrayAdapter);
        closeImg.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

}