package com.poona.agrocart.ui.nav_notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.databinding.RowNotificationBinding;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    private ArrayList<Notification> notifications = new ArrayList<>();
    private final Context context;
    private RowNotificationBinding notificationBinding;

    public NotificationAdapter(ArrayList<Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        notificationBinding = RowNotificationBinding.inflate(LayoutInflater.from(context),parent,false);
        return new NotificationHolder(notificationBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bindContent(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        public NotificationHolder(RowNotificationBinding binding) {
            super(binding.getRoot());
        }

        public void bindContent(Notification notification) {
            notificationBinding.setVariable(BR.moduleNotification,notification);
        }
    }
}
