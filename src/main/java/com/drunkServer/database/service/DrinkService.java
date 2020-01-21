package com.drunkServer.database.service;

import com.drunkServer.database.entities.DrinkEntity;
import com.drunkServer.database.entities.Ingredient;
import com.drunkServer.database.repository.DrinkRepository;
import com.drunkServer.database.repository.IngredientRepository;
import com.drunkServer.rest.resource.Drink;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

@Service
public class DrinkService {

    @Autowired
    DrinkRepository repo;

    @Autowired
    IngredientRepository ingredientRepository;

    public List<Drink> getAllDrinks(){
        List<DrinkEntity> drinksEntities = repo.getAllBy();
        return entityListToDrinkList(drinksEntities);
    }

    public List<Drink> getAllFavourites(Long[] userFavs) {
        List<DrinkEntity> drinksEntities = repo.getAllByIdIn(userFavs);
        return entityListToDrinkList(drinksEntities);
    }

    private List<Drink> entityListToDrinkList(List<DrinkEntity> entities) {
        List<Drink> drinks = new ArrayList<>();
        for(DrinkEntity d : entities){
            List<Ingredient> ingredients = ingredientRepository.getAllByDrinkId(d.getId());
            Drink drink = new Drink(d,ingredients);
            drinks.add(drink);
        }
        return drinks;

    }

    @PostConstruct
    private void init() throws IOException {
        repo.deleteAll();
        ingredientRepository.deleteAll();
        String name = "Bramble Cocktail";
        String recipe = "\n" +
                "\n" +
                "Add the gin, lemon juice and simple syrup into a shaker with ice and shake.\n" +
                "\n" +
                "Fine-strain into an Old Fashioned glass over crushed ice.\n" +
                "\n" +
                "Lace over top with the creme de mure.\n" +
                "\n" +
                "Garnish with a lemon half-wheel and a fresh blackberry.";
        String desc = "Famed bartender Dick Bradsell invented this classic at Fred’s Club in London’s Soho in the ’80s, inspired by the fresh blackberries he used to get as a child on the Isle of Wight. ";
        String glass = "Old Fashioned";

        DrinkEntity drinkEntity = new DrinkEntity(name,desc,recipe,glass);
        InputStream is = getClass().getResourceAsStream("/images/bramble.jpg");

        byte[] bytes = IOUtils.toByteArray(is);
        drinkEntity.setImage(bytes);
        Long id = repo.save(drinkEntity).getId();

        Ingredient i1 = new Ingredient("Gin","60","ml", id);
        Ingredient i2 = new Ingredient("Freshly squeezed lemon juice ","30","ml", id);
        Ingredient i4 = new Ingredient("Crème de mure","15","ml", id);
        Ingredient i3 = new Ingredient("Simple syrup","2","barspoons", id);

        ingredientRepository.save(i1);
        ingredientRepository.save(i2);
        ingredientRepository.save(i4);
        ingredientRepository.save(i3);

        name = "Spazerac";
        recipe  = "\n" +
                "\n" +
                "Add all the ingredients to a blender with just under 1 cup of ice.\n" +
                "\n" +
                "Blend until smooth and pour into an Old Fashioned glass.\n" +
                "\n" +
                "Garnish with a lemon twist.";
        desc = "Try this unusual frozen spin on the classic Sazerac.";
        glass = "Old Fashioned";

        drinkEntity = new DrinkEntity(name,desc,recipe,glass);
        is = getClass().getResourceAsStream("/images/spazerac.jpg");

        bytes = IOUtils.toByteArray(is);
        drinkEntity.setImage(bytes);
        id = repo.save(drinkEntity).getId();
        i1 = new Ingredient("Rye whiskey","75","ml", id);
        i2 = new Ingredient("Lemon juice ","7.5","ml", id);
        i4 = new Ingredient("Peychaud's bitters","3","dashes", id);
        i3 = new Ingredient("Simple syrup","15","ml", id);
        Ingredient i5 = new Ingredient("Absinthe","1","tsp", id);

        ingredientRepository.save(i1);
        ingredientRepository.save(i2);
        ingredientRepository.save(i4);
        ingredientRepository.save(i3);
        ingredientRepository.save(i5);

    }
}
