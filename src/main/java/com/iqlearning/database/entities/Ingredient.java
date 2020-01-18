package com.iqlearning.database.entities;

import javax.persistence.*;

@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "quantity")
    private String quantity;
    @Column(name = "units")
    private String units;
    @Column(name = "drink_id")
    private Long drinkId;

    public Ingredient() {}

    public Ingredient(String name, String quantity, String units, Long drinkId) {
        this.name = name;
        this.quantity = quantity;
        this.units = units;
        this.drinkId = drinkId;
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
