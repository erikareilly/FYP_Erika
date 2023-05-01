package com.example.fyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder>{

    private final ItemClickListener itemClickListener;
    Context context;
    ArrayList<Medication> list;

    public MedicationAdapter(Context context, ArrayList<Medication>list,ItemClickListener itemClickListener ){
        this.context=context;
        this.list=list;
        this.itemClickListener=itemClickListener;
    }
    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.medicationlayout,parent, false);
        return new MedicationAdapter.MedicationViewHolder(v, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationAdapter.MedicationViewHolder holder, int position) {
        Medication medication = list.get(position);
        //holder.time1.setText(medication.getTime());
        holder.event.setText(medication.getMedicationName());
        holder.time.setText(medication.getTime());
        holder.date.setText(medication.getDate());
        // mRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Medication");



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MedicationViewHolder extends RecyclerView.ViewHolder{

        TextView event, time,date;
        public MedicationViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            event = itemView.findViewById(R.id.medicationLayoutTV);
            time = itemView.findViewById(R.id.medicationTime);
            date=itemView.findViewById(R.id.medicationDate);
        }
    }


}
