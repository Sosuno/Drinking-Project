package com.drunkServer.database.repository;

import com.drunkServer.database.entities.DrinkEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkRepository extends CrudRepository<DrinkEntity,Long> {

    List<DrinkEntity> getAllBy();

    List<DrinkEntity> getAllByIdIn(Long[] favs);

}
