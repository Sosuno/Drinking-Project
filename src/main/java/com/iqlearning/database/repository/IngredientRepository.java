package com.iqlearning.database.repository;

import com.iqlearning.database.entities.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, Long> {

    List<Ingredient> getAllByDrinkId(Long drinkId);
}
