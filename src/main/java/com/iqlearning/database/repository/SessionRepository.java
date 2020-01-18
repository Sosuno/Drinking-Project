package com.iqlearning.database.repository;
/*****
 * Author: Ewa Jasinska
 */
import com.iqlearning.database.entities.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session,String> {

    @Query("SELECT s FROM Session s WHERE s.userID = :userId")
    Optional<Session> getByUser(@Param("userId") Long userId);

}
