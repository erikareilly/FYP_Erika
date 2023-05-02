package com.example.fyp;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPR_RIGHT = 1;
    Context context;
    List<Message> list;

    public MessageAdapter(Context context, List<Message> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_left, parent, false);
            return new MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_right, parent, false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String message = list.get(position).getMessage();
        String timeStamp = list.get(position).getTimestamp();
        String type = list.get(position).getType();
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        holder.message.setText(message);
        holder.time.setText(timedate);

        if (type.equals("text")) {
            holder.message.setVisibility(View.VISIBLE);
            holder.mimage.setVisibility(View.GONE);
            holder.message.setText(message);
        } else {
            holder.message.setVisibility(View.GONE);
            holder.mimage.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

   /* @Override
    public int getItemViewType(int position) {
        if (list.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPR_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }*/

    class MessageViewHolder extends RecyclerView.ViewHolder {

        ImageView mimage;
        TextView message, time, isSee;
        LinearLayout msglayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.msgc);
            time = itemView.findViewById(R.id.timetv);
            isSee = itemView.findViewById(R.id.isSeen);
            msglayout = itemView.findViewById(R.id.linearright);
            mimage = itemView.findViewById(R.id.images);
        }
    }
}
