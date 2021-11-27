package com.poona.agrocart.ui.explore;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.ExploreFragmentBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.explore.adapter.ExploreItemAdapter;
import com.poona.agrocart.ui.explore.model.ExploreItems;

import java.util.ArrayList;

public class ExploreFragment extends BaseFragment {

    private ExploreViewModel mViewModel;
    private ExploreFragmentBinding exploreFragmentBinding;
    private ExploreItemAdapter exploreItemAdapter;

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        exploreFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.explore_fragment,container,false);
        exploreFragmentBinding.setLifecycleOwner(this);
        View root = exploreFragmentBinding.getRoot();
        mViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        initTitleBar(getString(R.string.explore));
        setExploreList(root);
        return root;
    }

    private void setExploreList(View root) {
        mViewModel.exploreMutableLiveData.observe(requireActivity(),exploreItems -> {
            exploreItemAdapter = new ExploreItemAdapter(getActivity(),exploreItems,root);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
            exploreFragmentBinding.rvExplore.setLayoutManager(gridLayoutManager);
            exploreFragmentBinding.rvExplore.setHasFixedSize(true);
            exploreFragmentBinding.rvExplore.setAdapter(exploreItemAdapter);
            exploreItemAdapter.notifyDataSetChanged();
        });
    }

}