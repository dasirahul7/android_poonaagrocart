package com.poona.agrocart.ui.nav_stores;


import static com.poona.agrocart.app.AppConstants.IMAGE_DOC_BASE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvStoreBinding;
import com.poona.agrocart.ui.nav_stores.model.OurStoreListData;

import java.util.ArrayList;

public class OurStoreAdapter extends RecyclerView.Adapter<OurStoreAdapter.OurStoreViewHolder> {
    private final ArrayList<OurStoreListData> ourStoresList;
    private final Context context;
    private final OnStoreClickListener onStoreClickListener;
    private ImageView imageView;

    public OurStoreAdapter(ArrayList<OurStoreListData> ourStoresList, Context context, OurStoresFragment ourStoresFragment) {
        this.context = context;
        this.ourStoresList = ourStoresList;
        this.onStoreClickListener = ourStoresFragment;
    }

    @NonNull
    @Override
    public OurStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvStoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_store, parent, false);
        return new OurStoreAdapter.OurStoreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OurStoreViewHolder holder, int position) {
        OurStoreListData store = ourStoresList.get(position);
        holder.rvStoreBinding.setStore(store);
        holder.bind(store);

        imageView = holder.rvStoreBinding.ivIcon;
        // set page image
        Glide.with(context)
                .load(IMAGE_DOC_BASE_URL + ourStoresList.get(position).getStoreImage())
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return ourStoresList.size();
    }

    public interface OnStoreClickListener {
        void itemViewClick(int position);
    }

    public class OurStoreViewHolder extends RecyclerView.ViewHolder {
        RvStoreBinding rvStoreBinding;

        public OurStoreViewHolder(RvStoreBinding rvStoreBinding) {
            super(rvStoreBinding.getRoot());
            this.rvStoreBinding = rvStoreBinding;

            itemView.setOnClickListener(v -> {
                if (onStoreClickListener != null) {
                    int postion = getBindingAdapterPosition();
                    if (postion != RecyclerView.NO_POSITION) {
                        onStoreClickListener.itemViewClick(postion);
                    }
                }
            });

           /* this.rvStoreBinding.storeView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("store",ourStoresList.get(getAdapterPosition()));
                StoreDetailFragment.newInstance(ourStoresList.get(getAdapterPosition()));
                Navigation.findNavController(v).navigate(R.id.action_nav_store_to_storeLocationFragment,bundle);
            });*/
        }

        public void bind(OurStoreListData store) {
            rvStoreBinding.setVariable(BR.store, store);
            rvStoreBinding.executePendingBindings();
        }
    }
}
