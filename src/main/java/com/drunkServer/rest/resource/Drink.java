package com.drunkServer.rest.resource;

import com.drunkServer.database.entities.DrinkEntity;
import com.drunkServer.database.entities.Ingredient;

import java.util.List;

public class Drink {

    private Long id;
    private String name;
    private String description;
    private String recipe;
    private byte[] image;
    private String glass;
    private List<Ingredient> ingredients;

    public Drink(Long id, String name, String description, String recipe, byte[] image, String glass, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.recipe = recipe;
        this.image = image;
        this.glass = glass;
        this.ingredients = ingredients;
    }

    public Drink(DrinkEntity drink, List<Ingredient> ingredients) {
        this.id = drink.getId();
        this.name = drink.getName();
        this.description = drink.getDescription();
        this.recipe = drink.getRecipe();
        this.image = drink.getImage();
        this.glass = drink.getGlass();
        this.ingredients = ingredients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getGlass() {
        return glass;
    }

    public void setGlass(String glass) {
        this.glass = glass;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
