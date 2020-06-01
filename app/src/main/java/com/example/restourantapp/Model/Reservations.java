package com.example.restourantapp.Model;

public class Reservations {

    String dateAndTimeRes, restaurantName, userID, data, time, numberOfPersons, reservationID;

    public Reservations() {
    }

    public Reservations(String dateAndTimeRes, String restaurantName, String userID, String data, String time, String numberOfPersons, String reservationID) {
        this.dateAndTimeRes = dateAndTimeRes;
        this.restaurantName = restaurantName;
        this.userID = userID;
        this.data = data;
        this.time = time;
        this.numberOfPersons = numberOfPersons;
        this.reservationID = reservationID;
    }

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }

    public String getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(String numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public String getDateAndTimeRes() {
        return dateAndTimeRes;
    }

    public void setDateAndTimeRes(String dateAndTimeRes) {
        this.dateAndTimeRes = dateAndTimeRes;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
