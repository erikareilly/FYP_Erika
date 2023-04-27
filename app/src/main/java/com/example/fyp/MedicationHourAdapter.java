package com.example.fyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MedicationHourAdapter extends ArrayAdapter<MedicationHour> {

    public MedicationHourAdapter(@NonNull Context context, List<MedicationHour> medicationHour) {
        super(context,0 ,medicationHour);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MedicationHour event = getItem(position);

        if(convertView==null){
            //inflate to layout of list view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.medication_layout, parent, false);
        }

        setTime(convertView, event.time);
        setMeds(convertView, event.meds);
        return convertView;
    }


    private void setTime(View convertView, LocalTime time) {
        TextView timeText =convertView.findViewById(R.id.time);
        timeText.setText(CalendarUtils.formattedShortTime(time));
    }

    private void setMeds(View convertView, ArrayList<Medication> meds) {
        TextView event1 =convertView.findViewById(R.id.event1);
        // TextView event2 =convertView.findViewById(R.id.event2);
        //TextView event3 =convertView.findViewById(R.id.event3);
//if no events hide event 1
        if(meds.size()==0){
            hideMeds(event1);
            //  hideEvent(event2);
            //hideEvent(event3);

        }else if(meds.size()==1){
            setMeds(event1, meds.get(0));
            // hideEvent(event2);
            //hideEvent(event3);
        }
        /*else  if(meds.size()==2){
            setMeds(event1, meds.get(0));
            setEvent(event2, meds.get(1));
            hideEvent(event3);
        }
        else if(meds.size()==3){
            setMeds(event1, events.get(0));
            setEvent(event2, events.get(1));
            setEvent(event3, events.get(2));
        }
        else{
            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            event3.setVisibility(View.VISIBLE);
    }*/
    }
    private void setMeds(TextView textView, Medication medication) {
        textView.setText(medication.getMedicationName());
        textView.setVisibility(View.VISIBLE);

    }

    private void hideMeds(TextView tv) {
        tv.setVisibility(View.INVISIBLE);
    }

}
