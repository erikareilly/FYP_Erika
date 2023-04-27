package com.example.fyp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String text = bundle.getString("title");
        String description = bundle.getString("descr");
       // @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"My Notification")
                .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
                        .setContentTitle(text).setContentText(description)
                        .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH);
       // builder.setContentTitle(text);
        //builder.setContentText(description);
        //builder.setSmallIcon(R.drawable.ic_baseline_access_alarm_24);
        //builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(context);
        managerCompat.notify(1,builder.build());

// Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());

    }

    }

