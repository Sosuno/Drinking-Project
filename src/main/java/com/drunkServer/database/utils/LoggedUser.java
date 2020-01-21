package com.drunkServer.database.utils;


import com.drunkServer.database.entities.DrinkEntity;
import com.drunkServer.database.entities.User;
import com.drunkServer.rest.resource.Drink;

import java.util.List;


public class LoggedUser {

    private Long id;
    private String username;
    private String avatar;
    private String email;

    private List<Drink> drinks;

    public LoggedUser() {

    }
    public LoggedUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.drinks = null;
    }
    public LoggedUser(User user,  List<Drink> drinks) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();

        this.drinks = drinks;
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

    public  List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks( List<Drink> drinks) {
        this.drinks = drinks;
    }
}