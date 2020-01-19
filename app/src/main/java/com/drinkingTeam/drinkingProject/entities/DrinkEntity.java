package com.drinkingTeam.drinkingProject.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.drinkingTeam.drinkingProject.Drink;
import com.drinkingTeam.drinkingProject.tables.DrinksReaderContract;
import com.drinkingTeam.drinkingProject.tables.DrinksDbHelper;

import java.util.ArrayList;
import java.util.List;

public class DrinkEntity {
    private long id;
    private String name;
    private String image;
    private String recipe;
    private String description;
    private String glass;

    public DrinkEntity() {}

    public DrinkEntity(long id, String name,String image, String recipe, String description, String glass) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.recipe = recipe;
        this.description = description;
        this.glass = glass;
    }

    public DrinkEntity(Drink d){
        this.id = d.getId();
        this.name = d.getName();
        this.image = d.getImage();
        this.recipe = d.getRecipe();
        this.description = d.getDescription();
        this.glass = d.getGlass();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGlass() {
        return glass;
    }

    public void setGlass(String glass) {
        this.glass = glass;
    }
}
