package com.iqlearning.database.service.interfaces;
/*****
 * Author: Ewa Jasinska
 */
import com.iqlearning.database.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User getUser(long id);
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    boolean checkIfUserExists(String username);
    boolean checkIfEmailInUse(String email);
    List<User> getAllUsers();
    User saveUser(User user);
    boolean deleteUser(long id);
    User getUserBySession(String sessionId);

}
