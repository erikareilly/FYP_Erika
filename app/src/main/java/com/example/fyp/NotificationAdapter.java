package com.example.fyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    Context context;
    List<NotificationClass> list;
    DatabaseReference nReference;

    public NotificationAdapter(Context context, List<NotificationClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notificationlayout,parent, false);
        return new NotificationAdapter.NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        NotificationClass notificationClass = list.get(position);
        holder.titleText.setText(notificationClass.getNotificationTitle());
        holder.descriptionText.setText(notificationClass.getNotificationDescription());
        holder.timetext.setText(notificationClass.getNotificationTime());
        holder.datetext.setText(notificationClass.getNotificationDate());
        nReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Notifications");

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(holder.getAdapterPosition());
            }
        });

    }

    public void removeAt(int position){
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,list.size());
        nReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Notifications");
        nReference.removeValue();
    }

    public void editAt(int position){


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText, descriptionText;
        private TextView datetext, timetext;
        private Button editButton, deleteButton;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = (EditText) itemView.findViewById(R.id.titleNotif);
            descriptionText = (EditText) itemView.findViewById(R.id.notif_description);
            datetext = (TextView) itemView.findViewById(R.id.notif_date);
            timetext = (TextView) itemView.findViewById(R.id.notif_time);

            deleteButton = (Button) itemView.findViewById(R.id.deleteNotifs);
        }
    }
}
