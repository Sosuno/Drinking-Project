package com.iqlearning.database.service;
/*****
 * Author: Ewa Jasinska
 */
import com.iqlearning.database.entities.Session;
import com.iqlearning.database.entities.User;
import com.iqlearning.database.repository.SessionRepository;
import com.iqlearning.database.repository.UserRepository;
import com.iqlearning.database.service.interfaces.ISessionService;
import com.iqlearning.database.service.interfaces.IUserService;
import com.iqlearning.database.utils.LoggedUser;
import com.iqlearning.database.utils.PasswordEncoderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService,ISessionService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    private User user;
    private Session s;
    private LoggedUser loggedUser;
    private PasswordEncoderConfig hash = new PasswordEncoderConfig();;

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
    public User getUserBySession(String sessionId) {
        Optional<Long> o = userRepository.getUserBySession(sessionId);
        if(o.isPresent()){
            return getUser(o.get());
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

    @Override
    public Session getSession(String id) {
        Optional<Session> o = sessionRepository.findById(id);
        if (o.isPresent()) return o.get();
        return null;
    }

    @Override
    public Session createSession(Long userId) {
        Session s = new Session(UUID.randomUUID().toString(),userId, new Timestamp(System.currentTimeMillis()));
        return sessionRepository.save(s);
    }

    @Override
    public Session updateSession(Session s) {
        return sessionRepository.save(s);
    }

    @Override
    public Session getSessionByUser(Long userId) {
        Optional<Session> o = sessionRepository.getByUser(userId);
        if (o.isPresent()) return o.get();
        return null;
    }

    @Override
    public void deleteSession(String id) {
        Session s = getSession(id);
        if (s != null){
            sessionRepository.delete(s);
        }
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
                s = getSessionByUser(user.getId());
                if(s!=null)deleteSession(s.getSessionID());
                s = createSession(user.getId());
                loggedUser = new LoggedUser(user,s.getSessionID());
            }

        return loggedUser;
    }

    public LoggedUser loginWithSession(String sessionId) {
        user = getUserBySession(sessionId);
        loggedUser = new LoggedUser(user, sessionId);
        return loggedUser;
    }

    public void logout(String sessionId){
        deleteSession(sessionId);
    }


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
        s = createSession(user.getId());
        loggedUser = new LoggedUser(user,s.getSessionID());

        return loggedUser;
    }

    public LoggedUser changePassword(String session, String newPassword) {
        user = getUserBySession(session);
        user.setPassword(hash.passwordEncoder().encode(newPassword));
        return new LoggedUser(saveUser(user), session);
    }

    public LoggedUser updateUser(LoggedUser loggedUser){
        user = getUserBySession(loggedUser.getSessionID());


        return new LoggedUser(saveUser(user), loggedUser.getSessionID());
    }

    @PostConstruct
    private void init(){
        //register(String username,String password, String email)
       register("Evik", "admin", "evik@drunk.corp");
       register("karol", "TwojaStara1", "karol@drunk.corp");
       register("Shepard", "INeedToGo", "evik@drunk.corp");
    }
}
