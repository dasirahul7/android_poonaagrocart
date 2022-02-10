package com.poona.agrocart.ui.nav_faq.adaptor;

import static com.poona.agrocart.widgets.ExpandIconView.LESS;
import static com.poona.agrocart.widgets.ExpandIconView.MORE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FaqRecyclerViewItemBinding;
import com.poona.agrocart.ui.nav_faq.FaQFragment;
import com.poona.agrocart.ui.nav_faq.model.FaqListData;

import java.util.ArrayList;
import java.util.List;

public class FaqListAdaptor extends RecyclerView.Adapter<FaqListAdaptor.FaqViewHolder> {
    private List<FaqListData> faqListDataList = new ArrayList<>();
    private Context context;
    private FaQFragment faQFragment;

    public FaqListAdaptor (Context context, List<FaqListData> faqListData, FaQFragment faQFragment){
        this.context = context;
        this.faqListDataList = faqListData;
        this.faQFragment = faQFragment;
    }


    @NonNull
    @Override
    public FaqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FaqRecyclerViewItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.faq_recycler_view_item,parent,false);

        return new FaqListAdaptor.FaqViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull FaqViewHolder viewHolder, int position) {
        FaqListData faq = faqListDataList.get(position);
        viewHolder.binding.setFaqListData(faq);
        viewHolder.bind(faq);

        viewHolder.itemView.setTag("less");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                if(viewHolder.itemView.getTag().equals("more")){
                    viewHolder.binding.expandQuestion.setState(MORE, true);
                    faQFragment.collapse(viewHolder.binding.llAnswer);
                    viewHolder.binding.tvQuestion.setTextColor(ContextCompat.getColor(context, R.color.black));
                    viewHolder.binding.rlMain.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_et_border));
                    viewHolder.binding.llMain.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_et_border));
                    viewHolder.binding.rlMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    viewHolder.itemView.setTag("less");
                } else {
                    viewHolder.binding.expandQuestion.setState(LESS, true);
                    faQFragment.expand(viewHolder.binding.llAnswer);
                    viewHolder.binding.tvQuestion.setTextColor(ContextCompat.getColor(context, R.color.white));
                    viewHolder.binding.rlMain.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_et_border));
                    viewHolder.binding.llMain.setBackground(ContextCompat.getDrawable(context,R.drawable.background_faq_list));
                    viewHolder.binding.tvAnswer.setTextColor(ContextCompat.getColor(context, R.color.black));
                    viewHolder.binding.rlMain.setBackground(ContextCompat.getDrawable(context, R.drawable.background_faq_list_answer));
                   // viewHolder.binding.rlMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    viewHolder.itemView.setTag("more");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return faqListDataList.size();
    }

    public class FaqViewHolder extends RecyclerView.ViewHolder {

        public FaqRecyclerViewItemBinding binding;

        public FaqViewHolder(FaqRecyclerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        public void bind(FaqListData faq) {
            binding.setVariable(BR.faqListData,faq);
            binding.executePendingBindings();
        }
    }
}
