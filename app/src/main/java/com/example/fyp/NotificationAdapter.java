package com.example.fyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    Context context;
    List<NotificationClass> list;

    public NotificationAdapter(Context context, List<NotificationClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification,parent, false);
        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationClass notificationClass = list.get(position);
        holder.titleText.setText(notificationClass.getNotificationTitle());
        holder.descriptionText.setText(notificationClass.getNotificationDescription());
        holder.timetext.setText(notificationClass.getNotificationTime());
        holder.datetext.setText(notificationClass.getNotificationDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        private EditText titleText, descriptionText;
        private TextView datetext, timetext;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = (EditText) itemView.findViewById(R.id.notifTitle);
            descriptionText = (EditText) itemView.findViewById(R.id.notifDescrip);
            datetext = (TextView) itemView.findViewById(R.id.date);
            timetext = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
