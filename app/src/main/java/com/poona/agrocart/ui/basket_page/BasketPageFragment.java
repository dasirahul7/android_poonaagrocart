package com.poona.agrocart.ui.basket_page;

import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentBasketPageBinding;
import com.poona.agrocart.ui.BaseFragment;

import java.util.ArrayList;

public class BasketPageFragment extends BaseFragment
{
    private FragmentBasketPageBinding fragmentBasketPageBinding;
    private RecyclerView rvBasketCards;
    private ArrayList<BasketCard> basketCardsList;
    private BasketCardAdapter basketCardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentBasketPageBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_basket_page, container, false);
        fragmentBasketPageBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentBasketPageBinding).getRoot();

        initView();
        setRVAdapter();

        initTitleBar(getString(R.string.basket));

        return view;
    }

    private void setRVAdapter()
    {
        basketCardsList =new ArrayList<>();
        prepareListingData();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(),2);
        rvBasketCards.setHasFixedSize(true);
        rvBasketCards.setLayoutManager(gridLayoutManager);

        basketCardAdapter = new BasketCardAdapter(basketCardsList);

        rvBasketCards.setAdapter(basketCardAdapter);
    }

    private void initView()
    {
        this.rvBasketCards=fragmentBasketPageBinding.rvBasketCards;
    }

    private void prepareListingData()
    {
        for(int i = 0; i < 6; i++)
        {
            BasketCard basketCard = new BasketCard();
            basketCard.setProductName(getString(R.string.sprouts_basket));
            basketCard.setDiscountedPrice(getString(R.string.rs_800));
            basketCard.setImgUrl(String.valueOf(R.drawable.img_diet_basket));
            basketCardsList.add(basketCard);
        }
    }
}