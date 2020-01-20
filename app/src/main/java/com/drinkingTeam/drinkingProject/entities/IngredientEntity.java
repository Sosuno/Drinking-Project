package com.drinkingTeam.drinkingProject.entities;

import com.drinkingTeam.drinkingProject.Ingredient;

public class IngredientEntity {

    private Long id;
    private String name;
    private String quantity;
    private String units;
    private Long drinkId;

    public IngredientEntity(Long id, String name, String quantity, String units, Long drinkId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.units = units;
        this.drinkId = drinkId;
    }

    public IngredientEntity(Ingredient ingredient) {
        this.id = ingredient.getId();
        this.name = ingredient.getName();
        this.quantity = ingredient.getQuantity();
        this.units = ingredient.getUnits();
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Long getDrinkId() {
        return drinkId;
    }

    public void setDrinkId(Long drinkId) {
        this.drinkId = drinkId;
    }
}
