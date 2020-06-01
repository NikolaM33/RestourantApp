package com.example.restourantapp.Model;

public class RatingFood {

    private String userID, ratingID, rateValue, comment, restaurantIDFoodID, userResFood;

    public RatingFood(String userID, String ratingID, String rateValue, String comment, String restaurantIDFoodID, String userResFood) {
        this.userID = userID;
        this.ratingID = ratingID;
        this.rateValue = rateValue;
        this.comment = comment;
        this.restaurantIDFoodID = restaurantIDFoodID;
        this.userResFood = userResFood;
    }

    public RatingFood() {
    }

    public String getUserResFood() {
        return userResFood;
    }

    public void setUserResFood(String userResFood) {
        this.userResFood = userResFood;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRestaurantIDFoodID() {
        return restaurantIDFoodID;
    }

    public void setRestaurantIDFoodID(String restaurantIDFoodID) {
        this.restaurantIDFoodID = restaurantIDFoodID;
    }
}
