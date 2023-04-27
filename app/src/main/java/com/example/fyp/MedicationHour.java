package com.example.fyp;

import java.time.LocalTime;
import java.util.ArrayList;

public class MedicationHour {

    LocalTime time;
    ArrayList<Medication> meds;

    public MedicationHour(LocalTime time, ArrayList<Medication> meds) {
        this.time = time;
        this.meds = meds;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ArrayList<Medication> getMeds() {
        return meds;
    }

    public void setMeds(ArrayList<Medication> meds) {
        this.meds = meds;
    }
}
