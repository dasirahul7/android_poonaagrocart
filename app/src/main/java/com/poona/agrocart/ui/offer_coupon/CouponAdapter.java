package com.poona.agrocart.ui.offer_coupon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowCouponItemBinding;

import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponHolder> {

    private ArrayList<Coupons> coupons = new ArrayList<>();
    private final Context context;
    private RowCouponItemBinding rowCouponItemBinding;


    public CouponAdapter(ArrayList<Coupons> coupons, Context context) {
        this.coupons = coupons;
        this.context = context;
    }

    @NonNull
    @Override
    public CouponHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowCouponItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_coupon_item, parent, false);
        return new CouponHolder(rowCouponItemBinding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull CouponHolder holder, int position) {
        Coupons coupon = coupons.get(position);
        rowCouponItemBinding.setModuleCoupon(coupon);
        if (coupon.getId() % 3 == 1)
            rowCouponItemBinding.couponRow.setBackgroundResource(R.drawable.coupon_green);
        if (coupon.getId() % 3 == 2)
            rowCouponItemBinding.couponRow.setBackgroundResource(R.drawable.coupon_blue);
        if (coupon.getId() % 3 == 0)
            rowCouponItemBinding.couponRow.setBackgroundResource(R.drawable.coupon_pink);
        holder.bind(coupon);
    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }

    public class CouponHolder extends RecyclerView.ViewHolder {
        public CouponHolder(@NonNull RowCouponItemBinding itemView) {
            super(itemView.getRoot());
        }

        public void bind(Coupons coupon) {
            rowCouponItemBinding.setVariable(BR.moduleCoupon, coupon);
            rowCouponItemBinding.executePendingBindings();
        }
    }
}
