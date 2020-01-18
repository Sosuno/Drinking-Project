package com.iqlearning.database.repository;
/*****
 * Author: Ewa Jasinska
 */
import com.iqlearning.database.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT s.userID FROM Session s WHERE sessionID = :sessionId")
    Optional<Long> getUserBySession(@Param("sessionId") String sessionId);

    @Query("SELECT s FROM User s")
    List<User> getAll();


}
