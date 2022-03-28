package com.poona.agrocart.ui.nav_my_basket;

import static com.poona.agrocart.app.AppConstants.ORDER_SUBSCRIPTION_ID;
import static com.poona.agrocart.app.AppConstants.SUBSCRIPTION;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.myOrderResponse.SubscribeBasketListCustomerResponse;
import com.poona.agrocart.databinding.RvOrdersBasketBinding;

import java.util.ArrayList;

public class BasketOrdersAdapter extends RecyclerView.Adapter<BasketOrdersAdapter.BasketOrdersViewHolder> {
    private final ArrayList<SubscribeBasketListCustomerResponse.SubscribeBasketListCustomer> basketOrderArrayList;
    private final View view;
    private final boolean isWallet;
    OnInvoiceClickListener onInvoiceClickListener;
    private MyBasketFragment myBasketFragment;

    public BasketOrdersAdapter(ArrayList<SubscribeBasketListCustomerResponse.SubscribeBasketListCustomer> basketOrderArrayList, View view, boolean isWallet, MyBasketFragment myBasketFragment) {
        this.basketOrderArrayList = basketOrderArrayList;
        this.view = view;
        this.myBasketFragment = myBasketFragment;
        this.isWallet = isWallet;
        this.onInvoiceClickListener = myBasketFragment;
    }

    @NonNull
    @Override
    public BasketOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvOrdersBasketBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_orders_basket, parent, false);
        return new BasketOrdersAdapter.BasketOrdersViewHolder(binding, view, isWallet);
    }

    public interface OnInvoiceClickListener{
        void ItemClick(int position);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketOrdersViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final SubscribeBasketListCustomerResponse.SubscribeBasketListCustomer basketOrder = basketOrderArrayList.get(position);
        holder.rvOrdersBasketBinding.setBasketOrder(basketOrder);
        holder.bind(basketOrder);

        if(basketOrder.getTransactionId() != null){
            holder.rvOrdersBasketBinding.tvTransactionId.setText(basketOrder.getTransactionId());
        }else{
            holder.rvOrdersBasketBinding.tvTransactionId.setVisibility(View.GONE);
        }

//        String selectedDate = basketOrder.getCreatedAt();
//
//        String txtDisplayDate = "";
//        try {
//            txtDisplayDate = myBasketFragment.formatDate(selectedDate, "yyyy-mm-dd hh:mm:ss", "dd MMM yyyy ");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        holder.rvOrdersBasketBinding.tvDate.setText(txtDisplayDate);

        switch (basketOrder.getPaymentType()){

            case "1":
                holder.rvOrdersBasketBinding.tvPaymentMode.setText(R.string.basket_payment_type_1);
                break;
            case "2":
                holder.rvOrdersBasketBinding.tvPaymentMode.setText(R.string.basket_payment_type_2);
                break;
            case "3":
                holder.rvOrdersBasketBinding.tvPaymentMode.setText(R.string.basket_payment_type_3);
                break;
            case "4":
                holder.rvOrdersBasketBinding.tvPaymentMode.setText(R.string.basket_payment_type_4);
                break;
        }

        holder.rvOrdersBasketBinding.btnDownloadInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInvoiceClickListener.ItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return basketOrderArrayList.size();
    }

    public class BasketOrdersViewHolder extends RecyclerView.ViewHolder {
        RvOrdersBasketBinding rvOrdersBasketBinding;

        public BasketOrdersViewHolder(RvOrdersBasketBinding rvOrdersBasketBinding, View view, boolean isWallet) {
            super(rvOrdersBasketBinding.getRoot());
            this.rvOrdersBasketBinding = rvOrdersBasketBinding;
            if (!isWallet)
                rvOrdersBasketBinding.cardviewOrder.setOnClickListener(v -> {
                    redirectToBasketOrderView(view, basketOrderArrayList.get(getLayoutPosition()).getOrderSubscriptionId() );
                });

            if (isWallet)
                rvOrdersBasketBinding.tvBasketName.setVisibility(View.INVISIBLE);
            else
                rvOrdersBasketBinding.tvBasketName.setVisibility(View.VISIBLE);



        }

        private void redirectToBasketOrderView(View v, String orderSubscriptionId) {
            Bundle bundle = new Bundle();

            bundle.putBoolean(SUBSCRIPTION, true);
            bundle.putString(ORDER_SUBSCRIPTION_ID, orderSubscriptionId);
            Navigation.findNavController(v).navigate(R.id.action_nav_basket_to_orderViewFragment2, bundle);
        }

        public void bind(SubscribeBasketListCustomerResponse.SubscribeBasketListCustomer basketOrder) {
            rvOrdersBasketBinding.setVariable(BR.basketOrder, basketOrder);
            rvOrdersBasketBinding.executePendingBindings();
        }

    }

}
