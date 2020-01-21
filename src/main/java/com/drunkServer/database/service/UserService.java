package com.drunkServer.database.service;
/*****
 * Author: Ewa Jasinska
 */
import com.drunkServer.database.entities.User;
import com.drunkServer.database.repository.UserRepository;
import com.drunkServer.database.service.interfaces.IUserService;
import com.drunkServer.database.utils.LoggedUser;
import com.drunkServer.database.utils.PasswordEncoderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    private User user;

    private LoggedUser loggedUser;
    private PasswordEncoderConfig hash = new PasswordEncoderConfig();

    private DrinkService drinkService;
    @Autowired
    public void setMissionService(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @Override
    public User getUser(long id) {
        Optional<User> o = userRepository.findById(id);
        if( o.isPresent()){
            return o.get();
        }
        return new User(false);
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> o = userRepository.findByUsername(username);
        if( o.isPresent()){
            return o.get();
        }
        return new User(false);
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> o = userRepository.findByEmail(email);
        if( o.isPresent()){
            return o.get();
        }
        return new User(false);
    }

    @Override
    public boolean checkIfUserExists(String username) {
        return getUserByUsername(username).getId() != -1;
    }

    @Override
    public boolean checkIfEmailInUse(String email) {
        return getUserByEmail(email).getId() != -1;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(long id) {
        userRepository.deleteById(id);
        return !userRepository.existsById(id);
    }

    /****
     * returns legend:
     *  LOGGED USER
     *      null -> no user with such username
     *  LOGGED USER ID:
     *      -1   -> wrong password
     *      -2   -> account blocked
     *
     */
    public LoggedUser login(String username, String password) {

        user = getUserByUsername(username);
        if(user.getId() == -1) return null;

            if(!hash.passwordEncoder().matches(password,user.getPassword())){
                saveUser(user);
                loggedUser = new LoggedUser();
                loggedUser.setId((long) -1);
            }else {
                loggedUser = new LoggedUser(user);
                if (user.getFavouriteDrinks() != null && user.getFavouriteDrinks().length > 0) {
                    loggedUser.setDrinks(drinkService.getAllFavourites(user.getFavouriteDrinks()));
                }

            }

        return loggedUser;
    }



   /* public void logout(String sessionId){
        deleteSession(sessionId);
    }
*/

    /****
     * ID RETURN LEGEND:
     *      -1 --> username taken
     *      -2 --> email in use
     *
     */
    public LoggedUser register(String username,String password, String email) {
        if(checkIfUserExists(username)) {
            loggedUser = new LoggedUser();
            loggedUser.setId((long) -1);
            return loggedUser;
        }
        if (checkIfEmailInUse(email)){
            loggedUser = new LoggedUser();
            loggedUser.setId((long) -2);
            return loggedUser;
        }
        user = new User(username,hash.passwordEncoder().encode(password),email,null);
        user = saveUser(user);
        //s = createSession(user.getId());
        loggedUser = new LoggedUser(user);

        return loggedUser;
    }

    @PostConstruct
    private void init(){
        //register(String username,String password, String email)
       register("Evik", "admin", "evik@drunk.corp");
       register("karol", "TwojaStara1", "karol@drunk.corp");
       register("Shepard", "INeedToGo", "evik@drunk.corp");
    }
}
