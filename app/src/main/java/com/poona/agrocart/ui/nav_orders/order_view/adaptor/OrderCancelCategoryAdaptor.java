package com.poona.agrocart.ui.nav_orders.order_view.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.ItemsDetail;
import com.poona.agrocart.databinding.CancelOrderCategoryRecyclerViewBinding;
import com.poona.agrocart.ui.nav_orders.model.CancelOrderCategoryList;
import com.poona.agrocart.ui.nav_orders.order_view.OrderViewFragment;

import java.text.ParseException;
import java.util.List;

public class OrderCancelCategoryAdaptor extends RecyclerView.Adapter<OrderCancelCategoryAdaptor.OrderCancelCategoryViewHolder> {
    private int mSelectedItem = -1;
    private List<ItemsDetail> cancelOrderCategoryLists;
    private Context context;
    private CheckBox checkCategory;
    OrderViewFragment orderViewFragment = new OrderViewFragment();

    public OrderCancelCategoryAdaptor(Context context, List<ItemsDetail> cancelOrderCategoryLists) {
        this.context=context;
        this.cancelOrderCategoryLists=cancelOrderCategoryLists;
    }

    @NonNull
    @Override
    public OrderCancelCategoryAdaptor.OrderCancelCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CancelOrderCategoryRecyclerViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.cancel_order_category_recycler_view,parent,false);

        return new OrderCancelCategoryAdaptor.OrderCancelCategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCancelCategoryAdaptor.OrderCancelCategoryViewHolder viewHolder, int position) {
        ItemsDetail cancelOrderCategoryList = cancelOrderCategoryLists.get(position);
        viewHolder.binding.setCancelOrderCategoryList(cancelOrderCategoryList);
        viewHolder.bind(cancelOrderCategoryList);

        checkCategory = viewHolder.binding.cbCategory;
        viewHolder.binding.llMain.setOnClickListener(view -> {
            if (viewHolder.binding.cbCategory.isChecked()) {
                viewHolder.binding.cbCategory.setChecked(false);
            }else{
                viewHolder.binding.cbCategory.setChecked(true);
            }
        });


        /*Select the date and time*/
       /* String selectedDate = cancelOrderCategoryList.getShouldDeliverOnDate();

        String txtDisplayDate = "";
        try {
            txtDisplayDate = orderViewFragment.formatDate(selectedDate, "dd/mm/yyyy", "dd MMM yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.binding.tvSetDate.setText(txtDisplayDate);
*/
    }

    @Override
    public int getItemCount() {
        return cancelOrderCategoryLists.size();
    }

    public class OrderCancelCategoryViewHolder extends RecyclerView.ViewHolder {
        public CancelOrderCategoryRecyclerViewBinding binding;

        public OrderCancelCategoryViewHolder(CancelOrderCategoryRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding= binding;

           /* binding.llMain.setOnClickListener(view -> {
                mSelectedItem = getAdapterPosition();
                // strItrType = "check";
                //onTypeClickListener.itemViewTypeClick(strItrType,itrTypeList.get(getLayoutPosition()).getItrFillingChargesId());
                notifyDataSetChanged();
            });*/
        }

        public void bind(ItemsDetail cancelOrderCategoryList) {
            binding.setVariable(BR.cancelOrderCategoryList, cancelOrderCategoryList);
            binding.executePendingBindings();
        }
    }
}
