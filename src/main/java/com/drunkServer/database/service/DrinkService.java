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

        /*******************
         * 1
         * **********************************/
        String name = "Bramble Cocktail";
        String recipe = "Add the gin, lemon juice and simple syrup into a shaker with ice and shake.\n" +
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
        Ingredient i2 = new Ingredient("Lemon juice ","30","ml", id);
        Ingredient i4 = new Ingredient("Crème de mure","15","ml", id);
        Ingredient i3 = new Ingredient("Simple syrup","2","barspoons", id);

        ingredientRepository.save(i1);
        ingredientRepository.save(i2);
        ingredientRepository.save(i4);
        ingredientRepository.save(i3);

        /*******************
         * 2
         * **********************************/
        name = "Spazerac";
        recipe  = "Add all the ingredients to a blender with just under 1 cup of ice.\n" +
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

        /*******************
         * 3
         * **********************************/
        name = "Employees Manhattan";
        recipe = "Add all the ingredients into a mixing glass with ice and stir.\n" +
                "\n" +
                "Strain into a cocktail glass.\n" +
                "\n" +
                "Garnish with a lemon twist.";
        desc = "A pair of famed bartenders created this subtle variation on the classic.";
        glass = "Cocktail";

        drinkEntity = new DrinkEntity(name,desc,recipe,glass);
        is = getClass().getResourceAsStream("/images/manhattan.jpg");

        bytes = IOUtils.toByteArray(is);
        drinkEntity.setImage(bytes);
        id = repo.save(drinkEntity).getId();
        i1 = new Ingredient("Rye whiskey","45","ml", id);
        i2 = new Ingredient("Sweet vermouth","55","ml", id);
        i4 = new Ingredient("Angostura bitters","3","dashes", id);
        i3 = new Ingredient("Grand Marnier","15","ml", id);
        ingredientRepository.save(i1);
        ingredientRepository.save(i2);
        ingredientRepository.save(i4);
        ingredientRepository.save(i3);

        /*******************
         * 4
         * **********************************/
        name = "Tequila Manhattan";
        recipe = "Combine all the ingredients in mixing glass with ice, and stir 25 to 30 times.\n" +
                "\n" +
                "Strain into a rocks glass over ice, and garnish with an orange peel.";
        desc = "Is it still a Manhattan when it’s made with tequila? Who cares when a drink is this satisfying?\n" +
                "\n" +
                "For more great Mexican-inspired drinks—and tacos, too!—head right this way.\n";
        glass = "Cocktail";

        drinkEntity = new DrinkEntity(name,desc,recipe,glass);
        is = getClass().getResourceAsStream("/images/manhattan2.jpg");

        bytes = IOUtils.toByteArray(is);
        drinkEntity.setImage(bytes);
        id = repo.save(drinkEntity).getId();
        i1 = new Ingredient("Cazadores reposado tequila","45","ml", id);
        i2 = new Ingredient("Sweet vermouth ","30","ml", id);
        i4 = new Ingredient("Chocolate-mole bitters","2","dashes", id);
        i3 = new Ingredient("Fernet","15","ml", id);
        ingredientRepository.save(i1);
        ingredientRepository.save(i2);
        ingredientRepository.save(i4);
        ingredientRepository.save(i3);

        /*******************
         * 5
         * **********************************/

        name  = "Daiquiri ";
        recipe = "Add all the ingredients into a shaker with ice, and shake until well-chilled.\n" +
                "\n" +
                "Strain into a chilled coupe.\n" +
                "\n" +
                "Garnish with a lime twist.";
        desc = "No drink has suffered more abuse than the Daiquiri. In the roughly 130 years since its inception, the granddaddy of " +
                "rum cocktails has gone from the pride of Havana to an unloved extra on the back of a Señor Frog’s table tent.";
        glass = "Coupe";

        drinkEntity = new DrinkEntity(name,desc,recipe,glass);
        is = getClass().getResourceAsStream("/images/Daiquiri.jpg");

        bytes = IOUtils.toByteArray(is);
        drinkEntity.setImage(bytes);
        id = repo.save(drinkEntity).getId();
        i1 = new Ingredient("Light rum","60","ml", id);
        i2 = new Ingredient("Fresh lime juice ","30","ml", id);
        i3 = new Ingredient("Demerara sugar syrup","22","ml", id);
        ingredientRepository.save(i1);
        ingredientRepository.save(i2);
        ingredientRepository.save(i3);


        /*******************
         * 6
         * **********************************/

        name = "Frozen Margarita";
        recipe = "Salt the rim of a chilled Margarita glass and put aside.\n" +
                "\n" +
                "Add all the ingredients into a blender, and top with 1 cup of ice. Blend until the mixture is smooth and frothy.\n" +
                "\n" +
                "Pour the contents into the salted Margarita glass.\n" +
                "\n" +
                "Garnish with a lime wheel.";
        desc = "Purists, keep walking! When the mercury climbs and the dog days of summer start barking at your heels, " +
                "the only proper cocktail is the one that keeps you cool and happy. This super simple Frozen Margarita recipe will have you " +
                "bonding with your blender all season long. ";
        glass = "Margarita";

        drinkEntity = new DrinkEntity(name,desc,recipe,glass);
        is = getClass().getResourceAsStream("/images/marga.jpg");

        bytes = IOUtils.toByteArray(is);
        drinkEntity.setImage(bytes);
        id = repo.save(drinkEntity).getId();
        i1 = new Ingredient("Blanco tequila","60","ml", id);
        i2 = new Ingredient("Fresh lime juice ","30","ml", id);
        i3 = new Ingredient("Orange liqueur ","22","ml", id);
        ingredientRepository.save(i1);
        ingredientRepository.save(i2);
        ingredientRepository.save(i3);

        /*******************
         * 7
         * **********************************/

        name = "Teal-quila Sunrise";
        recipe = "Pour the syrup into a Highball glass and fill with crushed ice.\n" +
                "\n" +
                "Add the rest of the ingredients to a shaker with ice, and shake until chilled.\n" +
                "\n" +
                "Strain over the crushed ice.\n" +
                "\n" +
                "Garnish with a lemon twist and raspberry.\n" +
                "\n" +
                "*Raspberry syrup: In a small glass, muddle 6 raspberries. Add 6 oz simple syrup and stir. " +
                "Fine-strain into a separate glass to remove the seeds.";
        desc = "This fun, summer take on the Tequila Sunrise knocks out the grenadine for homemade raspberry syrup (don’t worry, it’s easy!) " +
                "and a dash of blue curaçao for a drink that’s as tasty as it is colorful. ";
        glass = "Highball";

        drinkEntity = new DrinkEntity(name,desc,recipe,glass);
        is = getClass().getResourceAsStream("/images/sun.jpg");

        bytes = IOUtils.toByteArray(is);
        drinkEntity.setImage(bytes);
        id = repo.save(drinkEntity).getId();
        i1 = new Ingredient("Tequila blanco","60","ml", id);
        i2 = new Ingredient("Grand Marnier liqueur","22","ml", id);
        i4 = new Ingredient("Lemon juice","22","ml", id);
        i3 = new Ingredient("Raspberry syrup","15","ml", id);
        i5 = new Ingredient("Blue curaçao","7.5","ml", id);
        ingredientRepository.save(i1);
        ingredientRepository.save(i2);
        ingredientRepository.save(i4);
        ingredientRepository.save(i3);
        ingredientRepository.save(i5);

        /*******************
         * 8
         * **********************************/

        name = "Devil’s Margarita";
        recipe = "In a pint glass, add the tequila, lime juice and syrup with ice and shake until well-chilled.\n" +
                "\n" +
                "Strain into a cocktail glass.\n" +
                "\n" +
                "Float the red wine over the glass.\n" +
                "\n" +
                "Garnish with a lime wheel.";
        desc = "A well-made Margarita is a pure joy to drink, but sometimes you crave a more sinful cocktail." +
                " This tasty twist on the tequila classic calls for red wine, adding a dash of color and earthiness to the mix." +
                " Quick tip: Skip the salted rim as it clashes with the wine’s acidity. Plus, it looks too much like a halo. ";
        glass = "Cocktail";

        drinkEntity = new DrinkEntity(name,desc,recipe,glass);
        is = getClass().getResourceAsStream("/images/marga2.jpg");

        bytes = IOUtils.toByteArray(is);
        drinkEntity.setImage(bytes);
        id = repo.save(drinkEntity).getId();
        i1 = new Ingredient("Blanco tequila","45","ml", id);
        i2 = new Ingredient("Lime juice ","30","ml", id);
        i4 = new Ingredient("Simple syrup","22","ml", id);
        i3 = new Ingredient("Red wine","15","ml", id);
        ingredientRepository.save(i1);
        ingredientRepository.save(i2);
        ingredientRepository.save(i4);
        ingredientRepository.save(i3);


        /*******************
         * 9
         * **********************************/
        name = "Paloma";
        recipe = "Add the tequila and lime juice to a highball glass filled with ice.\n" +
                "\n" +
                "Fill with grapefruit soda, and stir briefly." ;
        desc = "Celebrate Cinco de Mayo year-round with the tasty Paloma cocktail.";
        glass= "Hughball";

        drinkEntity = new DrinkEntity(name,desc,recipe,glass);
        is = getClass().getResourceAsStream("/images/pal.jpg");

        bytes = IOUtils.toByteArray(is);
        drinkEntity.setImage(bytes);
        id = repo.save(drinkEntity).getId();
        i1 = new Ingredient("Tequila","60","ml", id);
        i2 = new Ingredient("Lime juice","15","ml", id);
        i3 = new Ingredient("Grapefruit soda","to top","", id);
        ingredientRepository.save(i1);
        ingredientRepository.save(i2);
        ingredientRepository.save(i3);

        /*******************
         * 10
         * **********************************/
        name = "Mimosa";
        recipe = "Add the orange juice to a Champagne flute.\n" +
                "\n" +
                "Fill with sparkling wine.";
        desc = "Bring elegance to any brunch.";
        glass = "Champagne";

        drinkEntity = new DrinkEntity(name,desc,recipe,glass);
        is = getClass().getResourceAsStream("/images/mimo.jpg");

        bytes = IOUtils.toByteArray(is);
        drinkEntity.setImage(bytes);
        id = repo.save(drinkEntity).getId();
        i1 = new Ingredient("Orange juice","60","ml", id);
        i2 = new Ingredient("Sparkling wine","to top","", id);
        ingredientRepository.save(i1);
        ingredientRepository.save(i2);
    }
}
