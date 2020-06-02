package com.example.restourantapp.Model;

import java.util.List;

public class Request {
    private String phone;
    private String address;
    private String total;
    private List<Order> foods;
    private String userID;
    private String orderID;
    private String comment;


    public Request() {
    }

    public Request(String address, String total, List<Order> foods, String userID, String phone, String comment, String orderID) {
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.foods = foods;
        this.userID = userID;
        this.comment = comment;
        this.orderID = orderID;


    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}