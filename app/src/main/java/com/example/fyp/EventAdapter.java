package com.example.fyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {


    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);

        if(convertView==null){
            //inflate to layout of list view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.eventlayout, parent, false);
        }

        TextView eventLayoutTV=convertView.findViewById(R.id.eventLayoutTV);

        String eventTitle = event.getEvName()+ " " + CalendarUtils.formattedTime(event.getTime());
        eventLayoutTV.setText(eventTitle);
        return convertView;
    }
}
