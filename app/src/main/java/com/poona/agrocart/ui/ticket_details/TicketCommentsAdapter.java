package com.poona.agrocart.ui.ticket_details;

import static com.poona.agrocart.app.AppConstants.IMAGE_DOC_BASE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.help_center_response.recieveMessage.AllChat;
import com.poona.agrocart.databinding.RvTicketCommentBinding;
import com.poona.agrocart.widgets.imageview.CircularImageView;

import java.text.ParseException;
import java.util.ArrayList;

public class TicketCommentsAdapter extends RecyclerView.Adapter<TicketCommentsAdapter.CommentViewHolder> {
    private final ArrayList<AllChat> commentArrayList;
    private final TicketDetailFragment ticketDetailFragment;
    private CircularImageView imageView;
    private final Context context;

    public TicketCommentsAdapter(ArrayList<AllChat> commentArrayList, TicketDetailFragment ticketDetailFragment, Context context) {
        this.commentArrayList = commentArrayList;
        this.ticketDetailFragment = ticketDetailFragment;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvTicketCommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_ticket_comment, parent, false);
        return new TicketCommentsAdapter.CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        AllChat comment = commentArrayList.get(position);
        holder.rvTicketCommentBinding.setComment(comment);
        holder.bind(comment);

        if (comment.getUsername() == null) {
            holder.rvTicketCommentBinding.tvUserName.setText("N/A");
        }

        imageView = holder.rvTicketCommentBinding.ivProfileImage;
        Glide.with(context)
                .load(IMAGE_DOC_BASE_URL + comment.getImage())
                .placeholder(R.drawable.ic_profile_white)
                .error(R.drawable.ic_profile_white)
                .into(imageView);

        String selectedDate = comment.getCreatedOn();
        String txtDisplayDate = "";
        try {
            txtDisplayDate = ticketDetailFragment.formatDate(selectedDate, "yyyy-mm-dd hh:mm:ss", "dd MMM yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.rvTicketCommentBinding.tvDate.setText(txtDisplayDate);
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        RvTicketCommentBinding rvTicketCommentBinding;

        public CommentViewHolder(RvTicketCommentBinding rvTicketCommentBinding) {
            super(rvTicketCommentBinding.getRoot());
            this.rvTicketCommentBinding = rvTicketCommentBinding;
        }

        public void bind(AllChat comment) {
            rvTicketCommentBinding.setVariable(BR.comment, comment);
            rvTicketCommentBinding.executePendingBindings();
        }
    }
}
