package com.poona.agrocart.ui.nav_orders.order_view.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;

import com.poona.agrocart.data.network.responses.myOrderResponse.OrderCancelReasonResponse;
import com.poona.agrocart.databinding.CancelOrderReasonRecyclerViewBinding;
import com.poona.agrocart.ui.nav_orders.order_view.OrderViewFragment;

import java.util.ArrayList;
import java.util.List;

public class OrderCancelReasonAdaptor extends RecyclerView.Adapter<OrderCancelReasonAdaptor.OrderCancelReasonViewHolder> {

    private  String strReasonType = "";
    private List<OrderCancelReasonResponse.OrderCancelReason> cancelOrderReasonLists = new ArrayList<>();
    private Context context;
    public OnTypeClickListener onTypeClickListener;
    private RadioButton lastCheckedRB = null ;
    private int mSelectedItem = -1;

    public OrderCancelReasonAdaptor(Context context, List<OrderCancelReasonResponse.OrderCancelReason> cancelOrderReasonLists, OrderViewFragment orderViewFragment) {
        this.context=context;
        this.cancelOrderReasonLists=cancelOrderReasonLists;
        this.onTypeClickListener = orderViewFragment;
    }

    public interface OnTypeClickListener{
        void itemClick(String strReasonType, String cancelId);
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
        OrderCancelReasonResponse.OrderCancelReason cancelOrderReasonList = cancelOrderReasonLists.get(position);
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
                mSelectedItem = getLayoutPosition();
                strReasonType = "check";
                onTypeClickListener.itemClick(strReasonType,cancelOrderReasonLists.get(getLayoutPosition()).getCancelId());
                notifyDataSetChanged();
            });

        }

        public void bind(OrderCancelReasonResponse.OrderCancelReason cancelOrderReasonList) {
            binding.setVariable(BR.cancelOrderReasonList, cancelOrderReasonList);
            binding.executePendingBindings();
        }
    }
}
