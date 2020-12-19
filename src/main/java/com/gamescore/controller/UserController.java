package com.gamescore.controller;

import com.gamescore.model.Score;
import com.gamescore.model.User;
import com.gamescore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping(value="/start")
    private ResponseEntity<String> startDatabase() {
        userService.updateRedis();
        return new ResponseEntity<>("App has started!", HttpStatus.OK);
    }

    @GetMapping(value="/user/profile/{guid}")
    private ResponseEntity<User> getUserById(@PathVariable UUID guid) {
        User user = userService.getUserById(guid);
        if (user == null) {
            return new ResponseEntity<>((User) null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value="/leaderboard")
    private ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        if (users == null){
            return new ResponseEntity<>((List<User>) null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value="/leaderboard/{country_iso_code}")
    private ResponseEntity<List<User>> getAllUsersByCountry(@PathVariable String country_iso_code){
        List<User> users = userService.getAllUsersByCountry(country_iso_code);
        if (users == null){
            return new ResponseEntity<>((List<User>) null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value="/user/create")
    private ResponseEntity<User> addUser(@RequestBody User user){
        userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/score/submit")
    private ResponseEntity<Score> submitScore(@RequestBody Score score) {
        userService.submitScore(score);
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
