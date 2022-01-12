package com.poona.agrocart.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowSeasonalBannerBinding;
import com.poona.agrocart.ui.home.model.SeasonalProduct;

import java.util.ArrayList;

public class SeasonalBannerAdapter extends RecyclerView.Adapter<SeasonalBannerAdapter.SeasonBannerHolder> {
    private RowSeasonalBannerBinding seasonalBannerBinding;
    private Context sbContext;
    private ArrayList<SeasonalProduct> seasonalProducts;
    private View rootView;

    public SeasonalBannerAdapter(Context sbContext, ArrayList<SeasonalProduct> seasonalProducts, View rootView) {
        this.sbContext = sbContext;
        this.seasonalProducts = seasonalProducts;
        this.rootView = rootView;
    }

    @NonNull
    @Override
    public SeasonBannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        seasonalBannerBinding = RowSeasonalBannerBinding.inflate(LayoutInflater.from(sbContext));
        return new SeasonBannerHolder(seasonalBannerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonBannerHolder holder, int position) {
        SeasonalProduct product = seasonalProducts.get(position);
        seasonalBannerBinding.setModuleSeasonBanner(product);
        holder.bindSeasonItem(product);
        if (position/2==0)
            seasonalBannerBinding.rlSeasonalView.setBackgroundResource(R.drawable.seasonal_banner_green);
        if (position/2==1)
            seasonalBannerBinding.rlSeasonalView.setBackgroundResource(R.drawable.seasonal_banner_yellow);
    }

    @Override
    public int getItemCount() {
        return seasonalProducts.size();
    }

    public class SeasonBannerHolder extends RecyclerView.ViewHolder {

        public SeasonBannerHolder(RowSeasonalBannerBinding binding) {
            super(binding.getRoot());
        }

        private void bindSeasonItem(SeasonalProduct seasonalProduct){
            seasonalBannerBinding.setVariable(BR.moduleSeasonBanner,seasonalProduct);
            seasonalBannerBinding.executePendingBindings();
        }
    }
}
