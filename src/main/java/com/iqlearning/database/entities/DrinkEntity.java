package com.iqlearning.database.entities;

import javax.persistence.*;

@Entity
@Table(name = "drinks")
public class DrinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "recipe", nullable = false)
    private String recipe;
    @Column(name = "image", nullable = false)
    private byte[] image;
    @Column(name = "glass")
    private String glass;

    public DrinkEntity(){}

    public DrinkEntity(String name, String description, String recipe, String glass) {
        this.name = name;
        this.description = description;
        this.recipe = recipe;
        this.image = null;
        this.glass = glass;
    }

    public DrinkEntity(String name, String description, String recipe, byte[] image, String glass) {
        this.name = name;
        this.description = description;
        this.recipe = recipe;
        this.image = image;
        this.glass = glass;

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

}
