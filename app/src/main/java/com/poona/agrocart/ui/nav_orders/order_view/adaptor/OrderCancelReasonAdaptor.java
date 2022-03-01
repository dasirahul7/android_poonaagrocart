package com.poona.agrocart.ui.nav_orders.order_view.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;

import com.poona.agrocart.databinding.CancelOrderReasonRecyclerViewBinding;
import com.poona.agrocart.ui.nav_orders.model.CancelOrderReasonList;

import java.util.ArrayList;
import java.util.List;

public class OrderCancelReasonAdaptor extends RecyclerView.Adapter<OrderCancelReasonAdaptor.OrderCancelReasonViewHolder> {

    private List<CancelOrderReasonList> cancelOrderReasonLists = new ArrayList<>();
    private Context context;
    private RadioButton lastCheckedRB = null ;
    private int mSelectedItem = -1;

    public OrderCancelReasonAdaptor(Context context, List<CancelOrderReasonList> cancelOrderReasonLists) {
        this.context=context;
        this.cancelOrderReasonLists=cancelOrderReasonLists;
    }

    @NonNull
    @Override
    public OrderCancelReasonAdaptor.OrderCancelReasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       CancelOrderReasonRecyclerViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.cancel_order_reason_recycler_view,parent,false);

        return new OrderCancelReasonAdaptor.OrderCancelReasonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCancelReasonAdaptor.OrderCancelReasonViewHolder viewHolder, int position) {
        CancelOrderReasonList cancelOrderReasonList = cancelOrderReasonLists.get(position);
        viewHolder.binding.setCancelOrderReasonList(cancelOrderReasonList);
        viewHolder.bind(cancelOrderReasonList);

        /* radio Selected Item */
        viewHolder.binding.radioPlan.setChecked(mSelectedItem == position);

        viewHolder.binding.radioPlan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean radioBoolean ) {
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB =  viewHolder.binding.radioPlan;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cancelOrderReasonLists.size();
    }

    public class OrderCancelReasonViewHolder extends RecyclerView.ViewHolder {

        public CancelOrderReasonRecyclerViewBinding binding;

        @SuppressLint("NotifyDataSetChanged")
        public OrderCancelReasonViewHolder(CancelOrderReasonRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding= binding;

            binding.llMain.setOnClickListener(view -> {
                mSelectedItem = getAdapterPosition();
               // strItrType = "check";
                //onTypeClickListener.itemViewTypeClick(strItrType,itrTypeList.get(getLayoutPosition()).getItrFillingChargesId());
                notifyDataSetChanged();
            });

        }

        public void bind(CancelOrderReasonList cancelOrderReasonList) {
            binding.setVariable(BR.cancelOrderReasonList, cancelOrderReasonList);
            binding.executePendingBindings();
        }
    }
}
