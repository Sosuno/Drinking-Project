package com.drinkingTeam.drinkingProject.entities;

import com.drinkingTeam.drinkingProject.types.Drink;

public class DrinkEntity {
    private Long id;
    private String name;
    private String image;
    private String recipe;
    private String description;
    private String glass;

    public DrinkEntity() {}

    public DrinkEntity(Long id, String name,String image, String recipe, String description, String glass) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
