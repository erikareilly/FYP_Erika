package com.example.fyp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

//POJO
public class Event {

    private String evName;
    private String evDescr;
    private LocalDateTime date;
    private Long millis;

    private LocalTime time;

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
            int evHour = event.time.getHour();
            int layoutHour = time.getHour();
            if(event.getDate().equals(date)&& evHour==layoutHour){
                events.add(event);
            }
        }

        return events;
    }

    public Event(){

    }

    public Event(String evName, String evDescr, LocalDateTime date, LocalTime time) {
        this.evName = evName;
        this.evDescr=evDescr;
        this.date = date;
       // this.millis=millis;
        this.time = time;

    }

//generate getters and setters

    public Long getMillis() {
        return millis;
    }

    public void setMillis(Long millis) {
        this.millis = millis;
    }

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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalTime getTime() {return time;}

    public void setTime(LocalTime time) {this.time = time;}

    public static ArrayList<Event> getEventsList() {
        return eventsList;
    }

    public static void setEventsList(ArrayList<Event> eventsList) {
        Event.eventsList = eventsList;
    }
}
