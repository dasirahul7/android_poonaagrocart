package com.poona.agrocart.ui.nav_offers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.CouponResponse;
import com.poona.agrocart.databinding.RowCouponItemBinding;

import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponHolder> {

    private final Context context;
    private ArrayList<CouponResponse.Coupon> coupons = new ArrayList<>();
    private RowCouponItemBinding rowCouponItemBinding;
    private final CouponFragment couponFragment;
    private final TermsAndConditionClickItem termsAndConditionClickItem;


    public CouponAdapter(ArrayList<CouponResponse.Coupon> coupons,
                         Context context, CouponFragment couponFragment, TermsAndConditionClickItem termsAndConditionClickItem) {
        this.coupons = coupons;
        this.context = context;
        this.couponFragment = couponFragment;
        this.termsAndConditionClickItem = termsAndConditionClickItem;
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
        CouponResponse.Coupon coupon = coupons.get(position);
        rowCouponItemBinding.setModuleCoupon(coupon);
        holder.bind(coupon);
        if (Integer.parseInt(coupon.getCategoryId()) % 3 == 1) {
            rowCouponItemBinding.couponRow.setBackgroundResource(R.drawable.coupon_green);
            rowCouponItemBinding.tvCouponCode.setTextColor(context.getColor(R.color.light_green_txt_color));
            rowCouponItemBinding.mcvCouponCode.setStrokeColor(context.getColor(R.color.light_green_txt_color));
        }
        if (Integer.parseInt(coupon.getCategoryId()) % 3 == 2)
            rowCouponItemBinding.couponRow.setBackgroundResource(R.drawable.coupon_blue);
        if (Integer.parseInt(coupon.getCategoryId()) % 3 == 0) {
            rowCouponItemBinding.couponRow.setBackgroundResource(R.drawable.coupon_pink);
            rowCouponItemBinding.tvCouponCode.setTextColor(context.getColor(R.color.red_text_color));
            rowCouponItemBinding.mcvCouponCode.setStrokeColor(context.getColor(R.color.red_text_color));
        }

    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }

    public interface TermsAndConditionClickItem {
        void itemViewClick(int position);

        void onCopyClick(CouponResponse.Coupon coupon);
    }

    public class CouponHolder extends RecyclerView.ViewHolder {
        public CouponHolder(@NonNull RowCouponItemBinding itemView) {
            super(itemView.getRoot());

            itemView.llTermsAndCond.setOnClickListener(view -> {
                if (termsAndConditionClickItem != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        termsAndConditionClickItem.itemViewClick(position);
                    }
                }
            });
        }

        public void bind(CouponResponse.Coupon coupon) {
            rowCouponItemBinding.setVariable(BR.moduleCoupon, coupon);
            rowCouponItemBinding.executePendingBindings();
           /* rowCouponItemBinding.imgInfo.setOnClickListener(v -> {
                couponFragment.termsDialog();
            });*/
            rowCouponItemBinding.tvCouponCode.setOnClickListener(view -> {
                termsAndConditionClickItem.onCopyClick(coupon);
            });

        }
    }
}
