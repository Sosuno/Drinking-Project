package com.iqlearning.database.entities;
/*****
 * Author: Ewa Jasinska
 */


import org.hibernate.annotations.Type;

import javax.persistence.*;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "favourite_drinks", columnDefinition = "BIGINT[]")
    @Type(type = "long-array")
    private DrinkEntity[] favouriteDrinks = null;

    public User(){}

    public User(String username, String password, String email, DrinkEntity[] favouriteDrinks) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.favouriteDrinks = favouriteDrinks;
    }

    public User(boolean exists){
        this.id = -1L;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DrinkEntity[] getFavouriteDrinks() {
        return favouriteDrinks;
    }

    public void setFavouriteDrinks(DrinkEntity[] favouriteDrinks) {
        this.favouriteDrinks = favouriteDrinks;
    }


}
