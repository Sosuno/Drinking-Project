package com.iqlearning.database.utils;


import com.iqlearning.database.entities.DrinkEntity;
import com.iqlearning.database.entities.User;


public class LoggedUser {

    private Long id;
    private String username;
    private String avatar;
    private String email;
    private String sessionID;
    private DrinkEntity[] drinks;


    public LoggedUser() {

    }
    public LoggedUser(User user, String sessionID) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.sessionID = sessionID;
        this.drinks = user.getFavouriteDrinks();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public DrinkEntity[] getDrinks() {
        return drinks;
    }

    public void setDrinks(DrinkEntity[] drinks) {
        this.drinks = drinks;
    }
}