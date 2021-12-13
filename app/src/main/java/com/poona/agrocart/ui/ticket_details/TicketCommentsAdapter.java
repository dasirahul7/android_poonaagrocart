package com.poona.agrocart.ui.ticket_details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.RvTicketCommentBinding;
import com.poona.agrocart.ui.ticket_details.model.Comment;

import java.util.ArrayList;

public class TicketCommentsAdapter extends RecyclerView.Adapter<TicketCommentsAdapter.CommentViewHolder>
{
    private final ArrayList<Comment> commentArrayList;

    public TicketCommentsAdapter(ArrayList<Comment> commentArrayList)
    {
        this.commentArrayList = commentArrayList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        RvTicketCommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.rv_ticket_comment, parent, false);
        return new TicketCommentsAdapter.CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position)
    {
        final Comment comment = commentArrayList.get(position);
        holder.rvTicketCommentBinding.setComment(comment);
        holder.bind(comment);
    }

    @Override
    public int getItemCount()
    {
        return commentArrayList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder
    {
        RvTicketCommentBinding rvTicketCommentBinding;

        public CommentViewHolder(RvTicketCommentBinding rvTicketCommentBinding)
        {
            super(rvTicketCommentBinding.getRoot());
            this.rvTicketCommentBinding = rvTicketCommentBinding;
        }

        public void bind(Comment comment)
        {
            rvTicketCommentBinding.setVariable(BR.comment,comment);
            rvTicketCommentBinding.executePendingBindings();
        }
    }
}
