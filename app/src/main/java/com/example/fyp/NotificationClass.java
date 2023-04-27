package com.example.fyp;

import java.util.ArrayList;

public class NotificationClass {


    String notificationTitle;
    String notificationDescription;
    String notificationTime;
    String notificationDate;
    public static ArrayList<NotificationClass> notifList = new ArrayList<>();

    public NotificationClass(){}

    public NotificationClass(String notificationTitle, String notificationDescription, String notificationTime, String notificationDate) {

        this.notificationTitle = notificationTitle;
        this.notificationDescription = notificationDescription;
        this.notificationTime = notificationTime;
        this.notificationDate = notificationDate;
    }


    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public void setNotificationDescription(String notificationDescription) {
        this.notificationDescription = notificationDescription;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }
}
