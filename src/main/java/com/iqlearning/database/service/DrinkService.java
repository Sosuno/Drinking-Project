package com.iqlearning.database.service;

import com.iqlearning.database.entities.DrinkEntity;
import com.iqlearning.database.entities.Ingredient;
import com.iqlearning.database.repository.DrinkRepository;
import com.iqlearning.database.repository.IngredientRepository;
import com.iqlearning.rest.resource.Drink;
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
        List<Drink> drinks = new ArrayList<>();
        List<DrinkEntity> drinksEntities = repo.getAllBy();
        for(DrinkEntity d : drinksEntities){
            List<Ingredient> ingredients = ingredientRepository.getAllByDrinkId(d.getId());
            Drink drink = new Drink(d,ingredients);
            //drink.setImage(null);
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
        Long[] ingredients = new Long[] {i1.getId(),i2.getId(),i3.getId(),i4.getId()};
    }
}
