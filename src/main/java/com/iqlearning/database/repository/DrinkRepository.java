package com.iqlearning.database.repository;

import com.iqlearning.database.entities.DrinkEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkRepository extends CrudRepository<DrinkEntity,Long> {

    List<DrinkEntity> getAllBy();
}
