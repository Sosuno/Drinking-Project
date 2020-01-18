package com.iqlearning.rest;


import com.iqlearning.database.service.DrinkService;
import com.iqlearning.rest.resource.Drink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin("*")
public class DrinkController {

    @Autowired
    private DrinkService service;

    @GetMapping("get/drinks")
    public ResponseEntity<?> getDrinks(@RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization");
        if(!token.equals("Nasze tajne haslo")) return new ResponseEntity<>("Access denied", HttpStatus.UNAUTHORIZED);
        HashMap<String,List<Drink>> drinks = new HashMap();
                drinks.put("drinks",service.getAllDrinks());
        return new ResponseEntity<Object>(drinks, HttpStatus.OK);
    }
}
