package com.poona.agrocart.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.homeResponse.HomeResponse;
import com.poona.agrocart.data.network.responses.homeResponse.SeasonalProduct;
import com.poona.agrocart.databinding.RowSeasonalBannerBinding;

import java.util.ArrayList;

public class SeasonalBannerAdapter extends RecyclerView.Adapter<SeasonalBannerAdapter.SeasonBannerHolder> {
    private RowSeasonalBannerBinding seasonalBannerBinding;
    private final Context sbContext;
    private final ArrayList<SeasonalProduct> seasonalProducts;
    private View rootView;
    private final OnSeasonalClickListener onSeasonalClickListener;

    public SeasonalBannerAdapter(Context sbContext, ArrayList<SeasonalProduct> seasonalProducts,
                                 OnSeasonalClickListener onSeasonalClickListener) {
        this.sbContext = sbContext;
        this.seasonalProducts = seasonalProducts;
        this.onSeasonalClickListener = onSeasonalClickListener;
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

    }

    @Override
    public int getItemCount() {
        return seasonalProducts.size();
    }

    public interface OnSeasonalClickListener {
        void onSeasonRegister(String seasonalId);
    }

    public class SeasonBannerHolder extends RecyclerView.ViewHolder {

        public SeasonBannerHolder(RowSeasonalBannerBinding binding) {
            super(binding.getRoot());
        }

        private void bindSeasonItem(SeasonalProduct seasonalProduct) {
            if (seasonalProduct.getType().equals("Green"))
                seasonalBannerBinding.rlSeasonalView.setBackgroundResource(R.drawable.seasonal_banner_bg_green);
            else
                seasonalBannerBinding.rlSeasonalView.setBackgroundResource(R.drawable.seasonal_banner_bg_yellow);
            seasonalBannerBinding.setVariable(BR.moduleSeasonBanner, seasonalProduct);
            seasonalBannerBinding.executePendingBindings();
            seasonalBannerBinding.tvRegister.setOnClickListener(view -> {
                onSeasonalClickListener.onSeasonRegister(seasonalProduct.getId());
            });
        }
    }
}
