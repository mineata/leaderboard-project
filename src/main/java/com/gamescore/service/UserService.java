package com.gamescore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gamescore.model.Score;
import com.gamescore.model.User;
import com.gamescore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  private RedisTemplate redisTemplate;

  private static Logger log = LoggerFactory.getLogger(UserService.class);
  private static final String SET_KEY = "leaderBoard_set";
  private static final String HASH_KEY = "leaderBoard_hash";


  public void updateRedis() {
      boolean hash_found = false;

      redisTemplate.opsForZSet().removeRange(SET_KEY, 0, -1);
      List<User> users = userRepository.getAllUsers();
      for (User user: users) {
          String id = user.getUser_id().toString();
          hash_found = redisTemplate.opsForHash().hasKey(HASH_KEY, id);
          if (hash_found == false){redisTemplate.opsForHash().put(HASH_KEY, id, user);}
          redisTemplate.opsForZSet().add(SET_KEY, id, new Float(user.getPoints()));
      }
  }

  public List<User> getAllUsers() {
    boolean hash_found = false;
    Set<String> leaderboard = redisTemplate.opsForZSet().reverseRange(SET_KEY, 0, -1);

//    System.out.println("Leaderboard's size: " +leaderboard);
    List<User> user_sorted = new ArrayList<>();
    for (String id: leaderboard){
        User user;
        hash_found = redisTemplate.opsForHash().hasKey(HASH_KEY, id);
        if (hash_found == false){
            user = userRepository.getUserById(UUID.fromString(id));
            redisTemplate.opsForHash().put(HASH_KEY, id, user);
        }
        else{user =  (User) redisTemplate.opsForHash().get(HASH_KEY, id.toString());}
//        System.out.println("User id: " + id + " user: " + user);
        user.setRank(redisTemplate.opsForZSet().reverseRank(SET_KEY, user.getUser_id().toString())+1);
        user_sorted.add(user);
    }

    return user_sorted;
  }

  public List<User> getAllUsersByCountry(String country) {
    List<User> usersByCountry = userRepository.getAllUsersByCountry(country);
    Set<String> leaderboard = redisTemplate.opsForZSet().reverseRange(SET_KEY, 0, -1);

    boolean hash_found = false;
    List<User> user_sorted = new ArrayList<>();
    for(String id: leaderboard){
//        System.out.println("User id is " + id);
        User user;
        hash_found = redisTemplate.opsForHash().hasKey(HASH_KEY, id);
        if (hash_found == false){
            user = userRepository.getUserById(UUID.fromString(id));
            redisTemplate.opsForHash().put(HASH_KEY, id, user);
        }
        else{user =  (User) redisTemplate.opsForHash().get(HASH_KEY, id);}
//        System.out.println("user is " + user);
//        System.out.println("is contains " + usersByCountry.contains(user));
        boolean exists = usersByCountry.stream().anyMatch(o -> o.getUser_id().equals(user.getUser_id()));
        if (exists == true){
            Long rank = redisTemplate.opsForZSet().reverseRank(SET_KEY, id);
            user.setRank(rank +1);
//            log.info("User ", user.getDisplay_name(),"rank set to ", rank); does not work
//            System.out.println("User "+ user.getDisplay_name()+" rank set to " + user.getRank() +1);
            user_sorted.add(user);
        }
    }
    return user_sorted;
  }

  public User getUserById(UUID id) {
    boolean hash_found = false;
    User user;
    hash_found = redisTemplate.opsForHash().hasKey(HASH_KEY, id);
    if (hash_found == false){
        user = userRepository.getUserById(id);
        redisTemplate.opsForHash().put(HASH_KEY, id, user);
    }
    else{user =  (User) redisTemplate.opsForHash().get(HASH_KEY, id.toString());}

    user.setRank(redisTemplate.opsForZSet().reverseRank(SET_KEY, user.getUser_id().toString()) +1);
    return user;
  }

  public void addUser(User user) {
    String id =  user.getUser_id().toString();
    redisTemplate.opsForZSet().add(SET_KEY,id, user.getPoints());
    redisTemplate.opsForHash().put(HASH_KEY, id, user);
    user.setRank(redisTemplate.opsForZSet().reverseRank(SET_KEY,id) +1);
    userRepository.addUser(user);
  }

  public void submitScore (Score score) {
    redisTemplate.opsForZSet().incrementScore(SET_KEY, score.getUser_id().toString(), score.getScore_worth());
    redisTemplate.opsForHash().delete(HASH_KEY, score.getUser_id().toString());

    userRepository.submitScore(score);
  }


}
