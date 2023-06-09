package com.example.fyp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Medication {

    String medicationName;
    private String time;
    private String date;


    public static ArrayList<Medication> medsList = new ArrayList<>();

    public Medication(){

    }
    public static ArrayList<Medication> medsForDate(LocalDate date){
        ArrayList<Medication> meds = new ArrayList<>();

        for(Medication medication: medsList){
            if(medication.getDate().equals(date)){
                meds.add(medication);
            }
        }

        return meds;
    }

    /*public static ArrayList<Medication> medsForHour(LocalDate date, LocalTime time){
        ArrayList<Medication> meds = new ArrayList<>();
        for(Medication medication: medsList){
            int medHour = medication.time.getHour();
            int layoutHour = time.getHour();
            if(medication.getDate().equals(date)&& medHour==layoutHour){
                meds.add(medication);
            }
        }

        return meds;
    }*/

    public Medication(String medicationName, String time, String date){
        this.medicationName = medicationName;
        this.time=time;
        this.date=date;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
