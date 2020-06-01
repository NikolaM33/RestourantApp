package com.example.restourantapp.Model;

import java.util.List;

public class Request {
    private String phone;
    private String address;
    private String total;
    private List<Order> foods;
    private String userID;
    private String status;
    private String orderID;


    public Request() {
    }

    public Request(String address, String total, List<Order> foods, String userID, String phone, String orderID) {
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.foods = foods;
        this.userID = userID;
        this.orderID = orderID;
        this.status = "0"; //Default 0, 0:Placed, 1:Shipping, 2:Shipped
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
