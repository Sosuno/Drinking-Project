package com.drunkServer.rest;


import com.drunkServer.database.entities.User;
import com.drunkServer.database.service.UserService;
import com.drunkServer.database.utils.LoggedUser;
import com.drunkServer.rest.resource.AddToFavsForm;
import com.drunkServer.rest.resource.LoginForm;
import com.drunkServer.rest.resource.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins=("http://localhost:3000"))
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/token")
    public ResponseEntity<?> login(@RequestHeader Map<String, String> headers, @RequestBody LoginForm loginForm) {

        LoggedUser user = userService.login(loginForm.getUsername(), loginForm.getPassword());
        if(user == null) {
            return new ResponseEntity<>("Username not found", HttpStatus.FORBIDDEN);
        } else if(user.getId() == -1) {
            return new ResponseEntity<>("Bad password", HttpStatus.FORBIDDEN);
        } else if(user.getId() == -2) {
            return new ResponseEntity<>("Account suspended", HttpStatus.FORBIDDEN);
        } else return new ResponseEntity<Object>(user, HttpStatus.OK);
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestHeader Map<String, String> headers, @RequestBody RegisterForm registerForm) {
        LoggedUser user = userService.register(registerForm.getUsername(),registerForm.getPassword(),registerForm.getEmail());
        if(user.getId() == -1) {
            return new ResponseEntity<>( "Username taken", HttpStatus.NOT_ACCEPTABLE);
        } else if(user.getId() == -2) {
            return new ResponseEntity<>( "Email taken", HttpStatus.NOT_ACCEPTABLE);
        } else return new ResponseEntity<Object>(user, HttpStatus.OK);
    }

    @PostMapping("/user/updateFavs")
    public ResponseEntity<?> updateFavourites(@RequestHeader Map<String, String> headers, @RequestBody AddToFavsForm form) {
        String token = headers.get("authorization");
        if(!token.equals("Nasze tajne haslo")) return new ResponseEntity<>("Access denied", HttpStatus.UNAUTHORIZED);
        User user = userService.getUserByUsername(form.getUsername());
        user.setFavouriteDrinks(form.getFavourites());
        user = userService.saveUser(user);
        HashMap<String, Long[]> map = new HashMap<>();
        map.put("favs",user.getFavouriteDrinks());
        if(user.getFavouriteDrinks().length == form.getFavourites().length) return new ResponseEntity<>(map, HttpStatus.OK);
        return new ResponseEntity<>(map, HttpStatus.I_AM_A_TEAPOT);
    }

}
