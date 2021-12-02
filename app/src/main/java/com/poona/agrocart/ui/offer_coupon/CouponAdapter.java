package com.poona.agrocart.ui.offer_coupon;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowCouponItemBinding;

import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponHolder> {

    private ArrayList<Coupons> coupons = new ArrayList<>();
    private Context context;
    private RowCouponItemBinding binding;


    public CouponAdapter(ArrayList<Coupons> coupons, Context context) {
        this.coupons = coupons;
        this.context = context;
    }

    @NonNull
    @Override
    public CouponHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_coupon_item, parent, false);
        return new CouponHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponHolder holder, int position) {
        Coupons coupon = coupons.get(position);
        binding.setModuleCoupon(coupon);
        if (coupon.getId() % 3 == 1)
            binding.couponRow.setBackground(ContextCompat.getDrawable(context, R.drawable.coupon_green));
        if (coupon.getId() % 3 == 2)
            binding.couponRow.setBackground(ContextCompat.getDrawable(context, R.drawable.coupon_blue));
        if (coupon.getId() % 3 == 0)
            binding.couponRow.setBackground(ContextCompat.getDrawable(context, R.drawable.coupon_pink));
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
            binding.setVariable(BR.moduleCoupon, coupon);
            binding.executePendingBindings();
        }
    }
}
