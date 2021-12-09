package com.poona.agrocart.ui.our_stores;

import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentOurStoresBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.our_stores.model.Store;
import com.poona.agrocart.ui.product_detail.ProductDetailFragment;
import com.poona.agrocart.ui.product_detail.ProductImageFragment;

import java.util.ArrayList;

public class OurStoresFragment extends BaseFragment
{
    private FragmentOurStoresBinding fragmentOurStoresBinding;
    private RecyclerView rvOurStores;
    private LinearLayoutManager linearLayoutManager;
    private OurStoreAdapter ourStoreAdapter;
    private ArrayList<Store> storeArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentOurStoresBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_our_stores, container, false);
        fragmentOurStoresBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentOurStoresBinding).getRoot();

        initView();
        setRvAdapter();

        return view;
    }

    private void setRvAdapter()
    {
        storeArrayList=new ArrayList<>();
        storeListData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvOurStores.setHasFixedSize(true);
        rvOurStores.setLayoutManager(linearLayoutManager);

        ourStoreAdapter = new OurStoreAdapter(storeArrayList);
        rvOurStores.setAdapter(ourStoreAdapter);
    }

    private void storeListData()
    {
        for(int i = 0; i < 3; i++)
        {
            Store store = new Store();
            store.setImg(getString(R.string.img_sample_store));
            store.setName(getString(R.string.poona_agro_center));
            store.setLocation(getString(R.string.vishrantwadi_pune));
            store.setContact(getString(R.string.sample_contact));
            store.setAbout(getString(R.string.sample_about_store));
            store.setAddress(getString(R.string.sample_address));
            storeArrayList.add(store);
        }
    }

    private void initView()
    {
        rvOurStores=fragmentOurStoresBinding.rvOurStores;
        initTitleBar(getString(R.string.our_stores));
    }
}