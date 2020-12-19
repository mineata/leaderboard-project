package com.gamescore.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;
import java.util.UUID;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends JdkSerializationRedisSerializer implements Serializable {

    private UUID user_id;
    private String display_name;
    private Float points;
    private Long rank;
    private String country;

    public User() {}

    public User(UUID user_id, String display_name, Float points, Long rank, String country) {
        this.user_id = user_id;
        this.display_name = display_name;
        this.points = points;
        this.rank = rank;
        this.country = country;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Float getPoints() {
        return points;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
