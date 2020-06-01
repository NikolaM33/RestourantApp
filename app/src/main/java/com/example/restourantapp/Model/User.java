package com.example.restourantapp.Model;

public class User {
    private String Name;
    private String Password;
    private String Email;
    private String PhoneNumber;
    private String LastName;

    public User() {


    }

    public User(String name, String password, String email, String phoneNumber, String lastName) {
        Name = name;
        Password = password;
        Email = email;
        PhoneNumber = phoneNumber;
        LastName = lastName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


}
