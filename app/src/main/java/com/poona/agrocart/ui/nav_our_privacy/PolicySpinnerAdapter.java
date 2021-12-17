package com.poona.agrocart.ui.nav_our_privacy;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RowPolicyItemBinding;
import com.poona.agrocart.widgets.CustomTextView;

import java.util.ArrayList;

public class PolicySpinnerAdapter extends ArrayAdapter<PolicyItem> {

    private Context context;
    private ArrayList<PolicyItem> policyItems;
    private View view;

    public PolicySpinnerAdapter(Context context, ArrayList<PolicyItem> policyList) {
        super(context,0,policyList);
        this.policyItems = policyList;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public PolicyItem getItem(int position) {
        return policyItems.get(position);
    }

    @Override
    public int getCount() {
        return policyItems.size();
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

//        PolicyPlaceHolder holder;
        if (convertView == null) {
            RowPolicyItemBinding itemBinding = RowPolicyItemBinding.inflate(LayoutInflater.from(context));

            view = itemBinding.getRoot();

//            CustomTextView tv = view.findViewById(R.id.tv_policy);
//            ImageView img = view.findViewById(R.id.img);


            PolicyItem item = policyItems.get(position);
            itemBinding.setModulePolicy(item);
           /* holder = new PolicyPlaceHolder(itemBinding);
            holder.view = itemBinding.getRoot();
            holder.view.setTag(holder);*/
        }
//        else {
//            holder = (PolicyPlaceHolder) convertView.getTag();
//        }
//         holder.binding.setModulePolicy(policyItems.get(position));
        return view;
    }

//    private static class PolicyPlaceHolder {
//        private View view;
//        private RowPolicyItemBinding binding;
//
//        PolicyPlaceHolder(RowPolicyItemBinding binding) {
//            this.view = binding.getRoot();
//            this.binding = binding;
//        }
//    }
}
