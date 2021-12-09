package com.poona.agrocart.ui.our_stores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvStoreBinding;
import com.poona.agrocart.ui.our_stores.model.Store;

import java.util.ArrayList;

public class OurStoreAdapter extends RecyclerView.Adapter<OurStoreAdapter.OurStoreViewHolder>
{
    private ArrayList<Store> ourStoresList;

    public OurStoreAdapter(ArrayList<Store> ourStoresList)
    {
        this.ourStoresList = ourStoresList;
    }

    @NonNull
    @Override
    public OurStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RvStoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_store, parent, false);
        return new OurStoreAdapter.OurStoreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OurStoreViewHolder holder, int position)
    {
        final Store store = ourStoresList.get(position);
        holder.rvStoreBinding.setStore(store);
        holder.bind(store);
    }

    @Override
    public int getItemCount()
    {
        return ourStoresList.size();
    }



    public class OurStoreViewHolder extends RecyclerView.ViewHolder
    {
        RvStoreBinding rvStoreBinding;

        public OurStoreViewHolder(RvStoreBinding rvStoreBinding)
        {
            super(rvStoreBinding.getRoot());
            this.rvStoreBinding=rvStoreBinding;
            this.rvStoreBinding.storeView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("store",ourStoresList.get(getAdapterPosition()));
                StoreDetailFragment.newInstance(ourStoresList.get(getAdapterPosition()));
                Navigation.findNavController(v).navigate(R.id.action_nav_store_to_storeLocationFragment,bundle);
            });
        }

        public void bind(Store store)
        {
            rvStoreBinding.setVariable(BR.store,store);
            rvStoreBinding.executePendingBindings();
        }
    }
}
