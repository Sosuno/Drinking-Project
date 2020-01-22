package com.drinkingTeam.drinkingProject.types;

public class Ingredient {

    private Long id;
    private String name;
    private String quantity;
    private String units;

    public Ingredient(){}

    public Ingredient(Long id, String name, String quantity, String units) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.units = units;
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


}
