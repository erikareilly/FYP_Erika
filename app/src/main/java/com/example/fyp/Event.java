package com.example.fyp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//POJO
public class Event {

    private String evName;
    private String evDescr;
    private String date;
    private Long millis;
    private LocalTime time1;

    private String time;

    public static ArrayList<Event> eventsList = new ArrayList<>();


    public static ArrayList<Event> eventsForDate(LocalDate date){
        ArrayList<Event> events = new ArrayList<>();

        for(Event event: eventsList){
            if(event.getDate().equals(date)){
                events.add(event);
            }
        }

        return events;
    }
    public static ArrayList<Event> eventsForHour(LocalDate date, LocalTime time){
        ArrayList<Event> events = new ArrayList<>();
        for(Event event: eventsList){
            String eventHour = (event.time);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time2 = LocalTime.parse(eventHour,formatter);
            int evHour = time2.getHour();
            // int evHour = event.time1.getHour();
            int layoutHour = time.getHour();
            if(event.getDate().equals(date)&& evHour==layoutHour){
                events.add(event);
            }
        }

        return events;
    }

    public Event(){

    }

    public Event(String evName, String evDescr, String date, String time) {
        this.evName = evName;
        this.evDescr=evDescr;
        this.date = date;
        this.time = time;

    }

//generate getters and setters

    public String getEvName() {
        return evName;
    }

    public void setEvName(String evName) {
        this.evName = evName;
    }

    public String getEvDescr() {
        return evDescr;
    }

    public void setEvDescr(String evName) {
        this.evDescr = evDescr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {return time;}

    public void setTime(String time) {this.time = time;}

    public static ArrayList<Event> getEventsList() {
        return eventsList;
    }

    public static void setEventsList(ArrayList<Event> eventsList) {
        Event.eventsList = eventsList;
    }

    public LocalTime getTime1() {
        return time1;
    }

    public void setTime1(LocalTime time1) {
        this.time1 = time1;
    }
}