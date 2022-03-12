package com.poona.agrocart.ui.nav_orders.order_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails.ItemsDetail;
import com.poona.agrocart.databinding.RvBasketDetailBinding;

import java.text.ParseException;
import java.util.ArrayList;

public class BasketItemsAdapter extends RecyclerView.Adapter<BasketItemsAdapter.BasketItemViewHolder> {
    private  ArrayList<ItemsDetail> basketItems;
    private final boolean isBasketVisible;
    private Context context;
    private int from=0;
    private static int DETAIL =0;
    private static int REVIEW =1;
    OrderViewFragment orderViewFragment = new OrderViewFragment();

    public BasketItemsAdapter(ArrayList<ItemsDetail> basketItems, boolean isBasketVisible, Context context, int from) {
        this.basketItems = basketItems;
        this.isBasketVisible = isBasketVisible;
        this.context = context;
        this.from = from;
    }

    @NonNull
    @Override
    public BasketItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvBasketDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_basket_detail, parent, false);
        return new BasketItemViewHolder(binding, isBasketVisible);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BasketItemViewHolder holder, int position) {
        final ItemsDetail basketItem = basketItems.get(position);
        holder.rvBasketDetailBinding.setBasketItemModel(basketItem);
        holder.bind(basketItem, context);
        if(this.isBasketVisible){

            if(basketItem.getBasketName() != null && !basketItem.getBasketName().equals("")){
                holder.rvBasketDetailBinding.tvProductName.setText(basketItem.getBasketName());
            }else {
                holder.rvBasketDetailBinding.tvProductName.setText("N/A");
            }

            holder.rvBasketDetailBinding.tvWeight.setVisibility(View.GONE);

            /*Weight parameter*//*
            if(basketItem.getWeight() != null && !basketItem.getWeight().equals("")){
                holder.rvBasketDetailBinding.tvWeight.setText(basketItem.getWeight());
            }else {
                holder.rvBasketDetailBinding.tvWeight.setText("N/A");
            }*/

            /*Select the date and time*/
            String selectedDate = basketItem.getShouldDeliverOnDate();

            String txtDisplayDate = "";
            try {
                txtDisplayDate = orderViewFragment.formatDate(selectedDate, "dd/mm/yyyy", "dd MMM yyyy");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.rvBasketDetailBinding.tvDateAndTime.setText(txtDisplayDate + " "+ basketItem.getDelierySlotStartAndEndTime());


        }else{

            if(basketItem.getItemType().equals("basket")){
                if(basketItem.getBasketName() != null && !basketItem.getBasketName().equals("")){
                    holder.rvBasketDetailBinding.tvProductName.setText(basketItem.getBasketName());
                }else {
                    holder.rvBasketDetailBinding.tvProductName.setText("N/A");
                }

                holder.rvBasketDetailBinding.tvWeight.setVisibility(View.GONE);
            }else {

                if(basketItem.getProductName() != null && !basketItem.getProductName().equals("")){
                    holder.rvBasketDetailBinding.tvProductName.setText(basketItem.getProductName());
                }else {
                    holder.rvBasketDetailBinding.tvProductName.setText("N/A");
                }

                if(basketItem.getWeight() != null && !basketItem.getWeight().equals("")){
                    holder.rvBasketDetailBinding.tvWeight.setText(basketItem.getWeight());
                }else {
                    holder.rvBasketDetailBinding.tvWeight.setText("N/A");
                }
            }

        }


        /* imageView = holder.photoItemBinding.itemImg;
        // set page image
        Glide.with(pContext)
                .load(IMAGE_DOC_BASE_URL+galleryImages.get(position).getGalleryImage())
                .into(imageView);*/

    }

    @Override
    public int getItemCount() {

        if(isBasketVisible){
            if(from==DETAIL){
                if(basketItems.size() > 2){
                    return 3;
                }else {
                    return basketItems.size();
                }
            }else {
                return basketItems.size();
            }
        }else {
            return basketItems.size();
        }
    }

    public class BasketItemViewHolder extends RecyclerView.ViewHolder {

        RvBasketDetailBinding rvBasketDetailBinding;
        boolean isBasketVisible;



        public BasketItemViewHolder(RvBasketDetailBinding rvBasketDetailBinding, boolean isBasketVisible) {
            super(rvBasketDetailBinding.getRoot());
            this.rvBasketDetailBinding = rvBasketDetailBinding;
            this.isBasketVisible = isBasketVisible;
            if (this.isBasketVisible) {
                rvBasketDetailBinding.tvDateAndTime.setVisibility(View.VISIBLE);
                rvBasketDetailBinding.tvQuantity.setVisibility(View.GONE);
                rvBasketDetailBinding.tvOrderPrice.setTextColor(context.getColor(R.color.color_rv_basket_price));
            } else {
                rvBasketDetailBinding.tvDateAndTime.setVisibility(View.INVISIBLE);
                rvBasketDetailBinding.tvQuantity.setVisibility(View.VISIBLE);
                rvBasketDetailBinding.tvOrderStatus.setVisibility(View.GONE);
            }
        }

        @SuppressLint("ResourceType")
        public void bind(ItemsDetail basketItem, Context context) {
            rvBasketDetailBinding.setVariable(BR.basketItemModel, basketItem);

            if (this.isBasketVisible) {

                switch (basketItem.getOrderStatus()) {
                    case "3":
                        rvBasketDetailBinding.btnTrackOrder.setVisibility(View.VISIBLE);
                        /*rvBasketDetailBinding.tvOrderStatus.setText(context.getString(R.string.in_process));
                        rvBasketDetailBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_in_process)));*/
                        break;
                    case "4":
                        rvBasketDetailBinding.tvOrderStatus.setText(context.getString(R.string.delivered));
                        rvBasketDetailBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_delivered)));

                        rvBasketDetailBinding.btnTrackOrder.setVisibility(View.GONE);
                        break;
                    case "2":
                        rvBasketDetailBinding.tvOrderStatus.setText(context.getString(R.string.confirmed));
                        rvBasketDetailBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_confirmed)));

                        rvBasketDetailBinding.btnTrackOrder.setVisibility(View.GONE);
                        break;
                    case "5":
                        rvBasketDetailBinding.tvOrderStatus.setText(context.getString(R.string.cancelled));
                        rvBasketDetailBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color_cancelled)));

                        break;
                    default:
                        rvBasketDetailBinding.btnTrackOrder.setVisibility(View.VISIBLE);
                       /* rvBasketDetailBinding.tvOrderStatus.setText(context.getString(R.string.pending));
                        rvBasketDetailBinding.tvOrderStatus.setTextColor(Color.parseColor(context.getString(R.color.color4)));*/
                        break;
                }
            }

            rvBasketDetailBinding.executePendingBindings();
        }
    }
}
