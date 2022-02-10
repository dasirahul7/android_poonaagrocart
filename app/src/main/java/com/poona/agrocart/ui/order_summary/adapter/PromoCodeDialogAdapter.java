package com.poona.agrocart.ui.order_summary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowPromoCodeRadioBinding;
import com.poona.agrocart.ui.nav_offers.Coupons;
import com.poona.agrocart.ui.order_summary.model.Address;

import java.util.ArrayList;

public class PromoCodeDialogAdapter extends RecyclerView.Adapter<PromoCodeDialogAdapter.PromoCodeHolder> {
    private ArrayList<Coupons> promoCodeList = new ArrayList<>();
    private Context cpContext;
    private RowPromoCodeRadioBinding promoCodeRadioBinding;
    private int mSelectedItem = -1;
    private CompoundButton lastCheckedRB;
    private OnPromoCodeListener onPromoCodeListener;

    public interface OnPromoCodeListener {
         void OnPromoCodeClick(Coupons coupons);
    }

    public void setOnPromoCodeListener(OnPromoCodeListener onPromoCodeListener) {
        this.onPromoCodeListener = onPromoCodeListener;
    }

    public PromoCodeDialogAdapter(ArrayList<Coupons> couponsArrayList, Context context) {
        this.promoCodeList = couponsArrayList;
        this.cpContext = context;
    }

    @NonNull
    @Override
    public PromoCodeDialogAdapter.PromoCodeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        promoCodeRadioBinding = RowPromoCodeRadioBinding.inflate(LayoutInflater.from(cpContext),parent,false);
        return new PromoCodeHolder(promoCodeRadioBinding,onPromoCodeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoCodeDialogAdapter.PromoCodeHolder holder, int position) {
        Coupons coupon = promoCodeList.get(position);
        promoCodeRadioBinding.setModelPromoCode(coupon);
        holder.codeRadioBinding.prRadio.setChecked(mSelectedItem == position);
        holder.codeRadioBinding.prRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean radioBoolean ) {
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB =  holder.codeRadioBinding.prRadio;
            }
        });

        holder.promoBind(coupon);
    }

    @Override
    public int getItemCount() {
        return promoCodeList.size();
    }

    public class PromoCodeHolder extends RecyclerView.ViewHolder {
        private RowPromoCodeRadioBinding codeRadioBinding;
        OnPromoCodeListener promoCodeListener;
        public PromoCodeHolder(@NonNull RowPromoCodeRadioBinding promoCodeRadioBinding, OnPromoCodeListener onPromoCodeListener) {
            super(promoCodeRadioBinding.getRoot());
            this.promoCodeListener = onPromoCodeListener;
            codeRadioBinding = promoCodeRadioBinding;

        }

        public void promoBind(Coupons coupons){
            codeRadioBinding.setVariable(BR.productOldModule,coupons);
            codeRadioBinding.executePendingBindings();
            itemView.setOnClickListener(v -> {
                mSelectedItem = getAdapterPosition();
                codeRadioBinding.prRadio.setChecked(true);
                onPromoCodeListener.OnPromoCodeClick(coupons);
            });

        }
    }
}
