package com.drunkServer.rest.resource;

import java.util.List;

public class AddToFavsForm {
    private Long[] favourites;
    private String username;

    public Long[] getFavourites() {
        return favourites;
    }

    public void setFavourites(Long[] favourites) {
        this.favourites = favourites;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
