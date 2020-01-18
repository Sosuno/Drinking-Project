package com.iqlearning.rest;


import com.iqlearning.database.entities.User;
import com.iqlearning.database.service.UserService;
import com.iqlearning.database.utils.LoggedUser;
import com.iqlearning.rest.resource.LoginForm;
import com.iqlearning.rest.resource.PasswordForm;
import com.iqlearning.rest.resource.RegisterForm;
import com.iqlearning.rest.resource.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@CrossOrigin(origins=("http://localhost:3000"))
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/token")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {

        LoggedUser user = userService.login(loginForm.getUsername(), loginForm.getPassword());
        if(user == null) {
            return new ResponseEntity<>("Username not found", HttpStatus.FORBIDDEN);
        } else if(user.getId() == -1) {
            return new ResponseEntity<>("Bad password", HttpStatus.FORBIDDEN);
        } else if(user.getId() == -2) {
            return new ResponseEntity<>("Account suspended", HttpStatus.FORBIDDEN);
        } else return auth(user.getSessionID(), user);
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterForm registerForm) {
        LoggedUser user = userService.register(registerForm.getUsername(),registerForm.getPassword(),registerForm.getEmail());
        if(user.getId() == -1) {
            return new ResponseEntity<>( "Username taken", HttpStatus.NOT_ACCEPTABLE);
        } else if(user.getId() == -2) {
            return new ResponseEntity<>( "Email taken", HttpStatus.NOT_ACCEPTABLE);
        } else return auth(user.getSessionID(),user);
    }

    @DeleteMapping("/user/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").split(" ")[1];
        User u = userService.getUserBySession(token);
        if (u.getId() == -1) return new ResponseEntity<>("No active session", HttpStatus.UNAUTHORIZED);
        userService.logout(token);
        if(userService.getSession(token) != null) {
            return new ResponseEntity<>( "Logout unsuccessful", HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>( "Logout successful", HttpStatus.OK);
    }

    @PostMapping("/user/password")
    public ResponseEntity<?> editPassword(@RequestHeader Map<String, String> headers, @RequestBody PasswordForm passwordForm) {
        String token = headers.get("authorization").split(" ")[1];
        User user = userService.getUserBySession(token);
        if(user.getId() == -1) return new ResponseEntity<>("No active session", HttpStatus.UNAUTHORIZED);
        else if(!passwordForm.getCurrentPass().equals(user.getPassword())) {
            return new ResponseEntity<>("Current password doesn't match" + user.getPassword(), HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(passwordForm.getNewPass());
            return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
        }
    }

    private ResponseEntity<?> auth(String session, Object o) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization",
                session);
        Token token = new Token();
        token.setToken(session);
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(o);
    }
}
