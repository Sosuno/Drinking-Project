package com.drinkingTeam.drinkingProject.types;

import java.util.List;

public class Drink {

    private Long id;
    private String name;
    private String description;
    private String recipe;
    private String image;
    private String glass;
    private List<Ingredient> ingredients;

    public Drink() {}

    public Drink(Long id, String name, String description, String recipe, String image, String glass, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.recipe = recipe;
        this.image = image;
        this.glass = glass;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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



