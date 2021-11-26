package com.poona.agrocart.ui.basket_page;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvBasketPageCardviewBinding;
import java.util.ArrayList;

public class BasketCardAdapter extends RecyclerView.Adapter<BasketCardAdapter.BasketCardViewholder>
{
    private ArrayList<BasketCard> basketCards;

    public BasketCardAdapter(ArrayList<BasketCard> basketCards) {
        this.basketCards = basketCards;
    }

    @NonNull
    @Override
    public BasketCardViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RvBasketPageCardviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.rv_basket_page_cardview, parent, false);
        return new BasketCardAdapter.BasketCardViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketCardViewholder holder, int position)
    {
        final BasketCard basketCard = basketCards.get(position);
        holder.rvBasketPageCardviewBinding.setBasketCard(basketCard);
        holder.bind(basketCard);
    }

    @Override
    public int getItemCount()
    {
        return basketCards.size();
    }

    public static class BasketCardViewholder extends RecyclerView.ViewHolder
    {
        RvBasketPageCardviewBinding rvBasketPageCardviewBinding;

        public BasketCardViewholder(RvBasketPageCardviewBinding rvBasketPageCardviewBinding)
        {
            super(rvBasketPageCardviewBinding.getRoot());
            this.rvBasketPageCardviewBinding=rvBasketPageCardviewBinding;
        }

        public void bind(BasketCard basketCard)
        {
            rvBasketPageCardviewBinding.setVariable(BR.basketCard,basketCard);
            rvBasketPageCardviewBinding.executePendingBindings();
        }
    }
}
